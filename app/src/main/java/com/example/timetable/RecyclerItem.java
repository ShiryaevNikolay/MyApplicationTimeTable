package com.example.timetable;

public class RecyclerItem {

    private String text;

    RecyclerItem(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
