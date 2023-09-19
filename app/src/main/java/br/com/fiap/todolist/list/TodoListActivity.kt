package br.com.fiap.todolist.list

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.fiap.todolist.BaseActivity
import br.com.fiap.todolist.BaseViewModel
import br.com.fiap.todolist.databinding.ActivityTodoListBinding
import br.com.fiap.todolist.list.adapter.TodoListAdapter
import br.com.fiap.todolist.list.model.TodoListModel
import br.com.fiap.todolist.login.LoginActivity

class TodoListActivity : BaseActivity() {
    private lateinit var binding: ActivityTodoListBinding
    private val viewModel: TodoListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObsrever()

        binding.todoListRecyclerview.apply {
            adapter = TodoListAdapter(viewModel.getTodoList())
            layoutManager = LinearLayoutManager(this@TodoListActivity)
        }
    }

    private fun initObsrever() {
        viewModel.result.observe(this){
            when(it){
                is BaseViewModel.BaseState.Loading -> {

                }

                is BaseViewModel.BaseState.Sucess -> {

                }

                is BaseViewModel.BaseState.Error -> {

                }
                else -> {}
            }
        }
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