package br.com.fiap.todolist.presentation

import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

open class BaseActivity: AppCompatActivity() {

    open fun buildSnackBar(color: Int, msg: String,view: View){
        Snackbar.make(
            this,
            view,
            msg,
            Snackbar.LENGTH_SHORT
        ).setBackgroundTint(color).show()
    }

    open fun buildSucessSnackBar(msg: String, view: View){
        val color = ContextCompat.getColor(this, android.R.color.holo_green_light)
        buildSnackBar(color, msg, view)
    }

    open fun buildErrorSnackBar(msg: String,view: View){
        val color = ContextCompat.getColor(this, android.R.color.holo_red_light)
        buildSnackBar(color, msg, view)
    }

    fun showLogoutDialog(listener: LogoutListener) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.currentUser.let {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Logout")
            builder.setMessage("Você realmente deseja sair da sua conta?")
            builder.setPositiveButton("Sim") { _, _ ->
                firebaseAuth.signOut()
                listener.onLogout()
            }
            builder.setNegativeButton("Não") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }
    }

    interface LogoutListener {
        fun onLogout()
    }
}
