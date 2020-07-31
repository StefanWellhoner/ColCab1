package com.colcab.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.colcab.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class ViewTicketInfoFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton fabEdit;
    private MaterialButton btnSave;
    private static TextInputEditText tfCustomer, tfSerialNo, tfCaseModel, tfCaseDesc, tfReqBy, tfCustPo;
    private static AutoCompleteTextView spnWarranty;
    private static View view;

    public ViewTicketInfoFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_ticket_info, container, false);
        fabEdit = view.findViewById(R.id.fabEdit);
        btnSave = view.findViewById(R.id.btnSave);
        fabEdit.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewTicketInfoFragment.view = view;
    }

    @SuppressWarnings("unchecked")
    public static void fillFields(Map<String,Object> data) {
        HashMap<String,String> reqBy = (HashMap<String, String>) data.get("requestedBy");
        String requestedBy = null;
        if (reqBy != null) {
            requestedBy = reqBy.get("firstName") + " " + reqBy.get("lastName");
        }
        tfCustomer = view.findViewById(R.id.tfCustomer);
        tfSerialNo = view.findViewById(R.id.tfSerialNo);
        tfCaseModel = view.findViewById(R.id.tfCaseModel);
        tfCaseDesc = view.findViewById(R.id.tfCaseDesc);
        tfReqBy = view.findViewById(R.id.tfRequestedBy);
        tfCustPo = view.findViewById(R.id.tfCustomerPO);
        spnWarranty = view.findViewById(R.id.spnWarranty);

        tfCustomer.setText(String.valueOf(data.get("customer")));
        tfSerialNo.setText(String.valueOf(data.get("serialNumber")));
        tfCaseModel.setText(String.valueOf(data.get("caseModel")));
        tfCaseDesc.setText(String.valueOf(data.get("caseDescription")));
        tfReqBy.setText(requestedBy);
        tfCustPo.setText(String.valueOf(data.get("customerPO")));
        spnWarranty.setText(String.valueOf(data.get("warranty")));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == fabEdit.getId()) {
            enableComponents(true);
        }
        if (id == btnSave.getId()) {
            enableComponents(false);
        }
    }

    private void enableComponents(boolean condition) {
        if (condition) {
            btnSave.setVisibility(View.VISIBLE);
            fabEdit.setVisibility(View.GONE);
        } else {
            btnSave.setVisibility(View.GONE);
            fabEdit.setVisibility(View.VISIBLE);
        }
        tfCustomer.setEnabled(condition);
        tfSerialNo.setEnabled(condition);
        tfCaseModel.setEnabled(condition);
        tfCaseDesc.setEnabled(condition);
        tfReqBy.setEnabled(condition);
        tfCustPo.setEnabled(condition);
        spnWarranty.setEnabled(condition);
    }
}