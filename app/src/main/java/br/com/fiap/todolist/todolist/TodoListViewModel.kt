package br.com.fiap.todolist.todolist

import androidx.lifecycle.MutableLiveData
import br.com.fiap.todolist.BaseViewModel
import br.com.fiap.todolist.data.remote.FirebaseRepository
import br.com.fiap.todolist.todolist.model.TodoListModel
import kotlinx.coroutines.launch

class TodoListViewModel : BaseViewModel() {
    val todoListState = MutableLiveData<TodoListState>()
    private val repository: FirebaseRepository =
        FirebaseRepository()

    fun getTodoList() {
        try {
            launch {
                todoListState.postValue(TodoListState.Loading)
                val todoList = requestTodoList()
                todoListState.postValue(
                    TodoListState.OnDataChange(
                        todoList
                    )
                )
            }
        } catch (e: Exception) {
            todoListState.postValue(TodoListState.OnCancelled(e.message.toString()))
        }
    }

    private suspend fun requestTodoList():List<TodoListModel>{
        val todoList = mutableListOf<TodoListModel>()
        repository.requestTodoList()?.let {
            for (childSnapshot in it.children) {
                val todo = childSnapshot.getValue(TodoListModel::class.java)
                    ?.copy(id = childSnapshot.key)
                todo?.let {
                    todoList.add(it)
                }
            }
        }
        return todoList
    }

    fun deleteNote(noteId: String) {
        try {
            launch {
                todoListState.postValue(TodoListState.Loading)
                repository.deleteNote(noteId)
                todoListState.postValue(TodoListState.OnDataChange(requestTodoList()))
            }
        } catch (e: Exception) {
            todoListState.postValue(TodoListState.OnCancelled(e.message.toString()))
        }
    }

    sealed class TodoListState {
        object Loading : TodoListState()
        data class OnDataChange(val todoList: List<TodoListModel>) : TodoListState()
        data class OnCancelled(val message: String) : TodoListState()
    }
}
