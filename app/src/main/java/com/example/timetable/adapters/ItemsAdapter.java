package com.example.timetable.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.timetable.R;
import com.example.timetable.RecyclerItem;
import com.example.timetable.modules.OnItemListener;

import java.util.ArrayList;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder> {

    private OnItemListener onItemListener;

    private ArrayList<RecyclerItem> listItems;


    public ItemsAdapter(ArrayList<RecyclerItem> listItems, OnItemListener onItemListener){
        this.listItems = listItems;
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
        RecyclerItem itemList = listItems.get(position);
        holder.nameRvItem.setText(itemList.getText());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    static class ItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameRvItem;
        OnItemListener onItemListener;

        ItemsViewHolder(@NonNull final View itemView, OnItemListener onItemListener) {
            super(itemView);
            nameRvItem = itemView.findViewById(R.id.name_rv_item);
            this.onItemListener = onItemListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }
}
