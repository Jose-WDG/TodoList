package br.com.fiap.todolist.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.fiap.todolist.BaseActivity
import br.com.fiap.todolist.BaseViewModel
import br.com.fiap.todolist.data.local.UserPreferences
import br.com.fiap.todolist.data.remote.FirebaseRepository
import br.com.fiap.todolist.databinding.ActivityLoginBinding
import br.com.fiap.todolist.register.RegisterActivity
import br.com.fiap.todolist.todolist.TodoListActivity
import br.com.fiap.todolist.utils.makeInVisible
import br.com.fiap.todolist.utils.makeVisible

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by lazy { initViewModel() }
    private val sharedPreferences by lazy { UserPreferences(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initObserver()

        binding.btnLogin.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputSenha.text.toString()

            viewModel.singIn(email, password)
        }

        binding.btnContextualCadastrar.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        hasCurrentUser()
    }

    private fun initViewModel(): LoginViewModel {
        val repository = FirebaseRepository()
        val viewModelFactory = LoginViewModelFactory(repository)
        return ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }

    private fun hasCurrentUser(){
        viewModel.getCurrentUser()?.let {
            startActivity(Intent(this, TodoListActivity::class.java))
        }

        val currentEmail = sharedPreferences.getValue(UserPreferences.EMAIL_KEY)
        val currentPassword = sharedPreferences.getValue(UserPreferences.PASSWORD_KEY)

        if (currentEmail.isNotEmpty() && currentPassword.isNotEmpty()){
            binding.inputEmail.setText(currentEmail)
            binding.inputSenha.setText(currentPassword)
        }
    }

    private fun initObserver() {
        viewModel.result.observe(this) {
            when (it) {
                is BaseViewModel.BaseState.Loading -> loading(true)

                is BaseViewModel.BaseState.Sucess -> {
                    saveUser()
                    hasCurrentUser()
                    finish()
                }

                is BaseViewModel.BaseState.Error -> {
                    loading(false)
                    buildErrorSnackBar(it.message, binding.root.rootView)
                }

                else -> {
                    loading(false)
                    val message = "Error Inesperado."
                    buildErrorSnackBar(message, binding.root.rootView)
                    Log.e("TodoList", message)
                }
            }
        }
    }

    private fun saveUser(){
        val email = binding.inputEmail.text.toString()
        val password = binding.inputSenha.text.toString()

        sharedPreferences.setValue(UserPreferences.EMAIL_KEY,email)
        sharedPreferences.setValue(UserPreferences.PASSWORD_KEY,password)
    }
    private fun loading(isLoading: Boolean) {
        binding.btnLogin.makeInVisible(isLoading)
        binding.btnContextualCadastrar.isClickable = !isLoading
        binding.loading.makeVisible(isLoading)
    }

    private class LoginViewModelFactory(
        private val repository: FirebaseRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}