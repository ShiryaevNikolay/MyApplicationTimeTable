package com.example.timetable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.example.timetable.adapters.HomeworkAdapter;
import com.example.timetable.database.HomeworkDBHelper;
import com.example.timetable.dialogs.DeleteDialog;
import com.example.timetable.modules.DialogListener;
import com.example.timetable.modules.OnItemListener;
import com.example.timetable.modules.OnLongClickItemListener;
import com.example.timetable.modules.ToolbarBtnBackListener;
import com.example.timetable.util.RequestCode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class ListHomeworkActivity extends AppCompatActivity implements ToolbarBtnBackListener, OnItemListener, OnLongClickItemListener, View.OnClickListener, DialogListener {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ArrayList<RecyclerHomework> listItems;
    HomeworkAdapter homeworkAdapter;
    SQLiteDatabase database;
    int idItem = 0;
    String nameItem;
    FloatingActionButton fabRemoveItem;

    ArrayList<RecyclerHomework> listItemsRemoved = new ArrayList<>();
    ArrayList<Integer> positionItemRemoved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_homework);

        toolbar = findViewById(R.id.toolbar_homework);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_btn);

        new ButtonToReturnToMainActivity(toolbar, this);

        listItems = new ArrayList<>();

        HomeworkDBHelper homeworkDBHelper = new HomeworkDBHelper(this);
        database = homeworkDBHelper.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = database.query(HomeworkDBHelper.TABLE_HOMEWORK, null, null, null, null, null, null);

        // добавляем в список данные (названия предметов) из базы данных
        if (cursor.moveToFirst()){
            do {
                nameItem = cursor.getString(cursor.getColumnIndex(HomeworkDBHelper. KEY_TASKS));
                idItem = cursor.getInt(cursor.getColumnIndex(HomeworkDBHelper. KEY_ID));
                listItems.add(new RecyclerHomework(nameItem, idItem, false));
            }while (cursor.moveToNext());
        }
        cursor.close();

        // Находим RecyclerView
        recyclerView = findViewById(R.id.recyclerView_homework);
        // то, как будет выглядеть RecyclerView (то есть список)
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        // передаём layoutManager в RecyclerView
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        // значит, что список фиксированный
        recyclerView.setHasFixedSize(true);

        FloatingActionButton fabAddItem = findViewById(R.id.fab_add_item_homework);
        fabAddItem.setOnClickListener(this);
        fabRemoveItem = findViewById(R.id.fab_remove_item_homework);
        fabRemoveItem.setOnClickListener(this);
        checkVisibleBtn();

        homeworkAdapter = new HomeworkAdapter(listItems, this, this);
        //назначаем RecyclerView созданный Adapter
        recyclerView.setAdapter(homeworkAdapter);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            ContentValues contentValues = new ContentValues();
            contentValues.put(HomeworkDBHelper.KEY_TASKS, data.getStringExtra("text"));
            if (requestCode == RequestCode.REQUEST_CODE_HOMEWORK_CHANGE) {
                database.update(HomeworkDBHelper.TABLE_HOMEWORK, contentValues, HomeworkDBHelper.KEY_ID + " = " + listItems.get(Objects.requireNonNull(data.getExtras()).getInt("position")).getId(), null);
            } else if (requestCode == RequestCode.REQUEST_CODE_HOMEWORK) {
                database.insert(HomeworkDBHelper.TABLE_HOMEWORK, null, contentValues);
            }
            listItems.clear();
            @SuppressLint("Recycle") Cursor cursor = database.query(HomeworkDBHelper.TABLE_HOMEWORK, null, null, null, null, null, null);

            // добавляем в список данные (названия предметов) из базы данных
            if (cursor.moveToFirst()){
                do {
                    nameItem = cursor.getString(cursor.getColumnIndex(HomeworkDBHelper. KEY_TASKS));
                    idItem = cursor.getInt(cursor.getColumnIndex(HomeworkDBHelper. KEY_ID));
                    listItems.add(new RecyclerHomework(nameItem, idItem, false));
                }while (cursor.moveToNext());
            }
            cursor.close();

            homeworkAdapter = new HomeworkAdapter(listItems, this, this);
            //назначаем RecyclerView созданный Adapter
            recyclerView.setAdapter(homeworkAdapter);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, AddHomeworkActivity.class);
        intent.putExtra("requestCode", RequestCode.REQUEST_CODE_HOMEWORK_CHANGE);
        intent.putExtra("text", listItems.get(position).getText());
        intent.putExtra("position", position);
        startActivityForResult(intent, RequestCode.REQUEST_CODE_HOMEWORK_CHANGE);
    }


    @Override
    public void onLongClickItemListener(int position, boolean flag) {
        if (flag) {
            listItems.get(position).setCheckBox(true);
        } else {
            listItems.get(position).setCheckBox(false);
        }
        homeworkAdapter.notifyDataSetChanged();
        checkVisibleBtn();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add_item_homework:
                Intent intent = new Intent(this, AddHomeworkActivity.class);
                intent.putExtra("requestCode", RequestCode.REQUEST_CODE_HOMEWORK);
                startActivityForResult(intent, RequestCode.REQUEST_CODE_HOMEWORK);
                break;
            case R.id.fab_remove_item_homework:
                positionItemRemoved = new ArrayList<>();
                for (int i = 0; i < listItems.size(); i++) {
                    if (listItems.get(i).getCheckBox()) {
                        listItemsRemoved.add(listItems.get(i));
                        positionItemRemoved.add(i);
                    }
                }
                for (int i = positionItemRemoved.size() - 1; i >= 0; i--) {
                    listItems.remove(positionItemRemoved.get(i).intValue());
                    homeworkAdapter.notifyItemRemoved(positionItemRemoved.get(i));
                }

                DialogFragment deleteDialog = new DeleteDialog(this, listItemsRemoved.size());
                FragmentManager fragmentManager = (this.getSupportFragmentManager());
                deleteDialog.show(fragmentManager, "deleteDialog");
                break;
        }
    }

    @SuppressLint("RestrictedApi")
    private void checkVisibleBtn() {
        boolean flagCheckIfRemove = false;
        for (int i = 0; i < listItems.size(); i++) {
            if (listItems.get(i).getCheckBox()) {
                flagCheckIfRemove = true;
                break;
            }
        }
        if (flagCheckIfRemove) {
            fabRemoveItem.setVisibility(View.VISIBLE);
        } else {
            fabRemoveItem.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClickBtnBack() {
        finish();
    }

    @Override
    public void onClickRemoveDialog() {
        for (int i = 0; i < listItemsRemoved.size(); i ++) {
            database.delete(HomeworkDBHelper.TABLE_HOMEWORK, HomeworkDBHelper.KEY_ID + " = " + listItemsRemoved.get(i).getId(), null);
        }
        listItemsRemoved.clear();
        homeworkAdapter.notifyDataSetChanged();
        checkVisibleBtn();
    }

    @Override
    public void onClickCancelDialog(int position) {
        for (int i = 0; i < positionItemRemoved.size(); i++) {
            listItems.add(positionItemRemoved.get(i), listItemsRemoved.get(i));
            homeworkAdapter.notifyItemInserted(positionItemRemoved.get(i));
        }
        listItemsRemoved.clear();
        positionItemRemoved.clear();
        checkVisibleBtn();
    }
}
