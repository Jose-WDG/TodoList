package br.com.fiap.todolist.list

import br.com.fiap.todolist.BaseViewModel
import br.com.fiap.todolist.list.model.TodoListModel

class TodoListViewModel: BaseViewModel() {

    fun getTodoList(): List<TodoListModel> = listOf(
        TodoListModel("Cagar","Preciso lembrar de cagar", false)
    )
}