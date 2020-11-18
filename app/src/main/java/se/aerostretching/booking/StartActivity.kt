package se.aerostretching.booking

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import se.aerostretching.booking.GetData.trainingsHistory

class StartActivity : AppCompatActivity(), OnTrainingItemClickListener {

    lateinit var drawerLayout: DrawerLayout

    lateinit var trainingListView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        customActionBar()
        initialize()

    }

    fun customActionBar() {
        drawerLayout = findViewById(R.id.drawerLayout)
        Tools.setMenu(this, drawerLayout)

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.action_bar)
        val view = supportActionBar!!.customView

        // Title
        val titleView = view.findViewById<View>(R.id.title) as TextView
        titleView.text = getString(R.string.startActivity)

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
            phoneIntent.data = Uri.parse("tel:" + getString(R.string.phone))

            startActivity(phoneIntent)

        }

    }

    fun initialize() {
        trainingListView = findViewById(R.id.trainingList)
        GetData.trainingListAdapter = TrainingListAdapter(GetData.trainingListStart, this)
        trainingListView.layoutManager = LinearLayoutManager(this)
        trainingListView.adapter = GetData.trainingListAdapter

    }

    fun buttonAllTrainings(view: View) {
        startActivity(Intent(this, ScheduleBookingActivity::class.java))

    }

    override fun onTrainingItemClick(item: TrainingItem, position: Int) {
        if (item.users.contains("|" + FirebaseAuth.getInstance().currentUser?.uid)) {
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
            bookIntent.putExtra("users", item.users)

            startActivity(bookIntent)

        }

    }

    override fun onStart() {
        super.onStart()

        GetData.trainings()
        GetData.profile()
        GetData.trainingsHistory()


    }

}