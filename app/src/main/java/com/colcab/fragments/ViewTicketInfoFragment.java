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
import android.widget.Toast;

import com.colcab.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;

public class ViewTicketInfoFragment extends Fragment {

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
        return inflater.inflate(R.layout.fragment_view_ticket_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
    }

    public static void fillFields(HashMap data) {
        HashMap hmReqBy = (HashMap) data.get("requestedBy");
        String requestedBy = hmReqBy.get("firstName") + " " + hmReqBy.get("lastName");

        TextInputEditText tfCustomer = view.findViewById(R.id.tfCustomer);
        TextInputEditText tfSerialNo = view.findViewById(R.id.tfSerialNo);
        TextInputEditText tfCaseModel = view.findViewById(R.id.tfCaseModel);
        TextInputEditText tfCaseDesc = view.findViewById(R.id.tfCaseDesc);
        TextInputEditText tfReqBy = view.findViewById(R.id.tfRequestedBy);
        TextInputEditText tfCustPo = view.findViewById(R.id.tfCustomerPO);
        AutoCompleteTextView spnWarranty = view.findViewById(R.id.spnWarranty);

        tfCustomer.setText(data.get("customer").toString());
        tfSerialNo.setText(data.get("serialNumber").toString());
        tfCaseModel.setText(data.get("caseModel").toString());
        tfCaseDesc.setText(data.get("caseDescription").toString());
        tfReqBy.setText(requestedBy);
        tfCustPo.setText(data.get("customerPO").toString());
        spnWarranty.setText(data.get("warranty").toString());
    }
}