package br.com.fiap.todolist.presentation.todolist

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.fiap.todolist.presentation.BaseActivity
import br.com.fiap.todolist.data.remote.FirebaseRepository
import br.com.fiap.todolist.databinding.ActivityTodoListBinding
import br.com.fiap.todolist.presentation.login.LoginActivity
import br.com.fiap.todolist.presentation.registernote.RegisterNoteActivity
import br.com.fiap.todolist.presentation.todolist.adapter.TodoListAdapter
import br.com.fiap.todolist.presentation.todolist.model.TodoListModel
import br.com.fiap.todolist.presentation.utils.makeVisible

class TodoListActivity : BaseActivity(), TodoListAdapter.OnClickNote {
    private lateinit var binding: ActivityTodoListBinding
    private val viewModel: TodoListViewModel by lazy { initViewModel() }
    private val todoListAdapter = TodoListAdapter(arrayListOf(), this)
    private val onActivityResultLauncher: ActivityResultLauncher<Intent> = initOnActivityResult()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBackPressedDispatcher.addCallback(TodoListOnBackPressedCallback(true))
        initObsrever()
        initBindings()
        viewModel.getTodoList()
    }

    private fun initViewModel(): TodoListViewModel {
        val repository = FirebaseRepository()
        val viewModelFactory = TodoListViewModelFactory(repository)
        return ViewModelProvider(this, viewModelFactory)[TodoListViewModel::class.java]
    }

    private fun isLoading(isLoading: Boolean) {
        binding.todoListRecyclerview.makeVisible(true)
        binding.loading.makeVisible(isLoading)
    }

    private fun initBindings() {
        binding.todoListRecyclerview.apply {
            adapter = todoListAdapter
            layoutManager = LinearLayoutManager(this@TodoListActivity)
            setHasFixedSize(true)
        }

        binding.btnNewNote.setOnClickListener {
            onActivityResultLauncher.launch(
                Intent(this@TodoListActivity, RegisterNoteActivity::class.java)
            )
        }
    }

    private fun initOnActivityResult(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.getTodoList()
            }
        }
    }

    private fun initObsrever() {
        viewModel.todoListState.observe(this) {
            isLoading(false)
            when (it) {
                is TodoListViewModel.TodoListState.Loading -> isLoading(true)
                is TodoListViewModel.TodoListState.OnDataChange -> todoListAdapter.updateListItems(
                    it.todoList
                )

                is TodoListViewModel.TodoListState.OnCancelled -> buildErrorSnackBar(
                    it.message,
                    binding.root.rootView
                )

                else -> buildErrorSnackBar("Erro inesperado!", binding.root.rootView)
            }
        }
    }

    inner class TodoListOnBackPressedCallback(enabled: Boolean) : OnBackPressedCallback(enabled) {
        override fun handleOnBackPressed() {
            showLogoutDialog(object : LogoutListener {
                override fun onLogout() {
                    startActivity(Intent(this@TodoListActivity, LoginActivity::class.java))
                    finish()
                }
            })
        }
    }

    override fun clickNote(note: TodoListModel) {
        AlertDialog.Builder(this)
            .setTitle("O que você deseja fazer?")
            .setPositiveButton("Editar") { _, _ ->
                val editIntent = RegisterNoteActivity.getEditIntent(this@TodoListActivity, note)
                onActivityResultLauncher.launch(editIntent)
            }.setNegativeButton("Finalizar") { _, _ ->
                viewModel.deleteNote(note.id.toString())
            }.show()
    }

    override fun onDestroy() {
        onActivityResultLauncher.unregister()
        super.onDestroy()
    }

    private class TodoListViewModelFactory(
        private val repository: FirebaseRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TodoListViewModel::class.java)) {
                return TodoListViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
