package com.example.timetable;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.EditText;

import com.example.timetable.util.DBHelper;


public class OnClickDatabase implements View.OnClickListener {

    private DBHelper dbHelper;

    private EditText etNameItem;
    private String textName;

    @Override
    public void onClick(View v) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        // для добавления новых строк в таблицу
        ContentValues contentValues = new ContentValues();

        etNameItem.findViewById(R.id.editText_item);
        textName = etNameItem.getText().toString();

        switch (v.getId()){
            case R.id.add_item_ok_btn:
                contentValues.put(DBHelper.KEY_NAME, textName);

                database.insert(DBHelper.TABLE_ITEMS, null, contentValues);
                break;
        }

        Cursor cursor = database.query(DBHelper.TABLE_ITEMS, null, null, null, null, null, null);

        if (cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper. KEY_NAME);
            do {
                System.out.println("ID = " + cursor.getInt(idIndex) +
                                    ", name = " + cursor.getString(nameIndex));
            }while (cursor.moveToNext());
        } else {
            System.out.println("0 rows");
        }

        cursor.close();
        dbHelper.close();
    }
}
