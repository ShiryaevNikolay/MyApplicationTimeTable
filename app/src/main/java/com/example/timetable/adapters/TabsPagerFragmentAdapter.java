package com.example.timetable.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.timetable.fragments.ScheduleFragment;

public class TabsPagerFragmentAdapter extends FragmentPagerAdapter {

    private String[] tabs;

    public TabsPagerFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
        tabs = new String[] {
                "ПН",
                "ВТ",
                "СР",
                "ЧТ",
                "ПТ",
                "СБ"
        };
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return ScheduleFragment.getInstance(position);
    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
