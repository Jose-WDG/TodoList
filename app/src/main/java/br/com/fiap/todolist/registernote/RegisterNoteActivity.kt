package br.com.fiap.todolist.registernote

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import br.com.fiap.todolist.BaseActivity
import br.com.fiap.todolist.BaseViewModel
import br.com.fiap.todolist.databinding.ActivityRegisterTodoListBinding
import br.com.fiap.todolist.todolist.model.TodoListModel
import br.com.fiap.todolist.utils.BgColor
import br.com.fiap.todolist.utils.makeInVisible
import br.com.fiap.todolist.utils.makeVisible

class RegisterNoteActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterTodoListBinding
    private val viewModel: RegisterNoteViewModel by viewModels()
    private var editNote: TodoListModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterTodoListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObservers()
        isEditing()
        binding.btnRegister.setOnClickListener {
            val newNote = TodoListModel(
                title = binding.inputNote.text.toString(),
                textBody = binding.inputBody.text.toString(),
                finished = false,
                backGroundColor = BgColor.getRandomPostItColor()
            )

            editNote?.let {
                it.title = binding.inputNote.text.toString()
                it.textBody = binding.inputBody.text.toString()
                viewModel.editNote(it)
            } ?: viewModel.register(newNote)
        }
    }

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
        const val RESULT_EDIT_OK = Int.MAX_VALUE
        fun getEditIntent(context: Context, note: TodoListModel): Intent {
            return Intent(context, RegisterNoteActivity::class.java).apply {
                putExtra(NOTE_EXTRA, note)
            }
        }
    }
}
