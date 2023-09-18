package br.com.fiap.todolist.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class UserPreferences(context: Context) {
    companion object{
        private const val SECURE_KEY = "UserPreferences.SECURE_KEY"
        const val EMAIL_KEY = "UserPreferences.EMAIL_KEY"
        const val PASSWORD_KEY = "UserPreferences.PASSWORD_KEY"
    }

    private val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val sharedPreferences = EncryptedSharedPreferences.create(
        SECURE_KEY,
        masterKey,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun setValue(keyName: String,value: String){
        sharedPreferences.edit().putString(keyName,value).apply()
    }

    fun getValue(keyName: String): String = sharedPreferences.getString(keyName,"") ?: ""

}