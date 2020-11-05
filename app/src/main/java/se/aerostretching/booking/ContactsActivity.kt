package se.aerostretching.booking

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ContactsActivity : AppCompatActivity() {

    lateinit var textViewContactsPhone: TextView
    lateinit var textViewContactsEmail: TextView
    lateinit var textViewContactsPlace: TextView
    lateinit var mapView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        initialize()

    }

    fun initialize(){
        textViewContactsPhone = findViewById(R.id.textViewContactsPhone)
        textViewContactsEmail = findViewById(R.id.textViewContactsEmail)
        textViewContactsPlace = findViewById(R.id.textViewContactsPlace)

        mapView = findViewById(R.id.mapView)
            mapView.settings.setSupportZoom(false)
            mapView.settings.displayZoomControls = false
            mapView.settings.builtInZoomControls = false
            mapView.settings.javaScriptEnabled = true

        mapView.loadData("<iframe src=\"https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d2034.5934916998403!2d18.06755401615737!3d59.339740417229805!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x465f9d42e9ea3a9d%3A0x70c6e34f6b0879e9!2sEngelbrektsgatan%2017%2C%20114%2032%20Stockholm!5e0!3m2!1sen!2sse!4v1604575997430!5m2!1sen!2sse\" width=\"100%\" height=\"100%\" frameborder=\"0\" style=\"border:0;\" allowfullscreen=\"\" aria-hidden=\"false\" tabindex=\"0\"></iframe>",
            "text/html",
            "utf-8")

        // <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d2034.5934916998403!2d18.06755401615737!3d59.339740417229805!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x465f9d42e9ea3a9d%3A0x70c6e34f6b0879e9!2sEngelbrektsgatan%2017%2C%20114%2032%20Stockholm!5e0!3m2!1sen!2sse!4v1604575997430!5m2!1sen!2sse" width="800" height="600" frameborder="0" style="border:0;" allowfullscreen="" aria-hidden="false" tabindex="0"></iframe>

    }

    fun contactsPhone(view: View){
        val phoneIntent = Intent(Intent.ACTION_DIAL)
        phoneIntent.data = Uri.parse("tel:" + textViewContactsPhone.text.toString())

        startActivity(phoneIntent)

    }

    fun contactsEmail(view: View){
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", textViewContactsEmail.text.toString(), null))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.app_name))

        startActivity(Intent.createChooser(emailIntent, "Email"))

    }


    fun contactsPlace(view: View){
        val gmmIntentUri = Uri.parse("geo:59.3397404,18.067554")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(packageManager)?.let {
            startActivity(mapIntent)

        }

    }

}