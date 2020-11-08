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

           imageButton1.setOnClickListener{
           val intent = Intent(this,ProfileActivity::class.java)
           startActivity(intent)


       }

    }
}