package br.com.fiap.todolist.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import br.com.fiap.todolist.R
import br.com.fiap.todolist.list.TodoListActivity

class LoginActivity : AppCompatActivity() {
    private val btnLogin: Button by lazy { findViewById(R.id.btn_login) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            startActivity(Intent(this,TodoListActivity::class.java))
        }
    }
}