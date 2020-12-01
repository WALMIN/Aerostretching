package se.aerostretching.booking

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object GetData {

    lateinit var trainingListAdapter: TrainingListAdapter

    val trainingListAdmin = ArrayList<TrainingItem>()
    val trainingList = ArrayList<TrainingItem>()
    val trainingListStart = ArrayList<TrainingItem>()
    val trainingListUpcoming = ArrayList<TrainingItem>()
    val trainingListHistory = ArrayList<TrainingItem>()
    val messageToClientsList = ArrayList<MessageItem>()
    val messageFromClientsList = ArrayList<MessageItem>()

    var dateFilter = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Calendar.getInstance().time).toString()

    var titleFilter = "|Aeroyoga|Aerostretching|Kids Aerostretching|Suspension"
    var placeFilter = "|Odenplan|Bromma|Solna|Malmö"
    var trainerFilter = "|Anastasia|Anna|Sofia"

    var name = ""
    var birth = ""
    var email = ""
    var phone = ""

    fun trainings(context: Context) {
        FirebaseFirestore.getInstance().collection("trainings").orderBy("date")
            .whereGreaterThanOrEqualTo("date", SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Calendar.getInstance().time))
            .addSnapshotListener { snapshot, e ->
                Log.d("!!!", "READ: Trainings")

                if(snapshot != null){
                    trainingListAdmin.clear()
                    trainingList.clear()
                    trainingListStart.clear()
                    trainingListUpcoming.clear()

                    for (document in snapshot) {
                        // Admin
                        trainingListAdmin.add(
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
                                false
                            )
                        )

                        // All trainings
                        var booked = false
                        if (document.get("participants").toString().contains(FirebaseAuth.getInstance().currentUser?.uid.toString())) {
                            booked = true

                        }

                        if (dateFilter == document.getString("date").toString() &&
                            titleFilter.contains("|" + document.getString("title").toString()) &&
                            placeFilter.contains("|" + document.getString("place").toString()) &&
                            trainerFilter.contains("|" + document.getString("trainer").toString()) &&
                            !trainingList.equals(document.id)) {

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

                    if(snapshot.size() > 2){
                        for (i in 0..4) {
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

                    }

                    // Sort
                    for(i in 0..trainingListAdmin.size){
                        trainingListAdmin.sortBy {
                            it.time
                        }

                    }

                    trainingListAdmin.sortBy {
                        it.date
                    }

                    trainingList.sortBy { it.time }

                    for(i in 0..trainingListUpcoming.size){
                        trainingListUpcoming.sortBy {
                            it.time
                        }

                    }

                    trainingListUpcoming.sortBy {
                        it.date
                    }

                    for(i in 0..trainingListStart.size){
                        trainingListStart.sortBy {
                            it.time
                        }

                    }

                    trainingListStart.sortBy {
                        it.date
                    }

                    trainingListAdapter.notifyDataSetChanged()

                }else{
                    Toast.makeText(context, context.getString(R.string.errorRetrieving), Toast.LENGTH_SHORT).show()

                }

            }

    }

    // History
    fun history(context: Context) {
        FirebaseFirestore.getInstance().collection("trainings").orderBy("date")
            .whereLessThan("date", SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Calendar.getInstance().time))
            .addSnapshotListener { snapshot, e ->
                Log.d("!!!", "READ: History")

                if(snapshot != null){
                    trainingListHistory.clear()

                    for (document in snapshot) {
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

                    // Sort
                    for(i in 0..trainingListHistory.size){
                        trainingListHistory.sortBy {
                            it.time
                        }

                    }

                    trainingListHistory.sortBy {
                        it.date
                    }

                    trainingListAdapter.notifyDataSetChanged()

                }else{
                    Toast.makeText(context, context.getString(R.string.errorRetrieving), Toast.LENGTH_SHORT).show()

                }

            }

    }

    fun profile(context: Context) {
        FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString()).get()
            .addOnSuccessListener {
                if (FirebaseAuth.getInstance().currentUser != null) {
                    Log.d("!!!", "READ: Profile")

                    name = it.get("name").toString()
                    birth = it.get("birth").toString()
                    email = it.get("email").toString()
                    phone = it.get("phone").toString()

                }

            }
            .addOnFailureListener {
                Toast.makeText(context, context.getString(R.string.errorRetrieving), Toast.LENGTH_SHORT).show()

            }

    }

    fun messageFromClients(context: Context) {
        FirebaseFirestore.getInstance().collection("messagesFromClients").orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                Log.d("!!!", "READ: Message from clients")

                if(snapshot != null){
                    messageFromClientsList.clear()

                    for (document in snapshot) {
                        messageFromClientsList.add(
                            MessageItem(
                                document.id,
                                document.getString("user").toString(),
                                document.getString("date").toString(),
                                document.getString("name").toString(),
                                document.getString("text").toString(),
                                document.getString("email").toString(),
                                false,
                            )
                        )
                    }
                    AdminActivity.messageListAdapter.notifyDataSetChanged()

                }else{
                    Toast.makeText(context, context.getString(R.string.errorRetrieving), Toast.LENGTH_SHORT).show()

                }

            }

    }

    fun messageToClients(context: Context) {
        FirebaseFirestore.getInstance().collection("messagesToClients").orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                Log.d("!!!", "READ: Message to clients")

                if(snapshot != null){
                    messageToClientsList.clear()

                    for (document in snapshot) {
                        if (document.get("user").toString() == FirebaseAuth.getInstance().currentUser?.uid.toString()) {
                            messageToClientsList.add(
                                MessageItem(
                                    document.id,
                                    document.getString("user").toString(),
                                    document.getString("date").toString(),
                                    document.getString("name").toString(),
                                    document.getString("text").toString(),
                                    document.getString("email").toString(),
                                    false,
                                )
                            )
                        }

                    }
                    MessagesActivity.messageListAdapter.notifyDataSetChanged()

                }else{
                    Toast.makeText(context, context.getString(R.string.errorRetrieving), Toast.LENGTH_SHORT).show()

                }

            }

    }

}
