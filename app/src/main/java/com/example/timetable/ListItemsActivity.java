package com.example.timetable;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import com.example.timetable.adapters.ItemsAdapter;
import com.example.timetable.database.ItemDBHelper;
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

import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class ListItemsActivity extends AppCompatActivity implements OnItemListener, ItemTouchHelperAdapter, DialogListener, ToolbarBtnBackListener {

    Toolbar toolbar;
    String nameItem;
    int idItem = 0;
    RecyclerView recyclerView;
    ItemsAdapter itemsAdapter;
    ItemTouchHelper.Callback callback;
    ItemTouchHelper touchHelper;

    ArrayList<RecyclerItem> listItems;
    RecyclerItem item;

    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        toolbar = findViewById(R.id.toolbar_item);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.toolbar_back_btn);

        new ButtonToReturnToMainActivity(toolbar, this);

        //для базы данных
        ItemDBHelper itemDbHelper = new ItemDBHelper(this);
        database = itemDbHelper.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = database.query(ItemDBHelper.TABLE_ITEMS, null, null, null, null, null, null);

        listItems = new ArrayList<>();

        // добавляем в список данные (названия предметов) из базы данных
        if (cursor.moveToFirst()){
            do {
                String nameItem = cursor.getString(cursor.getColumnIndex(ItemDBHelper. KEY_NAME));
                idItem = cursor.getInt(cursor.getColumnIndex(ItemDBHelper. KEY_ID));
                listItems.add(new RecyclerItem(nameItem, idItem));
            }while (cursor.moveToNext());
        }
        cursor.close();

        // Находим RecyclerView
        recyclerView = findViewById(R.id.recyclerView_item);
        // то, как будет выглядеть RecyclerView (то есть список)
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // передаём layoutManager в RecyclerView
        recyclerView.setLayoutManager(layoutManager);
        // значит, что список фиксированный
        recyclerView.setHasFixedSize(true);

        FloatingActionButton fab = findViewById(R.id.fab_list_item);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListItemsActivity.this, AddItemsActivity.class);
                intent.putExtra("requestCode", RequestCode.REQUEST_CODE_ITEM);
                startActivityForResult(intent, RequestCode.REQUEST_CODE_ITEM);
            }
        });

        // numberItems - кол-во элементов в списке, nameItem - название предмета
        itemsAdapter = new ItemsAdapter(listItems, this);
        //назначаем RecyclerView созданный Adapter
        recyclerView.setAdapter(itemsAdapter);

        callback = new SimpleItemTouchHelperCallback( this, this);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            if (requestCode == RequestCode.REQUEST_CODE_ITEM_CHANGE) {
                nameItem = data.getStringExtra("text");
                listItems.get(Objects.requireNonNull(data.getExtras()).getInt("position")).setText(nameItem);
            } else if (requestCode == RequestCode.REQUEST_CODE_ITEM) {
                nameItem = data.getStringExtra("text");
                idItem = data.getIntExtra("idItem", 0);
                listItems.add(new RecyclerItem(nameItem, idItem));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        itemsAdapter = new ItemsAdapter(listItems, this);
        //назначаем RecyclerView созданный Adapter
        recyclerView.setAdapter(itemsAdapter);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, AddItemsActivity.class);
        intent.putExtra("requestCode", RequestCode.REQUEST_CODE_ITEM_CHANGE);
        intent.putExtra("text", listItems.get(position).getText());
        intent.putExtra("idItem", listItems.get(position).getId());
        intent.putExtra("position", position);
        startActivityForResult(intent, RequestCode.REQUEST_CODE_ITEM_CHANGE);
    }

    @Override
    public void onItemDismiss(int position) {
        item = listItems.get(position);
        listItems.remove(position);
        itemsAdapter.notifyItemRemoved(position);

        DialogFragment deleteDialog = new DeleteDialog(this, position);
        FragmentManager fragmentManager = (this.getSupportFragmentManager());
        deleteDialog.show(fragmentManager, "deleteDialog");
    }

    @Override
    public void onClickRemoveDialog() {
        database.delete(ItemDBHelper.TABLE_ITEMS, ItemDBHelper.KEY_ID + " = " + item.getId(), null);
    }

    @Override
    public void onClickCancelDialog(int position) {
        listItems.add(position, item);
        itemsAdapter.notifyItemInserted(position);
    }

    @Override
    public void onClickBtnBack() {
        finish();
    }
}
