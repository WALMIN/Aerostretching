package se.aerostretching.booking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MyPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)


        val imageButton1 = findViewById<ImageButton>(R.id.btn_Profile)
        val imageButton2 = findViewById<ImageButton>(R.id.imageButtonTrainings)
        val imageButton3 = findViewById<ImageButton>(R.id.imageButtonHistory)
        val imageButton4 = findViewById<ImageButton>(R.id.imageButtonMessages)

        imageButton1.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        imageButton2.setOnClickListener {
            val intent2 = Intent(this, PersonalTrainingsActivity::class.java)
            startActivity(intent2)
        }

        imageButton3.setOnClickListener {
            val intent3 = Intent(this, HistoryActivity::class.java)
            startActivity(intent3)
        }

        imageButton4.setOnClickListener {
            val intent4 = Intent(this, MessagesActivity::class.java)
            startActivity(intent4)
        }
    }
}



