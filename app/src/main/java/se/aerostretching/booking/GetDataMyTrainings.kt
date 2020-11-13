package se.aerostretching.booking

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object GetDataMyTrainings {

    lateinit var myTrainingListAdapter: MyTrainingsListAdapter
    val myTrainingList = ArrayList<TrainingItem>()

    fun myTrainings(){
        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().currentUser?.uid.toString()).collection("myTrainings").orderBy("date")
                .addSnapshotListener { snapshot, e ->
                    Log.d("!!!", "READ")

                    myTrainingList.clear()

                    for (document in snapshot!!) {
                        Log.d("!!!", document.id + " => " + document.data)

                        myTrainingList.add(TrainingItem(
                                document.getString("date").toString(),
                                document.getString("time").toString(),
                                document.getString("length").toString(),
                                document.getString("title").toString(),
                                document.getString("place").toString(),
                                document.getString("trainer").toString(),
                                document.getString("spots").toString()
                        ))

                    }

                    myTrainingListAdapter.notifyDataSetChanged()
                }
    }
}