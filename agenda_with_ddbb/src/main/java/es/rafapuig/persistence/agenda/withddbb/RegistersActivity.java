package es.rafapuig.persistence.agenda.withddbb;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import es.rafapuig.persistence.agenda.withddbb.databinding.ActivityRegistersBinding;

public class RegistersActivity extends AppCompatActivity {

    SQLiteOpenHelper sqLiteOpenHelper;
    SQLiteDatabase db;
    Cursor cursor;

    ActivityRegistersBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_registers);
        binding = ActivityRegistersBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        sqLiteOpenHelper = new DbAgendaOpenHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        db = sqLiteOpenHelper.getWritableDatabase();
        if (loadPeopleData()) {
            showCurrentPersonData();
        } else {
            Toast.makeText(this, "There's no people", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        // TODO: Close the cursor and the database
        if (cursor != null) cursor.close();
        if (db != null && db.isOpen()) db.close();
        super.onPause();
    }


    boolean loadPeopleData() {
        // TODO: Make the query, get the cursor and place it in the first tuple
        cursor = db.rawQuery("SELECT * FROM person", null);

        boolean result = cursor.moveToFirst();
        return result;
    }


    public void onPreviousButtonClickCallback(View view) {
        // TODO: Make the cursor point to the previous person at the cursor and display data from this person.
        if (cursor.moveToPrevious()) {
            showCurrentPersonData();
        } else {
            if (cursor.isBeforeFirst()) {
                cursor.moveToFirst();
            }
            Toast.makeText(this, "There's no previous register", Toast.LENGTH_SHORT).show();
        }
    }

    public void onNextButtonClickCallback(View view) {
        // TODO: Make the cursor point to the next person at the cursor and display data for this person.
        if (cursor.moveToNext()) {
            showCurrentPersonData();
        } else {
            if (cursor.isAfterLast()) {
                cursor.moveToLast();
            }
            Toast.makeText(this, "There's no next register", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackButtonClickCallback(View view) {
        this.finish();
    }

    void showCurrentPersonData() {
        // TODO: Show in the UI data from the tuple / row pointed by the cursor

        String fullName = cursor.getString(0);
        int age = cursor.getInt(1);
        boolean isWorking = cursor.getInt(2) == 1 ? true : false;

        binding.fullNameRegisterTextView.setText(fullName);
        binding.ageRegisterTextView.setText(Integer.toString(age));
        binding.isWorkingRegisterTextView.setText(isWorking ? "YES" : "NO");
    }

}