package br.com.fiap.todolist.utils

import android.util.Patterns

object ValidateUtils {
    fun isValidEmail(email: String) =
        email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

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
}