package se.aerostretching.booking

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import se.aerostretching.booking.LocaleHelper.onAttach

class MainActivity : AppCompatActivity() {

    lateinit var textEmail: EditText
    lateinit var textPassword: EditText
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        customActionBar()

        auth = FirebaseAuth.getInstance()

        Glide.with(this)
            .load(FirebaseStorage.getInstance().reference.child("start.jpg"))
            .centerCrop()
            .error(R.drawable.start_error)
            .placeholder(R.drawable.start_loading)
            .into(findViewById(R.id.imageViewStart))

        textEmail = findViewById(R.id.editTextMainEmail)
        textPassword = findViewById(R.id.editTextMainPassword)

        val loginButton = findViewById<Button>(R.id.buttonMainLogin)
        loginButton.setOnClickListener {
            loginUser()
        }

        val registerButton = findViewById<Button>(R.id.buttonMainRegister)
        registerButton.setOnClickListener {
            goToRegisterActivity()
        }

    }

    fun goToRegisterActivity() {
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()

    }

    fun goToStartActivity() {
        startActivity(Intent(this, StartActivity::class.java))
        finish()

        GetData.trainings(this)
        GetData.history(this)
        GetData.profile(this)
    }

    fun loginUser() {
        val email = textEmail.text.toString()
        val password = textPassword.text.toString()

        if (email.isEmpty() || password.isEmpty())
            return

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("!!!", "LOGIN: ${auth.currentUser?.email}")
                    goToStartActivity()

                } else {
                    Log.d("!!!", "User not logged in ${task.exception}")
                }

            }
    }

    fun forgotPasswordBtn(view: View){
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_forgot_password, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.editTextMsg)

        AlertDialog.Builder(this)
            .setTitle(R.string.forgotPassword)
            .setView(dialogLayout)
            .setPositiveButton(getString(R.string.sendTraining)) { dialog, i ->
                if(editText.text.toString().isNotEmpty()) {
                    auth.sendPasswordResetEmail(editText.text.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, getString(R.string.resetPasswordSent), Toast.LENGTH_SHORT).show()
                                Log.d("!!!", "PASSWORD: Reset sent")
                                dialog.dismiss()
                            }else{
                                Toast.makeText(this, getString(R.string.errorSending), Toast.LENGTH_SHORT).show()

                            }
                        }

                }else{
                    Toast.makeText(this, getString(R.string.noTrainingText), Toast.LENGTH_LONG).show()

                }

            }
            .show()

    }

    fun customActionBar() {
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.action_bar)
        val view = supportActionBar!!.customView

        // Title
        val titleView = view.findViewById<View>(R.id.title) as TextView
        titleView.text = getString(R.string.mainActivity)

    }

    override fun onStart() {
        super.onStart()

        if (auth.currentUser != null) {
            Log.d("!!!", "USER: ${auth.currentUser?.email}")
            goToStartActivity()

        }

    }

}