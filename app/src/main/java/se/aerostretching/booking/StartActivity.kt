package se.aerostretching.booking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StartActivity : AppCompatActivity(), OnTrainingItemClickListener {

    lateinit var trainingListView: RecyclerView
    lateinit var trainingListAdapter: TrainingListAdapter
    var trainingList = ArrayList<TrainingItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        initialize()

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