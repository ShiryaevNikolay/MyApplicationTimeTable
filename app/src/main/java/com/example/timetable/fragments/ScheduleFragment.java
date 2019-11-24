package com.example.timetable.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.timetable.R;

public class ScheduleFragment extends Fragment {
    private static final int LAYOUT = R.layout.fragment_schedule;

    public static ScheduleFragment getInstance(int position) {
        Bundle args = new Bundle();
        ScheduleFragment fragment = new ScheduleFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(LAYOUT, container, false);
        return view;
    }
}
