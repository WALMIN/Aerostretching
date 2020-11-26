package se.aerostretching.booking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClientMessageListAdapter(
        var list: ArrayList<MessageItem>,
        var onClickListener: OnClientMessageItemClickListener,
        var read: Boolean = false
) : RecyclerView.Adapter<ClientMessageListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.client_message_item, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(list[position], onClickListener, read)

    }

    override fun getItemCount() = list.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item: MessageItem, onClick: OnClientMessageItemClickListener, read: Boolean) {
            val dateView = itemView.findViewById<TextView>(R.id.messageDate)
            val messageView = itemView.findViewById<TextView>(R.id.messageText)

            dateView.text = Tools.millisToDate(item.date.toLong())
            messageView.text = item.text

            // OnClick
            itemView.setOnClickListener {
                onClick.onClientMessageItemClick(item, adapterPosition)

            }

        }

    }

}

interface OnClientMessageItemClickListener {

    fun onClientMessageItemClick(item: MessageItem, position: Int)

}