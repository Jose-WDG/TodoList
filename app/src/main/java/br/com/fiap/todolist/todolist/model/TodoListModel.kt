package br.com.fiap.todolist.todolist.model

data class TodoListModel(
    val title: String = "",
    val textBody: String = "",
    val finished: Boolean = false,
    val backGroundColor: String = ""
)