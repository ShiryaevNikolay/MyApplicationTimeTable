package com.example.timetable.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.timetable.fragments.AbstractTabFragment;
import com.example.timetable.fragments.ScheduleFragment;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TabsFragmentAdapter extends FragmentPagerAdapter {

    private Map<Integer, AbstractTabFragment> tabs;

    public TabsFragmentAdapter(Context context, @NonNull FragmentManager fm, SQLiteDatabase database) {
        super(fm);
        initTabsMap(context, database);
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return Objects.requireNonNull(tabs.get(position)).getTitle();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @SuppressLint("UseSparseArrays")
    private void initTabsMap(Context context, SQLiteDatabase database) {
        tabs = new HashMap<>();
        tabs.put(0, ScheduleFragment.getInstance(context, 0, database));
        tabs.put(1, ScheduleFragment.getInstance(context, 1, database));
        tabs.put(2, ScheduleFragment.getInstance(context, 2, database));
        tabs.put(3, ScheduleFragment.getInstance(context, 3, database));
        tabs.put(4, ScheduleFragment.getInstance(context, 4, database));
        tabs.put(5, ScheduleFragment.getInstance(context, 5, database));
    }
}
