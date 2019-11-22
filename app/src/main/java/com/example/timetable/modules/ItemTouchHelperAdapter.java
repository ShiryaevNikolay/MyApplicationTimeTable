package com.example.timetable.modules;

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);

    // для удаления предметов из списка свайпом
    void onItemDismiss(int position);
}
