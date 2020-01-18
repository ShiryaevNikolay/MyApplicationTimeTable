package com.example.timetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.timetable.database.ItemDBHelper;
import com.example.timetable.database.TeacherDBHelper;
import com.example.timetable.modules.ToolbarBtnBackListener;

public class AddTeachersActivity extends AppCompatActivity implements ToolbarBtnBackListener {

    private String text = "";
    private int idItem = 0;

    EditText etSurname;
    EditText etName;
    EditText etPatronymic;

    //==============================================================================================
    TeacherDBHelper teacherDbHelper;
    ContentValues contentValues;
    SQLiteDatabase database;
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teachers);

        //==========================================================================================
        teacherDbHelper = new TeacherDBHelper(this);

        database = teacherDbHelper.getWritableDatabase();

        // для добавления новых строк в таблицу
        contentValues = new ContentValues();
        //==========================================================================================

        Toolbar toolbar = findViewById(R.id.toolbar_add_teacher);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_btn);

        new ButtonToReturnToMainActivity(toolbar, this);

        Button cancelBtn = findViewById(R.id.add_teacher_cancel_btn);
        Button okBtn = findViewById(R.id.add_teacher_ok_btn);

        // находим поля с вводом текста "Фамилия, имя, отчество"
        etSurname = findViewById(R.id.editText_surname);
        etName = findViewById(R.id.editText_name);
        etPatronymic = findViewById(R.id.editText_patronymic);

        // проверяем, пустое ли поле ввода
        checkEmptyField(etSurname, okBtn);
        checkEmptyField(etName, okBtn);
        checkEmptyField(etPatronymic, okBtn);

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
                if (!etSurname.getText().toString().equals("") || !etName.getText().toString().equals("") || !etPatronymic.getText().toString().equals("")){
                    text = etSurname.getText() + " " + etName.getText() + " " + etPatronymic.getText();

                    // ВЫВОД ДАННЫХ ИЗ БАЗЫ ДАННЫХ В ТЕРМИНАЛ
                    //==============================================================================
                    contentValues.put(TeacherDBHelper.KEY_NAME, text);

                    //вставляем данные в таблицу базы данных
                    database.insert(TeacherDBHelper.TABLE_TEACHERS, null, contentValues);

                    Cursor cursor = database.query(TeacherDBHelper.TABLE_TEACHERS, null, null, null, null, null, null);

                    if (cursor.moveToFirst()){
                        int idIndex = cursor.getColumnIndex(TeacherDBHelper.KEY_ID);
                        int nameIndex = cursor.getColumnIndex(TeacherDBHelper. KEY_NAME);
                        do {
                            System.out.println("ID = " + cursor.getInt(idIndex) +
                                    ", name = " + cursor.getString(nameIndex));
                            idItem = cursor.getInt(idIndex);
                        }while (cursor.moveToNext());
                    } else {
                        System.out.println("0 rows");
                    }

                    cursor.close();
                    teacherDbHelper.close();
                    //==============================================================================

                    sendMessage(text, idItem);
                } else {
                    text = "";
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!etSurname.getText().toString().equals("") || !etName.getText().toString().equals("") || !etPatronymic.getText().toString().equals("")){
            text = etSurname.getText() + " " + etName.getText() + " " + etPatronymic.getText();

            // ВЫВОД ДАННЫХ ИЗ БАЗЫ ДАННЫХ В ТЕРМИНАЛ
            //==============================================================================
            contentValues.put(TeacherDBHelper.KEY_NAME, text);

            //вставляем данные в таблицу базы данных
            database.insert(TeacherDBHelper.TABLE_TEACHERS, null, contentValues);

            Cursor cursor = database.query(TeacherDBHelper.TABLE_TEACHERS, null, null, null, null, null, null);

            if (cursor.moveToFirst()){
                int idIndex = cursor.getColumnIndex(TeacherDBHelper.KEY_ID);
                do {
                    idItem = cursor.getInt(idIndex);
                }while (cursor.moveToNext());
            }

            cursor.close();
            teacherDbHelper.close();
            //==============================================================================

            sendMessage(text, idItem);
        } else {
            text = "";
        }
        return super.onOptionsItemSelected(item);
    }

    protected void checkEmptyField(final EditText editText, final Button btn){
        editText.addTextChangedListener(new TextWatcher() {
            // действия перед тем, как что то введено
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // если текст пустой, то кнопка "Ок" серая (неактивная)
                if (s.toString().equals("")){
                    btn.setBackground(getDrawable(R.drawable.btn_no_activated));
                } else { // иначе зеленая (активная)
                    btn.setBackground(getDrawable(R.drawable.bg_green_corner_view));
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
                if (s.toString().equals("")){
                    btn.setBackground(getDrawable(R.drawable.btn_no_activated));
                } else { // иначе зеленая (активная)
                    btn.setBackground(getDrawable(R.drawable.bg_green_corner_view));
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

    @Override
    public void onClickBtnBack() {
        finish();
    }
}
