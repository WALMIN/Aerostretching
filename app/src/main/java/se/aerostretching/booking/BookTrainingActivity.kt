package se.aerostretching.booking

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class BookTrainingActivity : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_training)

        customActionBar()
        initialize()

    }

    fun initialize() {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Title
        val nameView: TextView = findViewById(R.id.textViewBookTrainingName)
        nameView.text = intent.getStringExtra("title")

        // Time and length
        val timeView: TextView = findViewById(R.id.textViewBookTime)
        timeView.text = getString(R.string.trainingTime) + "${intent.getStringExtra("time")} (${intent.getStringExtra("length")}${getString(R.string.minutes)})"

        // Place
        val placeView: TextView = findViewById(R.id.textView)
        placeView.text = intent.getStringExtra("place")

        // Spots
        val spotsView: TextView = findViewById(R.id.textViewBookPlaces)
        spotsView.text = getString(R.string.trainingSpots) + "${intent.getStringExtra("spots")}"

        // Book btn
        val buttonBook: Button = findViewById(R.id.buttonBook)
        buttonBook.setOnClickListener {
            bookTraining()

        }

        // Trainer
        Glide.with(this)
            .load(FirebaseStorage.getInstance().reference.child("trainers/${intent.getStringExtra("trainer")?.toLowerCase()}_small.jpg"))
            .centerCrop()
            .circleCrop()
            .error(R.drawable.error)
            .placeholder(R.drawable.loading)
            .into(findViewById(R.id.trainerSmallImage))

        val trainerView: TextView = findViewById(R.id.textViewBookTrainer)
        trainerView.text = intent.getStringExtra("trainer")

        val btnTrainer = findViewById<ConstraintLayout>(R.id.btn_trainer)
        btnTrainer.setOnClickListener {
            goToTrainerActivity()

        }

        // Description
        val textViewBookDescription: TextView = findViewById(R.id.textViewBookDescription)
        textViewBookDescription.movementMethod = ScrollingMovementMethod.getInstance()
        when {
            intent.getStringExtra("title").equals(resources.getStringArray(R.array.titles)[0]) -> {
                textViewBookDescription.text = resources.getStringArray(R.array.trainingDescription)[0]

            }
            intent.getStringExtra("title").equals(resources.getStringArray(R.array.titles)[1]) -> {
                textViewBookDescription.text = resources.getStringArray(R.array.trainingDescription)[1]

            }
            intent.getStringExtra("title").equals(resources.getStringArray(R.array.titles)[2]) -> {
                textViewBookDescription.text = resources.getStringArray(R.array.trainingDescription)[2]

            }
            intent.getStringExtra("title").equals(resources.getStringArray(R.array.titles)[3]) -> {
                textViewBookDescription.text = resources.getStringArray(R.array.trainingDescription)[3]

            }

        }

    }

    fun goToTrainerActivity() {
        val trainerIntent = Intent(this, TrainerActivity::class.java)
        trainerIntent.putExtra("trainer", intent.getStringExtra("trainer"))
        startActivity(trainerIntent)
    }

    fun customActionBar() {
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
            addToCalendar()

        }

    }

    fun addToCalendar() {
        val startTime =
            Tools.getMillisFromDate(intent.getStringExtra("date") + " " + intent.getStringExtra("time"))
        val endTime =
            Tools.getMillisFromDate(intent.getStringExtra("date") + " " + intent.getStringExtra("time"))

        val insertCalendarIntent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.Events.TITLE, intent.getStringExtra("title"))
            .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
            .putExtra(CalendarContract.Events.EVENT_LOCATION, intent.getStringExtra("place"))
            .putExtra(CalendarContract.Events.DESCRIPTION, intent.getStringExtra("trainer"))
            .putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE)
            .putExtra(
                CalendarContract.Events.AVAILABILITY,
                CalendarContract.Events.AVAILABILITY_FREE
            )

        startActivity(insertCalendarIntent)

    }

    fun bookTraining() {
        if (intent.getStringExtra("spots")!!.toInt() > 0) {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.bookTitle))
                .setMessage(getString(R.string.bookMsg))
                .setPositiveButton(getString(R.string.book)) { dialog, id ->
                    val trainingItem = TrainingItem(
                        intent.getStringExtra("id").toString(),
                        intent.getStringExtra("date").toString(),
                        intent.getStringExtra("time").toString(),
                        intent.getStringExtra("length").toString(),
                        intent.getStringExtra("title").toString(),
                        intent.getStringExtra("place").toString(),
                        intent.getStringExtra("trainer").toString(),
                        intent.getStringExtra("spots").toString(),
                        intent.getStringExtra("users").toString(),
                        true
                    )

                    val user = auth.currentUser
                    db.collection("users").document(user!!.uid).collection("myTrainings")
                        .add(trainingItem)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                book()
                                startActivity(Intent(this, ScheduleBookingActivity::class.java))

                            }

                        }

                }
                .setNegativeButton(getString(R.string.cancel)) { dialog, id ->
                }
                .show()

        } else {
            Toast.makeText(this, getString(R.string.noSpots), Toast.LENGTH_LONG).show()

        }

    }

    private fun book() {
        Log.d("!!!", intent.getStringExtra("id").toString())

        val trainingReference = FirebaseFirestore.getInstance().collection("trainings")
            .document(intent.getStringExtra("id").toString())
        trainingReference.update("spots", (intent.getStringExtra("spots")!!.toInt() - 1).toString())

        trainingReference.update(
            "users",
            (intent.getStringExtra("users")
                .toString() + "|" + (FirebaseAuth.getInstance().currentUser?.uid))
        )

    }

}