package com.example.timetable.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ScheduleDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "scheduleDB";
    public static final String TABLE_SCHEDULE = "schedule";

    public static final String KEY_ID = "_id";
    public static final String KEY_DAY = "day";
    public static final String KEY_CLOCK = "clock";
    public static final String KEY_NAME = "name";
    public static final String KEY_TEACHER = "teacher";
    public static final String KEY_HOURS = "hours";
    public static final String KEY_MINUTES = "minutes";

    public ScheduleDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String strItem = "CREATE TABLE " + TABLE_SCHEDULE + "(" + KEY_ID + " INTEGER PRIMARY KEY, "
                                                                + KEY_DAY + " TEXT, "
                                                                + KEY_CLOCK + " TEXT, "
                                                                + KEY_HOURS + " INTEGER, "
                                                                + KEY_MINUTES + " INTEGER, "
                                                                + KEY_NAME + " TEXT, "
                                                                + KEY_TEACHER + " TEXT)";
        db.execSQL(strItem);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);

        onCreate(db);
    }
}