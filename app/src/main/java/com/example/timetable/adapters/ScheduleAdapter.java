package com.example.timetable.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetable.R;
import com.example.timetable.RecyclerSchedule;
import com.example.timetable.database.ScheduleDBHelper;
import com.example.timetable.modules.ItemTouchHelperAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> implements ItemTouchHelperAdapter {

    private List<RecyclerSchedule> listItems;
    private SQLiteDatabase database;
    private RecyclerView recyclerView;

    public ScheduleAdapter(List<RecyclerSchedule> listItems, SQLiteDatabase database, RecyclerView recyclerView){
        this.listItems = listItems;
        this.database = database;
        this.recyclerView = recyclerView;
    }

    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        // находим recyclerview_schedule.xml
        int layoutIdForListItem = R.layout.recyclerview_schedule;
        // нужен для создания нового xml файла на основе recyclerview_schedule.xml
        LayoutInflater inflater = LayoutInflater.from(context);
        // создаём новое представление (элемент) из layoutIdForItem, parent - <RecyclerView> в activity_list_schedule.xml, false - нужно ли помещать созданный объект layoutIdForItem внутрь parent(<RecyclerView>)
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        // обернём созданный элемент списка в ViewHolder
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        final RecyclerSchedule itemList = listItems.get(position);
        holder.clockRvItem.setText(itemList.getClock());
        holder.nameRvItem.setText(itemList.getName());
        holder.teacherRvItem.setText(itemList.getTeacher());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public void onItemDismiss(final int position) {
        // удаление элемента из списка по позиции
        final RecyclerSchedule item = listItems.get(position);
        listItems.remove(position);
        notifyItemRemoved(position);

        Snackbar snackbar = Snackbar.make(recyclerView, "Item has been deleted.", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItems.add(position, item);
                notifyItemInserted(position);
            }
        });
        snackbar.show();
        snackbar.addCallback(new Snackbar.Callback() {
            @SuppressLint("SwitchIntDef")
            public void onDismissed(Snackbar snackbar, int event) {
                switch (event) {
                    case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:
                    case Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE:
                        // удаление элемента из базы данных
                        database.delete(ScheduleDBHelper.TABLE_SCHEDULE, ScheduleDBHelper.KEY_CLOCK + "= ?", new String[] {item.getClock()});
                        break;
                }
            }
        });
    }

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView clockRvItem;
        TextView nameRvItem;
        TextView teacherRvItem;

        ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            clockRvItem = itemView.findViewById(R.id.clock_rv_schedule);
            nameRvItem = itemView.findViewById(R.id.name_rv_schedule);
            teacherRvItem = itemView.findViewById(R.id.teacher_rv_schedule);
        }
    }
}