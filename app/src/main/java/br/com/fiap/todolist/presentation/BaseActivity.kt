package br.com.fiap.todolist.presentation

import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import br.com.fiap.todolist.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

open class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        val toolbar = MaterialToolbar(baseContext).apply {
            setTheme(com.google.android.material.R.style.ThemeOverlay_MaterialComponents_Toolbar_Primary)
        }
        setSupportActionBar(toolbar)
    }

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

    fun showLogoutDialog(listener: () -> Unit) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.currentUser.let {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.logout))
            builder.setMessage(getString(R.string.logout_describe))
            builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
                firebaseAuth.signOut()
                listener()
            }
            builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                showLogoutDialog {
                    finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
