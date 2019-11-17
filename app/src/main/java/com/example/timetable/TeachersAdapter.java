package com.example.timetable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TeachersAdapter extends RecyclerView.Adapter<TeachersAdapter.TeachersViewHolder> {

    private List<RecyclerItem> listTeachers;

    TeachersAdapter(List<RecyclerItem> listTeachers){
        this.listTeachers = listTeachers;
    }

    @NonNull
    @Override
    public TeachersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        // находим recyclerview_teacher.xml
        int layoutIdForListTeacher = R.id.recyclerView_teacher;
        // нужен для создания нового xml файла на основе recyclerview_item.xml
        LayoutInflater inflater = LayoutInflater.from(context);
        // создаём новое представление (элемент) из layoutIdForItem, parent - <RecyclerView> в activity_list_items.xml, false - нужно ли помещать созданный объект layoutIdForItem внутрь parent(<RecyclerView>)
        View view = inflater.inflate(layoutIdForListTeacher, parent, false);
        return new TeachersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeachersViewHolder holder, int position) {
        final RecyclerItem itemList = listTeachers.get(position);
        holder.nameRvTeacher.setText(itemList.getText());
    }

    @Override
    public int getItemCount() {
        return listTeachers.size();
    }

    static class TeachersViewHolder extends RecyclerView.ViewHolder {
        private TextView nameRvTeacher;

        TeachersViewHolder(View itemView){
            super(itemView);
            nameRvTeacher = itemView.findViewById(R.id.name_rv_teacher);
        }
    }
}
