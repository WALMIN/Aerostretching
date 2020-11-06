package se.aerostretching.booking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar

class PersonalTrainingsActivity : AppCompatActivity() {

    lateinit var editTextApply: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_trainings)
        customActionBar()
        initialize()

    }

    fun customActionBar(){
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.action_bar)
        val view = supportActionBar!!.customView

        // Title
        val titleView = view.findViewById<View>(R.id.title) as TextView
        titleView.text = "Personal trainings"

        // Start button
        val startBtn = view.findViewById<View>(R.id.startBtn) as ImageButton
        startBtn.visibility = View.VISIBLE
        startBtn.setImageResource(R.drawable.menu)
        startBtn.setOnClickListener {


        }

    }

    fun initialize(){
        editTextApply = findViewById(R.id.editTextApply)

    }

    fun applyPersonalTraining(view: View){
        if(editTextApply.text.toString().trim().isNotEmpty()){
            sendPersonalTraining()

        }else{
            Toast.makeText(this, "Du måste skriva något först.", Toast.LENGTH_LONG).show()

        }

    }

    fun sendPersonalTraining(){
        Toast.makeText(this, "(send request)", Toast.LENGTH_LONG).show()

    }

}