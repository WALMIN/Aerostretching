package se.aerostretching.booking

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ParseException
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess


object Tools {

    fun setMenu(activity: Activity, drawerLayout: DrawerLayout) {
        val navigationView: NavigationView = activity.findViewById(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawerLayout.closeDrawers()

            when (menuItem.itemId) {
                R.id.home -> {
                    activity.startActivity(Intent(activity, StartActivity::class.java))
                    activity.finish()
                }
                R.id.myPage -> {
                    activity.startActivity(Intent(activity, MyPageActivity::class.java))
                    activity.finish()
                }
                R.id.groupTraining -> {
                    activity.startActivity(Intent(activity, ScheduleBookingActivity::class.java))
                    activity.finish()
                }
                R.id.personalTraining -> {
                    activity.startActivity(Intent(activity, PersonalTrainingsActivity::class.java))
                    activity.finish()
                }
                R.id.contacts -> {
                    activity.startActivity(Intent(activity, ContactsActivity::class.java))
                    activity.finish()
                }
                R.id.admin -> {
                    if (checkAdmin(activity)) {
                        activity.startActivity(Intent(activity, AdminActivity::class.java))
                        activity.finish()

                    }

                }
                R.id.language -> {
                    if (LocaleHelper.getLanguage(activity) == "en") {
                        LocaleHelper.setLocale(activity, "sv")

                    } else {
                        LocaleHelper.setLocale(activity, "en")

                    }
                    restartApp(activity)

                }

            }
            true

        }

    }

    fun checkAdmin(activity: Activity): Boolean {
        for (i in activity.resources.getStringArray(R.array.admins)) {
            if (FirebaseAuth.getInstance().currentUser?.uid == i) {
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

    fun getDate(year: String, month: String, day: String): String {
        val monthString = SimpleDateFormat("MMMM", Locale.getDefault())
            .format(SimpleDateFormat("MM", Locale.getDefault()).parse(month))

        val dayString = SimpleDateFormat("d", Locale.getDefault())
            .format(SimpleDateFormat("dd", Locale.getDefault()).parse(day))

        val yearString = SimpleDateFormat("yyyy", Locale.getDefault())
            .format(SimpleDateFormat("yyyy", Locale.getDefault()).parse(year))

        return "$dayString $monthString $yearString"

    }

    fun millisToDate(timestamp: Long): String {
        val calendar = Calendar.getInstance()
        val tz = TimeZone.getDefault()
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.timeInMillis))
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        val currenTimeZone = Date(timestamp * 1000)

        return sdf.format(currenTimeZone)
    }

    fun getMillisFromDate(dateFormat: String?): Long {
        var date = Date()
        val formatter = SimpleDateFormat("MMddyyyy HH:mm", Locale.getDefault())
        try {
            date = formatter.parse(dateFormat)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date.time

    }

    fun removeBooking(context: Context, item: TrainingItem) {
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.removeBookingTitle))
            .setMessage(context.getString(R.string.removeBookingMsg))
            .setPositiveButton(context.getString(R.string.unbook)) { dialog, id ->
                val trainingReference =
                    FirebaseFirestore.getInstance().collection("trainings").document(item.id)

                trainingReference.update("spots", (item.spots.toInt() + 1).toString())
                trainingReference.update(
                    "participants." + FirebaseAuth.getInstance().currentUser?.uid.toString(),
                    FieldValue.delete()
                )

                GetData.trainings(context)
                GetData.trainingListAdapter.notifyDataSetChanged()

            }
            .setNegativeButton(context.getString(R.string.cancel)) { dialog, id ->
            }
            .show()

    }

    fun restartApp(activity: Activity){
        Toast.makeText(activity, activity.resources.getString(R.string.applyingLanguage), Toast.LENGTH_SHORT).show()

        activity.startActivity(Intent(activity, MainActivity::class.java))
        activity.finishAffinity()
        activity.finish()

    }

}