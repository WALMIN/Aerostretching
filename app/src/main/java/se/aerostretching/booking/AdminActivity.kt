package se.aerostretching.booking

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class AdminActivity : AppCompatActivity() {
    lateinit var drawerLayoutAdmin: DrawerLayout

    lateinit var textDate: EditText
    lateinit var lengthadmin: EditText
    lateinit var buttonSave: Button

    lateinit var spotsadmin: EditText
    lateinit var timeadmin: EditText

    lateinit var spinnerTitles: Spinner
    lateinit var spinnerPlaces: Spinner
    lateinit var spinnerTrainer: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        customActionBar()

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        spinnerTitles = findViewById(R.id.spinnerTitles)
        spinnerPlaces = findViewById(R.id.spinnerPlaces)
        spinnerTrainer = findViewById(R.id.spinnerTrainer)

        textDate = findViewById(R.id.editTextdate)
        timeadmin = findViewById(R.id.editTexttime)
        lengthadmin = findViewById(R.id.editTextlenght)
        spotsadmin = findViewById(R.id.editTextspots)

        buttonSave = findViewById(R.id.buttonSave)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH) + 1
        val day = c.get(Calendar.DAY_OF_MONTH)

        textDate.setOnFocusChangeListener { view, focused ->
            if(focused){
                val dialog = DatePickerDialog(this, { view, year, month, dayOfMonth ->
                    // Display Selected date in TextView
                    textDate.setText("$month$dayOfMonth$year")

                    textDate.clearFocus()
                    Tools.hideKeyboard(this, textDate)

                }, year, month, day)

                dialog.setOnCancelListener {
                    textDate.clearFocus()
                    Tools.hideKeyboard(this, textDate)

                }
                dialog.show()

            }


        }

        timeadmin.setOnFocusChangeListener { view, focused ->
            if(focused) {
                val cal = Calendar.getInstance()

                val dialog = TimePickerDialog(this, { timePicker, hour, minute ->
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, minute)
                    timeadmin.setText(SimpleDateFormat("HH:mm").format(cal.time))

                    timeadmin.clearFocus()
                    Tools.hideKeyboard(this, timeadmin)

                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true)

                dialog.setOnCancelListener {
                    textDate.clearFocus()
                    Tools.hideKeyboard(this, textDate)

                }
                dialog.show()

            }

        }

        buttonSave.setOnClickListener {
            createTraining(it as Button)
        }

    }

    fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }

    fun createTraining(view: Button) {
        val trainingItem = TrainingItem(
            textDate.text.toString(),
            timeadmin.text.toString(),
            lengthadmin.text.toString(),
            spinnerTitles.selectedItem.toString(),
            spinnerPlaces.selectedItem.toString(),
            spinnerTrainer.selectedItem.toString(),
            spotsadmin.text.toString()
        )

        if (Tools.checkAdmin(this)) {
            if (textDate.text.isEmpty() || timeadmin.text.isEmpty() || timeadmin.text.isEmpty() || lengthadmin.text.isEmpty() || spotsadmin.text.isEmpty()) {
                Toast.makeText(this, getString(R.string.trainingEmpty), Toast.LENGTH_LONG).show()

            } else {
                db.collection("trainings").add(trainingItem)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, getString(R.string.trainingAdded), Toast.LENGTH_LONG).show()
                            Log.d("!!!", "Add: ${task.exception}")

                            view.isEnabled = false

                        } else {
                            Toast.makeText(this, getString(R.string.trainingError), Toast.LENGTH_LONG).show()
                            Log.d("!!!", "Error: ${task.exception}")

                        }


                    }

            }

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
        titleView.text = getString(R.string.adminActivity)

        // Start button
        val startBtn = view.findViewById<View>(R.id.startBtn) as ImageButton
        startBtn.visibility = View.VISIBLE
        startBtn.setImageResource(R.drawable.back)
        startBtn.setOnClickListener {
            goToMainActivity()
        }
    }

    override fun onStart() {
        super.onStart()

        if (!Tools.checkAdmin(this)) {
            startActivity(Intent(this, MainActivity::class.java))

        }

    }

}


