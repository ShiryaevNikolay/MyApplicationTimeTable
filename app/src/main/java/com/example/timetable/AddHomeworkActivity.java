package com.example.timetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.timetable.modules.ToolbarBtnBackListener;
import com.example.timetable.util.RequestCode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddHomeworkActivity extends AppCompatActivity implements ToolbarBtnBackListener, View.OnClickListener {
    private String text = "";
    private TextView tvToDate;
    private String textAddDate = "";
    private String textToDate = "";
    private Button okBtn;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_homework);

        Toolbar toolbar = findViewById(R.id.toolbar_add_homework);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_btn);

        new ButtonToReturnToMainActivity(toolbar, this);

        Button cancelBtn = findViewById(R.id.add_homework_cancel_btn);
        okBtn = findViewById(R.id.add_homework_ok_btn);

        tvToDate = findViewById(R.id.tv_date_homework);
        tvToDate.setOnClickListener(this);

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MMM.yyyy", Locale.getDefault());
        textAddDate = dateFormat.format(date);

        editText = findViewById(R.id.editText_homework);

        if (Objects.requireNonNull(getIntent().getExtras()).getInt("requestCode") == RequestCode.REQUEST_CODE_HOMEWORK_CHANGE) {
            editText.setText(getIntent().getExtras().getString("text"));
            text = getIntent().getExtras().getString("text");
            textAddDate = getIntent().getExtras().getString("addDate");
            textToDate = getIntent().getExtras().getString("toDate");
            tvToDate.setText(textAddDate);
            okBtn.setBackground(getDrawable(R.drawable.bg_green_corner_view));
        }

        // проверяем, пустое ли поле ввода
        editText.addTextChangedListener(new TextWatcher() {
            // действия перед тем, как что то введено
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // если текст пустой, то кнопка "Ок" серая (неактивная)
                checkOkBtn();
            }
            // действия, когда вводится какой то текст
            // s - то, что вводится, для преобразования в строку - s.toString()
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkOkBtn();
            }
            // действия после того, как что то введено
            // editable - то, что введено. В строку - editable.toString()
            @Override
            public void afterTextChanged(Editable s) {
                // если текст пустой, то кнопка "Ок" серая (неактивная)
                checkOkBtn();
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
        okBtn.setOnClickListener(this);
    }

    @Override
    public void onClickBtnBack() {
        setResult(RESULT_CANCELED);
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
    private void addOrChangeItem() {
        if(text.length() != 0){
            Intent data = new Intent();
            data.putExtra("text", text);
            data.putExtra("addDate", textAddDate);
            data.putExtra("toDate", textToDate);
            if (Objects.requireNonNull(getIntent().getExtras()).getInt("requestCode") == RequestCode.REQUEST_CODE_HOMEWORK_CHANGE) {
                data.putExtra("position", getIntent().getExtras().getInt("position"));
            }
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_homework_ok_btn:
                addOrChangeItem();
                break;
            case R.id.tv_date_homework:
                callDatePicker();
                break;
        }
    }

    private void callDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String fullDateFix;
                if (dayOfMonth < 10) {
                    fullDateFix = "0" + dayOfMonth + ".";
                } else {
                    fullDateFix = dayOfMonth + ".";
                }
                switch (month) {
                    case 0:
                        fullDateFix = fullDateFix + "янв";
                        break;
                    case 1:
                        fullDateFix = fullDateFix + "февр";
                        break;
                    case 2:
                        fullDateFix = fullDateFix + "мар";
                        break;
                    case 3:
                        fullDateFix = fullDateFix + "апр";
                        break;
                    case 4:
                        fullDateFix = fullDateFix + "мая";
                        break;
                    case 5:
                        fullDateFix = fullDateFix + "июн";
                        break;
                    case 6:
                        fullDateFix = fullDateFix + "июл";
                        break;
                    case 7:
                        fullDateFix = fullDateFix + "авг";
                        break;
                    case 8:
                        fullDateFix = fullDateFix + "сент";
                        break;
                    case 9:
                        fullDateFix = fullDateFix + "окт";
                        break;
                    case 10:
                        fullDateFix = fullDateFix + "нояб";
                        break;
                    case 11:
                        fullDateFix = fullDateFix + "дек";
                        break;
                }
                fullDateFix = fullDateFix + "." + year;
                tvToDate.setText(fullDateFix);
                textToDate = fullDateFix;
                checkOkBtn();
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void checkOkBtn() {
        if (editText.getText().toString().equals("") || tvToDate.getText().equals("")) {
            okBtn.setBackground(getDrawable(R.drawable.btn_no_activated));
        } else {
            okBtn.setBackground(getDrawable(R.drawable.bg_green_corner_view));
        }
    }
}
