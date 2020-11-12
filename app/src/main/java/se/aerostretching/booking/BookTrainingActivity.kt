package se.aerostretching.booking

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BookTrainingActivity : AppCompatActivity() {

    lateinit var db : FirebaseFirestore
    lateinit var auth : FirebaseAuth



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_training)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        customActionBar()
        val buttonBook = findViewById<Button>(R.id.buttonBook)
        buttonBook.setOnClickListener {
            val myTrainingItem = TrainingItem(
                    intent.getStringExtra("date").toString(),
                    intent.getStringExtra("time").toString(),
                    intent.getStringExtra("length").toString(),
                    intent.getStringExtra("title").toString(),
                    intent.getStringExtra("place").toString(),
                    intent.getStringExtra("trainer").toString(),
                    intent.getStringExtra("spots").toString())


            val user = auth.currentUser
            db.collection("users").document(user!!.uid).collection("myTrainings").add(myTrainingItem)
                    .addOnCompleteListener { task ->
                        Log.d("!!!", "Add: ${task.exception}")

                    }
            finish()
        }
        val btn_trainer = findViewById<ImageButton>(R.id.btn_trainer)
        btn_trainer.setOnClickListener {
            goToTrainerActivity()
        }
        val nameView : TextView = findViewById<View>(R.id.textViewBookTrainingName) as TextView
        nameView.text = intent.getStringExtra("title")

        val timeView : TextView = findViewById<View>(R.id.textViewBookTime) as TextView
        timeView.text = "Start: ${intent.getStringExtra("time")} (${intent.getStringExtra("length")} )"

        val placeView : TextView = findViewById<View>(R.id.textView) as TextView
        placeView.text = intent.getStringExtra("place")

        val trainerView : TextView = findViewById<View>(R.id.textViewBookTrainer) as TextView
        trainerView.text = intent.getStringExtra("trainer")

        val spotsView : TextView = findViewById<View>(R.id.textViewBookPlaces) as TextView
        spotsView.text = intent.getStringExtra("spots")


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
        titleView.text = intent.getStringExtra("date")

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