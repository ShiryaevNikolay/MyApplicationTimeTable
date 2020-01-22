package com.example.timetable.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TeacherDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "teacherDB";
    public static final String TABLE_TEACHERS = "teachers";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_SURNAME = "surname";
    public static final String KEY_PATRONYMIC = "patronymic";

    public TeacherDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String strItem = "CREATE TABLE " + TABLE_TEACHERS + "(" + KEY_ID + " INTEGER PRIMARY KEY, "
                                                                + KEY_NAME + " TEXT, "
                                                                + KEY_PATRONYMIC + " TEXT, "
                                                                + KEY_SURNAME + " TEXT)";
        db.execSQL(strItem);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEACHERS);

        onCreate(db);
    }
}
