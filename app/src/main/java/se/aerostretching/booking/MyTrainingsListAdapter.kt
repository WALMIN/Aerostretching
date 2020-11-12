package se.aerostretching.booking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

class MyTrainingsListAdapter(var list: ArrayList<TrainingItem>, var onClickListener: OnTrainingItemClickListener) : RecyclerView.Adapter<MyTrainingsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.training_item, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(list[position], onClickListener)

    }

    override fun getItemCount() = list.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item: TrainingItem, onClick: OnTrainingItemClickListener) {
            val timeView = itemView.findViewById<TextView>(R.id.trainingTime)
            val lengthView = itemView.findViewById<TextView>(R.id.trainingLength)
            val titleView = itemView.findViewById<TextView>(R.id.trainingTitle)
            val placeView = itemView.findViewById<TextView>(R.id.trainingPlace)
            val trainerView = itemView.findViewById<TextView>(R.id.trainingTrainer)

            timeView.text = item.time
            lengthView.text = item.length
            titleView.text = item.title
            placeView.text = item.place
            trainerView.text = item.trainer

            // OnClick
            itemView.setOnClickListener{
                onClick.onTrainingItemClick(item, adapterPosition)

            }

        }

    }

    interface OnTrainingItemClickListener{
        fun onTrainingItemClick(item: TrainingItem, position: Int)

    }

}

