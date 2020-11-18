package se.aerostretching.booking

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    lateinit var editTextName: EditText
    lateinit var editTextBirth: EditText
    lateinit var editTextEmail: EditText
    lateinit var editTextPhone: EditText
    lateinit var endBtn: ImageButton
             var editing = false
    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        customActionBar()

        editTextName = findViewById(R.id.userName)
        editTextBirth = findViewById(R.id.dateOfBirth)
        editTextEmail = findViewById(R.id.mailAccount)
        editTextPhone = findViewById(R.id.phoneNumber)
        button = findViewById<Button>(R.id.password)

        editTextName.setText(GetData.name)
        editTextBirth.setText(GetData.birth)
        editTextEmail.setText(GetData.email)
        editTextPhone.setText(GetData.phone)


        button.setOnClickListener(){

            openDialog()
        }

    }

     fun openDialog() {
         val logindialog = AlertDialog.Builder(this)

         val myView: View = layoutInflater.inflate(R.layout.layout_alert_dialog, null)

         logindialog.setView(myView)
         logindialog.show()

     }


    fun saveData() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        // Update database info
        val userReference = FirebaseFirestore.getInstance()
            .collection("users").document(uid.toString()).collection("info").document(GetData.id)

        userReference.update("name", editTextName.text.toString())
        userReference.update("birth", editTextBirth.text.toString())
        userReference.update("phone", editTextPhone.text.toString())
        userReference.update("email", editTextEmail.text.toString())

        // Update auth email
        val credential = EmailAuthProvider.getCredential(GetData.email, GetData.password)
        FirebaseAuth.getInstance().currentUser!!.reauthenticate(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    FirebaseAuth.getInstance().currentUser!!.updateEmail(editTextEmail.text.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("!!!", "User email address updated.")
                            }else{
                                Log.d("!!!", "Email address not updated: ${task.exception}")

                            }

                        }

                }
                Log.d("!!!", "User re-authenticated.")

        }

    }

    fun changePassword(newPassword: String){
        val credential = EmailAuthProvider.getCredential(GetData.email, GetData.password)
        FirebaseAuth.getInstance().currentUser!!.reauthenticate(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    FirebaseAuth.getInstance().currentUser!!.updatePassword(newPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("!!!", "Password updated.")
                            }else{
                                Log.d("!!!", "Password not updated: ${task.exception}")

                            }

                        }

                }
                Log.d("!!!", "User re-authenticated.")

            }

    }

    fun changeAllEditText(enabled: Boolean) {
        editTextName.isEnabled = enabled
        editTextBirth.isEnabled = enabled
        editTextEmail.isEnabled = enabled
        editTextPhone.isEnabled = enabled

    }

    fun customActionBar() {
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.action_bar)
        val view = supportActionBar!!.customView

        // Title
        val titleView = view.findViewById<View>(R.id.title) as TextView
        titleView.text = getString(R.string.profileActivity)

        // Start button
        val startBtn = view.findViewById<View>(R.id.startBtn) as ImageButton
        startBtn.visibility = View.VISIBLE
        startBtn.setImageResource(R.drawable.back)
        startBtn.setOnClickListener {
            startActivity(Intent(this, MyPageActivity::class.java))

        }

        // End button
        endBtn = view.findViewById<View>(R.id.endBtn) as ImageButton
        endBtn.visibility = View.VISIBLE
        endBtn.setImageResource(R.drawable.edit)
        endBtn.setOnClickListener {
            if (editing) {
                endBtn.setImageResource(R.drawable.edit)
                editing = false
                saveData()
                changeAllEditText(false)

                Toast.makeText(this, getString(R.string.profileUpdated), Toast.LENGTH_LONG).show()

            } else {
                endBtn.setImageResource(R.drawable.save)
                editing = true

                changeAllEditText(true)
            }


        }


    }

}