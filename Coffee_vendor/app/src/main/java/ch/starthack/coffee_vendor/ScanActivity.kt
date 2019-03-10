package ch.starthack.coffee_vendor

import android.media.ToneGenerator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

/**
 * Created by Parsania Hardik on 19-Mar-18.
 */
class ScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private var mScannerView: ZXingScannerView? = null

    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        mScannerView = ZXingScannerView(this)   // Programmatically initialize the scanner view

        setContentView(mScannerView)
    }

    public override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this) // Register ourselves as a handler for scan results.
        mScannerView!!.startCamera()          // Start camera on resume
    }

    public override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()           // Stop camera on pause
    }

    override fun handleResult(rawResult: Result) {
        MainActivity.beeper.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200)
        MainActivity.tvresult!!.text = rawResult.text
        onBackPressed()
        // If you would like to resume scanning, call this method below:
//        mScannerView!!.resumeCameraPreview(this)
    }

}