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
        messageListAdapter = ClientMessageListAdapter(GetData.messageToClientsList, this, false)
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
        val dialogLayout = layoutInflater.inflate(R.layout.alert_dialog_answer_message, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.editTextMsg)

        AlertDialog.Builder(this)
            .setTitle(R.string.reply)
            .setView(dialogLayout)
            .setPositiveButton(getString(R.string.sendTraining)) { dialog, i ->
                if(editText.text.toString().isNotEmpty()) {
                    val messageToClient = MessageItem(
                        item.user,
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