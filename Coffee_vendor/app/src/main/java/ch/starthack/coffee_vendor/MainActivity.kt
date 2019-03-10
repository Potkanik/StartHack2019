package ch.starthack.coffee_vendor

import android.app.PendingIntent
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.nfc.NfcAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.text.Spanned
import android.widget.Button
import android.widget.TextView
import khttp.post
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.longToast
import org.jetbrains.anko.uiThread
import java.util.*

class MainActivity : AppCompatActivity() {


    // NFC adapter for checking NFC state in the device
    private var nfcAdapter : NfcAdapter? = null

    // Pending intent for NFC intent foreground dispatch.
    // Used to read all NDEF tags while the app is running in the foreground.
    private var nfcPendingIntent: PendingIntent? = null
    // Optional: filter NDEF tags this app receives through the pending intent.
    //private var nfcIntentFilters: Array<IntentFilter>? = null
    private val DEFAULT_FIELD = "Please scan a cup"
    private val KEY_LOG_TEXT = "logText"
    companion object Mytv {
        var tvresult: TextView? = null
        val beeper = ToneGenerator(AudioManager.STREAM_ALARM, 500)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Mytv.tvresult = findViewById(R.id.tvresult)
        Mytv.tvresult!!.text = DEFAULT_FIELD

        var btn = findViewById<Button>(R.id.btn)

        btn!!.setOnClickListener {
            val intent = Intent(this@MainActivity, ScanActivity::class.java)
            startActivity(intent)
        }


//        // Restore saved text if available
//        if (savedInstanceState != null) {
//            tv_messages.text = savedInstanceState.getCharSequence(KEY_LOG_TEXT)
//        }

        // Check if NFC is supported and enabled
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        logMessage("NFC supported", (nfcAdapter != null).toString())
        logMessage("NFC enabled", (nfcAdapter?.isEnabled).toString())


        // Read all tags when app is running and in the foreground
        // Create a generic PendingIntent that will be deliver to this activity. The NFC stack
        // will fill in the intent with the details of the discovered tag before delivering to
        // this activity.
        nfcPendingIntent = PendingIntent.getActivity(this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)

        // Make sure the text view is scrolled down so that the latest messages are visible
        scrollDown()
    }

    override fun onResume() {
        super.onResume()
        // Get all NDEF discovered intents
        // Makes sure the app gets all discovered NDEF messages as long as it's in the foreground.
        nfcAdapter?.enableForegroundDispatch(this, nfcPendingIntent, null, null);

    }

    override fun onPause() {
        super.onPause()
        // Disable foreground dispatch, as this activity is no longer in the foreground
        nfcAdapter?.disableForegroundDispatch(this);
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        beeper.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200)
        if(Mytv.tvresult!!.text.equals(DEFAULT_FIELD)){
            longToast("Please scan a cup first")
        } else {
            val local_hash = Mytv.tvresult!!.text
            val user: String = Integer.toHexString(Arrays.hashCode(intent!!.getByteArrayExtra(NfcAdapter.EXTRA_ID)))
            longToast("Sending information...")
            doAsync {
                val postRet = post(
                        "http://130.82.238.197:8080/api/users/buy-cup",
                        json = (mapOf("userHash" to user, "cupHash" to local_hash)))
                uiThread {
                    logMessage("POST", postRet.text)
                    Mytv.tvresult!!.text = DEFAULT_FIELD
                }
            }

        }
    }


    // --------------------------------------------------------------------------------
    // Utility functions

    /**
     * Save contents of the text view to the state. Ensures the text view contents survive
     * screen rotation.
     */
    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putCharSequence(KEY_LOG_TEXT, tv_messages.text)
        super.onSaveInstanceState(outState)
    }

    /**
     * Log a message to the debug text view.
     * @param header title text of the message, printed in bold
     * @param text optional parameter containing details about the message. Printed in plain text.
     */
    private fun logMessage(header: String, text: String?) {
        tv_messages.append(if (text.isNullOrBlank()) fromHtml("<b>$header</b><br>") else fromHtml("<b>$header</b>: $text<br>"))
        scrollDown()
    }

    /**
     * Convert HTML formatted strings to spanned (styled) text, for inserting to the TextView.
     * Externalized into an own function as the fromHtml(html) method was deprecated with
     * Android N. This method chooses the right variant depending on the OS.
     * @param html HTML-formatted string to convert to a Spanned text.
     */
    private fun fromHtml(html: String): Spanned {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(html)
        }
    }

    /**
     * Scroll the ScrollView to the bottom, so that the latest appended messages are visible.
     */
    private fun scrollDown() {
        sv_messages.post({ sv_messages.smoothScrollTo(0, sv_messages.bottom) })
    }
}

