package se.aerostretching.booking
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    lateinit var textEmail : EditText
    lateinit var textPassword : EditText
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        customActionBar()

        auth = FirebaseAuth.getInstance()

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

        if(auth.currentUser != null) {
            Log.d("!!!", "Current user: ${auth.currentUser?.email}")
        }

    }

    fun goToRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    fun goToStartActivity() {
        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)
    }

    fun loginUser() {
        val email = textEmail.text.toString()
        val password = textPassword.text.toString()

        if ( email.isEmpty() || password.isEmpty())
            return

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("!!!", "Login success ${auth.currentUser?.email}")
                    goToStartActivity()
                } else {
                    Log.d("!!!", "USer not loged in ${task.exception}")
                }

            }
    }

    fun customActionBar(){
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.action_bar)
        val view = supportActionBar!!.customView

        // Title
        val titleView = view.findViewById<View>(R.id.title) as TextView
        titleView.text = "AnastasiaAeroStudio"

    }
}