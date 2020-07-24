package com.colcab;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UnscheduledTicketsFragment extends Fragment implements View.OnClickListener {

    private NavController navController;

    public UnscheduledTicketsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_unscheduled_tickets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.btn_view_open_ticket).setOnClickListener(this);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_view_open_ticket:
                navController.navigate(R.id.action_openTicketsFragment_to_viewOpenTicketFragment);
                break;
        }
    }
}