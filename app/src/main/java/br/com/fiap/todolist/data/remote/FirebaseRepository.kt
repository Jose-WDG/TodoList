package br.com.fiap.todolist.data.remote

import br.com.fiap.todolist.data.local.Constantes
import br.com.fiap.todolist.presentation.todolist.model.TodoListModel
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val dataBase = FirebaseDatabase.getInstance(Constantes.DATA_BASE_URL).reference
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    suspend fun requestTodoList(): DataSnapshot? {
        return userId?.let {
            val dataBaseRef = dataBase.child(Constantes.DATA_BASE_NAME).child(it)
            dataBaseRef.get().await()
        }
    }

    suspend fun deleteNote(noteId: String) {
        userId?.let {
            val noteRef = dataBase.child(Constantes.DATA_BASE_NAME).child(it).child(noteId)
            noteRef.removeValue().await()
        }
    }

    suspend fun registerNote(note: TodoListModel) {
        userId?.let {
            val key = dataBase.push().key
            dataBase.child(Constantes.DATA_BASE_NAME)
                .child(it)
                .child(key.toString())
                .setValue(note).await()
        }
    }

    suspend fun editNote(note: TodoListModel) {
        userId?.let {
            val noteRef = dataBase.child(Constantes.DATA_BASE_NAME).child(userId!!).child(note.id!!)
            noteRef.setValue(note).await()
        }
    }

    suspend fun registerUser(email: String, password: String) {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }

    suspend fun singIn(email: String, password: String): AuthResult? = firebaseAuth.signInWithEmailAndPassword(email, password).await()

    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

}
