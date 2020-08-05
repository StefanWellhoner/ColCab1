package com.colcab.fragments;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ViewTicketInfoFragment extends Fragment implements View.OnClickListener{

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

        tfCustomer = view.findViewById(R.id.tfCustomer);
        tfSerialNo = view.findViewById(R.id.tfSerialNo);
        tfCaseModel = view.findViewById(R.id.tfCaseModel);
        tfCaseDesc = view.findViewById(R.id.tfCaseDesc);
        tfReqBy = view.findViewById(R.id.tfRequestedBy);
        tfCustPo = view.findViewById(R.id.tfCustomerPO);
        spnWarranty = view.findViewById(R.id.spnWarranty);

        fabEdit.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewTicketInfoFragment.view = view;
        enableComponents(ViewOpenTicketFragment.condition);
    }

    @SuppressWarnings("unchecked")
    public static void fillFields(Map<String,Object> data) {
        HashMap<String,String> reqBy = (HashMap<String, String>) data.get("requestedBy");
        String requestedBy = null;
        if (reqBy != null) {
            requestedBy = reqBy.get("firstName") + " " + reqBy.get("lastName");
        }

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
            ViewOpenTicketFragment.condition = true;
        }
        if (id == btnSave.getId()) {
            enableComponents(false);

            updateFields();

            ViewOpenTicketFragment.condition = false;
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

    private void updateFields() {
        String fullname = tfReqBy.getText().toString();

        String reqName = fullname.substring(0, fullname.indexOf(" "));
        String reqSurname = fullname.substring(fullname.indexOf(" ") + 1);

        HashMap<String, Object> requestedBy = new HashMap<>();
        requestedBy.put("firstName", reqName);
        requestedBy.put("lastName", reqSurname);

        Map<String, Object> ticket = new HashMap<>();
        ticket.put("customer", tfCustomer.getText().toString());
        ticket.put("serialNumber", tfSerialNo.getText().toString());
        ticket.put("caseModel", tfCaseModel.getText().toString());
        ticket.put("caseDescription", tfCaseDesc.getText().toString());
        ticket.put("requestedBy", requestedBy);
        ticket.put("customerPO", tfCustPo.getText().toString());
        ticket.put("warranty", spnWarranty.getText().toString());

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference ref = db.collection("tickets").document(ViewOpenTicketFragment.ticketID);

        ref.update(ticket).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Ticket updated!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed to update ticket", Toast.LENGTH_SHORT).show();
            }
        });

    }
}

