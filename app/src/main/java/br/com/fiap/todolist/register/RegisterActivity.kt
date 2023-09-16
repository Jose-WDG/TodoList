package br.com.fiap.todolist.register

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.fiap.todolist.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {
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
        viewModel.result.observe(this, Observer {
            when (it) {
                is RegisterViewModel.RegisterState.Loading -> {
                    Log.d("TESTE","Carregando...")
                }
                is RegisterViewModel.RegisterState.Sucess -> {
                    Snackbar.make(
                        this@RegisterActivity,
                        binding.root.rootView,
                        "Usuário cadastrado com sucesso!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                is RegisterViewModel.RegisterState.Error -> {
                    Snackbar.make(
                        this@RegisterActivity,
                        binding.root.rootView,
                        it.message,
                        Snackbar.LENGTH_SHORT
                    ).show()

                }
                else -> {
                    Log.d("TESTE","Error Inesperado.")
                }
            }
        })
    }
}