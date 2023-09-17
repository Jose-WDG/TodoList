package br.com.fiap.todolist.login

import br.com.fiap.todolist.BaseViewModel
import br.com.fiap.todolist.utils.ValidateUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : BaseViewModel() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun singIn(email: String, password: String) {
        try {
            result.postValue(BaseState.Loading)
            if (isFieldsValid(email, password)) return
            if (isEmailValid(email)) return

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        it.result.user?.let {
                            result.postValue(BaseState.Sucess)
                        } ?: throw IllegalStateException("Usuário não pode ser null!")
                    } else {
                        result.postValue(BaseState.Error("Falha no login: ${it.exception?.message}"))
                    }
                }
        } catch (e: Exception) {
            result.postValue(BaseState.Error("Falha no login: ${e.message}"))
        }
    }

    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser


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
