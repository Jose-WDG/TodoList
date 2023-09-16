package br.com.fiap.todolist.register

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.SnapHelper
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
                is RegisterViewModel.RegisterState.Loading -> loading(true)
                is RegisterViewModel.RegisterState.Sucess -> {
                    loading(false)
                    val color = ContextCompat.getColor(this,android.R.color.holo_green_light)
                    buildSnackBar(color,"Usuário cadastrado com sucesso!")
                }
                is RegisterViewModel.RegisterState.Error -> {
                    loading(false)
                    val color = ContextCompat.getColor(this,android.R.color.holo_red_light)
                    buildSnackBar(color,it.message)
                }
                else -> {
                    loading(false)
                    Log.e("TodoList","Error Inesperado.")
                }
            }
        })
    }
    private fun buildSnackBar(color: Int, msg: String){
        Snackbar.make(
            this@RegisterActivity,
            binding.root.rootView,
            msg,
            Snackbar.LENGTH_SHORT
        ).setBackgroundTint(color).show()
    }
    private fun loading(isLoading: Boolean){
        binding.btnRegister.makeInVisible(isLoading)
        binding.loading.makeVisible(isLoading)
    }
}

fun View.makeInVisible(makeVisible: Boolean){
    visibility = if (makeVisible){
        View.INVISIBLE
    }else{
        View.VISIBLE
    }
}

fun View.makeVisible(makeVisible: Boolean){
    visibility = if (makeVisible){
        View.VISIBLE
    }else{
        View.GONE
    }
}