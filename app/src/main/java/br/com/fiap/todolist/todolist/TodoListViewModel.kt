package br.com.fiap.todolist.todolist

import androidx.lifecycle.MutableLiveData
import br.com.fiap.todolist.BaseViewModel
import br.com.fiap.todolist.data.remote.FirebaseRepository
import br.com.fiap.todolist.todolist.model.TodoListModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class TodoListViewModel : BaseViewModel() {
    val todoListState = MutableLiveData<TodoListState>()
    private val repository: FirebaseRepository = FirebaseRepository(TodoListValueEventListener(todoListState))

    fun getTodoList() {
        todoListState.postValue(TodoListState.Loading)
        repository.requestTodoList()
    }

    class TodoListValueEventListener(
        private val todoListState: MutableLiveData<TodoListState>
    ) : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val todoList = mutableListOf<TodoListModel>()

            for (childSnapshot in snapshot.children) {
                val todo = childSnapshot.getValue(TodoListModel::class.java)
                todo?.let {
                    todoList.add(it)
                }
            }
            todoListState.postValue(
                TodoListState.OnDataChange(
                    todoList
                )
            )
        }

        override fun onCancelled(error: DatabaseError) {
            todoListState.postValue(TodoListState.OnCancelled(error.message))
        }
    }

    sealed class TodoListState {
        object Loading : TodoListState()
        data class OnDataChange(val todoList: List<TodoListModel>) : TodoListState()
        data class OnCancelled(val message: String) : TodoListState()
    }
}
