package se.aerostretching.booking

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ScheduleBookingActivity : AppCompatActivity(), OnTrainingItemClickListener {

    lateinit var db : FirebaseFirestore

    lateinit var trainingListView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_booking)
        customActionBar()
        initialize()

    }

    fun customActionBar(){
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.action_bar)
        val view = supportActionBar!!.customView

        // Title
        val titleView = view.findViewById<View>(R.id.title) as TextView
        titleView.text = "#" + resources.getString(R.string.app_name)

        // Start button
        val startBtn = view.findViewById<View>(R.id.startBtn) as ImageButton
        startBtn.visibility = View.VISIBLE
        startBtn.setImageResource(R.drawable.back)
        startBtn.setOnClickListener {
            startActivity(Intent(this, StartActivity::class.java))

        }

        // End button
        val endBtn = view.findViewById<View>(R.id.endBtn) as ImageButton
        endBtn.visibility = View.VISIBLE
        endBtn.setImageResource(R.drawable.filter)
        endBtn.setOnClickListener {

        }

    }

    fun initialize(){

        db = FirebaseFirestore.getInstance()
        trainingListView = findViewById(R.id.trainingList)
        GetData.trainingListAdapter = TrainingListAdapter(GetData.trainingList, this)
        trainingListView.layoutManager = LinearLayoutManager(this)
        trainingListView.adapter = GetData.trainingListAdapter

    }

    override fun onTrainingItemClick(item: TrainingItem, position: Int) {
        val bookIntent = Intent(this, BookTrainingActivity::class.java)

        bookIntent.putExtra("date", item.date)
        bookIntent.putExtra("time", item.time)
        bookIntent.putExtra("length", item.length)
        bookIntent.putExtra("title", item.title)
        bookIntent.putExtra("place", item.place)
        bookIntent.putExtra("trainer", item.trainer)
        bookIntent.putExtra("spots", item.spots)

        startActivity(bookIntent)

    }

}