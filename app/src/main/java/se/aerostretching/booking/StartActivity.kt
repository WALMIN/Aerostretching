package se.aerostretching.booking

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView

class StartActivity : AppCompatActivity(), OnTrainingItemClickListener {

    lateinit var drawerLayout: DrawerLayout

    lateinit var trainingListView: RecyclerView
    lateinit var trainingListAdapter: TrainingListAdapter
    var trainingList = ArrayList<TrainingItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        customActionBar()
        initialize()


    }

    fun customActionBar(){
        drawerLayout = findViewById(R.id.drawerLayout)
        Tools.setMenu(this, drawerLayout)

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
        startBtn.setImageResource(R.drawable.menu)
        startBtn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)

        }

        // End button
        val endBtn = view.findViewById<View>(R.id.endBtn) as ImageButton
        endBtn.visibility = View.VISIBLE
        endBtn.setImageResource(R.drawable.phone)
        endBtn.setOnClickListener {
            val phoneIntent = Intent(Intent.ACTION_DIAL)
            phoneIntent.data = Uri.parse("tel:" + "070-491 31 05")

            startActivity(phoneIntent)

        }

    }

    fun initialize() {
        trainingListView = findViewById(R.id.trainingList)
        trainingListAdapter = TrainingListAdapter(trainingList, this)
        trainingListView.layoutManager = LinearLayoutManager(this)
        trainingListView.adapter = trainingListAdapter

        // TODO - Get data from Firebase
        var item = TrainingItem("13:00", "55 min", "Aerostretching", "Odenplan, Stockholm", "Anastasia Dobrova")
        trainingList.add(item)
        item = TrainingItem("14:00", "55 min", "Aeroyoga", "Odenplan, Stockholm", "Anastasia Dobrova")
        trainingList.add(item)
        item = TrainingItem("15:00", "55 min", "Kids Aerostretching", "Odenplan, Stockholm", "Anastasia Dobrova")
        trainingList.add(item)
        item = TrainingItem("14:00", "55 min", "Aeroyoga", "Odenplan, Stockholm", "Anastasia Dobrova")
        trainingList.add(item)
        item = TrainingItem("15:00", "55 min", "Kids Aerostretching", "Odenplan, Stockholm", "Anastasia Dobrova")
        trainingList.add(item)

        trainingListAdapter.notifyDataSetChanged()

    }

    fun buttonAllTrainings(view: View){
        // TODO - Show all trainings

    }

    override fun onTrainingItemClick(item: TrainingItem, position: Int) {
        // TODO - Open training

    }

}