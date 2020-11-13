package se.aerostretching.booking

import android.app.Activity
import android.content.Intent
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
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

            }
            true

        }

    }

    fun getMonth(month: String?): String? {
        return SimpleDateFormat("MMM", Locale.getDefault())
                .format(SimpleDateFormat("MM", Locale.getDefault()).parse(month))

    }

}