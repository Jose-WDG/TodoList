package br.com.fiap.todolist.data.remote

import br.com.fiap.todolist.data.local.Constantes
import br.com.fiap.todolist.todolist.TodoListViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FirebaseRepository(private val listener: TodoListViewModel.TodoListValueEventListener) {
    private val dataBase = FirebaseDatabase.getInstance(Constantes.DATA_BASE_URL).reference
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    fun requestTodoList() {
        val dataBaseRef = dataBase.child(Constantes.DATA_BASE_NAME).child(userId!!)
        dataBaseRef.addListenerForSingleValueEvent(listener)
    }
}