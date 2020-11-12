package se.aerostretching.booking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MyPageActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)
        customActionBar()

        val imageButton1 = findViewById<ImageButton>(R.id.btn_Profile)
        val imageButton2 = findViewById<ImageButton>(R.id.imageButtonTrainings)
        val imageButton3 = findViewById<ImageButton>(R.id.imageButtonHistory)
        val imageButton4 = findViewById<ImageButton>(R.id.imageButtonMessages)

        imageButton1.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        imageButton2.setOnClickListener {
            val intent2 = Intent(this, UpcomingTrainingsActivity::class.java)
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

    fun customActionBar() {
        drawerLayout = findViewById(R.id.drawerLayout)
        Tools.setMenu(this, drawerLayout)

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.action_bar)
        val view = supportActionBar!!.customView

        // Title
        val titleView = view.findViewById<View>(R.id.title) as TextView
        titleView.text = "My page"

        // Start button
        val startBtn = view.findViewById<View>(R.id.startBtn) as ImageButton
        startBtn.visibility = View.VISIBLE
        startBtn.setImageResource(R.drawable.menu)
        startBtn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)

        }

        // End button
        val endBtn = view.findViewById<View>(R.id.endBtn) as ImageButton
        endBtn.visibility = View.VISIBLE
        endBtn.setImageResource(R.drawable.logout)
        endBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, MainActivity::class.java))

        }

    }

}



