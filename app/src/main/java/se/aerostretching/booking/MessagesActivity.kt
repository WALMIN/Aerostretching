package se.aerostretching.booking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MessagesActivity : AppCompatActivity(), OnClientMessageItemClickListener {

    companion object {
        lateinit var messageListAdapter: ClientMessageListAdapter

    }


    lateinit var messageListView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)
        customActionBar()
        initialize()

    }

    fun initialize() {
        messageListView = findViewById(R.id.messageList)
        messageListAdapter = ClientMessageListAdapter(GetData.messageToClientsList, this,  this,false)
        messageListView.layoutManager = LinearLayoutManager(this)
        messageListView.adapter = messageListAdapter

    }

    fun customActionBar() {
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.action_bar)
        val view = supportActionBar!!.customView

        // Title
        val titleView = view.findViewById<View>(R.id.title) as TextView
        titleView.text = getString(R.string.messagesActivity)

        // Start button
        val startBtn = view.findViewById<View>(R.id.startBtn) as ImageButton
        startBtn.visibility = View.VISIBLE
        startBtn.setImageResource(R.drawable.back)
        startBtn.setOnClickListener {
            goToPreviousActivity()

        }

        // End button
        val endBtn = view.findViewById<View>(R.id.endBtn) as ImageButton
        endBtn.visibility = View.VISIBLE
        endBtn.setImageResource(R.drawable.send)
        endBtn.setOnClickListener {
            sendMessageDialog(getString(R.string.sendTraining))

        }

    }

    fun goToPreviousActivity() {
        startActivity(Intent(this, MyPageActivity::class.java))
        finish()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        goToPreviousActivity()

    }

    override fun onClientMessageItemClick(item: MessageItem, position: Int) {
        sendMessageDialog(getString(R.string.reply))

    }

    override fun onClientMessageItemLongClick(item: MessageItem, position: Int) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.deleteMessageTitle))
            .setMessage(getString(R.string.deleteMessageMsg))
            .setPositiveButton(getString(R.string.delete)) { dialog, id ->
                FirebaseFirestore.getInstance().collection("messagesToClients")
                    .document(item.id).delete()
                    .addOnSuccessListener { Log.d("!!!", "SUCCESS: Message deleted") }
                    .addOnFailureListener { e -> Log.d("!!!", "ERROR: $e") }

            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, id -> }
            .show()

    }

    fun sendMessageDialog(title: String){
        val dialogLayout = layoutInflater.inflate(R.layout.alert_dialog_answer_message, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.editTextMsg)

        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(dialogLayout)
            .setPositiveButton(getString(R.string.sendTraining)) { dialog, i ->
                if(editText.text.toString().isNotEmpty()) {
                    val messageToClient = MessageItem(
                        "",
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        (System.currentTimeMillis() / 1000).toString(),
                        GetData.name,
                        editText.text.toString(),
                        GetData.email,
                        false
                    )

                    FirebaseFirestore.getInstance().collection("messagesFromClients")
                        .add(messageToClient)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("!!!", "SENT: Success")

                                Toast.makeText(this, getString(R.string.messageSent), Toast.LENGTH_LONG).show()
                                dialog.dismiss()

                            } else {
                                Log.d("!!!", "ERROR: ${task.exception}")
                                Toast.makeText(this, getString(R.string.messageNotSent), Toast.LENGTH_LONG).show()

                            }

                        }

                }else{
                    Toast.makeText(this, getString(R.string.noTrainingText), Toast.LENGTH_LONG).show()

                }

            }
            .show()

    }

    override fun onStart() {
        super.onStart()
        GetData.messageToClients()

    }

}