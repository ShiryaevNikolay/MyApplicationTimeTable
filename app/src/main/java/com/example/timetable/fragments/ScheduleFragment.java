package com.example.timetable.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.timetable.AddScheduleActivity;
import com.example.timetable.R;
import com.example.timetable.util.RequestCode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class ScheduleFragment extends Fragment {
    private static final int LAYOUT = R.layout.fragment_schedule;
    private static int position;

    public static ScheduleFragment getInstance(int position) {
        ScheduleFragment.position = position;

        Bundle args = new Bundle();
        ScheduleFragment fragment = new ScheduleFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(LAYOUT, container, false);
        FloatingActionButton fab = view.findViewById(R.id.fab_fragment_schedule);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddScheduleActivity.class);
                startActivityForResult(intent, RequestCode.REQUEST_CODE_SCHEDULE);
            }
        });

        return view;
    }
}
