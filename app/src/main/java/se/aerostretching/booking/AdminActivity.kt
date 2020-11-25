package se.aerostretching.booking

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class AdminActivity : AppCompatActivity(), OnMessageItemClickListener{

    companion object{
        lateinit var messageListAdapter: MessageListAdapter

    }

    lateinit var db: FirebaseFirestore

    // Views add
    lateinit var btnDate: Button
    lateinit var btnTime: Button
    lateinit var textViewLength: TextView
    lateinit var textViewSpots: TextView
    lateinit var spinnerTitles: Spinner
    lateinit var spinnerPlaces: Spinner
    lateinit var spinnerTrainer: Spinner
    lateinit var spotsSeekBar: SeekBar
    lateinit var lengthSeekBar: SeekBar

    lateinit var btnAddTraining: Button

    // Views messages
    lateinit var messageList: RecyclerView

    // Views client list
    lateinit var trainingClientList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        customActionBar()

        initialize()
        initializeTrainingClientList()
        initializeMessageList()

    }

    fun customActionBar() {
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

    fun initialize() {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        spinnerTitles = findViewById(R.id.spinnerTitles)
        spinnerPlaces = findViewById(R.id.spinnerPlaces)
        spinnerTrainer = findViewById(R.id.spinnerTrainer)

        btnDate = findViewById(R.id.btnDate)
        btnTime = findViewById(R.id.btnTime)

        textViewLength = findViewById(R.id.textViewLength)
        textViewSpots = findViewById(R.id.textViewSpots)

        spotsSeekBar = findViewById(R.id.spotsSeekBar)
        lengthSeekBar = findViewById(R.id.lengthSeekBar)

        btnAddTraining = findViewById(R.id.btnAddTraining)

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        btnDate.setOnClickListener {
            val dialog = DatePickerDialog(this, { view, year_, month_, day_ ->
                var year = year_.toString()
                var month = (month_ + 1).toString()
                if (month_ < 10) {
                    month = "0" + (month_ + 1).toString()
                }
                var day = "$day_"
                if (day_ < 10) {
                    day = "0$day_"
                }
                btnDate.text = "$year$month$day"

            }, year, month, day)
            dialog.setOnCancelListener {

            }
            dialog.show()

        }

        btnTime.setOnClickListener {
            TimePickerDialog(this, { timePicker, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)

                btnTime.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()

        }

        spotsSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, number: Int, b: Boolean) {
                textViewSpots.text = number.toString()

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}

        })

        lengthSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBarLenght: SeekBar, number: Int, b: Boolean) {
                textViewLength.text = number.toString()

            }

            override fun onStartTrackingTouch(seekBarLenght: SeekBar) {}
            override fun onStopTrackingTouch(seekBarLenght: SeekBar) {}

        })

        btnAddTraining.setOnClickListener {
            createTraining(it as Button)

        }

    }

    fun initializeTrainingClientList() {
        trainingClientList = findViewById(R.id.trainingClientList)
        trainingClientList.layoutManager = LinearLayoutManager(this)
        trainingClientList.adapter = TrainingClientListAdapter(GetData.trainingList)
    }

    fun initializeMessageList() {
        messageList = findViewById(R.id.messageList)
        messageList.layoutManager = LinearLayoutManager(this)
        messageListAdapter = MessageListAdapter(GetData.messageList, this, false)
        messageList.adapter = messageListAdapter
    }

    fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun createTraining(view: Button) {
        val trainingItem = TrainingItem(
            "",
            btnDate.text.toString(),
            btnTime.text.toString(),
            textViewLength.text.toString(),
            spinnerTitles.selectedItem.toString(),
            spinnerPlaces.selectedItem.toString(),
            spinnerTrainer.selectedItem.toString(),
            textViewSpots.text.toString(),
            listOf(""),false
        )

        if (Tools.checkAdmin(this)) {
            if (btnDate.text.isEmpty() || btnTime.text.isEmpty() || textViewLength.text.isEmpty() || textViewSpots.text.isEmpty()) {
                Toast.makeText(this, getString(R.string.trainingEmpty), Toast.LENGTH_LONG).show()

            } else {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.addTraining))
                    .setMessage(getString(R.string.addTrainingMsg))
                    .setPositiveButton(getString(R.string.addTraining)) { dialog, id ->
                        db.collection("trainings").add(trainingItem)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, getString(R.string.trainingAdded), Toast.LENGTH_LONG).show()
                                    Log.d("!!!", "ADDED: $task")
                                    view.isEnabled = false

                                    startActivity(Intent(this, AdminActivity::class.java))

                                } else {
                                    Toast.makeText(this, getString(R.string.trainingError), Toast.LENGTH_LONG).show()
                                    Log.d("!!!", "ERROR: ${task.exception}")

                                }

                            }

                    }
                    .setNegativeButton(getString(R.string.cancel)) { dialog, id -> }
                    .show()
            }

        }

    }

    override fun onStart() {
        super.onStart()
        if (!Tools.checkAdmin(this)) {
            startActivity(Intent(this, MainActivity::class.java))
        }else{
            GetData.message()

        }

    }

    override fun onMessageItemClick(item: MessageItem, position: Int) {

    }

}
