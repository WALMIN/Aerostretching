package se.aerostretching.booking

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object GetData {

    lateinit var trainingListAdapter: TrainingListAdapter
    val trainingList = ArrayList<TrainingItem>()
    val trainingListStart = ArrayList<TrainingItem>()
    val trainingListUpcoming = ArrayList<TrainingItem>()
    val trainingListHistory = ArrayList<TrainingItem>()
    val messageList = ArrayList<MessageItem>()

    var titleFilter = "|Aeroyoga|Aerostretching|Kids Aerostretching|Suspension"
    var placeFilter = "|Odenplan|Bromma|Solna|Malmö"
    var trainerFilter = "|Anastasia|Anna|Sofia"

    var id = ""
    var name = ""
    var birth = ""
    var email = ""
    var password = ""
    var phone = ""

    fun trainings() {
        FirebaseFirestore.getInstance().collection("trainings").orderBy("date")
            .whereGreaterThanOrEqualTo("date", SimpleDateFormat("MMddyyyy", Locale.getDefault()).format(Calendar.getInstance().time))
            .addSnapshotListener { snapshot, e ->
                Log.d("!!!", "READ: Trainings")

                trainingList.clear()
                trainingListStart.clear()
                trainingListUpcoming.clear()

                // All trainings
                for (document in snapshot!!) {
                    var booked = false

                    if (document.getString("users").toString().contains("|" + FirebaseAuth.getInstance().currentUser?.uid)) {
                        booked = true

                    }

                    if(titleFilter.contains("|" + document.getString("title").toString()) &&
                        placeFilter.contains("|" + document.getString("place").toString()) &&
                        trainerFilter.contains("|" + document.getString("trainer").toString()) &&
                        !trainingList.equals(document.id)){
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
                                    document.getString("users").toString(),
                                    booked
                                )
                            )

                    }

                    // Upcoming
                    if (document.getString("users").toString().contains("|" + FirebaseAuth.getInstance().currentUser?.uid)) {
                        trainingListUpcoming.add(
                            TrainingItem(
                                document.id,
                                document.getString("date").toString(),
                                document.getString("time").toString(),
                                document.getString("length").toString(),
                                document.getString("title").toString(),
                                document.getString("place").toString(),
                                document.getString("trainer").toString(),
                                document.getString("spots").toString(),
                                document.getString("users").toString(),
                                true
                            )
                        )

                    }

                }

                // Start trainings
                for (i in 0..3) {
                    var booked = false

                    if (snapshot.documents[i].getString("users").toString().contains("|" + FirebaseAuth.getInstance().currentUser?.uid)) {
                        booked = true

                    }

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
                            snapshot.documents[i].getString("users").toString(),
                            booked
                        )
                    )

                }

                trainingListAdapter.notifyDataSetChanged()

            }

    }

    // History
    fun history(){
        FirebaseFirestore.getInstance().collection("trainings").orderBy("date")
            .whereLessThan("date", SimpleDateFormat("MMddyyyy", Locale.getDefault()).format(Calendar.getInstance().time))
            .addSnapshotListener { snapshot, e ->
                Log.d("!!!", "READ: History")

                trainingListHistory.clear()

                for (document in snapshot!!) {
                    if (document.getString("users").toString().contains("|" + FirebaseAuth.getInstance().currentUser?.uid)) {
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
                                document.getString("users").toString(),
                                true
                            )
                        )

                    }

                }

                trainingListAdapter.notifyDataSetChanged()

            }

    }

    fun profile() {
        FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString()).collection("info")
            .addSnapshotListener { snapshot, e ->
                if (FirebaseAuth.getInstance().currentUser != null) {
                    for (document in snapshot!!) {
                        Log.d("!!!", "READ: Profile")

                        id = document.id
                        name = document.getString("name").toString()
                        birth = document.getString("birth").toString()
                        email = document.getString("email").toString()
                        phone = document.getString("phone").toString()
                        password = document.getString("password").toString()

                    }

                }

            }

    }

    fun message() {
        FirebaseFirestore.getInstance().collection("messages").orderBy("date")
            .addSnapshotListener { snapshot, e ->
                Log.d("!!!", "READ")

                messageList.clear()


                // All trainings
                for (document in snapshot!!) {

                    messageList.add(
                        MessageItem(
                            document.id,
                            document.getString("date").toString(),
                            document.getString("name").toString(),
                            document.getString("text").toString(),
                            document.getString("user").toString(),
                        )
                    ) }

            }

    }

}