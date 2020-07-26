package com.colcab.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.colcab.fragments.ScheduledTicketsFragment;
import com.colcab.fragments.UnscheduledTicketsFragment;

public class OpenTicketPagerAdapter extends FragmentStatePagerAdapter {

    public OpenTicketPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ScheduledTicketsFragment();
            case 1:
                return new UnscheduledTicketsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Scheduled";
            case 1:
                return "Unscheduled";
            default:
                return "";
        }
    }
}
