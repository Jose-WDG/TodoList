package br.com.fiap.todolist.list.model

import br.com.fiap.todolist.utils.BgColor

data class TodoListModel(
    val title: String = "",
    val textBody: String = "",
    val isFinished: Boolean = false,
    val backGroundColor: String = BgColor.getRandomPostItColor()
)