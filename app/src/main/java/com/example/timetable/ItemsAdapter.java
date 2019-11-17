package com.example.timetable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder> {

    private List<RecyclerItem> listItems;

    ItemsAdapter(List<RecyclerItem> listItems){
        this.listItems = listItems;
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
        return new ItemsViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        final RecyclerItem itemList = listItems.get(position);
        holder.nameRvItem.setText(itemList.getText());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    static class ItemsViewHolder extends RecyclerView.ViewHolder {

        TextView nameRvItem;

        ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            nameRvItem = itemView.findViewById(R.id.name_rv_item);
        }
    }
}
