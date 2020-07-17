package com.colcab;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TicketAdminFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton fabAddContractor;
    private TableRow addContractorPanel;
    private Button btnAddContractor;

    public TicketAdminFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.fragment_ticket_admin, container, false);
        fabAddContractor = frameLayout.findViewById(R.id.fabAddContractor);
        addContractorPanel = frameLayout.findViewById(R.id.addContractorPanel);
        btnAddContractor = frameLayout.findViewById(R.id.btnAddContractor);
        fabAddContractor.setOnClickListener(this);
        btnAddContractor.setOnClickListener(this);
        return frameLayout;
    }


    @Override
    public void onClick(View view) {
        if (view.equals(fabAddContractor)) {
            addContractorPanel.setVisibility(View.VISIBLE);
        }
        if (view.equals(btnAddContractor)) {
            Toast.makeText(getContext(), "Contractor Added", Toast.LENGTH_LONG).show();
            addContractorPanel.setVisibility(View.INVISIBLE);
        }
    }
}