package br.com.fiap.todolist.presentation.registernote

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.fiap.todolist.presentation.BaseActivity
import br.com.fiap.todolist.presentation.BaseViewModel
import br.com.fiap.todolist.data.remote.FirebaseRepository
import br.com.fiap.todolist.databinding.ActivityRegisterTodoListBinding
import br.com.fiap.todolist.presentation.todolist.model.TodoListModel
import br.com.fiap.todolist.presentation.utils.BgColor
import br.com.fiap.todolist.presentation.utils.makeInVisible
import br.com.fiap.todolist.presentation.utils.makeVisible

class RegisterNoteActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterTodoListBinding
    private val viewModel: RegisterNoteViewModel by lazy { initViewModel() }
    private var editNote: TodoListModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterTodoListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObservers()
        isEditing()
        binding.btnRegister.setOnClickListener {
            registerNote()
        }
    }

    private fun initViewModel(): RegisterNoteViewModel {
        val repository = FirebaseRepository()
        val viewModelFactory = RegisterNoteViewModelFactory(repository)
        return ViewModelProvider(this, viewModelFactory)[RegisterNoteViewModel::class.java]
    }

    private fun registerNote() {
        val title = binding.inputNote.text.toString()
        val textBody = binding.inputBody.text.toString()

        if (!validField(title, textBody)) {
            buildErrorSnackBar(
                "Preencha pelo menos um dos campos.",
                binding.root.rootView
            )
            return
        }

        val newNote = TodoListModel(
            title = title,
            textBody = textBody,
            finished = false,
            backGroundColor = BgColor.getRandomPostItColor()
        )

        editNote?.let {
            it.title = binding.inputNote.text.toString()
            it.textBody = binding.inputBody.text.toString()
            viewModel.editNote(it)
        } ?: viewModel.register(newNote)
    }

    private fun validField(title: String, textBody: String) = title.isNotEmpty() || textBody.isNotEmpty()

    private fun isEditing() {
        editNote = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(NOTE_EXTRA, TodoListModel::class.java)
        } else {
            intent.getSerializableExtra(NOTE_EXTRA) as? TodoListModel
        }
        editNote?.let {
            binding.inputNote.setText(it.title)
            binding.inputBody.setText(it.textBody)
        }
    }

    private fun initObservers() {
        viewModel.result.observe(this) {
            loading(false)
            when (it) {
                is BaseViewModel.BaseState.Loading -> loading(true)

                is BaseViewModel.BaseState.Sucess -> {
                    buildSucessSnackBar(
                        "Nota cadastrada com sucesso!",
                        binding.root.rootView
                    )
                    setResult(RESULT_OK)
                    finish()
                }

                is BaseViewModel.BaseState.Error -> buildErrorSnackBar(
                    it.message,
                    binding.root.rootView
                )

                else -> buildErrorSnackBar("Erro inesperado!", binding.root.rootView)
            }
        }
    }

    private fun loading(isLoading: Boolean) {
        binding.btnRegister.makeInVisible(isLoading)
        binding.loading.makeVisible(isLoading)
    }

    companion object {
        const val NOTE_EXTRA = "RegisterNoteActivity.NOTE_EXTRA"

        fun getEditIntent(context: Context, note: TodoListModel): Intent {
            return Intent(context, RegisterNoteActivity::class.java).apply {
                putExtra(NOTE_EXTRA, note)
            }
        }
    }

    private class RegisterNoteViewModelFactory(
        private val repository: FirebaseRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegisterNoteViewModel::class.java)) {
                return RegisterNoteViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
