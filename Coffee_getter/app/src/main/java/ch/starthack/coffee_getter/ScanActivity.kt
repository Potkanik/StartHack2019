package ch.starthack.coffee_getter

import android.app.PendingIntent
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.nfc.NfcAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.zxing.Result
import khttp.put
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.longToast
import org.jetbrains.anko.uiThread
import java.util.*

/**
 * Created by Parsania Hardik on 19-Mar-18.
 */
class ScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private var mScannerView: ZXingScannerView? = null
    private val beeper = ToneGenerator(AudioManager.STREAM_ALARM, 500)

    private var nfcAdapter: NfcAdapter? = null
    private var nfcPendingIntent: PendingIntent? = null

    private val DEFAULT_FIELD = "NONE"
    private var timeScanned : Date = Date()
    private var idScanned : String = DEFAULT_FIELD

    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        mScannerView = ZXingScannerView(this)   // Programmatically initialize the scanner view
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcPendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )
//        longToast("Gonna set scanner view")
        setContentView(mScannerView)
    }

    public override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, nfcPendingIntent, null, null)
        mScannerView!!.setResultHandler(this) // Register ourselves as a handler for scan results.
        setContentView(mScannerView)                // Set the scanner view as the content view
        mScannerView!!.startCamera()          // Start camera on resume
    }

    public override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
        mScannerView!!.stopCamera()           // Stop camera on pause
    }

    override fun handleResult(rawResult: Result) {
        val cupHash = rawResult.text
        val localId = idScanned
//        onBackPressed()
        longToast("Sending...")
        beeper.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200)
        doAsync{
            val postRet = if((idScanned != DEFAULT_FIELD) && ((Date().time - timeScanned.time).toInt() < 30*1000)){
                // if user has been identified correctly less than 30 seconds in advance
                longToast("Sending login")
                put(
                    "http://130.82.238.197:8080/api/users/return-cup",
                    json = mapOf("userLogin" to localId, "cupHash" to cupHash))
            } else{
                longToast("NOT sending login")
                put(
                    "http://130.82.238.197:8080/api/users/return-cup",
                    json = mapOf("cupHash" to cupHash)
                )
            }
            uiThread {
                longToast("POSTED" + postRet.text)
                idScanned = DEFAULT_FIELD
            }
        }
        // If you would like to resume scanning, call this method below:
        mScannerView!!.resumeCameraPreview(this)
    }

    override fun onNewIntent(intent: Intent?) {
        mScannerView!!.stopCamera()
        super.onNewIntent(intent)
        idScanned = "user"
        timeScanned = Date()
        beeper.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200)
        longToast("Credits will go to $idScanned for 30 seconds")
        mScannerView!!.startCamera()
    }

}