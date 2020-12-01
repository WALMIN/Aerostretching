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
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class StartActivity : AppCompatActivity(), OnTrainingItemClickListener {

    lateinit var drawerLayout: DrawerLayout

    lateinit var noTrainingsBtn: TextView
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
        Glide.with(this)
            .load(FirebaseStorage.getInstance().reference.child("start.jpg"))
            .centerCrop()
            .error(R.drawable.start_error)
            .placeholder(R.drawable.start_loading)
            .into(findViewById(R.id.startImage))

        noTrainingsBtn = findViewById(R.id.noTrainingsBtn)
        noTrainingsBtn.setOnClickListener {
            startActivity(Intent(this, PersonalTrainingsActivity::class.java))
            finish()
        }

        trainingListView = findViewById(R.id.trainingList)
        GetData.trainingListAdapter = TrainingListAdapter(GetData.trainingListStart, this, false)
        trainingListView.layoutManager = LinearLayoutManager(this)
        trainingListView.adapter = GetData.trainingListAdapter

        noTrainingsBtn.visibility = (if ( GetData.trainingListAdapter.itemCount == 0) View.VISIBLE else View.GONE)
        GetData.trainingListAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                checkEmpty()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                checkEmpty()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                checkEmpty()
            }

            fun checkEmpty() {
                noTrainingsBtn.visibility = (if ( GetData.trainingListAdapter.itemCount == 0) View.VISIBLE else View.GONE)

            }

        })

    }

    fun buttonAllTrainings(view: View) {
        startActivity(Intent(this, ScheduleBookingActivity::class.java))
        finish()

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

            startActivity(bookIntent)
            finish()

        }

    }

    override fun onStart() {
        super.onStart()

        if(GetData.dateFilter != SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Calendar.getInstance().time).toString()){
            GetData.dateFilter = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Calendar.getInstance().time).toString()

            GetData.titleFilter = "|Aeroyoga|Aerostretching|Kids Aerostretching|Suspension"
            GetData.placeFilter = "|Odenplan|Bromma|Solna|Malmö"
            GetData.trainerFilter = "|Anastasia|Anna|Sofia"

            GetData.trainings(this)

        }

    }

}