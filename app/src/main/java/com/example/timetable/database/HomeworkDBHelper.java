package com.example.timetable.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HomeworkDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "homeworkDB";
    public static final String TABLE_HOMEWORK = "homework";

    public static final String KEY_ID = "_id";
    public static final String KEY_TASKS = "tasks";

    public HomeworkDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String strItem = "CREATE TABLE " + TABLE_HOMEWORK + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TASKS + " TEXT)";
        db.execSQL(strItem);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOMEWORK);

        onCreate(db);
    }
}
