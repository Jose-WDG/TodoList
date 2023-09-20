package br.com.fiap.todolist.registernote

import br.com.fiap.todolist.BaseViewModel
import br.com.fiap.todolist.data.local.Constantes
import br.com.fiap.todolist.todolist.model.TodoListModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class RegisterNoteViewModel : BaseViewModel() {
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val dataBase = FirebaseDatabase.getInstance(Constantes.DATA_BASE_URL).reference
    //val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId!!)

    fun register(note: TodoListModel) {
        try {
            result.postValue(BaseState.Loading)
            val key = dataBase.push().key
            dataBase.child(Constantes.DATA_BASE_NAME)
                .child(userId!!)
                .child(key.toString())
                .setValue(note)
                .addOnSuccessListener {
                    result.postValue(BaseState.Sucess)
                }
                .addOnFailureListener {
                    result.postValue(BaseState.Error(it.message.toString()))
                }
        } catch (e: Exception) {
            result.postValue(BaseState.Error(e.message.toString()))
        }
    }
}
