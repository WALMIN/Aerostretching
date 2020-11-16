package se.aerostretching.booking

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

object Tools {

    fun setMenu(activity: Activity, drawerLayout: DrawerLayout) {
        val navigationView: NavigationView = activity.findViewById(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawerLayout.closeDrawers()

            when (menuItem.itemId) {
                R.id.home -> {
                    activity.startActivity(Intent(activity, StartActivity::class.java))
                }
                R.id.myPage -> {
                    activity.startActivity(Intent(activity, MyPageActivity::class.java))
                }
                R.id.groupTraining -> {
                    activity.startActivity(Intent(activity, ScheduleBookingActivity::class.java))
                }
                R.id.personalTraining -> {
                    activity.startActivity(Intent(activity, PersonalTrainingsActivity::class.java))
                }
                R.id.contacts -> {
                    activity.startActivity(Intent(activity, ContactsActivity::class.java))
                }
                R.id.admin -> {
                    if(checkAdmin(activity)){
                        activity.startActivity(Intent(activity, AdminActivity::class.java))

                    }

                }

            }
            true

        }

    }

    fun checkAdmin(activity: Activity): Boolean{
        for(i in activity.resources.getStringArray(R.array.admins)){
            if(FirebaseAuth.getInstance().currentUser?.uid == i){
                return true

            }

        }
        Toast.makeText(activity, activity.getString(R.string.notAdmin), Toast.LENGTH_LONG).show()
        return false

    }

    fun getMonth(month: String?): String? {
        return SimpleDateFormat("MMM", Locale.getDefault())
                .format(SimpleDateFormat("MM", Locale.getDefault()).parse(month))

    }

    fun hideKeyboard(activity: Activity, view: EditText){
        val imm: InputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)

    }

}