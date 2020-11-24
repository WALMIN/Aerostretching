package se.aerostretching.booking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import se.aerostretching.booking.GetData.id

class PersonalTrainingsActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout

    lateinit var editTextApply: EditText

    lateinit var button_apply: Button
    val messageList = ArrayList<MessageItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_trainings)
        customActionBar()

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        editTextApply = findViewById(R.id.editTextApply)
        button_apply = findViewById(R.id.buttonApplyPersonalTraining)

        button_apply.setOnClickListener{
            sendMessage()
            goToStartActivity()
        }

    }

    fun customActionBar() {
        drawerLayout = findViewById(R.id.drawerLayout)
        Tools.setMenu(this, drawerLayout)

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.action_bar)
        val view = supportActionBar!!.customView

        // Title
        val titleView = view.findViewById<View>(R.id.title) as TextView
        titleView.text = getString(R.string.personalTrainingsActivity)

        // Start button
        val startBtn = view.findViewById<View>(R.id.startBtn) as ImageButton
        startBtn.visibility = View.VISIBLE
        startBtn.setImageResource(R.drawable.menu)
        startBtn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)

        }

    }
    fun goToStartActivity() {
        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)
    }

    /*fun sendPersonalTraining() {
        Toast.makeText(this, "(send request)", Toast.LENGTH_LONG).show()

    }*/



    fun sendMessage(){
        var text = editTextApply.text.toString()
        val name = GetData.name
        val email = GetData.email

        if (text.isEmpty()) {
            Toast.makeText(this, getString(R.string.noTrainingText), Toast.LENGTH_LONG).show()
        } else {
            val message = MessageItem((System.currentTimeMillis() / 1000).toString(), name, text, email, false)

            db.collection("messagesFromClients").add(message)
                .addOnSuccessListener{
                    Log.d("!!!", "saved our message")
                    Toast.makeText(this, "message sent", Toast.LENGTH_LONG).show()
                }

        }



    }


}

data class MessageItem(
    val date: String,
    val name: String,
    val text: String,
    val email: String,
    val read : Boolean)