package es.rafapuig.persistence.agenda.withddbb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import es.rafapuig.persistence.agenda.withddbb.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    SQLiteOpenHelper sqLiteOpenHelper;
    SQLiteDatabase db;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        sqLiteOpenHelper = new DbAgendaOpenHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        db = sqLiteOpenHelper.getWritableDatabase();
    }

    @Override
    protected void onPause() {
        if (db.isOpen()) {
            db.close();
        }
        super.onPause();
    }

    public void onSaveButtonClickCallback(View view) {
        saveData();
        clearUI();
    }

    private void saveData() {
        // Get register info
        String fullName = binding.fullNameEditText.getText().toString();
        int age = Integer.parseInt(binding.ageEditText.getText().toString());
        boolean isWorking = binding.workingCheckbox.isChecked();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbAgendaOpenHelper.COLUMN_NAME_FULLNAME, fullName);
        contentValues.put(DbAgendaOpenHelper.COLUMN_NAME_AGE, age);
        contentValues.put(DbAgendaOpenHelper.COLUMN_NAME_IS_WORKING, isWorking);

        try {
            db.insertOrThrow("person", null, contentValues);
        } catch (SQLException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    void clearUI() {
        binding.fullNameEditText.setText("");
        binding.ageEditText.setText("");
        binding.workingCheckbox.setChecked(false);
    }

    public void onShowButtonClickCallback(View view) {
        Intent registersActivity = new Intent(this, RegistersActivity.class);
        startActivity(registersActivity);
    }


    public void onExitButtonClickCallback(View view) {
        this.finish();
    }

}