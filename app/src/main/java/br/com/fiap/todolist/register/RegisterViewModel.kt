package br.com.fiap.todolist.register

import androidx.lifecycle.MutableLiveData
import br.com.fiap.todolist.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegisterViewModel : BaseViewModel() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    val result: MutableLiveData<RegisterState> = MutableLiveData()
    fun register(email: String, password: String, confirmPassword: String) {
        result.postValue(RegisterState.Loading)
        if (password != confirmPassword) {
            result.postValue(RegisterState.Error("As senhas não coincidem."))
            return
        }

        launch {
            try {
                firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                result.postValue(RegisterState.Sucess(true))

            } catch (e: Exception){
                result.postValue(RegisterState.Error(e.message.toString()))
            }
        }
    }

    sealed class RegisterState {
        object Loading : RegisterState()
        data class Sucess(val singIn: Boolean) : RegisterState()
        data class Error(val message: String) : RegisterState()
    }
}