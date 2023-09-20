package br.com.fiap.todolist.todolist

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.fiap.todolist.BaseActivity
import br.com.fiap.todolist.databinding.ActivityTodoListBinding
import br.com.fiap.todolist.login.LoginActivity
import br.com.fiap.todolist.registernote.RegisterNoteActivity
import br.com.fiap.todolist.todolist.adapter.TodoListAdapter

class TodoListActivity : BaseActivity() {
    private lateinit var binding: ActivityTodoListBinding
    private val viewModel: TodoListViewModel by viewModels()
    private val todoListAdapter = TodoListAdapter(arrayListOf())
    private val onActivityResultLauncher: ActivityResultLauncher<Intent> by lazy { initOnActivityResult() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBackPressedDispatcher.addCallback(TodoListOnBackPressedCallback(true))
        initObsrever()
        initBindings()
        viewModel.getTodoList()
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
            when (it) {
                is TodoListViewModel.TodoListState.Loading -> {

                }

                is TodoListViewModel.TodoListState.OnDataChange -> {
                    todoListAdapter.updateListItems(it.todoList)
                }

                is TodoListViewModel.TodoListState.OnCancelled -> {
                    buildErrorSnackBar(it.message, binding.root.rootView)
                }

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
}
