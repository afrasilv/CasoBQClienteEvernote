package com.bqclientevernote.afrasilv.asyntask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bqclientevernote.afrasilv.casobqclienteevernote.MainActivity;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.microblink.directApi.DirectApiErrorListener;
import com.microblink.directApi.Recognizer;
import com.microblink.hardware.orientation.Orientation;
import com.microblink.recognition.FeatureNotSupportedException;
import com.microblink.recognition.InvalidLicenceKeyException;
import com.microblink.recognizers.BaseRecognitionResult;
import com.microblink.recognizers.RecognitionResults;
import com.microblink.recognizers.blinkocr.BlinkOCRRecognitionResult;
import com.microblink.recognizers.blinkocr.BlinkOCRRecognizerSettings;
import com.microblink.recognizers.blinkocr.engine.BlinkOCREngineOptions;
import com.microblink.recognizers.blinkocr.parser.generic.RawParserSettings;
import com.microblink.recognizers.settings.RecognitionSettings;
import com.microblink.recognizers.settings.RecognizerSettings;
import com.microblink.util.Log;
import com.microblink.view.recognition.ScanResultListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DirectAPIAsyntask extends AsyncTask<Void, Void, Void> {
    /** Recognizer instance. */
    private Recognizer mRecognizer = null;
    /** Button which starts the recognition. */
    private Button mScanAssetBtn = null;

    private MainActivity mMainAcitivity;
    private Bitmap mBitmap;

    private String parsed;

    public DirectAPIAsyntask(MainActivity _activity, Bitmap _bitmap) {

        mMainAcitivity = _activity;


        mBitmap = _bitmap;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            mRecognizer = Recognizer.getSingletonInstance();
        } catch (FeatureNotSupportedException e) {
            Toast.makeText(mMainAcitivity, "Feature not supported! Reason: " + e.getReason().getDescription(), Toast.LENGTH_LONG).show();

            return;
        }
        try {
            // set license key
            mRecognizer.setLicenseKey(mMainAcitivity, "MMVQMH5P-JKS2TSRO-KBTRS5HC-JY4MGSVH-SJ6IGOUG-M3F74CC3-AQAL6ZYR-TYJH3N6D");
        } catch (InvalidLicenceKeyException exc) {
            return;
        }
        // prepare settings for raw OCR
        BlinkOCRRecognizerSettings ocrSett = new BlinkOCRRecognizerSettings();
        RawParserSettings rawSett = new RawParserSettings();

        // set OCR engine options
        BlinkOCREngineOptions engineOptions = new BlinkOCREngineOptions();
        // set to false to scan colored text (set to true only for black text on color background)
        engineOptions.setColorDropoutEnabled(false);
        rawSett.setOcrEngineOptions(engineOptions);

        // add raw parser with name "Raw" to default parser group
        // parser name is important for obtaining results later
        ocrSett.addParser("Raw", rawSett);

        // prepare recognition settings
        RecognitionSettings settings = new RecognitionSettings();
        // set recognizer settings array that is used to configure recognition
        // BlinkOCRRecognizer will be used in the recognition process
        settings.setRecognizerSettingsArray(new RecognizerSettings[]{ocrSett});

        // initialize recognizer singleton with defined settings
        mRecognizer.initialize(mMainAcitivity, settings, new DirectApiErrorListener() {
            @Override
            public void onRecognizerError(Throwable throwable) {
                Log.e("ERROR", "Failed to initialize recognizer.", throwable);
                Toast.makeText(mMainAcitivity, "Failed to initialize recognizer. Reason: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected Void doInBackground(Void... params) {
        // check whether the recognizer is ready
        if (mRecognizer.getCurrentState() != Recognizer.State.READY) {
            Toast.makeText(mMainAcitivity, "Recognizer not ready!", Toast.LENGTH_LONG).show();
        }

        if (mBitmap != null) {
            // recognize image
            mRecognizer.recognizeBitmap(mBitmap, Orientation.ORIENTATION_PORTRAIT, new ScanResultListener() {
                @Override
                public void onScanningDone(RecognitionResults results) {
                    // get results array
                    BaseRecognitionResult[] dataArray = results.getRecognitionResults();
                    if (dataArray != null && dataArray.length > 0) {
                        // only single result from BlinkOCRRecognizer is expected
                        if (dataArray[0] instanceof BlinkOCRRecognitionResult) {
                            BlinkOCRRecognitionResult result = (BlinkOCRRecognitionResult) dataArray[0];
                            // get string result from configured parser with parser name "Raw"
                            final String parsed = result.getParsedResult("Raw");
                            setParsed(parsed);
                        }
                    } else {
                        Toast.makeText(mMainAcitivity, "Nothing scanned!", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

        return null;
    }

    private void setParsed(String parsed){
        this.parsed = parsed;
    }


    @Override
    protected void onPostExecute(Void aVoid){
        // terminate the native library and free unnecessary resources
        mRecognizer.terminate();
        // for further use, recognizer must be initialized again
        mRecognizer = null;

        this.mMainAcitivity.setParseTextOCR(this.parsed);
    }
}