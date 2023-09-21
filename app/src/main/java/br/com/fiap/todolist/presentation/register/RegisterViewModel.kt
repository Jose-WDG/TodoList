package br.com.fiap.todolist.presentation.register

import br.com.fiap.todolist.presentation.BaseViewModel
import br.com.fiap.todolist.data.remote.FirebaseRepository
import br.com.fiap.todolist.presentation.utils.ValidateUtils
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: FirebaseRepository
) : BaseViewModel() {

    fun register(email: String, password: String, confirmPassword: String) {
        result.postValue(BaseState.Loading)

        if (isFieldsValid(email, password, confirmPassword)) return
        if (isPasswordValid(password, confirmPassword)) return
        if (isPasswordValid(password)) return
        if (isEmailValid(email)) return

        register(email, password)
    }

    private fun register(email: String, password: String) {
        launch {
            try {
                repository.registerUser(email, password)
                result.postValue(BaseState.Sucess)
            } catch (e: Exception) {
                result.postValue(BaseState.Error(e.message.toString()))
            }
        }

    }


    private fun isFieldsValid(
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (email.isEmpty() || password.isEmpty() && confirmPassword.isEmpty()) {
            result.postValue(BaseState.Error("Preencha todos os campos!"))
            return true
        }
        return false
    }

    private fun isEmailValid(email: String): Boolean {
        if (!ValidateUtils.isValidEmail(email)) {
            result.postValue(BaseState.Error("E-mail inválido."))
            return true
        }
        return false
    }

    private fun isPasswordValid(password: String): Boolean {
        if (!ValidateUtils.isValidPassword(password)) {
            result.postValue(
                BaseState.Error(
                    "Senha inválida. Deve ter entre 6-20 caracteres, " +
                            "incluir uma letra maiúscula, uma letra minúscula e um número."
                )
            )
            return true
        }
        return false
    }

    private fun isPasswordValid(password: String, confirmPassword: String): Boolean {
        if (password != confirmPassword) {
            result.postValue(BaseState.Error("As senhas não coincidem."))
            return true
        }
        return false
    }
}
