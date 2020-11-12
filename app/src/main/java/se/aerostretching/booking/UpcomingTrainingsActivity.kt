package se.aerostretching.booking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class UpcomingTrainingsActivity : AppCompatActivity() , OnTrainingItemClickListener {
    lateinit var db : FirebaseFirestore

    lateinit var myTrainingListView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming_trainings)
        initialize()
        customActionBar()

    }

    fun initialize(){

        db = FirebaseFirestore.getInstance()
        myTrainingListView = findViewById(R.id.trainingList)
        GetDataMyTrainings.myTrainingListAdapter = TrainingListAdapter(GetDataMyTrainings.myTrainingList, this)
        myTrainingListView.layoutManager = LinearLayoutManager(this)
        myTrainingListView.adapter = GetDataMyTrainings.myTrainingListAdapter

    }

    fun customActionBar(){
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.action_bar)
        val view = supportActionBar!!.customView

        // Title
        val titleView = view.findViewById<View>(R.id.title) as TextView
        titleView.text = "Upcoming trainings"

        // Start button
        val startBtn = view.findViewById<View>(R.id.startBtn) as ImageButton
        startBtn.visibility = View.VISIBLE
        startBtn.setImageResource(R.drawable.back)
        startBtn.setOnClickListener {
            startActivity(Intent(this, MyPageActivity::class.java))

        }

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

    override fun onStart() {
        super.onStart()
        GetDataMyTrainings.myTrainings()

    }

}