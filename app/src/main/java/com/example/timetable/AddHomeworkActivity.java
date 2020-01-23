package com.example.timetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.timetable.database.HomeworkDBHelper;
import com.example.timetable.modules.ToolbarBtnBackListener;
import com.example.timetable.util.RequestCode;

import java.util.Objects;

public class AddHomeworkActivity extends AppCompatActivity implements ToolbarBtnBackListener {
    private String text = "";
    private int idItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_homework);

        Toolbar toolbar = findViewById(R.id.toolbar_add_homework);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_btn);

        new ButtonToReturnToMainActivity(toolbar, this);

        Button cancelBtn = findViewById(R.id.add_homework_cancel_btn);
        final Button okBtn = findViewById(R.id.add_homework_ok_btn);

        final EditText editText = findViewById(R.id.editText_homework);

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
                addOrChangeItem();
            }
        });
    }

    @Override
    public void onClickBtnBack() {
        finish();
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

    // отправка результата EditText в ListItemActivity
    private void sendMessage(String message, int idItem){
        Intent data = new Intent();
        data.putExtra("text", message);
        data.putExtra("idItem", idItem);
        if (Objects.requireNonNull(getIntent().getExtras()).getInt("requestCode") == RequestCode.REQUEST_CODE_HOMEWORK_CHANGE) {
            data.putExtra("position", getIntent().getExtras().getInt("position"));
        }
        setResult(RESULT_OK, data);
        finish();
    }

    private void addOrChangeItem() {
        if(text.length() != 0){
            if (Objects.requireNonNull(getIntent().getExtras()).getInt("requestCode") == RequestCode.REQUEST_CODE_ITEM_CHANGE) {
                idItem = getIntent().getExtras().getInt("idItem");
            }
            sendMessage(text, idItem);
        }
    }
}
