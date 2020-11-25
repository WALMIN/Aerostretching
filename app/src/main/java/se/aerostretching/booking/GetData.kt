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

    var name = ""
    var birth = ""
    var email = ""
    var phone = ""

    fun trainings() {
        FirebaseFirestore.getInstance().collection("trainings").orderBy("date")
            .whereGreaterThanOrEqualTo("date", SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Calendar.getInstance().time))
            .addSnapshotListener { snapshot, e ->
                Log.d("!!!", "READ: Trainings")

                trainingList.clear()
                trainingListStart.clear()
                trainingListUpcoming.clear()

                // All trainings
                for (document in snapshot!!) {
                    var booked = false

                    if (document.get("participants").toString().contains(FirebaseAuth.getInstance().currentUser?.uid.toString())) {
                        booked = true

                    }

                    if (titleFilter.contains("|" + document.getString("title").toString()) &&
                        placeFilter.contains("|" + document.getString("place").toString()) &&
                        trainerFilter.contains("|" + document.getString("trainer").toString()) &&
                        !trainingList.equals(document.id)
                    ) {

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
                                listOf(document.get("participants").toString()),
                                booked
                            )
                        )
                    }

                    // Upcoming
                    if (document.get("participants").toString().contains(FirebaseAuth.getInstance().currentUser?.uid.toString())) {
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
                                listOf(document.get("participants").toString()),
                                true
                            )
                        )

                    }

                }

                // Start trainings
                for (i in 0..3) {
                    var booked = false

                    if (snapshot.documents[i].get("participants").toString().contains(FirebaseAuth.getInstance().currentUser?.uid.toString())) {
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
                            listOf(snapshot.documents[i].get("participants").toString()),
                            booked
                        )
                    )

                }
                trainingListAdapter.notifyDataSetChanged()

            }

    }

    // History
    fun history() {
        FirebaseFirestore.getInstance().collection("trainings").orderBy("date")
            .whereLessThan("date", SimpleDateFormat("MMddyyyy", Locale.getDefault()).format(Calendar.getInstance().time))
            .addSnapshotListener { snapshot, e ->
                Log.d("!!!", "READ: History")

                trainingListHistory.clear()

                for (document in snapshot!!) {
                    if (document.get("participants").toString().contains(FirebaseAuth.getInstance().currentUser?.uid.toString())) {
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
                                listOf(document.get("participants").toString()),
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
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString()).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (FirebaseAuth.getInstance().currentUser != null) {
                        Log.d("!!!", "READ: Profile")

                        name = it.result?.get("name").toString()
                        birth = it.result?.get("birth").toString()
                        email = it.result?.get("email").toString()
                        phone = it.result?.get("phone").toString()

                    }

                }

            }

    }

    fun message() {
        FirebaseFirestore.getInstance().collection("messagesFromClients").orderBy("date")
            .addSnapshotListener { snapshot, e ->
                Log.d("!!!", "READ")

                messageList.clear()

                // All trainings
                for (document in snapshot!!) {

                    messageList.add(
                        MessageItem(
                            document.getString("date").toString(),
                            document.getString("name").toString(),
                            document.getString("text").toString(),
                            document.getString("email").toString(),
                            false,
                        )
                    )
                }
                AdminActivity.messageListAdapter.notifyDataSetChanged()

            }

    }
}
