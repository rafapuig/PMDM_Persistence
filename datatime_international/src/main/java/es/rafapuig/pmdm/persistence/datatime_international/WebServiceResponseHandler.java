package es.rafapuig.pmdm.persistence.datatime_international;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class WebServiceResponseHandler extends Handler {

    private static final int RESPONSE_READY = 0;
    private static final int ERROR = -1;

    private static final String ERROR_TEXT = "ERROR_TEXT";

    Context context;
    Runnable action;

    public WebServiceResponseHandler(Context context, Runnable action) {
        super(Looper.getMainLooper());
        this.context = context;
        this.action = action;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        Bundle bundle = msg.getData();

        switch (msg.what) {
            case RESPONSE_READY:
                action.run();
                break;

            case ERROR:
                String errorText = bundle.getString(ERROR_TEXT);
                showToastMessage(errorText);
                break;
        }
    }

    public void showToastMessage(String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG)
                .show();
    }

    public void sendResponseReadyMessage() {
        Log.i("RAFA","sendHTMLReadyMessage: " + Thread.currentThread().getName());
        Message msg = this.obtainMessage(RESPONSE_READY);
        this.sendMessage(msg);
    }

    public void sendErrorMessage(String text) {
        Message msg = this.obtainMessage(ERROR);
        Bundle bundle = new Bundle();
        bundle.putString(ERROR_TEXT, text);
        msg.setData(bundle);
        msg.sendToTarget();
    }

}
