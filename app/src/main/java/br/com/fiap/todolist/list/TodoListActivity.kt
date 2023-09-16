package br.com.fiap.todolist.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.fiap.todolist.R

class TodoListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)
    }
}