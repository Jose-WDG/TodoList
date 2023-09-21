package br.com.fiap.todolist.login

import br.com.fiap.todolist.BaseViewModel
import br.com.fiap.todolist.data.remote.FirebaseRepository
import br.com.fiap.todolist.utils.ValidateUtils
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: FirebaseRepository
) : BaseViewModel() {

    fun singIn(email: String, password: String) {
        launch {
            try {
                result.postValue(BaseState.Loading)
                if (isFieldsValid(email, password)) return@launch
                if (isEmailValid(email)) return@launch

                val singIn = repository.singIn(email, password)
                singIn?.let {
                    result.postValue(BaseState.Sucess)
                } ?: throw IllegalStateException("Usuário não encontrado")
            } catch (e: Exception) {
                result.postValue(BaseState.Error("Falha no login: ${e.message}"))
            }
        }
    }

    fun getCurrentUser(): FirebaseUser? = repository.getCurrentUser()

    private fun isEmailValid(email: String): Boolean {
        if (!ValidateUtils.isValidEmail(email)) {
            result.postValue(BaseState.Error("E-mail inválido."))
            return true
        }
        return false
    }

    private fun isFieldsValid(
        email: String,
        password: String
    ): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            result.postValue(BaseState.Error("Preencha todos os campos!"))
            return true
        }
        return false
    }
}
