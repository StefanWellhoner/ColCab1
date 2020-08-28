package com.colcab.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.colcab.R;

public class MainFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private FrameLayout loadingBar;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loadingBar = getActivity().findViewById(R.id.loadingBar);
        loadingBar.setVisibility(View.GONE);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.btn_log_ticket).setOnClickListener(this);
        view.findViewById(R.id.btn_open_tickets).setOnClickListener(this);
        view.findViewById(R.id.btn_closed_tickets).setOnClickListener(this);
        navController = Navigation.findNavController(view);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_log_ticket:
                navController.navigate(R.id.action_mainFragment_to_logTicketFragment);
                break;
            case R.id.btn_open_tickets:
                navController.navigate(R.id.action_mainFragment_to_openTicketsFragment);
                break;
            case R.id.btn_closed_tickets:
                navController.navigate(R.id.action_mainFragment_to_closedTicketsFragment);
                break;
        }
    }
}