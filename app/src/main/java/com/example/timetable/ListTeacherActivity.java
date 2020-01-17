package com.example.timetable;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.timetable.adapters.TeachersAdapter;
import com.example.timetable.database.TeacherDBHelper;
import com.example.timetable.dialogs.DeleteDialog;
import com.example.timetable.modules.DialogListener;
import com.example.timetable.modules.ItemTouchHelperAdapter;
import com.example.timetable.modules.OnItemListener;
import com.example.timetable.modules.SimpleItemTouchHelperCallback;
import com.example.timetable.modules.ToolbarBtnBackListener;
import com.example.timetable.util.RequestCode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListTeacherActivity extends AppCompatActivity implements OnItemListener, ItemTouchHelperAdapter, DialogListener, ToolbarBtnBackListener {

    Toolbar toolbar;
    String nameTeacher;
    int idItem = 0;
    RecyclerView recyclerView;
    TeachersAdapter teachersAdapter;

    public ArrayList<RecyclerItem> listTeacher;
    RecyclerItem item;

    TeacherDBHelper teacherDBHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_teacher);
        toolbar = findViewById(R.id.toolbar_teacher);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_btn);

        //для базы данных
        teacherDBHelper = new TeacherDBHelper(this);
        database = teacherDBHelper.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = database.query(TeacherDBHelper.TABLE_TEACHERS, null, null, null, null, null, null);

        new ButtonToReturnToMainActivity(toolbar, this);

        listTeacher = new ArrayList<>();

        // добавляем в список данные (названия предметов) из базы данных
        if (cursor.moveToFirst()){
            do {
                String nameTeacher = cursor.getString(cursor.getColumnIndex(TeacherDBHelper. KEY_NAME));
                int idItem = cursor.getInt(cursor.getColumnIndex(TeacherDBHelper. KEY_ID));
                listTeacher.add(new RecyclerItem(nameTeacher, idItem));
            }while (cursor.moveToNext());
        }

        // Находим RecyclerView
        recyclerView = findViewById(R.id.recyclerView_teacher);
        // то, как будет выглядеть RecyclerView (то есть список)
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // передаём layoutManager в RecyclerView
        recyclerView.setLayoutManager(layoutManager);
        // значит, что список фиксированный
        recyclerView.setHasFixedSize(true);

        FloatingActionButton fab = findViewById(R.id.fab_list_teacher);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListTeacherActivity.this, AddTeachersActivity.class);
                startActivityForResult(intent, RequestCode.REQUEST_CODE_TEACHER);
            }
        });

        teachersAdapter = new TeachersAdapter(listTeacher, this);
        //назначаем RecyclerView созданный Adapter
        recyclerView.setAdapter(teachersAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(this, this);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.REQUEST_CODE_TEACHER){
            if (resultCode == RESULT_OK){
                nameTeacher = data.getStringExtra("text");
                idItem = data.getIntExtra("idItem", idItem);
                listTeacher.add(new RecyclerItem(nameTeacher, idItem));
            } else {
                nameTeacher = "Ошибка доступа";
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        teachersAdapter = new TeachersAdapter(listTeacher, this);
        //назначаем RecyclerView созданный Adapter
        recyclerView.setAdapter(teachersAdapter);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemDismiss(int position) {
        item = listTeacher.get(position);
        listTeacher.remove(position);
        teachersAdapter.notifyItemRemoved(position);

        DialogFragment deleteDialog = new DeleteDialog(this, position);
        FragmentManager fragmentManager = (this.getSupportFragmentManager());
        deleteDialog.show(fragmentManager, "deleteDialog");
    }

    @Override
    public void onClickRemoveDialog() {
        database.delete(TeacherDBHelper.TABLE_TEACHERS, TeacherDBHelper.KEY_ID + " = " + item.getId(), null);
    }

    @Override
    public void onClickCancelDialog(int position) {
        listTeacher.add(position, item);
        teachersAdapter.notifyItemInserted(position);
    }

    @Override
    public void onClickBtnBack() {
        finish();
    }
}
