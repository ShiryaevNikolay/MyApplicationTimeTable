package com.example.timetable;

public class RecyclerSchedule {

    private int hours;
    private int minutes;
    private int idItem;
    private String clock;
    private String name;
    private String teacher;

    public RecyclerSchedule(String clock, String name, String teacher, int idItem, int hours, int minutes) {
        this.clock = clock;
        this.name = name;
        this.teacher = teacher;
        this.idItem = idItem;
        this.hours = hours;
        this.minutes = minutes;
    }

    public String getClock() {
        return clock;
    }
    public String getName() {
        return name;
    }
    public String getTeacher(){
        return teacher;
    }
    public int getId() { return idItem; }
    public int getHours() { return hours; }
    public int getMinutes() { return minutes; }

    public void setClock(String clock) {
        this.clock = clock;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setTeacher(String teacher){
        this.teacher = teacher;
    }
    public void setId(int idItem) { this.idItem = idItem; }
    public void setHours(int hours) { this.hours = hours; }
    public void setMinutes(int minutes) { this.minutes = minutes; }
}