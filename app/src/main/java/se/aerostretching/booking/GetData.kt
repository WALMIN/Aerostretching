package se.aerostretching.booking

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object GetData {

    lateinit var trainingListAdapter: TrainingListAdapter
    val trainingListStart = ArrayList<TrainingItem>()
    val trainingList = ArrayList<TrainingItem>()
    val trainingListHistory = ArrayList<TrainingItem>()


    var name = ""
    var birth = ""
    var email = ""
    var phone = ""

    fun trainings() {
        FirebaseFirestore.getInstance().collection("trainings").orderBy("date")
            .whereGreaterThanOrEqualTo(
                "date",
                SimpleDateFormat(
                    "MMddyyyy",
                    Locale.getDefault()
                ).format(Calendar.getInstance().time)
            )
            .addSnapshotListener { snapshot, e ->
                Log.d("!!!", "READ")

                trainingList.clear()
                trainingListStart.clear()
                trainingListHistory.clear()

                // All trainings
                for (document in snapshot!!) {
                    trainingList.add(
                        TrainingItem(
                            document.id,
                            document.getString("date").toString(),
                            document.getString("time").toString(),
                            document.getString("length").toString(),
                            document.getString("title").toString(),
                            document.getString("place").toString(),
                            document.getString("trainer").toString(),
                            document.getString("spots").toString(),
                            document.getString("users").toString()
                        )
                    )

                }

                // Start trainings
                for (i in 0..3) {
                    trainingListStart.add(
                        TrainingItem(
                            snapshot.documents[i].id,
                            snapshot.documents[i].getString("date").toString(),
                            snapshot.documents[i].getString("time").toString(),
                            snapshot.documents[i].getString("length").toString(),
                            snapshot.documents[i].getString("title").toString(),
                            snapshot.documents[i].getString("place").toString(),
                            snapshot.documents[i].getString("trainer").toString(),
                            snapshot.documents[i].getString("spots").toString(),
                            snapshot.documents[i].getString("users").toString()
                        )
                    )

                }
                trainingListAdapter.notifyDataSetChanged()

            }

    }

    fun profile() {
        FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString()).collection("info")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d("!!!", document.id + " => " + document.data)

                        name = document.getString("name").toString()
                        birth = document.getString("birth").toString()
                        email = document.getString("email").toString()
                        phone = document.getString("phone").toString()

                    }
                } else {
                    Log.w("!!!", "Error getting documents.", task.exception)
                }

            }

    }


    fun trainingsHistory() {
        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().currentUser?.uid.toString()).collection("myTrainings").orderBy("date")
                .whereLessThanOrEqualTo(
                        "date",
                        SimpleDateFormat(
                                "MMddyyyy",
                                Locale.getDefault()
                        ).format(Calendar.getInstance().time)
                )
                .addSnapshotListener { snapshot, e ->
                    Log.d("!!!", "READ")



                    for (document in snapshot!! ) {
                        trainingListHistory.add(
                                TrainingItem(
                                        document.id,
                                        document.getString("date").toString(),
                                        document.getString("time").toString(),
                                        document.getString("length").toString(),
                                        document.getString("title").toString(),
                                        document.getString("place").toString(),
                                        document.getString("trainer").toString(),
                                        document.getString("spots").toString(),
                                        document.getString("users").toString()
                                )
                        )

                    }

                    trainingListAdapter.notifyDataSetChanged()

                }

    }

}