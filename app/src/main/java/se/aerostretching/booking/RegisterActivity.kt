package se.aerostretching.booking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    lateinit var r_email: EditText
    lateinit var r_password_1: EditText
    lateinit var register_btn: Button

    lateinit var r_name: EditText
    lateinit var r_phone: EditText
    lateinit var r_birthDate: EditText
    lateinit var r_password_2: EditText

    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var agreeBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        r_email = findViewById(R.id.editTextRegisterEmail)
        r_password_1 = findViewById(R.id.editTextRegisterPassword)

        r_name = findViewById(R.id.editTextRegisterName)
        r_phone = findViewById(R.id.editTextRegisterPhone)
        r_birthDate = findViewById(R.id.editTextRegisterBirth)
        r_password_2 = findViewById(R.id.editTextRegisterPassword2)
        agreeBox = findViewById(R.id.agreeBox)
        register_btn = findViewById(R.id.buttonRegister)
        register_btn.setOnClickListener {
            if (agreeBox.isChecked) {
                createLogin()
            } else {
                Toast.makeText(this, "You have not agreed to the terms", Toast.LENGTH_SHORT).show()
            }

        }
        customActionBar()

    }

    fun agreeButton(view: View) {
        val dialog = AlertDialog.Builder(this)
        val layout: View = layoutInflater.inflate(R.layout.web_dialog, null)
        val webView = layout.findViewById<WebView>(R.id.webView)
        webView.loadUrl("file:///android_asset/privacy_${LocaleHelper.getLanguage(this)}.html")

        dialog.setPositiveButton(resources.getString(R.string.close), null)
        dialog.setView(layout)
        dialog.show()

    }

    fun createLogin() {
        val email = r_email.text.toString()
        val password = r_password_1.text.toString()
        val password2 = r_password_2.text.toString()

        if (email.isEmpty() || password.isEmpty() || password != password2) {
            Toast.makeText(this, getString(R.string.enterAllFields), Toast.LENGTH_SHORT).show()

            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("!!!", "Success")
                    Toast.makeText(this, getString(R.string.registrationSuccessful), Toast.LENGTH_SHORT).show()
                    saveUserToDb()
                    goToPreviousActivity()

                } else {
                    Log.d("!!!", "User not created ${task.exception}")
                    Toast.makeText(this, getString(R.string.userNotCreated), Toast.LENGTH_SHORT).show()
                }

            }
    }

    private fun saveUserToDb() {
        val uid = auth.currentUser?.uid

        val user = User(
            r_email.text.toString(), r_name.text.toString(), r_phone.text.toString(),
            r_birthDate.text.toString()
        )

        db.collection("users").document(uid.toString()).set(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("!!!", "Success")
                } else {
                    Log.d("!!!", "User not created ${task.exception}")
                }

            }

    }

    fun customActionBar() {
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.action_bar)
        val view = supportActionBar!!.customView

        // Title
        val titleView = view.findViewById<View>(R.id.title) as TextView
        titleView.text = getString(R.string.registerActivity)

        // Start button
        val startBtn = view.findViewById<View>(R.id.startBtn) as ImageButton
        startBtn.visibility = View.VISIBLE
        startBtn.setImageResource(R.drawable.back)
        startBtn.setOnClickListener {
            goToPreviousActivity()
        }

    }

    fun goToPreviousActivity(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        goToPreviousActivity()

    }

}


class User(val email: String, val name: String, val phone: String, val birth: String)