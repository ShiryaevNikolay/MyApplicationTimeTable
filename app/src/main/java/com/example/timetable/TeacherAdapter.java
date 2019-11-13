package com.example.timetable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TeacherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static int viewHolderCount;
    private int numberTeacher;

    public TeacherAdapter(int numberOfItems){
        numberTeacher = numberOfItems;
        viewHolderCount = 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        // находим recyclerview_item.xml
        int layoutIdForListItem = R.layout.recyclerview_teacher;
        // нужен для создания нового xml файла на основе recyclerview_item.xml
        LayoutInflater inflater = LayoutInflater.from(context);
        // создаём новое представление (элемент) из layoutIdForItem, parent - <RecyclerView> в activity_list_items.xml, false - нужно ли помещать созданный объект layoutIdForItem внутрь parent(<RecyclerView>)
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        // обернём созданный элемент списка в ViewHolder
        TeacherViewHolder viewHolder = new TeacherViewHolder(view);
        //...
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return numberTeacher;
    }

    static class TeacherViewHolder extends RecyclerView.ViewHolder {

        public TeacherViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        // тут будем находить id всех TextView и т.д. в recyclerview_item.xml
    }
}
