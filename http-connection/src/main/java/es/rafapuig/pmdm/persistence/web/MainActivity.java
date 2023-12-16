package es.rafapuig.pmdm.persistence.web;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import es.rafapuig.pmdm.persistence.web.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    //"https://www.uv.es"; //
    static final String HOME_URL = "https://www.uv.es"; //"https://portal.edu.gva.es/iesabastos/es/centre/";

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.urlEditText.setText(HOME_URL);

        //binding.browserWebView.setWebViewClient(new WebViewClient());
        binding.browserWebView.getSettings().setJavaScriptEnabled(true);
    }

    public void onLoadHTMLButtonClickCallback(View view) {
        String url = binding.urlEditText.getText().toString().toLowerCase();

        // Using an executor allows to reuse Threads from an existing pool
        ExecutorService executor = Executors.newSingleThreadExecutor();

        //We call method submit passing a Runnable instance
        //Runnable is a functional interface so that we can use a lambda
        executor.submit(
                () -> loadHTML(getHttpURL(url)) // Lambda call method loadHTML
        );
        //Notify the executor that it should shutdown after the task completes.
        executor.shutdown();
    }

    static String getHttpURL(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return "http://" + url;
        }
        return url;
    }

    //WeakReference<MainActivity> activityWeakReference = new WeakReference<>(this);

    /*class LoadHTMLHandler extends Handler {

        private static final int HTML_READY = 0;
        private static final int ERROR = -1;

        private static final String HTML = "LoadHTMLHandler.HTML";
        private static final String ERROR_TEXT = "LoadHTMLHandler.ERROR_TEXT";

        WeakReference<MainActivity> activityWeakReference;

        public LoadHTMLHandler(WeakReference<MainActivity> activity) {
            super(Looper.getMainLooper());
            this.activityWeakReference = activity;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {

            Bundle bundle = msg.getData();

            switch (msg.what) {
                case HTML_READY:
                    String html = bundle.getString(HTML);
                    activityWeakReference.get().updateHTMLTextView(html);
                    break;

                case ERROR:
                    String errorText = bundle.getString(ERROR_TEXT);
                    activityWeakReference.get().showToastMessage(errorText);
                    break;

            }
        }

        public void sendHTMLReadyMessage(String html) {
            Message msg = this.obtainMessage(LoadHTMLHandler.HTML_READY);
            Bundle bundle = new Bundle();
            bundle.putString(HTML, html);
            msg.setData(bundle);
            this.sendMessage(msg);
        }

        public void sendErrorMessage(String text) {
            Message msg = this.obtainMessage(LoadHTMLHandler.ERROR);
            Bundle bundle = new Bundle();
            bundle.putString(ERROR_TEXT, text);
            msg.setData(bundle);
            msg.sendToTarget();
        }
    }*/

    // The handler to we will send messages from the Thread performing
    // the task of loading HTML from Internet
    // Handler needs to call methods on the activity so that we pass
    // the reference to the activity
    LoadHTMLHandler loadHTMLHandler = new LoadHTMLHandler(this); //new LoadHTMLHandler(activityWeakReference);


    // Try to load the HTML code from the specURL conecting through HTTP
    void tryLoadHTML(String specURL) throws IOException {

        //TODO: Create an URL instance from the specURL
        URL url = new URL(specURL);

        //TODO: Get the connection object
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        //TODO: Configure connection object with GET method
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5 * 1000);

        //TODO: check if response code is HTTP_OK
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

            //TODO: Prepare input streams from the HTTP connection
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            //TODO: Read the lines of html code received
            StringJoiner stringJoiner = new StringJoiner("\n");

            String line;
            while ((line = br.readLine()) != null) {
                stringJoiner.add(line);
            }

            //TODO: Close the streams
            br.close();
            isr.close();
            is.close();

            String html = stringJoiner.toString();

            //We cannot call the method updateHTMLTextView(String html)
            //if this code is been executed in other thread
            //Only the main Thread can "touch" the UI views
            //So, we send a message to the handler, passing the html String
            Log.i("RAFA","tryLoadHTML: " + Thread.currentThread().getName());
            loadHTMLHandler.sendHTMLReadyMessage(html);

        } else {
            String errorMsg = connection.getResponseCode() +
                    " - " + connection.getResponseMessage();

            loadHTMLHandler.sendErrorMessage(errorMsg);
        }

        //TODO: Close the connection
        connection.disconnect();
    }


    // This met
    void loadHTML(String specURL) {
        try {
            tryLoadHTML(specURL);

        } catch (SocketTimeoutException e) {
            //We cannot call a Toast directly because this is running in
            // a task inside a Thread that is not the main Thread
            loadHTMLHandler.sendErrorMessage("Time out connection");

        } catch (MalformedURLException e) {
            loadHTMLHandler.sendErrorMessage("Invalid URL!");

        } catch (IOException e) {
            loadHTMLHandler.sendErrorMessage("I/O Error:" + e.getMessage());
        }
    }



    void updateHTMLTextView(String html) {
        binding.HTMLTextView.setText(html);
    }

    public void showToastMessage(String text) {
        Toast.makeText(this.getApplicationContext(), text, Toast.LENGTH_LONG)
                .show();
    }

    public void onLoadInWebViewButtonClickCallback(View view) {
        byte[] input = binding.HTMLTextView.getText().toString().getBytes();
        String encodedHtml = Base64.encodeToString(input, Base64.NO_PADDING);
        binding.browserWebView.loadData(encodedHtml, "text/html", "base64");
        //binding.browserWebView.loadUrl(HOME_URL);
    }

}