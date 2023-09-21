package br.com.fiap.todolist.registernote

import br.com.fiap.todolist.BaseViewModel
import br.com.fiap.todolist.data.remote.FirebaseRepository
import br.com.fiap.todolist.todolist.model.TodoListModel
import kotlinx.coroutines.launch

class RegisterNoteViewModel(
    private val repository: FirebaseRepository
) : BaseViewModel() {

    fun register(note: TodoListModel) {
        launch {
            try {
                result.postValue(BaseState.Loading)
                repository.registerNote(note)
                result.postValue(BaseState.Sucess)
            } catch (e: Exception) {
                result.postValue(BaseState.Error(e.message.toString()))
            }
        }
    }

    fun editNote(note: TodoListModel) {
        launch {
            try {
                result.postValue(BaseState.Loading)
                repository.editNote(note)
                result.postValue(BaseState.Sucess)
            } catch (e: Exception) {
                result.postValue(BaseState.Error(e.message.toString()))
            }
        }
    }
}
