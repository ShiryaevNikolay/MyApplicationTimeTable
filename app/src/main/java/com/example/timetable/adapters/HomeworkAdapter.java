package com.example.timetable.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetable.R;
import com.example.timetable.RecyclerHomework;
import com.example.timetable.modules.OnItemListener;
import com.example.timetable.modules.OnLongClickItemListener;

import java.util.ArrayList;

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder> {
    private OnItemListener onItemListener;
    private ArrayList<RecyclerHomework> listItems;
    private OnLongClickItemListener onLongClickItemListener;

    public HomeworkAdapter(ArrayList<RecyclerHomework> listItems, OnItemListener onItemListener, OnLongClickItemListener onLongClickItemListener) {
        this.listItems = listItems;
        this.onItemListener = onItemListener;
        this.onLongClickItemListener = onLongClickItemListener;
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

    class HomeworkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView textView;
        CheckBox checkBox;

        HomeworkViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_item_rv_homework);
            checkBox = itemView.findViewById(R.id.checkBox_item_homework);
            if (checkBox.isChecked()) {
                checkBox.setVisibility(View.VISIBLE);
            } else {
                checkBox.setVisibility(View.INVISIBLE);
            }

            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            checkBox.setChecked(true);
            if (checkBox.isChecked()) {
                checkBox.setVisibility(View.VISIBLE);
            } else {
                checkBox.setVisibility(View.INVISIBLE);
            }
            onLongClickItemListener.onLongClickItemListener(getAdapterPosition());
            return false;
        }
    }
}
