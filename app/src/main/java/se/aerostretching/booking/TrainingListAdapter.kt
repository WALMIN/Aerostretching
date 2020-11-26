package se.aerostretching.booking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrainingListAdapter(
    var list: ArrayList<TrainingItem>,
    var onClickListener: OnTrainingItemClickListener,
    var history: Boolean = false
) : RecyclerView.Adapter<TrainingListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.training_item, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(list[position], onClickListener, history)

    }

    override fun getItemCount() = list.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item: TrainingItem, onClick: OnTrainingItemClickListener, history: Boolean) {
            val dateView = itemView.findViewById<TextView>(R.id.trainingDate)
            val timeView = itemView.findViewById<TextView>(R.id.trainingTime)
            val lengthView = itemView.findViewById<TextView>(R.id.trainingLength)
            val titleView = itemView.findViewById<TextView>(R.id.trainingTitle)
            val placeView = itemView.findViewById<TextView>(R.id.trainingPlace)
            val trainerView = itemView.findViewById<TextView>(R.id.trainingTrainer)

            val openBtn = itemView.findViewById<ImageView>(R.id.openBtn)

            if (!history) {
                openBtn.visibility = View.VISIBLE
                if (item.booked) {
                    openBtn.setImageResource(R.drawable.remove)

                } else {
                    openBtn.setImageResource(R.drawable.forward)

                }

            } else {
                openBtn.visibility = View.GONE

            }

            if(item.date.isNotEmpty()) {
                dateView.text = "${item.date.substring(6, 8)}\n${Tools.getMonth(item.date.substring(4, 6))}"

            }

            timeView.text = item.time
            lengthView.text = item.length + itemView.context.getString(R.string.minutes)
            titleView.text = item.title
            placeView.text = item.place
            trainerView.text = item.trainer

            // OnClick
            itemView.setOnClickListener {
                onClick.onTrainingItemClick(item, adapterPosition)

            }

        }

    }

}

interface OnTrainingItemClickListener {
    fun onTrainingItemClick(item: TrainingItem, position: Int)

}