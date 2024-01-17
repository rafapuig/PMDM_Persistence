package es.rafapuig.pmdm.persistence.datatime_international;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.StringJoiner;


public class WorldTimeAPIWebServiceClient {

    // URL base for a GET:  query date and time for a given city
    private static final String BASE_URL_STRING = "http://worldtimeapi.org/api/timezone/";

    private static final URL BASE_URL;

    static {
        try {
            BASE_URL = new URL(BASE_URL_STRING);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getHTTPResponse(String area, String location) {

        HttpURLConnection connection = null;

        try {
            String spec = new StringJoiner("/").add(area).add(location).toString();
            URL url = new URL(BASE_URL, spec + ".txt");

            //Open connection
            connection = (HttpURLConnection) url.openConnection();

            //Set request method GET
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return readHttpResponse(connection);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) connection.disconnect();
        }
        return null;
    }

    @NonNull
    private static String readHttpResponse(HttpURLConnection connection) throws IOException {
        try (InputStream inputStream = connection.getInputStream();
             InputStreamReader isr = new InputStreamReader(inputStream);
             BufferedReader br = new BufferedReader(isr)) {

            StringJoiner stringJoiner = new StringJoiner(System.lineSeparator());

            String line;
            while ((line = br.readLine()) != null) {
                stringJoiner.add(line);
            }
            return stringJoiner.toString();
        }
    }


    private static LocalDateTime parseResponse(String response) {
        // split the response (it is plain text, not json) and hold time and date
        String formattedDateTime = response
                .split("\n")[2]
                .replace("datetime: ", "");

        String[] tokens = formattedDateTime.split("T");
        String formattedDate = tokens[0];
        String formattedTime = tokens[1];

        String[] dateTokens = formattedDate.split("-");
        int year = Integer.parseInt(dateTokens[0]);
        int month = Integer.parseInt(dateTokens[1]);
        int day = Integer.parseInt(dateTokens[2]);

        String[] timeTokens = formattedTime.split(":");
        int hours = Integer.parseInt(timeTokens[0]);
        int minutes = Integer.parseInt(timeTokens[1]);

        return LocalDateTime.of(year, month, day, hours, minutes);
    }

    boolean parsed = false;

    String httpResponse = "";

    LocalDateTime dateTime;

    public void callService(String area, String location) {
        httpResponse = getHTTPResponse(area, location);
        parsed = false;
    }

    public String getHttpResponse() {
        return this.httpResponse;
    }

    public LocalDateTime getCurrent() {
        if (!parsed) {
            dateTime = parseResponse(getHttpResponse());
            parsed = true;
        }
        return dateTime;
    }
}
