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
import com.example.timetable.database.TeacherDBHelper;
import com.example.timetable.modules.ItemTouchHelperAdapter;
import com.example.timetable.modules.OnItemListener;

import java.util.List;

public class TeachersAdapter extends RecyclerView.Adapter<TeachersAdapter.TeachersViewHolder> implements ItemTouchHelperAdapter {

    private OnItemListener onItemListener;

    private List<RecyclerItem> listTeachers;
    private SQLiteDatabase database;

    public TeachersAdapter(List<RecyclerItem> listTeachers, SQLiteDatabase database, OnItemListener onItemListener){
        this.listTeachers = listTeachers;
        this.database = database;
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

    @Override
    public void onItemDismiss(int position) {
        // удаление элемента из списка по позиции
        RecyclerItem teacher = listTeachers.get(position);
        listTeachers.remove(position);
        notifyItemRemoved(position);

        // удаление элемента из базы данных
        database.delete(TeacherDBHelper.TABLE_TEACHERS, TeacherDBHelper.KEY_NAME + "= ?", new String[] {teacher.getText()});
    }

    static class TeachersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nameRvTeacher;
        OnItemListener onItemListener;

        TeachersViewHolder(View itemView, OnItemListener onItemListener){
            super(itemView);
            nameRvTeacher = itemView.findViewById(R.id.name_rv_teacher);
            this.onItemListener = onItemListener;

            nameRvTeacher.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }
}
