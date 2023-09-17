package br.com.fiap.todolist.register

import androidx.lifecycle.MutableLiveData
import br.com.fiap.todolist.BaseViewModel
import br.com.fiap.todolist.utils.ValidateUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class RegisterViewModel : BaseViewModel() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    val result: MutableLiveData<RegisterState> = MutableLiveData()
    fun register(email: String, password: String, confirmPassword: String) {
        result.postValue(RegisterState.Loading)

        if (isFieldsValid(email, password, confirmPassword)) return
        if (isPasswordValid(password, confirmPassword)) return
        if (isPasswordValid(password)) return
        if (isEmailValid(email)) return

        launch {
            register(email, password)
        }
    }

    private fun register(email: String, password: String) {
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    it.result.user?.let {user ->
                        result.postValue(RegisterState.Sucess(user))
                    }
                } else {
                    result.postValue(RegisterState.Error(it.exception?.message.toString()))
                }
            }
        } catch (e: Exception) {
            result.postValue(RegisterState.Error(e.message.toString()))
        }
    }


    private fun isFieldsValid(
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (email.isEmpty() || password.isEmpty() && confirmPassword.isEmpty()) {
            result.postValue(RegisterViewModel.RegisterState.Error("Preencha todos os campos!"))
            return true
        }
        return false
    }

    private fun isEmailValid(email: String): Boolean {
        if (!ValidateUtils.isValidEmail(email)) {
            result.postValue(RegisterState.Error("E-mail inválido."))
            return true
        }
        return false
    }

    private fun isPasswordValid(password: String): Boolean {
        if (!ValidateUtils.isValidPassword(password)) {
            result.postValue(
                RegisterState.Error(
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
            result.postValue(RegisterState.Error("As senhas não coincidem."))
            return true
        }
        return false
    }

    sealed class RegisterState {
        object Loading : RegisterState()
        data class Sucess(val user: FirebaseUser) : RegisterState()
        data class Error(val message: String) : RegisterState()
    }
}