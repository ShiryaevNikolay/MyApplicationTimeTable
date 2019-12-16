package com.example.timetable;

public class RecyclerItem {

    private String text;
    private int idItem;

    RecyclerItem(String text, int idItem) {
        this.text = text;
        this.idItem = idItem;
    }

    public String getText() {
        return text;
    }
    public int getId() { return idItem; }

    public void setText(String text) {
        this.text = text;
    }
    public void setId(int idItem) { this.idItem = idItem; }
}
