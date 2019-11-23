package com.example.timetable;

public class RecyclerSchedule {

    private String clock;
    private String name;
    private String teacher;

    RecyclerSchedule(String clock, String name, String teacher) {
        this.clock = clock;
        this.name = name;
        this.teacher = teacher;
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

    public void setClock(String clock) {
        this.clock = clock;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setTeacher(String teacher){
        this.teacher = teacher;
    }
}
