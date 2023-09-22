package br.com.fiap.todolist.presentation.registernote

import br.com.fiap.todolist.R
import br.com.fiap.todolist.presentation.BaseViewModel
import br.com.fiap.todolist.data.remote.FirebaseRepository
import br.com.fiap.todolist.presentation.todolist.model.TodoListModel
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
                result.postValue(BaseState.Error(R.string.erro_unespected))
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
                result.postValue(BaseState.Error(R.string.erro_unespected))
            }
        }
    }
}
