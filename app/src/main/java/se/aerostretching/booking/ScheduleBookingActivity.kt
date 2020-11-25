package se.aerostretching.booking

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ScheduleBookingActivity : AppCompatActivity(), OnTrainingItemClickListener {

    lateinit var db: FirebaseFirestore

    lateinit var trainingListView: RecyclerView

    //
    var aeroyoga: Boolean = true
    var aerostretching: Boolean = true
    var kidsAerostretching: Boolean = true
    var suspension: Boolean = true

    var odenplan: Boolean = true
    var bromma: Boolean = true
    var solna: Boolean = true
    var malmo: Boolean = true

    var anastasia: Boolean = true
    var anna: Boolean = true
    var sofia: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_booking)
        customActionBar()
        initialize()

    }

    fun customActionBar() {
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.action_bar)
        val view = supportActionBar!!.customView

        // Title
        val titleView = view.findViewById<View>(R.id.title) as TextView
        titleView.text = getString(R.string.scheduleBookingActivity)

        // Start button
        val startBtn = view.findViewById<View>(R.id.startBtn) as ImageButton
        startBtn.visibility = View.VISIBLE
        startBtn.setImageResource(R.drawable.back)
        startBtn.setOnClickListener {
            goToStart()

        }

        // End button
        val endBtn = view.findViewById<View>(R.id.endBtn) as ImageButton
        endBtn.visibility = View.VISIBLE
        endBtn.setImageResource(R.drawable.filter)
        endBtn.setOnClickListener {
            filterDialog()

        }

    }

    fun initialize() {
        db = FirebaseFirestore.getInstance()

        trainingListView = findViewById(R.id.trainingList)
        GetData.trainingListAdapter = TrainingListAdapter(GetData.trainingList, this, false)
        trainingListView.layoutManager = LinearLayoutManager(this)
        trainingListView.adapter = GetData.trainingListAdapter

    }

    fun filterDialog() {
        val dialog = AlertDialog.Builder(this).setTitle(R.string.filter).create()
        val view: View = layoutInflater.inflate(R.layout.filter_dialog, null)

        // Title
        val aeroyogaSwitch: SwitchCompat = view.findViewById(R.id.aeroyogaSwitch)
        aeroyogaSwitch.isChecked = aeroyoga
        val aerostretchingSwitch: SwitchCompat = view.findViewById(R.id.aerostretchingSwitch)
        aerostretchingSwitch.isChecked = aerostretching
        val kidsAerostretchingSwitch: SwitchCompat =
            view.findViewById(R.id.kidsAerostretchingSwitch)
        kidsAerostretchingSwitch.isChecked = kidsAerostretching
        val suspensionSwitch: SwitchCompat = view.findViewById(R.id.suspensionSwitch)
        suspensionSwitch.isChecked = suspension

        // Place
        val odenplanSwitch: SwitchCompat = view.findViewById(R.id.odenplanSwitch)
        odenplanSwitch.isChecked = odenplan
        val brommaSwitch: SwitchCompat = view.findViewById(R.id.brommaSwitch)
        brommaSwitch.isChecked = bromma
        val solnaSwitch: SwitchCompat = view.findViewById(R.id.solnaSwitch)
        solnaSwitch.isChecked = solna
        val malmoSwitch: SwitchCompat = view.findViewById(R.id.malmoSwitch)
        malmoSwitch.isChecked = malmo

        // Place
        val anastasiaSwitch: SwitchCompat = view.findViewById(R.id.anastasiaSwitch)
        anastasiaSwitch.isChecked = anastasia
        val annaSwitch: SwitchCompat = view.findViewById(R.id.annaSwitch)
        annaSwitch.isChecked = anna
        val sofiaSwitch: SwitchCompat = view.findViewById(R.id.sofiaSwitch)
        sofiaSwitch.isChecked = sofia

        val applyBtn: Button = view.findViewById(R.id.applyBtn)
        applyBtn.setOnClickListener {
            GetData.titleFilter = ""
            GetData.placeFilter = ""
            GetData.trainerFilter = ""

            // Title
            if (aeroyogaSwitch.isChecked) {
                GetData.titleFilter = "${GetData.titleFilter}|Aeroyoga"
            }
            if (aerostretchingSwitch.isChecked) {
                GetData.titleFilter = "${GetData.titleFilter}|Aerostretching"
            }
            if (kidsAerostretchingSwitch.isChecked) {
                GetData.titleFilter = "${GetData.titleFilter}|Kids Aerostretching"
            }
            if (suspensionSwitch.isChecked) {
                GetData.titleFilter = "${GetData.titleFilter}|Suspension"
            }

            aeroyoga = aeroyogaSwitch.isChecked
            aerostretching = aerostretchingSwitch.isChecked
            kidsAerostretching = kidsAerostretchingSwitch.isChecked
            suspension = suspensionSwitch.isChecked

            // Place
            if (odenplanSwitch.isChecked) {
                GetData.placeFilter = "${GetData.placeFilter}|Odenplan"
            }
            if (brommaSwitch.isChecked) {
                GetData.placeFilter = "${GetData.placeFilter}|Bromma"
            }
            if (solnaSwitch.isChecked) {
                GetData.placeFilter = "${GetData.placeFilter}|Solna"
            }
            if (malmoSwitch.isChecked) {
                GetData.placeFilter = "${GetData.placeFilter}|Malmö"
            }

            odenplan = odenplanSwitch.isChecked
            bromma = brommaSwitch.isChecked
            solna = solnaSwitch.isChecked
            malmo = malmoSwitch.isChecked

            // Trainer
            if (anastasiaSwitch.isChecked) {
                GetData.trainerFilter = "${GetData.trainerFilter}|Anastasia"
            }
            if (annaSwitch.isChecked) {
                GetData.trainerFilter = "${GetData.trainerFilter}|Anna"
            }
            if (sofiaSwitch.isChecked) {
                GetData.trainerFilter = "${GetData.trainerFilter}|Sofia"
            }

            anastasia = anastasiaSwitch.isChecked
            anna = annaSwitch.isChecked
            sofia = sofiaSwitch.isChecked

            GetData.trainings()
            dialog.dismiss()

        }

        val closeBtn: Button = view.findViewById(R.id.closeBtn)
        closeBtn.setOnClickListener { dialog.dismiss() }

        dialog.setView(view)
        dialog.show()

    }

    override fun onTrainingItemClick(item: TrainingItem, position: Int) {
        if (item.booked) {
            Tools.removeBooking(this, item)

        } else {
            val bookIntent = Intent(this, BookTrainingActivity::class.java)

            bookIntent.putExtra("id", item.id)
            bookIntent.putExtra("date", item.date)
            bookIntent.putExtra("time", item.time)
            bookIntent.putExtra("length", item.length)
            bookIntent.putExtra("title", item.title)
            bookIntent.putExtra("place", item.place)
            bookIntent.putExtra("trainer", item.trainer)
            bookIntent.putExtra("spots", item.spots)

            finish()
            startActivity(bookIntent)

        }

    }

    fun goToStart() {
        finish()
        startActivity(Intent(this, StartActivity::class.java))

    }

    override fun onBackPressed() {
        super.onBackPressed()
        goToStart()

    }

}