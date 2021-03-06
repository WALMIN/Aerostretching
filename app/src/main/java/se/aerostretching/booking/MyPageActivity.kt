package se.aerostretching.booking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.FirebaseAuth

class MyPageActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)
        customActionBar()

        val nameView = findViewById<TextView>(R.id.textViewPageUserName)
        nameView.text = GetData.name

    }

    fun profileBtn(view: View){
        startActivity(Intent(this, ProfileActivity::class.java))
        finish()

    }

    fun bookedBtn(view: View){
        startActivity(Intent(this, UpcomingTrainingsActivity::class.java))
        finish()

    }

    fun historyBtn(view: View){
        startActivity(Intent(this, HistoryActivity::class.java))
        finish()

    }

    fun messagesBtn(view: View){
        startActivity(Intent(this, MessagesActivity::class.java))
        finish()

    }

    fun licensesBtn(view: View){
        val dialog = AlertDialog.Builder(this)
        val layout: View = layoutInflater.inflate(R.layout.web_dialog, null)
        val webView = layout.findViewById<WebView>(R.id.webView)
        webView.loadUrl("file:///android_asset/licenses_${LocaleHelper.getLanguage(this)}.html")

        dialog.setPositiveButton(resources.getString(R.string.close), null)
        dialog.setView(layout)
        dialog.show()

    }

    fun privacyPolicyBtn(view: View){
        val dialog = AlertDialog.Builder(this)
        val layout: View = layoutInflater.inflate(R.layout.web_dialog, null)
        val webView = layout.findViewById<WebView>(R.id.webView)
        webView.loadUrl("file:///android_asset/privacy_${LocaleHelper.getLanguage(this)}.html")

        dialog.setPositiveButton(resources.getString(R.string.close), null)
        dialog.setView(layout)
        dialog.show()

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
        titleView.text = getString(R.string.myPageActivity)

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
        endBtn.setImageResource(R.drawable.logout)
        endBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }

    }

    fun goToPreviousActivity(){
        startActivity(Intent(this, StartActivity::class.java))
        finish()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        goToPreviousActivity()

    }

}



