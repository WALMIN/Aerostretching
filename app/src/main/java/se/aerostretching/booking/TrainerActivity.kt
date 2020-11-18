package se.aerostretching.booking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage

class TrainerActivity : AppCompatActivity() {

    lateinit var textViewTrainerName: TextView
    lateinit var textViewTrainerDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trainer)
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
        titleView.text = getString(R.string.trainerActivity)

        // Start button
        val startBtn = view.findViewById<View>(R.id.startBtn) as ImageButton
        startBtn.visibility = View.VISIBLE
        startBtn.setImageResource(R.drawable.back)
        startBtn.setOnClickListener {
            finish()
        }

    }

    fun initialize(){
        Glide.with(this)
            .load(FirebaseStorage.getInstance().reference.child("trainers/${intent.getStringExtra("trainer")?.toLowerCase()}.jpg"))
            .centerCrop()
            .error(R.drawable.start_error)
            .placeholder(R.drawable.start_loading)
            .into(findViewById(R.id.trainerImage))

        textViewTrainerName = findViewById(R.id.textViewTrainerName)
        textViewTrainerName.text = intent.getStringExtra("trainer")

        textViewTrainerDescription = findViewById(R.id.textViewTrainerDescription)
        when {
            intent.getStringExtra("trainer").equals(resources.getStringArray(R.array.trainer)[0]) -> {
                textViewTrainerDescription.text = resources.getStringArray(R.array.trainerDescription)[0]

            }
            intent.getStringExtra("trainer").equals(resources.getStringArray(R.array.trainer)[1]) -> {
                textViewTrainerDescription.text = resources.getStringArray(R.array.trainerDescription)[1]

            }
            intent.getStringExtra("trainer").equals(resources.getStringArray(R.array.trainer)[2]) -> {
                textViewTrainerDescription.text = resources.getStringArray(R.array.trainerDescription)[2]

            }

        }

    }

}