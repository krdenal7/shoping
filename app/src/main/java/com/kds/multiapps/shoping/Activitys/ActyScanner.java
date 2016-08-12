package com.kds.multiapps.shoping.Activitys;

import android.app.Activity;
import android.os.Bundle;

import com.google.zxing.Result;
import com.kds.multiapps.shoping.R;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Isaac Martinez on 01/08/2016.
 * shoping
 */
public class ActyScanner extends Activity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView mScannerView;
    private String TAG=getClass().getSimpleName();

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.layout_scanner);
        mScannerView =(ZXingScannerView)findViewById(R.id.zxScanner);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {

    }
}
