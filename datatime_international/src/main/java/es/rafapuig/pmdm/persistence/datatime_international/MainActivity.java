package es.rafapuig.pmdm.persistence.datatime_international;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.rafapuig.pmdm.persistence.datatime_international.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    // Countries for the spinner
    private String[] countries = {"España", "Portugal", "Francia", "Italia", "Reino Unido", "Alemania"};

    // Languages that match to each country in countries array
    private String[] languageCodes = {"es", "pt", "fr", "it", "en", "de"};

    // Capitals  that match each country in countries array
    private String[] cityCodes = {"Madrid", "Lisbon", "Paris", "Rome", "London", "Berlin"};


    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fillCoutriesSpinner();
    }


    private void fillCoutriesSpinner() {
        binding.countriesSpinner.setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries));
    }

    public void getFriendlyDateButtonCallback(View view) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(this::callTimeAPIService);
        executor.shutdown();
    }

    TimeAPIWebServiceClient timeAPI = new TimeAPIWebServiceClient();

    WebServiceResponseHandler timeAPIResponseHandler =
            new WebServiceResponseHandler(this, this::onTimeAPIServiceResponseReady);


    private void callTimeAPIService() {
        LocalDateTime dateTime = chosenDateTime;

        String languageCode = languageCodes[binding.countriesSpinner.getSelectedItemPosition()];
        try {
            timeAPI.callService(dateTime, languageCode);
            timeAPIResponseHandler.sendResponseReadyMessage();

        } catch (RuntimeException e) {
            timeAPIResponseHandler.sendErrorMessage(e.getMessage());
        }
    }


    public void onTimeAPIServiceResponseReady() {
        String response = timeAPI.getResponse();
        binding.httpResponseTextview.setText(response);

        String friendlyDateTime = timeAPI.getFriendlyDateTime();
        binding.friendlyDatetimeTextview.setText(friendlyDateTime);
    }



    public void getCurrentDateButtonCallback(View view) {
        // Using an executor allows to reuse Threads from an existing pool
        ExecutorService executor = Executors.newSingleThreadExecutor();

        //We call method submit passing a Runnable instance
        //Runnable is a functional interface so that we can use a lambda
        executor.submit(this::callWorldTimeAPIService);

        //Notify the executor that it should shutdown after the task completes.
        executor.shutdown();
    }


    WorldTimeAPIWebServiceClient worldTimeAPI = new WorldTimeAPIWebServiceClient();

    WebServiceResponseHandler worldTimeAPIResponseHandler =
            new WebServiceResponseHandler(this, this::onWorldTimeAPIServiceResponseReady);


    //It is executed in another thread
    private void callWorldTimeAPIService() {
        String location = cityCodes[binding.countriesSpinner.getSelectedItemPosition()];
        try {
            worldTimeAPI.callService("Europe", location);
            worldTimeAPIResponseHandler.sendResponseReadyMessage();
        } catch (Exception e) {
            worldTimeAPIResponseHandler.sendErrorMessage(e.getMessage());
        }
    }

    public void onWorldTimeAPIServiceResponseReady() {
        String response = worldTimeAPI.getHttpResponse();
        binding.httpResponseTextview.setText(response);

        chosenDateTime = worldTimeAPI.getCurrent();
        updateChosenDateTime();
    }


    void updateChosenDateTime() {
        binding.chosenDatetimeTextview.setText(chosenDateTime.toString());
    }


    private LocalDateTime chosenDateTime = LocalDateTime.now(ZoneId.of("UTC+1"));

    private final DatePickerDialog.OnDateSetListener onDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                //When user clicks OK on dialog
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    //month param is (0-11) but LocalDate uses (1-12)
                    LocalDate chosenDate = LocalDate.of(year, month + 1, day);

                    chosenDateTime = LocalDateTime.of(chosenDate, chosenDateTime.toLocalTime());

                    updateChosenDateTime();
                }
            };


    //Action response to click button "SET A DATE": opens a DatePickerDialog
    public void setDateButtonCallback(View view) {

        DatePickerDialog dateDialog =
                new DatePickerDialog(this, onDateSetListener,
                        chosenDateTime.getYear(),
                        chosenDateTime.getMonthValue() - 1, // Uses (0-11) from (1-12)
                        chosenDateTime.getDayOfMonth());

        dateDialog.show();
    }


    private final TimePickerDialog.OnTimeSetListener onTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {

            LocalTime chosenTime = LocalTime.of(hour, minute);
            chosenDateTime = LocalDateTime.of(chosenDateTime.toLocalDate(), chosenTime);

            //Show in textview
            updateChosenDateTime();
        }
    };

    //Action response to click button "SET A TIME": opens a TimePickerDialog
    public void setTimeButtonCallback(View v) {

        TimePickerDialog timeDialog =
                new TimePickerDialog(this, onTimeSetListener,
                        chosenDateTime.getHour(), chosenDateTime.getMinute(), true);
        timeDialog.show();
    }
}