package com.example.timetable.adapters;

import android.annotation.SuppressLint;
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final HomeworkViewHolder holder, int position) {
        final RecyclerHomework itemList = listItems.get(position);
        holder.textView.setText(itemList.getText());
        holder.tvAddDate.setText(itemList.getAddDate());
        holder.tvToDate.setText("Выполнить к: " + itemList.getToDate());
        boolean flag = false;
        for (int i = 0; i < listItems.size(); i++) {
            if (listItems.get(i).getCheckBox()) {
                flag = true;
                break;
            }
        }
        if (flag) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }
        if (itemList.getCheckBox()) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.getVisibility() == View.VISIBLE) {
                    if (holder.checkBox.isChecked()) {
                        holder.checkBox.setChecked(false);
                        onLongClickItemListener.onLongClickItemListener(holder.getAdapterPosition(), false);
                    } else {
                        holder.checkBox.setChecked(true);
                        onLongClickItemListener.onLongClickItemListener(holder.getAdapterPosition(), true);
                    }
                } else {
                    onItemListener.onItemClick(holder.getAdapterPosition());
                }
            }
        });
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (holder.checkBox.isChecked()) {
                    holder.checkBox.setChecked(false);
                    holder.checkBox.setVisibility(View.INVISIBLE);
                    onLongClickItemListener.onLongClickItemListener(holder.getAdapterPosition(), false);
                } else {
                    holder.checkBox.setChecked(true);
                    holder.checkBox.setVisibility(View.VISIBLE);
                    onLongClickItemListener.onLongClickItemListener(holder.getAdapterPosition(), true);
                }
                return true;
            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    holder.checkBox.setChecked(true);
                    onLongClickItemListener.onLongClickItemListener(holder.getAdapterPosition(), true);
                } else {
                    holder.checkBox.setChecked(false);
                    onLongClickItemListener.onLongClickItemListener(holder.getAdapterPosition(), false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class HomeworkViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView textView;
        TextView tvAddDate;
        TextView tvToDate;
        CheckBox checkBox;

        HomeworkViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            textView = itemView.findViewById(R.id.text_item_rv_homework);
            tvAddDate = itemView.findViewById(R.id.tv_add_date_item_homework);
            tvToDate = itemView.findViewById(R.id.tv_to_date_item_homework);
            checkBox = itemView.findViewById(R.id.checkBox_item_homework);
        }
    }
}
