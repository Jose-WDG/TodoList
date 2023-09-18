package br.com.fiap.todolist.register

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import br.com.fiap.todolist.BaseActivity
import br.com.fiap.todolist.BaseViewModel
import br.com.fiap.todolist.databinding.ActivityRegisterBinding
import br.com.fiap.todolist.utils.makeInVisible
import br.com.fiap.todolist.utils.makeVisible

class RegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initObserver()

        binding.btnRegister.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            val confirmPassword = binding.inputConfirmPassword.text.toString()

            viewModel.register(email, password, confirmPassword)
        }
    }

    private fun initObserver() {
        viewModel.result.observe(this) {
            when (it) {
                is BaseViewModel.BaseState.Loading -> loading(true)
                is BaseViewModel.BaseState.Sucess -> {
                    loading(false)
                    buildSucessSnackBar("Usuário cadastrado com sucesso!", binding.root.rootView)
                }

                is BaseViewModel.BaseState.Error -> {
                    loading(false)
                    buildErrorSnackBar(it.message, binding.root.rootView)
                }

                else -> {
                    loading(false)
                    Log.e("TodoList", "Error Inesperado.")
                }
            }
        }
    }

    private fun loading(isLoading: Boolean){
        binding.btnRegister.makeInVisible(isLoading)
        binding.loading.makeVisible(isLoading)
    }
}
