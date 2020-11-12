package se.aerostretching.booking

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object GetDataMyTrainings {

    lateinit var myTrainingListAdapter: TrainingListAdapter
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

    /*
    fun trainings(db: FirebaseFirestore, trainingList: ArrayList<TrainingItem>, trainingListAdapter: TrainingListAdapter){
        db.collection("trainings").orderBy("date").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("!!!", "READ")

                    for (document in task.result!!) {
                        //Log.d("!!!", document.id + " => " + document.data)

                        trainingList.add(TrainingItem(
                                document.getString("date").toString(),
                                document.getString("time").toString(),
                                document.getString("length").toString(),
                                document.getString("title").toString(),
                                document.getString("place").toString(),
                                document.getString("trainer").toString()))

                    }

                    trainingListAdapter.notifyDataSetChanged()

                } else {
                    Log.w("!!!", "Error getting documents.", task.exception)

                }

            }

    }
    */

}