package com.example.timetable.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetable.R;
import com.example.timetable.RecyclerItem;
import com.example.timetable.database.ItemDBHelper;
import com.example.timetable.modules.ItemTouchHelperAdapter;
import com.example.timetable.modules.OnItemListener;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder> implements ItemTouchHelperAdapter {

    private OnItemListener onItemListener;

    private List<RecyclerItem> listItems;
    private SQLiteDatabase database;

    public ItemsAdapter(List<RecyclerItem> listItems, SQLiteDatabase database, OnItemListener onItemListener){
        this.listItems = listItems;
        this.database = database;
        this.onItemListener = onItemListener;
    }

    @NonNull
    public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        // находим recyclerview_item.xml
        int layoutIdForListItem = R.layout.recyclerview_item;
        // нужен для создания нового xml файла на основе recyclerview_item.xml
        LayoutInflater inflater = LayoutInflater.from(context);
        // создаём новое представление (элемент) из layoutIdForItem, parent - <RecyclerView> в activity_list_items.xml, false - нужно ли помещать созданный объект layoutIdForItem внутрь parent(<RecyclerView>)
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        // обернём созданный элемент списка в ViewHolder
        return new ItemsViewHolder(view, onItemListener);
    }

    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        final RecyclerItem itemList = listItems.get(position);
        holder.nameRvItem.setText(itemList.getText());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public void onItemDismiss(int position) {
        // удаление элемента из списка по позиции
        RecyclerItem item = listItems.get(position);
        listItems.remove(position);
        notifyItemRemoved(position);

        // удаление элемента из базы данных
        database.delete(ItemDBHelper.TABLE_ITEMS, ItemDBHelper.KEY_NAME + "= ?", new String[] {item.getText()});
    }

    static class ItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameRvItem;
        OnItemListener onItemListener;

        ItemsViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            nameRvItem = itemView.findViewById(R.id.name_rv_item);
            this.onItemListener = onItemListener;

            nameRvItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }
}
