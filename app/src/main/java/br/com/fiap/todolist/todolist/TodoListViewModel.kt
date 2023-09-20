package br.com.fiap.todolist.todolist

import androidx.lifecycle.MutableLiveData
import br.com.fiap.todolist.BaseViewModel
import br.com.fiap.todolist.data.local.Constantes
import br.com.fiap.todolist.todolist.model.TodoListModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TodoListViewModel : BaseViewModel() {
    private val dataBase = FirebaseDatabase.getInstance(Constantes.DATA_BASE_URL).reference
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val todoList: ArrayList<TodoListModel> = arrayListOf()
    val todoListState = MutableLiveData<TodoListState>()

    fun getTodoList() {
        todoListState.postValue(TodoListState.Loading)
        val dataBaseRef = dataBase.child(Constantes.DATA_BASE_NAME).child(userId!!)
        dataBaseRef.addListenerForSingleValueEvent(
            TodoListValueEventListener(
                todoList,
                todoListState
            )
        )
    }

    class TodoListValueEventListener(
        private val currentTodoList: ArrayList<TodoListModel>,
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

            if (todoList != currentTodoList) {
                currentTodoList.clear()
                currentTodoList.addAll(todoList)
                todoListState.postValue(
                    TodoListState.OnDataChange(
                        todoList
                    )
                )
            }
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
