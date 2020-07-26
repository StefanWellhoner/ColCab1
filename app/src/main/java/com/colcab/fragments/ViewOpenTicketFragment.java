package com.colcab.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.colcab.R;
import com.colcab.adapters.ViewTicketPagerAdapter;

public class ViewOpenTicketFragment extends Fragment {

    public static final String TICKET_ID = "ticketID";
    public static String ticketID;

    public ViewOpenTicketFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ticketID = getArguments().getString(TICKET_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_open_ticket, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewTicketPagerAdapter adapter = new ViewTicketPagerAdapter(getChildFragmentManager());
        ViewPager viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.view_ticket_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                return NavigationUI.onNavDestinationSelected(item, navController)
                        || super.onOptionsItemSelected(item);
            case R.id.view_ticket_delete:
                Toast.makeText(getContext(), "Delete ticket: " + getArguments().getString(TICKET_ID), Toast.LENGTH_SHORT).show();
                break;
            case R.id.view_ticket_close:
                Toast.makeText(getContext(), "Close ticket: " + getArguments().getString(TICKET_ID), Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}

