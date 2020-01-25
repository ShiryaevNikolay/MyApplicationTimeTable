package com.example.timetable;

public class RecyclerHomework {
    private String text;
    private String textAddDate;
    private String textToDate;
    private int idItem;
    private boolean checkBox;

    RecyclerHomework(String text, int idItem, boolean checkBox, String textAddDate, String textToDate) {
        this.text = text;
        this.textAddDate = textAddDate;
        this.textToDate = textToDate;
        this.idItem = idItem;
        this.checkBox = checkBox;
    }

    public String getText() {
        return text;
    }
    public String getAddDate() {
        return textAddDate;
    }
    public String getToDate() {
        return textToDate;
    }
    public int getId() { return idItem; }
    public boolean getCheckBox() { return checkBox; }

    public void setText(String text) {
        this.text = text;
    }
    public void setAddDate(String textAddDate) {
        this.textAddDate = textAddDate;
    }
    public void setToDate(String textToDate) {
        this.textToDate = textToDate;
    }
    public void setId(int idItem) { this.idItem = idItem; }
    public void setCheckBox(boolean checkBox) { this.checkBox = checkBox; }
}
