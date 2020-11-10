package se.aerostretching.booking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBar

class BookTrainingActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_training)
        customActionBar()
        val buttonBook = findViewById<Button>(R.id.buttonBook)
        buttonBook.setOnClickListener {
            goToUpcomingTrainingsActivity()
        }
        val btn_trainer = findViewById<ImageButton>(R.id.btn_trainer)
        btn_trainer.setOnClickListener {
            goToTrainerActivity()
        }
    }

    fun goToUpcomingTrainingsActivity() {
        val intent = Intent(this, UpcomingTrainingsActivity::class.java)
        startActivity(intent)
    }

    fun goToTrainerActivity() {
        val intent = Intent(this, TrainerActivity::class.java)
        startActivity(intent)
    }


    fun customActionBar(){
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.action_bar)
        val view = supportActionBar!!.customView

        // Title
        val titleView = view.findViewById<View>(R.id.title) as TextView
        titleView.text = "(date of booking)"

        // Start button
        val startBtn = view.findViewById<View>(R.id.startBtn) as ImageButton
        startBtn.visibility = View.VISIBLE
        startBtn.setImageResource(R.drawable.back)
        startBtn.setOnClickListener {
            startActivity(Intent(this, ScheduleBookingActivity::class.java))

        }

        // End button
        val endBtn = view.findViewById<View>(R.id.endBtn) as ImageButton
        endBtn.visibility = View.VISIBLE
        endBtn.setImageResource(R.drawable.add_event)
        endBtn.setOnClickListener {

        }



    }

}