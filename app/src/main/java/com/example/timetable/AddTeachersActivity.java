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

import com.example.timetable.database.TeacherDBHelper;

public class AddTeachersActivity extends AppCompatActivity {

    private String text = "";
    private int idItem = 0;

    //==============================================================================================
    TeacherDBHelper teacherDbHelper;
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teachers);

        //==========================================================================================
        teacherDbHelper = new TeacherDBHelper(this);

        final SQLiteDatabase database = teacherDbHelper.getWritableDatabase();

        // для добавления новых строк в таблицу
        final ContentValues contentValues = new ContentValues();
        //==========================================================================================

        Toolbar toolbar = findViewById(R.id.toolbar_add_teacher);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_btn);

        new ButtonToReturnToMainActivity(toolbar, this);

        Button cancelBtn = findViewById(R.id.add_teacher_cancel_btn);
        Button okBtn = findViewById(R.id.add_teacher_ok_btn);

        // находим поля с вводом текста "Фамилия, имя, отчество"
        final EditText etSurname = findViewById(R.id.editText_surname);
        final EditText etName = findViewById(R.id.editText_name);
        final EditText etPatronymic = findViewById(R.id.editText_patronymic);

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
}
