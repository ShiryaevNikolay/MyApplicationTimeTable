package com.example.timetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
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
import com.example.timetable.util.RequestCode;

import java.util.Objects;

public class AddTeachersActivity extends AppCompatActivity implements ToolbarBtnBackListener {

    private String text = "";
    private String name = "";
    private String patronymic = "";
    private String surname = "";
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

        if (Objects.requireNonNull(getIntent().getExtras()).getInt("requestCode") == RequestCode.REQUEST_CODE_TEACHER_CHANGE) {
            @SuppressLint("Recycle") Cursor cursor = database.query(TeacherDBHelper.TABLE_TEACHERS, null, TeacherDBHelper.KEY_ID + " = ?", new String[] {String.valueOf(getIntent().getExtras().getInt("idItem"))}, null, null, null);
            cursor.moveToFirst();
            etName.setText(cursor.getString(cursor.getColumnIndex(TeacherDBHelper.KEY_NAME)));
            etPatronymic.setText(cursor.getString(cursor.getColumnIndex(TeacherDBHelper.KEY_PATRONYMIC)));
            etSurname.setText(cursor.getString(cursor.getColumnIndex(TeacherDBHelper.KEY_SURNAME)));
            name = cursor.getString(cursor.getColumnIndex(TeacherDBHelper.KEY_NAME));
            patronymic = cursor.getString(cursor.getColumnIndex(TeacherDBHelper.KEY_PATRONYMIC));
            surname = cursor.getString(cursor.getColumnIndex(TeacherDBHelper.KEY_SURNAME));
            text = surname + " " + name + " " + patronymic;
            okBtn.setBackground(getDrawable(R.drawable.bg_green_corner_view));
        }

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
                addOrChangeItem();
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
        if (item.getItemId() == R.id.menu_action_save) {
            addOrChangeItem();
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
                switch (editText.getId()) {
                    case R.id.editText_surname:
                        surname = s.toString();
                        break;
                    case R.id.editText_name:
                        name = s.toString();
                        break;
                    case R.id.editText_patronymic:
                        patronymic = s.toString();
                        break;
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
                switch (editText.getId()) {
                    case R.id.editText_surname:
                        surname = s.toString();
                        break;
                    case R.id.editText_name:
                        name = s.toString();
                        break;
                    case R.id.editText_patronymic:
                        patronymic = s.toString();
                        break;
                }
            }
        });
    }

    // отправка результата EditText в ListItemActivity
    private void sendMessage(String message, int idItem){
        Intent data = new Intent();
        data.putExtra("text", message);
        data.putExtra("idItem", idItem);
        if (Objects.requireNonNull(getIntent().getExtras()).getInt("requestCode") == RequestCode.REQUEST_CODE_TEACHER_CHANGE) {
            data.putExtra("position", getIntent().getExtras().getInt("position"));
        }
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onClickBtnBack() {
        finish();
    }

    private void addOrChangeItem() {
        if(!etSurname.getText().toString().equals("") || !etName.getText().toString().equals("") || !etPatronymic.getText().toString().equals("")){
            contentValues.put(TeacherDBHelper.KEY_NAME, etName.getText().toString());
            contentValues.put(TeacherDBHelper.KEY_PATRONYMIC, etPatronymic.getText().toString());
            contentValues.put(TeacherDBHelper.KEY_SURNAME, etSurname.getText().toString());

            if (Objects.requireNonNull(getIntent().getExtras()).getInt("requestCode") == RequestCode.REQUEST_CODE_TEACHER_CHANGE) {
                database.update(TeacherDBHelper.TABLE_TEACHERS, contentValues, TeacherDBHelper.KEY_ID + " = " + getIntent().getExtras().getInt("idItem"), null);
                idItem = getIntent().getExtras().getInt("idItem");
            } else if (Objects.requireNonNull(getIntent().getExtras()).getInt("requestCode") == RequestCode.REQUEST_CODE_TEACHER) {
                //вставляем данные в таблицу базы данных
                database.insert(TeacherDBHelper.TABLE_TEACHERS, null, contentValues);
                Cursor cursor = database.query(TeacherDBHelper.TABLE_TEACHERS, null, null, null, null, null, null);
                if (cursor.moveToFirst()){
                    int idIndex = cursor.getColumnIndex(ItemDBHelper.KEY_ID);
                    do {
                        idItem = cursor.getInt(idIndex);
                    }while (cursor.moveToNext());
                }
                cursor.close();
                teacherDbHelper.close();
            }

            text = etSurname.getText() + " " + etName.getText() + " " + etPatronymic.getText();
            sendMessage(text, idItem);
        }
    }
}
