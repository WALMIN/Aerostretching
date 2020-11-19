package se.aerostretching.booking

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ParseException
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
                    if (checkAdmin(activity)) {
                        activity.startActivity(Intent(activity, AdminActivity::class.java))

                    }

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

    fun hideKeyboard(activity: Activity, view: EditText) {
        val imm: InputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)

    }

    fun getMillisFromDate(dateFormat: String?): Long {
        var date = Date()
        val formatter = SimpleDateFormat("MMddyyyy HH:mm")
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
                val trainingReference = FirebaseFirestore.getInstance().collection("trainings").document(item.id)

                trainingReference.update("spots", (item.spots.toInt() + 1).toString())
                trainingReference.update("users", item.users.replace("|${FirebaseAuth.getInstance().currentUser?.uid}", ""))

                GetData.trainings()
                GetData.trainingListAdapter.notifyDataSetChanged()

            }
            .setNegativeButton(context.getString(R.string.cancel)) { dialog, id ->
            }
            .show()

    }

}