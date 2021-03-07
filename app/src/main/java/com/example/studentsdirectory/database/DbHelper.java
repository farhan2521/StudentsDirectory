package com.example.studentsdirectory.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.studentsdirectory.database.DetailsContract.*;
import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DetailsEntry.TABLE_NAME + " (" +
                    DetailsEntry._ID + " INTEGER PRIMARY KEY," +
                    DetailsEntry.COLUMN_NAME_STUDENT + " TEXT," +
                    DetailsEntry.COLUMN_NAME_DOB + " DATE," +
                    DetailsEntry.COLUMN_NAME_DEPARTMENT +" TEXT)";

    public DbHelper(@Nullable Context context) {
        super(context, DetailsContract.DetailsEntry.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
