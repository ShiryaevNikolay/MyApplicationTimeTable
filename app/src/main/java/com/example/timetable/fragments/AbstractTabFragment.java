package com.example.timetable.fragments;

import android.content.Context;

import androidx.fragment.app.Fragment;

public class AbstractTabFragment extends Fragment {
    private String title;
    protected Context context;

    public String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }
}
