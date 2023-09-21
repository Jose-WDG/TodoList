package br.com.fiap.todolist.register

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.fiap.todolist.BaseActivity
import br.com.fiap.todolist.BaseViewModel
import br.com.fiap.todolist.data.remote.FirebaseRepository
import br.com.fiap.todolist.databinding.ActivityRegisterBinding
import br.com.fiap.todolist.utils.makeInVisible
import br.com.fiap.todolist.utils.makeVisible

class RegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by lazy { initViewModel() }

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

    private fun initViewModel(): RegisterViewModel {
        val repository = FirebaseRepository()
        val viewModelFactory = RegisterViewModelFactory(repository)
        return ViewModelProvider(this, viewModelFactory)[RegisterViewModel::class.java]
    }

    private fun initObserver() {
        viewModel.result.observe(this) {
            loading(false)
            when (it) {
                is BaseViewModel.BaseState.Loading -> loading(true)
                is BaseViewModel.BaseState.Sucess -> buildSucessSnackBar(
                    "Usuário cadastrado com sucesso!",
                    binding.root.rootView
                )

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

    private class RegisterViewModelFactory(
        private val repository: FirebaseRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
                return RegisterViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
