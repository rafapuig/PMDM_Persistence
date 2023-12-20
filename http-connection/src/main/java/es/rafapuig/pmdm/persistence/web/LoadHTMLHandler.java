package es.rafapuig.pmdm.persistence.web;

import android.os.Bundle;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import android.os.Handler;
import android.util.Log;

public class LoadHTMLHandler extends Handler {

    private static final int HTML_READY = 0;
    private static final int ERROR = -1;

    private static final String HTML = "LoadHTMLHandler.HTML";
    private static final String ERROR_TEXT = "LoadHTMLHandler.ERROR_TEXT";

    MainActivity mainActivity;

    public LoadHTMLHandler(MainActivity activity) {
        super(Looper.getMainLooper());
        this.mainActivity = activity;
    }

    //This method is executed by the main Thread and can update de UI
    @Override
    public void handleMessage(@NonNull Message msg) {
        Log.i("RAFA","handleMessage: " + Thread.currentThread().getName());

        Bundle bundle = msg.getData();

        switch (msg.what) {
            case HTML_READY:
                String html = bundle.getString(HTML);
                mainActivity.updateHTMLTextView(html);
                break;

            case ERROR:
                String errorText = bundle.getString(ERROR_TEXT);
                mainActivity.showToastMessage(errorText);
                break;

        }
    }

    // These two sendXXX method are executed by other thread, not main
    public void sendHTMLReadyMessage(String html) {
        Log.i("RAFA","sendHTMLReadyMessage: " + Thread.currentThread().getName());
        Message msg = this.obtainMessage(LoadHTMLHandler.HTML_READY);
        Bundle bundle = new Bundle();
        bundle.putString(HTML, html);
        msg.setData(bundle);
        this.sendMessage(msg);
    }

    public void sendErrorMessage(String text) {
        //TODO: send a message to handle an error

    }

}
