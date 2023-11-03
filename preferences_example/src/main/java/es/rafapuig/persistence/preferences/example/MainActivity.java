package es.rafapuig.persistence.preferences.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        //The second argument is the value to return in case there's no value for the key
        String persistedText = prefs.getString("persistent_text", ""); // By key name

        this.<EditText>findViewById(R.id.et_text_to_persist).setText(persistedText);
    }

    @Override
    protected void onPause() {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE); //Always private mode
        SharedPreferences.Editor editor = prefs.edit(); //Get the editor

        String textToPersist =
                this.<EditText>findViewById(R.id.et_text_to_persist)
                .getText().toString();

        editor.putString("persistent_text", textToPersist); // Save key-value pair
        //editor.putXXX(...)
        //...

        editor.commit(); // commit changes

        super.onPause();
    }
}