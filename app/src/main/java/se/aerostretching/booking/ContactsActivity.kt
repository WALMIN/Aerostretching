package se.aerostretching.booking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ContactsActivity : AppCompatActivity() {

    lateinit var textViewContactsPhone: TextView
    lateinit var textViewContactsEmail: TextView
    lateinit var textViewContactsPlace: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        initialize()

    }

    fun initialize(){
        textViewContactsPhone = findViewById(R.id.textViewContactsPhone)
            textViewContactsPhone.setOnClickListener{


            }

        textViewContactsEmail = findViewById(R.id.textViewContactsEmail)
            textViewContactsEmail.setOnClickListener{


            }

        textViewContactsPlace = findViewById(R.id.textViewContactsPlace)
            textViewContactsPlace.setOnClickListener{


            }

    }

}