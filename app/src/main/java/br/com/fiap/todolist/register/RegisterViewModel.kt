package br.com.fiap.todolist.register

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import br.com.fiap.todolist.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
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
                    result.postValue(RegisterState.Sucess(true))
                } else {
                    result.postValue(RegisterState.Error(it.exception?.message.toString()))
                }
            }
        } catch (e: Exception) {
            result.postValue(RegisterState.Error(e.message.toString()))
        }
    }

    private fun isEmailValid(email: String): Boolean {
        if (!isValidEmail(email)) {
            result.postValue(RegisterState.Error("E-mail inválido."))
            return true
        }
        return false
    }

    private fun isPasswordValid(password: String): Boolean {
        if (!isValidPassword(password)) {
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

    private fun isFieldsValid(
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (email.isEmpty() || password.isEmpty() && confirmPassword.isEmpty()) {
            result.postValue(RegisterState.Error("Preencha todos os campos!"))
            return true
        }
        return false
    }

    fun isValidPassword(password: String): Boolean {
        if (password.length < 6 || password.length > 20) {
            return false
        }
        if (!password.any { it.isUpperCase() } ||
            !password.any { it.isLowerCase() } ||
            !password.any { it.isDigit() }) {
            return false
        }
        return true
    }


    private fun isValidEmail(email: String) =
        email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    sealed class RegisterState {
        object Loading : RegisterState()
        data class Sucess(val singIn: Boolean) : RegisterState()
        data class Error(val message: String) : RegisterState()
    }
}