package com.colcab.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.colcab.fragments.ViewTicketAdminFragment;
import com.colcab.fragments.ViewTicketInfoFragment;
import com.colcab.fragments.ViewTicketNotesFragment;

public class ViewTicketPagerAdapter extends FragmentStatePagerAdapter {

    public ViewTicketPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ViewTicketInfoFragment();
            case 1:
                return new ViewTicketAdminFragment();
            case 2:
                return new ViewTicketNotesFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Info";
            case 1:
                return "Admin";
            case 2:
                return "Notes";
            default:
                return "";
        }
    }
}
