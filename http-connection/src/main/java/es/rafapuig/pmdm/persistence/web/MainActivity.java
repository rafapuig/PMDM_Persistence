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


    // The handler to we will send messages from the Thread performing
    // the task of loading HTML from Internet
    // Handler needs to call methods on the activity so that we pass
    // the reference to the activity
    LoadHTMLHandler loadHTMLHandler = new LoadHTMLHandler(this); //new LoadHTMLHandler(activityWeakReference);


    // Try to load the HTML code from the specURL conecting through HTTP
    void tryLoadHTML(String specURL) throws IOException {

        //TODO: Create an URL instance from the specURL


        //TODO: Get the connection object


        //TODO: Configure connection object with GET method

        //TODO: check if response code is HTTP_OK
        if (...) {

            //TODO: Prepare input streams from the HTTP connection


            //TODO: Read the lines of html code received



            //TODO: Close the streams


            String html = ...

            //We cannot call the method updateHTMLTextView(String html)
            //if this code is been executed in other thread
            //Only the main Thread can "touch" the UI views
            //So, we send a message to the handler, passing the html String

            loadHTMLHandler.sendHTMLReadyMessage(html);

        } else {
            String errorMsg = connection.getResponseCode() +
                    " - " + connection.getResponseMessage();

            loadHTMLHandler.sendErrorMessage(errorMsg);
        }

        //TODO: Close the connection

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