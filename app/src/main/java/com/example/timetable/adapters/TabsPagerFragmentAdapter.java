package com.example.timetable.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.timetable.fragments.ExampleFragment;

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
        switch (position){
            case 0:
                return ExampleFragment.getInstance();
            case 1:
                return ExampleFragment.getInstance();
            case 2:
                return ExampleFragment.getInstance();
            case 3:
                return ExampleFragment.getInstance();
            case 4:
                return ExampleFragment.getInstance();
            case 5:
                return ExampleFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
