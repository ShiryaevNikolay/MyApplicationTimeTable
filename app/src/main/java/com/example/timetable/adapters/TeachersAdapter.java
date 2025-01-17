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

public class TeachersAdapter extends RecyclerView.Adapter<TeachersAdapter.TeachersViewHolder> {

    private OnItemListener onItemListener;

    private ArrayList<RecyclerItem> listTeachers;

    public TeachersAdapter(ArrayList<RecyclerItem> listTeachers, OnItemListener onItemListener){
        this.listTeachers = listTeachers;
        this.onItemListener = onItemListener;
    }

    @NonNull
    public TeachersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        // находим recyclerview_teacher.xml
        int layoutIdForListTeacher = R.layout.recyclerview_teacher;
        // нужен для создания нового xml файла на основе recyclerview_item.xml
        LayoutInflater inflater = LayoutInflater.from(context);
        // создаём новое представление (элемент) из layoutIdForItem, parent - <RecyclerView> в activity_list_items.xml, false - нужно ли помещать созданный объект layoutIdForItem внутрь parent(<RecyclerView>)
        View view = inflater.inflate(layoutIdForListTeacher, parent, false);
        return new TeachersViewHolder(view, onItemListener);
    }

    public void onBindViewHolder(@NonNull TeachersViewHolder holder, int position) {
        final RecyclerItem itemList = listTeachers.get(position);
        holder.nameRvTeacher.setText(itemList.getText());
    }

    @Override
    public int getItemCount() {
        return listTeachers.size();
    }

    static class TeachersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nameRvTeacher;
        OnItemListener onItemListener;

        TeachersViewHolder(View itemView, OnItemListener onItemListener){
            super(itemView);
            nameRvTeacher = itemView.findViewById(R.id.name_rv_teacher);
            this.onItemListener = onItemListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }
}
