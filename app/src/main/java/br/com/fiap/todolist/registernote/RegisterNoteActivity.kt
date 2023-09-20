package br.com.fiap.todolist.registernote

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterTodoListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObservers()
        binding.btnRegister.setOnClickListener {
            val newNote = TodoListModel(
                binding.inputNote.text.toString(),
                binding.inputBody.text.toString(),
                false,
                BgColor.getRandomPostItColor()
            )

            viewModel.register(newNote)
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
}
