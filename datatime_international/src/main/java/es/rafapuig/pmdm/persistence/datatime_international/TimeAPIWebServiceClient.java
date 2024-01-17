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
            connection = (HttpURLConnection) SERVICE_URL.openConnection();

            //TODO: Set request method POST
            connection.setRequestMethod("POST");

            //TODO: We indicate that what we are going to send and receive in Json format.
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            sendPost(connection, dataToPost);

            //TODO: Check if server response code is OK
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
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
        try(OutputStream output = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(output)) {

            // TODO: We send the json with the Post data.
            writer.print(dataToPost.toString());
            writer.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private JSONObject generateJSONtoPost() throws JSONException {
        // TODO: build the json (put the data to be sent: dateTime and codeLanguage), use formatter!!!
        JSONObject json = new JSONObject();
        json.put("dateTime", dateTime.format(dateTimeFormatter));
        json.put("languageCode", languageCode);
        return json;
    }

    @NonNull
    private static String readResponse(HttpURLConnection connection) throws IOException {
        // TODO: Preparing incoming flows from the network
        try (InputStream inputStream = connection.getInputStream();
             InputStreamReader isr = new InputStreamReader(inputStream);
             BufferedReader br = new BufferedReader(isr)) {

            //TODO: Read the response content
            StringJoiner stringJoiner = new StringJoiner(System.lineSeparator());

            String line;
            while ((line = br.readLine()) != null) {
                stringJoiner.add(line);
            }

            //TODO: return the content
            return stringJoiner.toString();
        }
    }

    private static JSONObject parse(String jsonResponseText) throws JSONException {
        //TODO: Create the JSON object with the HTTP response
        JSONObject json = new JSONObject(jsonResponseText);
        return json;
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
        try {
            return dataReceived.get("friendlyDateTime").toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}
