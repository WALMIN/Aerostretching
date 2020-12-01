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
        var onLongClickListener: OnMessageItemClickListener,
        var history: Boolean = false
) : RecyclerView.Adapter<MessageListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(list[position], onClickListener, onLongClickListener, history)

    }

    override fun getItemCount() = list.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item: MessageItem, onClick: OnMessageItemClickListener, onLongClick: OnMessageItemClickListener, history: Boolean) {
            val dateView = itemView.findViewById<TextView>(R.id.messageDate)
            val nameView = itemView.findViewById<TextView>(R.id.messageName)
            val messageView = itemView.findViewById<TextView>(R.id.messageText)

            dateView.text = Tools.millisToDate(item.date.toLong())
            nameView.text = item.email
            messageView.text = item.text

            // OnClick
            itemView.setOnClickListener {
                onClick.onMessageItemClick(item, adapterPosition)

            }

            // OnLongClick
            itemView.setOnLongClickListener {
                onLongClick.onMessageItemLongClick(item, adapterPosition)

                return@setOnLongClickListener true

            }

        }

    }

}

interface OnMessageItemClickListener {

    fun onMessageItemClick(item: MessageItem, position: Int)
    fun onMessageItemLongClick(item: MessageItem, position: Int)

}