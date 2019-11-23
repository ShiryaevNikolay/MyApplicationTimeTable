package com.example.timetable.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ItemDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "itemDB";
    public static final String TABLE_ITEMS = "items";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";

    public ItemDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String strItem = "CREATE TABLE " + TABLE_ITEMS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT)";
        db.execSQL(strItem);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);

        onCreate(db);
    }
}
