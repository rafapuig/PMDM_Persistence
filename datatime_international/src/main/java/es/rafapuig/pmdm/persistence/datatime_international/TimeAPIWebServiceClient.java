package es.rafapuig.pmdm.persistence.datatime_international;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.StringJoiner;

public class TimeAPIWebServiceClient {

    // URL base for a GET:  query date and time for a given city
    private static final String URL_STRING = "https://timeapi.io/api/Conversion/Translate";

    private static final URL SERVICE_URL;

    static {
        try {
            SERVICE_URL = new URL(URL_STRING);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    LocalDateTime dateTime;
    String languageCode;

    JSONObject dataReceived;
    String httpResponse;

    public static String getHTTPResponse(JSONObject dataToPost) {

        HttpURLConnection connection = null;

        try {
            //TODO: Open connection


            //TODO: Set request method POST


            //TODO: We indicate that what we are going to send and receive in Json format.


            sendPost(connection, dataToPost);

            //TODO: Check if server response code is OK
            if () {
                return readResponse(connection);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) connection.disconnect();
        }
        return null;
    }



    private static void sendPost(HttpURLConnection connection, JSONObject dataToPost) {
        // TODO: Prepare output streams to the network.
        try() {

            // TODO: We send the json with the Post data.


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private JSONObject generateJSONtoPost() throws JSONException {
        // TODO: build the json (put the data to be sent: dateTime and codeLanguage), use formatter!!!

    }

    @NonNull
    private static String readResponse(HttpURLConnection connection) throws IOException {
        // TODO: Preparing incoming flows from the network
        try () {

            //TODO: Read the response content


            //TODO: return the response content

        }
    }

    private static JSONObject parse(String jsonResponseText) throws JSONException {
        //TODO: Create the JSON object with the HTTP response

    }

    private void callService() {
        try {
            JSONObject dataToPost = generateJSONtoPost();
            httpResponse = getHTTPResponse(dataToPost);
            dataReceived = parse(httpResponse);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void callService(LocalDateTime dateTime, String languageCode) {

        this.dateTime = dateTime;
        this.languageCode = languageCode;

        callService();
    }

    public String getResponse() {
        return httpResponse;
    }

    public String getFriendlyDateTime() {
        // TODO: Get the field with the friendly date from the json object

    }

}
