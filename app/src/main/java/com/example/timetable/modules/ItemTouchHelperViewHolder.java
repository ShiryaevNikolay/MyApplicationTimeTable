package com.example.timetable.modules;

public interface ItemTouchHelperViewHolder {
    // Вызывается, когда впервые регистрируется элемент как перемещенынй или проведённый
    void onItemSelected();

    // Вызывавется, когда элемент завершил перемещение и он должен быть очищен
    void onItemClear();
}
