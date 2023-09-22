package br.com.fiap.todolist.presentation.login

import br.com.fiap.todolist.R
import br.com.fiap.todolist.presentation.BaseViewModel
import br.com.fiap.todolist.data.remote.FirebaseRepository
import br.com.fiap.todolist.presentation.utils.ValidateUtils
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
                } ?: throw Exception()
            } catch (e: Exception) {
                result.postValue(BaseState.Error(R.string.error_login))
            }
        }
    }

    fun getCurrentUser(): FirebaseUser? = repository.getCurrentUser()

    private fun isEmailValid(email: String): Boolean {
        if (!ValidateUtils.isValidEmail(email)) {
            result.postValue(BaseState.Error(R.string.error_register_validation_email))
            return true
        }
        return false
    }

    private fun isFieldsValid(
        email: String,
        password: String
    ): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            result.postValue(BaseState.Error(R.string.error_register_validation))
            return true
        }
        return false
    }
}
