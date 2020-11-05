package se.aerostretching.booking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

class PersonalTrainingsActivity : AppCompatActivity() {

    lateinit var editTextApply: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_trainings)
        initialize()

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