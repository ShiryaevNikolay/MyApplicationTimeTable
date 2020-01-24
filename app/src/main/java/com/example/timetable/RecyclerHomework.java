package com.example.timetable;

public class RecyclerHomework {
    private String text;
    private int idItem;
    private boolean checkBox;

    RecyclerHomework(String text, int idItem, boolean checkBox) {
        this.text = text;
        this.idItem = idItem;
        this.checkBox = checkBox;
    }

    public String getText() {
        return text;
    }
    public int getId() { return idItem; }
    public boolean getCkeckBox() { return checkBox; }

    public void setText(String text) {
        this.text = text;
    }
    public void setId(int idItem) { this.idItem = idItem; }
    public void setCheckBox(boolean checkBox) { this.checkBox = checkBox; }
}
