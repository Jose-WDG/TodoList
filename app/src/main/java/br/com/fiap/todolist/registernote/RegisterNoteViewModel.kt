package br.com.fiap.todolist.registernote

import br.com.fiap.todolist.BaseViewModel
import br.com.fiap.todolist.data.local.Constantes
import br.com.fiap.todolist.data.remote.FirebaseRepository
import br.com.fiap.todolist.todolist.model.TodoListModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class RegisterNoteViewModel : BaseViewModel() {
    private val repository = FirebaseRepository()

    fun register(note: TodoListModel) {
        try {
            launch {
                result.postValue(BaseState.Loading)
                repository.registerNote(note)
                result.postValue(BaseState.Sucess)
            }
        } catch (e: Exception) {
            result.postValue(BaseState.Error(e.message.toString()))
        }
    }

    fun editNote(note: TodoListModel) {
        try {
            launch {
                result.postValue(BaseState.Loading)
                repository.editNote(note)
                result.postValue(BaseState.Sucess)
            }
        } catch (e: Exception) {
            result.postValue(BaseState.Error(e.message.toString()))
        }
    }
}
