package se.aerostretching.booking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrainingClientListAdapter(var list: ArrayList<TrainingItem>) : RecyclerView.Adapter<TrainingClientListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.training_clients_item, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(list[position])

    }

    override fun getItemCount() = list.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item: TrainingItem) {
            val titleView = itemView.findViewById<TextView>(R.id.textViewTrainingAdminClient)
            val trainerView = itemView.findViewById<TextView>(R.id.textViewAdminTrainer)
            val spotsView = itemView.findViewById<TextView>(R.id.textViewAdminSpots)
            val dateView = itemView.findViewById<TextView>(R.id.textViewDateAdminClient)
            val clientView = itemView.findViewById<TextView>(R.id.textViewClientList)

            titleView.text = item.title
            trainerView.text = item.trainer
            spotsView.text = "${itemView.resources.getString(R.string.spotsHint)} ${item.spots}"
            dateView.text = "${item.date.substring(2, 4)}\n${Tools.getMonth(item.date.substring(0, 2))}"

            clientView.text = item.participants[0].replace(",", "")
                .replace("{", "").replace("}", "")

        }

    }

}