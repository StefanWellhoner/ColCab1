package com.colcab;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OpenTicketsFragment extends Fragment {

    public OpenTicketsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_open_tickets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecipientPagerAdapter recipientAdapter = new RecipientPagerAdapter(getChildFragmentManager());
        ViewPager viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(recipientAdapter);
    }

}
class RecipientPagerAdapter extends FragmentStatePagerAdapter {

    public RecipientPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
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