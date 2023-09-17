package br.com.fiap.todolist.list

import android.content.Intent
import android.os.Bundle
import br.com.fiap.todolist.BaseActivity
import br.com.fiap.todolist.databinding.ActivityTodoListBinding
import br.com.fiap.todolist.login.LoginActivity

class TodoListActivity : BaseActivity() {
    private lateinit var binding: ActivityTodoListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onBackPressed() {
        showLogoutDialog(object : LogoutListener{
            override fun onLogout() {
                startActivity(Intent(this@TodoListActivity,LoginActivity::class.java))
                finish()
            }
        })
    }
}