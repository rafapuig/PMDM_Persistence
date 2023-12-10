package es.rafapuig.persistence.agenda.withddbb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DbAgendaOpenHelper extends SQLiteOpenHelper {

    private final String TAG = "DbAgendaOpenHelper";

    private static final String DATABASE_NAME = "agenda.db3";   //SQLite database's Filename

    protected static final int DATABASE_VERSION = 1;

    public static final String COLUMN_NAME_FULLNAME = "fullName";
    public static final String COLUMN_NAME_AGE = "age";
    public static final String COLUMN_NAME_IS_WORKING = "isWorking";


    private static final String SQL_CREATE_TABLE_PERSON =
            "CREATE TABLE person (fullName TEXT, age INTEGER, isWorking INTEGER)";

    private static final String SQL_DROP_TABLE_PERSON =
            "DROP TABLE IF EXISTS person";

    public DbAgendaOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_PERSON);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        // Logs that the database is being upgraded
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");

        // Kills the table and existing data
        sqLiteDatabase.execSQL(SQL_DROP_TABLE_PERSON);

        // Recreates the database with a new version
        onCreate(sqLiteDatabase);
    }
}
