package se.aerostretching.booking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageListAdapter(
        var list: ArrayList<MessageItem>,
        var onClickListener: OnMessageItemClickListener,
        var history: Boolean = false
) : RecyclerView.Adapter<MessageListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
                LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(list[position], onClickListener, history)

    }

    override fun getItemCount() = list.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item: MessageItem, onClick: OnMessageItemClickListener, history: Boolean) {
            val dateView = itemView.findViewById<TextView>(R.id.messageDate)
            val nameView = itemView.findViewById<TextView>(R.id.messageName)
            val messageView = itemView.findViewById<TextView>(R.id.messageText)


            val sendBtn = itemView.findViewById<ImageView>(R.id.imageButton)

            if (!history) {
                sendBtn.visibility = View.VISIBLE
                if (item.read) {
                    sendBtn.setImageResource(R.drawable.remove)

                } else {
                    sendBtn.setImageResource(R.drawable.forward)

                }

            } else {
                sendBtn.visibility = View.GONE

            }

            dateView.text = Tools.millisToDate(item.date.toLong())
            nameView.text = item.name
            messageView.text = item.text

            // OnClick
            itemView.setOnClickListener {
                onClick.onMessageItemClick(item, adapterPosition)

            }

        }

    }

}

interface OnMessageItemClickListener {
    fun onMessageItemClick(item: MessageItem, position: Int)

}