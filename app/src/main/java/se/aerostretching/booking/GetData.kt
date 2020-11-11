package se.aerostretching.booking

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

object GetData {

    lateinit var trainingListAdapter: TrainingListAdapter
    val trainingList = ArrayList<TrainingItem>()

    fun trainings(){
        FirebaseFirestore.getInstance().collection("trainings").orderBy("date")
            .addSnapshotListener { snapshot, e ->
                Log.d("!!!", "READ")

                trainingList.clear()

                for (document in snapshot!!) {
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