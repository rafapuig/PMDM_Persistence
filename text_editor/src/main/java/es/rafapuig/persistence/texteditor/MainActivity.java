package es.rafapuig.persistence.texteditor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {

    private static final String FILENAME = "text_editor.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickExitCallback(View view) {
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO: call method to load text from file in the EditText
    }


    @Override
    protected void onPause() {
        //TODO: call method to save text to file from the EditText
        super.onPause();
    }

    private void loadEditTextContentFromFile() {
        //TODO: load text content from file
        //TODO: restore the EditText with that content
    }

    private void saveEditTextContentToFile() {
        //TODO: get text content from EditText
        //TODO: save the text to a file
    }

    private void restoreEditTextContent(String text) {
        //TODO: set the text from parameter to the EditText
    }

    private String getContentToStore() {
        //TODO: return the text content of the EditText

    }


    private String loadTextFromFile() {
        //TODO: try lo load the text from file and catch the exceptions it may produce
    }


    private void saveTextToFile(String text) {
        //TODO: try to save the text to a file and catch the exceptions it may produce
    }


    private String tryLoadTextFromFile() throws FileNotFoundException, IOException {
        //TODO: read the contents of the file, close the streams and return the text read
    }


    private void trySaveTextToFile(String text) throws IOException {
        //TODO: save the text to the file and close the streams

    }

}