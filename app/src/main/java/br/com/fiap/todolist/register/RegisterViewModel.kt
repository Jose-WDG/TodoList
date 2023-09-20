package br.com.fiap.todolist.register

import br.com.fiap.todolist.BaseViewModel
import br.com.fiap.todolist.utils.ValidateUtils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class RegisterViewModel : BaseViewModel() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun register(email: String, password: String, confirmPassword: String) {
        result.postValue(BaseState.Loading)

        if (isFieldsValid(email, password, confirmPassword)) return
        if (isPasswordValid(password, confirmPassword)) return
        if (isPasswordValid(password)) return
        if (isEmailValid(email)) return

        register(email, password)
    }

    private fun register(email: String, password: String) {
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    it.result.user?.let {
                        result.postValue(BaseState.Sucess)
                    }
                } else {
                    result.postValue(BaseState.Error(it.exception?.message.toString()))
                }
            }
        } catch (e: Exception) {
            result.postValue(BaseState.Error(e.message.toString()))
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
