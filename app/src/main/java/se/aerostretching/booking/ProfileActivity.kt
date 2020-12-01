package se.aerostretching.booking

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
    lateinit var editTextPhone: EditText

    lateinit var endBtn: ImageButton

    var editing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        customActionBar()

        editTextName = findViewById(R.id.userName)
        editTextBirth = findViewById(R.id.dateOfBirth)
        editTextPhone = findViewById(R.id.phoneNumber)

        editTextName.setText(GetData.name)
        editTextBirth.setText(GetData.birth)
        editTextPhone.setText(GetData.phone)

    }

    fun saveData() {
        // Update database info
        val userReference = FirebaseFirestore.getInstance()
            .collection("users").document(FirebaseAuth.getInstance().currentUser?.uid.toString())

        userReference.update("name", editTextName.text.toString())
        userReference.update("birth", editTextBirth.text.toString())
        userReference.update("phone", editTextPhone.text.toString())

    }

    fun changeEmailBtn(view: View) {
        val dialog = AlertDialog.Builder(this).create()

        val dialogView: View = layoutInflater.inflate(R.layout.change_email_dialog, null)
        val passwordView = dialogView.findViewById<EditText>(R.id.currentPassword)
        val emailView = dialogView.findViewById<EditText>(R.id.newEmail)

        val confirmBtn = dialogView.findViewById<Button>(R.id.confirmBtn)
        val cancelBtn = dialogView.findViewById<Button>(R.id.cancelBtn)

        confirmBtn.setOnClickListener() {
            if (emailView.text.toString().isNotEmpty() && passwordView.text.toString().isNotEmpty()) {
                changeEmail(emailView.text.toString(), passwordView.text.toString())
                dialog.dismiss()

            } else {
                Toast.makeText(this, getString(R.string.emaildNotChanged), Toast.LENGTH_LONG).show()
            }

        }

        dialog.setView(dialogView)
        dialog.setTitle(R.string.changeEmail)
        dialog.show()

        cancelBtn.setOnClickListener() {
            dialog.dismiss()

        }

    }

    fun changeEmail(newEmail: String, password: String) {
        // Update auth email
        val credential = EmailAuthProvider.getCredential(GetData.email, password)
        FirebaseAuth.getInstance().currentUser!!.reauthenticate(credential)
            .addOnSuccessListener {
                Log.d("!!!", "User re-authenticated.")

                FirebaseAuth.getInstance().currentUser!!.updateEmail(newEmail)
                    .addOnSuccessListener {
                        GetData.profile(this)

                        Toast.makeText(this, getString(R.string.emailUpdated), Toast.LENGTH_LONG).show()
                        Log.d("!!!", "User email address updated.")

                        val userReference = FirebaseFirestore.getInstance().collection("users")
                                .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
                        userReference.update("email", newEmail)

                    }
                    .addOnFailureListener {
                        Toast.makeText(this, getString(R.string.emaildNotChanged), Toast.LENGTH_LONG).show()
                        Log.d("!!!", "Email address not updated: $it")


                    }
            }
            .addOnFailureListener {
                Log.d("!!!", "Error when trying to re-authenticate.")
                Toast.makeText(this, getString(R.string.enterAllFields), Toast.LENGTH_LONG).show()

            }

    }

    fun changePasswordBtn(view: View) {
        val dialog = AlertDialog.Builder(this).create()

        val dialogView: View = layoutInflater.inflate(R.layout.change_password_dialog, null)
        val confirmBtn = dialogView.findViewById<Button>(R.id.Confirm_button)
        val oldPasswordView = dialogView.findViewById<EditText>(R.id.OldPassword)
        val newPasswordView = dialogView.findViewById<EditText>(R.id.NewPassword)
        val repeatPasswordView = dialogView.findViewById<EditText>(R.id.RepeatPassword)
        val cancelBtn = dialogView.findViewById<Button>(R.id.Cancel)

        confirmBtn.setOnClickListener() {
            if (oldPasswordView.text.isNotEmpty() && newPasswordView.text.toString() == repeatPasswordView.text.toString()) {
                changePassword(newPasswordView.text.toString(), oldPasswordView.text.toString())
                dialog.dismiss()

            } else {
                Toast.makeText(this, getString(R.string.passwordNoMatch), Toast.LENGTH_SHORT)
                    .show()
            }

        }

        dialog.setView(dialogView)
        dialog.setTitle(R.string.password)
        dialog.show()

        cancelBtn.setOnClickListener() {
            dialog.dismiss()

        }

    }

    fun changePassword(newPassword: String, currentPassword: String) {
        // Update auth password
        val credential = EmailAuthProvider.getCredential(GetData.email, currentPassword)
        FirebaseAuth.getInstance().currentUser!!.reauthenticate(credential)
            .addOnSuccessListener {
                Log.d("!!!", "User re-authenticated.")
                FirebaseAuth.getInstance().currentUser!!.updatePassword(newPassword)
                    .addOnSuccessListener {
                        Toast.makeText(this, getString(R.string.passwordUpdated), Toast.LENGTH_LONG).show()
                        Log.d("!!!", "Password updated.")

                    }
                    .addOnFailureListener{
                        Toast.makeText(this, getString(R.string.passwordNotChanged), Toast.LENGTH_SHORT).show()
                        Log.d("!!!", "Password not updated: $it")

                    }
                }
            .addOnFailureListener{
                Log.d("!!!", "Error when trying to re-authenticate.")
                Toast.makeText(this, getString(R.string.enterAllFields), Toast.LENGTH_LONG).show()

            }

    }

    fun changeAllEditText(enabled: Boolean) {
        editTextName.isEnabled = enabled
        editTextBirth.isEnabled = enabled
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
            goToPreviousActivity()

        }

        // End button
        endBtn = view.findViewById<View>(R.id.endBtn) as ImageButton
        endBtn.visibility = View.VISIBLE
        endBtn.setImageResource(R.drawable.edit)
        endBtn.setOnClickListener {
            if (editing) {
                if (editTextName.text.isEmpty() || editTextBirth.text.isEmpty() || editTextPhone.text.isEmpty()) {
                    Toast.makeText(this, getString(R.string.enterAllFields), Toast.LENGTH_LONG).show()

                } else {
                    endBtn.setImageResource(R.drawable.edit)
                    editing = false
                    saveData()
                    GetData.profile(this)
                    changeAllEditText(false)

                    Toast.makeText(this, getString(R.string.profileUpdated), Toast.LENGTH_LONG).show()

                }

            } else {
                endBtn.setImageResource(R.drawable.save)
                editing = true

                changeAllEditText(true)
            }

        }

    }

    fun goToPreviousActivity(){
        startActivity(Intent(this, MyPageActivity::class.java))
        finish()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        goToPreviousActivity()

    }

}