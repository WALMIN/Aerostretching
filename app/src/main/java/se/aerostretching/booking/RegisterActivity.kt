package se.aerostretching.booking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentId


lateinit var r_email : EditText
lateinit var r_password_1 : EditText
lateinit var register_btn : Button

lateinit var r_name : EditText
lateinit var r_surname : EditText
lateinit var r_phone : EditText
lateinit var r_birthDate : EditText
lateinit var r_password_2 : EditText

lateinit var auth : FirebaseAuth
lateinit var db : FirebaseFirestore


class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        r_email = findViewById(R.id.editTextRegisterEmail)
        r_password_1 = findViewById(R.id.editTextRegisterPassword)

        r_surname = findViewById(R.id.editTextRegisterSurname)
        r_name = findViewById(R.id.editTextRegisterName)
        r_phone = findViewById(R.id.editTextRegisterPhone)
        r_birthDate = findViewById(R.id.editTextRegisterBirth)
        r_password_2 = findViewById(R.id.editTextRegisterPassword2)

        register_btn = findViewById(R.id.buttonRegister)
        register_btn.setOnClickListener {
            createLogin()
        }
        customActionBar()

    }

    fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun createLogin() {

        val email = r_email.text.toString()
        val password = r_password_1.text.toString()
        val password2 = r_password_2.text.toString()

        if (email.isEmpty() || password.isEmpty() || password != password2) {
            Toast.makeText(this, "Please enter text and try again", Toast.LENGTH_SHORT).show()
            return
        }



        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("!!!", "Success")
                        saveUserToDb()
                        goToMainActivity()
                    } else {
                        Log.d("!!!", "User not created ${task.exception}")
                        Toast.makeText(this, "User not created", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun saveUserToDb() {
        //val uid = FirebaseAuth.getInstance().uid?:""
        val uid = auth.currentUser?.uid

        val user = User(r_email.text.toString(), r_name.text.toString(), r_surname.text.toString(), r_phone.text.toString(),
                r_birthDate.text.toString())

        db.collection("users").document(uid.toString()).collection("info").add(user)
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
        titleView.text = "Register"

        // Start button
        val startBtn = view.findViewById<View>(R.id.startBtn) as ImageButton
        startBtn.visibility = View.VISIBLE
        startBtn.setImageResource(R.drawable.back)
        startBtn.setOnClickListener {
            goToMainActivity()
        }
    }
}


class User(val email: String, val name : String, val surname : String, val phone : String, val birth : String)