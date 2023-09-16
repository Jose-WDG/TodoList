package br.com.fiap.todolist.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.fiap.todolist.R
import br.com.fiap.todolist.databinding.ActivityTodoListBinding

class TodoListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTodoListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}