package se.aerostretching.booking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileActivity : AppCompatActivity() {


    lateinit var editTextName: EditText
    lateinit var editTextBirth: EditText
    lateinit var editTextEmail: EditText
    lateinit var editTextPhone: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        customActionBar()


        editTextName = findViewById(R.id.userName)
        editTextBirth = findViewById(R.id.dateOfBirth)
        editTextEmail = findViewById(R.id.mailAccount)
        editTextPhone = findViewById(R.id.phoneNumber)

        editTextName.setText(GetData.name)
        editTextBirth.setText(GetData.birth)
        editTextEmail.setText(GetData.email)
        editTextPhone.setText(GetData.phone)


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
        val endBtn = view.findViewById<View>(R.id.endBtn) as ImageButton
        endBtn.visibility = View.VISIBLE
        endBtn.setImageResource(R.drawable.edit)
        endBtn.setOnClickListener {
            startActivity(Intent(this, ContactsActivity::class.java))

        }


    }

}