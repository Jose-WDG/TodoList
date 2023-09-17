package br.com.fiap.todolist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.fiap.todolist.login.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel(), CoroutineScope {

    open val result: MutableLiveData<BaseState> = MutableLiveData()

    private val viewModelJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + viewModelJob

    override fun onCleared() {
        super.onCleared()
        coroutineContext.cancel()
    }

    sealed class BaseState {
        object Loading : BaseState()
        object Sucess : BaseState()
        data class Error(val message: String) : BaseState()
    }
}