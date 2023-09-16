package br.com.fiap.todolist.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import br.com.fiap.todolist.R
import br.com.fiap.todolist.databinding.ActivityLoginBinding
import br.com.fiap.todolist.list.TodoListActivity
import br.com.fiap.todolist.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
//    private val btnLogin: Button by lazy { findViewById(R.id.btn_login) }
//    private val btnRegister: Button by lazy { findViewById(R.id.btn_contextual_cadastrar) }
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this,TodoListActivity::class.java))
            finish()
        }
        binding.btnContextualCadastrar.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }
}