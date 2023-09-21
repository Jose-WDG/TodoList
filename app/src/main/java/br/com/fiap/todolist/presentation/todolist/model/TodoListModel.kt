package br.com.fiap.todolist.presentation.todolist.model

import java.io.Serializable

data class TodoListModel(
    val id: String? = null,
    var title: String = "",
    var textBody: String = "",
    val finished: Boolean = false,
    val backGroundColor: String = ""
): Serializable