package se.aerostretching.booking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentId
import java.text.DateFormat
import java.util.*

class AdminActivity : AppCompatActivity() {
    lateinit var drawerLayoutAdmin: DrawerLayout

    lateinit var textDate : TextView
    lateinit var lengthadmin : EditText
    lateinit var buttonSave : Button


    lateinit var spotsadmin : EditText
    lateinit var timeadmin : EditText



    lateinit var spinnerTitles : Spinner
    lateinit var spinnerPlaces : Spinner
    lateinit var spinnerTrainer : Spinner

    lateinit var calendarView : CalendarView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        calendarView = findViewById(R.id.calendarView)
        // get a calendar instance
        val calendar = Calendar.getInstance()

        // calendar view date change listener
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // set the calendar date as calendar view selected date
            calendar.set(year,month,dayOfMonth)

            // set this date as calendar view selected date
            calendarView.date = calendar.timeInMillis

            // format the calendar view selected date
            val dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM)


        spinnerTitles = findViewById(R.id.spinnerTitles)
        spinnerPlaces = findViewById(R.id.spinnerPlaces)
        spinnerTrainer = findViewById(R.id.spinnerTrainer)


        textDate = findViewById(R.id.textDate)
        timeadmin = findViewById(R.id.editTexttime)
        lengthadmin = findViewById(R.id.editTextlenght)

        spotsadmin = findViewById(R.id.editTextspots)

        buttonSave = findViewById(R.id.buttonSave)
        buttonSave.setOnClickListener {
            createTraining()
        }


    }
        customActionBar()
    }

    fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }



    fun createTraining() {

        // get calendar view selected date
        val selectedDate:Long = calendarView.date

        // set the calendar date as calendar view selected date
        //calendar.timeInMillis = selectedDate

        // format the calendar view selected date
        val dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM)

//val dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM)

        val trainingItem = TrainingItem(
            calendarView.toString(),
            timeadmin.text.toString(), lengthadmin.text.toString(),
            spinnerTitles.selectedItem.toString(), spinnerPlaces.selectedItem.toString(),
            spinnerTrainer.selectedItem.toString(), spotsadmin.text.toString())

        val user = auth.currentUser
        if( user == null)
            return



        db.collection("trainings").add(trainingItem)
            .addOnCompleteListener { task ->
                Log.d("!!!", "Add: ${task.exception}")

            }


    }

    fun customActionBar() {
        drawerLayoutAdmin = findViewById(R.id.drawerLayoutAdmin)
        Tools.setMenu(this, drawerLayoutAdmin)

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.action_bar)
        val view = supportActionBar!!.customView

        // Title
        val titleView = view.findViewById<View>(R.id.title) as TextView
        titleView.text = "Add training"

        // Start button
        val startBtn = view.findViewById<View>(R.id.startBtn) as ImageButton
        startBtn.visibility = View.VISIBLE
        startBtn.setImageResource(R.drawable.back)
        startBtn.setOnClickListener {
            goToMainActivity()
        }
    }
}


