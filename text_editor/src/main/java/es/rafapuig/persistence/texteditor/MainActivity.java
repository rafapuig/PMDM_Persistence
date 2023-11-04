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
        loadEditTextContentFromFile(); //TODO: call method to load text from file
    }


    @Override
    protected void onPause() {
        saveEditTextContentToFile(); //TODO: call method to save text to file
        super.onPause();
    }

    private void loadEditTextContentFromFile() {
        String content = loadTextFromFile();
        restoreEditTextContent(content);
    }

    private void saveEditTextContentToFile() {
        String content = getContentToStore();
        saveTextToFile(content);
    }

    private void restoreEditTextContent(String text) {
        EditText editText = findViewById(R.id.editTextTextMultiLine);
        editText.setText(text);
    }

    private String getContentToStore() {
        EditText editText = this.findViewById(R.id.editTextTextMultiLine);
        String content = editText.getText().toString();
        return content;
    }


    private String loadTextFromFile() {
        try {
            return tryLoadTextFromFile();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "File not found!!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "There's was an I/O error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }


    private void saveTextToFile(String text) {
        try {
            trySaveTextToFile(text);
        } catch (IOException e) {
            Toast.makeText(this, "There's was an I/O error" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private String tryLoadTextFromFile() throws FileNotFoundException, IOException {

        FileInputStream fis = this.openFileInput(FILENAME); //byte stream from file

        InputStreamReader isr = new InputStreamReader(fis); // from bytes to chars

        BufferedReader br = new BufferedReader(isr); // optimizes reading with a buffer

        //Use br.readLine() -> String to read the file contents line by line

        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
        }

        //Close de input streams
        br.close();
        isr.close();
        fis.close();

        return sb.toString();
    }


    private void trySaveTextToFile(String text) throws IOException {

        //Open a file for writing, it clear all contents that were there before
        FileOutputStream fos = this.openFileOutput(FILENAME, Context.MODE_PRIVATE);

        PrintWriter pw = new PrintWriter(fos); //from byte stream to a buffered char stream

        //Use PrintWriter methods as with System.out

        String[] lines = text.split(System.lineSeparator());

        for (String line : lines) {
            pw.println(line);
        }

        //Close the  output streams
        pw.close();
        fos.close();
    }

}