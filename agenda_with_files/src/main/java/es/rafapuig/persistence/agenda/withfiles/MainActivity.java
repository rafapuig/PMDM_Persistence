package es.rafapuig.persistence.agenda.withfiles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import es.rafapuig.persistence.agenda.withfiles.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static final String FILENAME = "agenda.dat";

    public static boolean isWriteableSD; // Is there an SD card in read/write mode?

    // In modern versions of Android, 1st SD is always emulated in an internal memory folder
    // So, the first real SD card is the second of the array
    public static File[] sdCards; //SD cards available array


    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        manageExternalStorage();
    }

    private void manageExternalStorage() {
        // Check SD exists and we have read/write permissions
        String stateSD = Environment.getExternalStorageState();
        isWriteableSD = stateSD.equals(Environment.MEDIA_MOUNTED);

        // Now, check that it is not the emulated
        if (isWriteableSD) {
            // Get SD cards folders paths
            sdCards = ContextCompat.getExternalFilesDirs(this, null);
            // There really is a physical SD card if there are more than one items in the array
            isWriteableSD = isWriteableSD && (sdCards.length > 1);
        }
    }

    public void onSaveButtonClickCallback(View view) {
        saveData();
    }

    public static File getSDCardFile(String filename) {
        File pathToSD = sdCards[1]; // The second is the real first one
        File file = new File(pathToSD.getAbsolutePath(), filename); // parent folder + filename
        return file;
    }

    private FileOutputStream getFileOutputStream(String filename, boolean append) throws FileNotFoundException {
        if (isWriteableSD) // There is an SD card physical
            return new FileOutputStream(getSDCardFile(filename), append); // 2nd parameter = append
        else
            // If there's no SD card, then use internal memory
            return this.openFileOutput(filename, append ? Context.MODE_APPEND : Context.MODE_PRIVATE);
    }

    void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    boolean validateNameField(String fullName) {
        if(fullName == null || fullName.isEmpty()) {
            showError("Full name cannot be empty");
            return false;
        }
        return true;
    }

    boolean validateAgeField(String age) {
        if(age == null || age.isEmpty()) {
            showError("Age cannot be empty");
            return false;
        } else {
            try {
                int iAge = Integer.parseInt(age);
            } catch (NumberFormatException ex) {
                showError("Age field is not valid");
                return false;
            }
        }
        return true;
    }


    private void saveData() {
        // Get the register info

        String fullName = binding.fullNameEditText.getText().toString();
        if(!validateNameField(fullName)) return;

        String ageFieldContent = binding.ageEditText.getText().toString();
        if(!validateAgeField(ageFieldContent)) return;
        int age = Integer.parseInt(ageFieldContent);

        boolean isWorking = binding.workingCheckbox.isChecked();

        //Write data to file
        if(writeDataToFile(fullName, age, isWorking)) {
            clearUI();
        } else {
            showError("There was an error writing data!");
        }
    }

    boolean writeDataToFile(String fullName, int age, boolean isWorking) {
        try {
            tryToWriteDataToFile(fullName, age, isWorking);
            return true;
        } catch (IOException e) {
            Log.i("","There was an error writing to the file!" + e.getMessage());
        }
        return false;
    }

    void clearUI() {
        binding.fullNameEditText.setText("");
        binding.ageEditText.setText("");
        binding.workingCheckbox.setChecked(false);
    }


    void tryToWriteDataToFile(String fullName, int age, boolean isWorking) throws IOException {
        FileOutputStream fos = getFileOutputStream(FILENAME, true); // byte stream to a file
        // We use APPEND mode so that new data is added to an existent file with all historical saved data
        BufferedOutputStream bos = new BufferedOutputStream(fos); // Optimize bytes to blocks writings
        DataOutputStream dos = new DataOutputStream(bos); // translate typed data to bytes

        // Write register info in the file as 3 typed data
        dos.writeUTF(fullName); // writeUTF is the equivalent of "writeString"
        dos.writeInt(age);
        dos.writeBoolean(isWorking);

        // We must close the output streams
        dos.close();
        bos.close();
        fos.close();
    }


    public void onShowButtonClickCallback(View view) {
        Intent registersActivity = new Intent(this, RegistersActivity.class);
        startActivity(registersActivity);
    }

    public void onExitButtonClickCallback(View view) {
        this.finish();
    }

}