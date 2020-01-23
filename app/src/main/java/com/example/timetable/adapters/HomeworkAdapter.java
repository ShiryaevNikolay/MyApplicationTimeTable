package com.example.timetable.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetable.R;
import com.example.timetable.RecyclerHomework;
import com.example.timetable.modules.OnItemListener;

import java.util.ArrayList;

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder> {
    private OnItemListener onItemListener;
    ArrayList<RecyclerHomework> listItems;

    public HomeworkAdapter(ArrayList<RecyclerHomework> listItems, OnItemListener onItemListener) {
        this.listItems = listItems;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public HomeworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        // находим recyclerview_homework.xml
        int layoutIdForListItem = R.layout.recyclerview_homework;
        // нужен для создания нового xml файла на основе recyclerview_homework.xml
        LayoutInflater inflater = LayoutInflater.from(context);
        // создаём новое представление (элемент) из layoutIdForItem, parent - <RecyclerView> в activity_list_homework.xml, false - нужно ли помещать созданный объект layoutIdForItem внутрь parent(<RecyclerView>)
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        // обернём созданный элемент списка в ViewHolder
        return new HomeworkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeworkViewHolder holder, int position) {
        RecyclerHomework itemList = listItems.get(position);
        holder.textView.setText(itemList.getText());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class HomeworkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;

        public HomeworkViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_item_rv_homework);

        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }
}
