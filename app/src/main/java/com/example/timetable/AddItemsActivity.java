package com.example.timetable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.timetable.database.ItemDBHelper;

public class AddItemsActivity extends AppCompatActivity {

    private String text = "";
    private int idItem = 0;

    //==============================================================================================
    ItemDBHelper itemDbHelper;
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        //==========================================================================================
        itemDbHelper = new ItemDBHelper(this);

        final SQLiteDatabase database = itemDbHelper.getWritableDatabase();

        // для добавления новых строк в таблицу
        final ContentValues contentValues = new ContentValues();
        //==========================================================================================

        Toolbar toolbar = findViewById(R.id.toolbar_add_item);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_btn);

        new ButtonToReturnToMainActivity(toolbar, this);

        final Button cancelBtn = findViewById(R.id.add_item_cancel_btn);
        final Button okBtn = findViewById(R.id.add_item_ok_btn);

        // находим поле с вводом текста "предметы"
        final EditText editText = findViewById(R.id.editText_item);
        // проверяем, пустое ли поле ввода
        editText.addTextChangedListener(new TextWatcher() {
            // действия перед тем, как что то введено
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // если текст пустой, то кнопка "Ок" серая (неактивная)
                if (s.toString().equals("")) {
                    okBtn.setBackground(getDrawable(R.drawable.btn_no_activated));
                } else { // иначе зеленая (активная)
                    okBtn.setBackground(getDrawable(R.drawable.bg_green_corner_view));
                }
            }
            // действия, когда вводится какой то текст
            // s - то, что вводится, для преобразования в строку - s.toString()
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            // действия после того, как что то введено
            // editable - то, что введено. В строку - editable.toString()
            @Override
            public void afterTextChanged(Editable s) {
                // если текст пустой, то кнопка "Ок" серая (неактивная)
                if (s.toString().equals("")) {
                    okBtn.setBackground(getDrawable(R.drawable.btn_no_activated));
                } else { // иначе зеленая (активная)
                    okBtn.setBackground(getDrawable(R.drawable.bg_green_corner_view));
                }
                text = editText.getText().toString();
            }
        });

        // при нажатии на "Отмена" закрывается текущее окно activity
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        // при нажатии на "Ок", отправляется текст в ListItemActivity и закрывается текущее окно activity
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text.length() != 0){
                    contentValues.put(ItemDBHelper.KEY_NAME, text);

                    //вставляем данные в таблицу базы данных
                    database.insert(ItemDBHelper.TABLE_ITEMS, null, contentValues);

                    // ВЫВОД ДАННЫХ ИЗ БАЗЫ ДАННЫХ В ТЕРМИНАЛ
                    //==============================================================================
                    Cursor cursor = database.query(ItemDBHelper.TABLE_ITEMS, null, null, null, null, null, null);

                    if (cursor.moveToFirst()){
                        int idIndex = cursor.getColumnIndex(ItemDBHelper.KEY_ID);
                        int nameIndex = cursor.getColumnIndex(ItemDBHelper. KEY_NAME);
                        do {
                            System.out.println("ID = " + cursor.getInt(idIndex) +
                                    ", name = " + cursor.getString(nameIndex));
                            idItem = cursor.getInt(idIndex);
                        }while (cursor.moveToNext());
                    } else {
                        System.out.println("0 rows");
                    }

                    cursor.close();
                    itemDbHelper.close();
                    //==============================================================================

                    sendMessage(text, idItem);
                }
            }
        });
    }

    // отправка результата EditText в ListItemActivity
    private void sendMessage(String message, int idItem){
        Intent data = new Intent();
        data.putExtra("text", message);
        data.putExtra("idItem", idItem);
        setResult(RESULT_OK, data);
        finish();
    }
}
