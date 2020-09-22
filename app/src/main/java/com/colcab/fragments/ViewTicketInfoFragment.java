package com.colcab.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.colcab.R;
import com.colcab.helpers.Validator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ViewTicketInfoFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton fabEdit;
    private MaterialButton btnSave, btnCancel;
    private static TextInputEditText tfCustomer, tfSerialNo, tfCaseModel, tfCaseDesc, tfReqBy, tfCustPo;
    private TextInputLayout lblCustomer, lblSerialNo, lblCaseModel, lblCaseDesc, lblReqBy, lblCustomerPO;
    private FrameLayout loadingBar;
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
        btnCancel = view.findViewById(R.id.btnCancel);

        tfCustomer = view.findViewById(R.id.tfCustomer);
        tfSerialNo = view.findViewById(R.id.tfSerialNo);
        tfCaseModel = view.findViewById(R.id.tfCaseModel);
        tfCaseDesc = view.findViewById(R.id.tfCaseDesc);
        tfReqBy = view.findViewById(R.id.tfRequestedBy);
        tfCustPo = view.findViewById(R.id.tfCustomerPO);
        spnWarranty = view.findViewById(R.id.spnWarranty);

        lblCustomer = view.findViewById(R.id.lCustomer);
        lblSerialNo = view.findViewById(R.id.lSerialNo);
        lblCaseModel = view.findViewById(R.id.lCaseModel);
        lblCaseDesc = view.findViewById(R.id.lCaseDesc);
        lblReqBy = view.findViewById(R.id.lRequestedBy);
        lblCustomerPO = view.findViewById(R.id.lCustomerPO);

        fabEdit.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        loadingBar = getActivity().findViewById(R.id.loadingBar);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.warranty));
        spnWarranty.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewTicketInfoFragment.view = view;
        enableComponents(ViewOpenTicketFragment.condition);
    }

    @SuppressWarnings("unchecked")
    public static void fillFields(Map<String, Object> data) {
        HashMap<String, String> reqBy = (HashMap<String, String>) data.get("requestedBy");
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
        spnWarranty.setText(String.valueOf(data.get("warranty")),false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabEdit:
                enableComponents(true);
                ViewOpenTicketFragment.condition = true;
                break;
            case R.id.btnSave:
                updateFields();
                break;
            case R.id.btnCancel:
                enableComponents(false);
                ViewOpenTicketFragment.condition = false;
                break;
            default:
                break;
        }
    }

    private void enableComponents(boolean condition) {
        if (condition) {
            btnSave.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
            fabEdit.setVisibility(View.GONE);
        } else {
            btnSave.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
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

    private void resetErrors() {
        lblCustomer.setError(null);
        lblSerialNo.setError(null);
        lblCaseModel.setError(null);
        lblCaseDesc.setError(null);
        lblReqBy.setError(null);
        lblCustomerPO.setError(null);
    }

    private boolean isValidData(Map<String, Object> data) {
        int errorCounter = 0;
        resetErrors();
        if (!Validator.isCustomerValid(String.valueOf(data.get("customer")))) {
            errorCounter++;
            lblCustomer.setError("Customer can't be empty");
        }
        if (!Validator.isSerialNumberValid(String.valueOf(data.get("serialNumber")))) {
            errorCounter++;
            lblSerialNo.setError("Serial Number can't be empty");
        }
        if (!Validator.isCaseModelValid(String.valueOf(data.get("caseModel")))) {
            errorCounter++;
            lblCaseModel.setError("Case Model can't be empty");
        }
        if (!Validator.isCaseDescValid(String.valueOf(data.get("caseDescription")))) {
            errorCounter++;
            lblCaseDesc.setError("Case Description can't be empty");
        }
        if (!Validator.isRequestedByValid(String.valueOf(data.get("requestedBy")))) {
            errorCounter++;
            lblReqBy.setError("Requested By can't be empty");
        }
        if (!Validator.isCustomerPOValid(String.valueOf(data.get("customerPO")))) {
            errorCounter++;
            lblCustomerPO.setError("Customer PO can't be empty");
        }
        return errorCounter == 0;
    }

    private Map<String, Object> splitFullName(String fName){
        String lastName = "";
        String firstName = "";
        if (fName.split("\\w+").length > 1) {

            lastName = fName.substring(fName.lastIndexOf(" ") + 1);
            firstName = fName.substring(0, fName.lastIndexOf(' '));
        } else {
            firstName = fName;
        }
        Map<String, Object> fullName = new HashMap<>();
        fullName.put("firstName", firstName);
        fullName.put("lastName", lastName);
        return fullName;
    }

    private void updateFields() {
        loadingBar.setVisibility(View.VISIBLE);
        String fullname = tfReqBy.getText().toString().trim();

        Map<String, Object> nameSplit = splitFullName(fullname);

        Map<String, Object> ticket = new HashMap<>();
        ticket.put("customer", tfCustomer.getText().toString());
        ticket.put("serialNumber", tfSerialNo.getText().toString());
        ticket.put("caseModel", tfCaseModel.getText().toString());
        ticket.put("caseDescription", tfCaseDesc.getText().toString());
        ticket.put("requestedBy", nameSplit);
        ticket.put("customerPO", tfCustPo.getText().toString());
        ticket.put("warranty", spnWarranty.getText().toString());

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference ref = db.collection("tickets").document(ViewOpenTicketFragment.ticketID);

        if (isValidData(ticket)) {
            ref.update(ticket).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getContext(), "Ticket Updated!", Toast.LENGTH_SHORT).show();
                    enableComponents(false);
                    ViewOpenTicketFragment.condition = false;
                    loadingBar.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Failed to update ticket", Toast.LENGTH_SHORT).show();
                    loadingBar.setVisibility(View.GONE);
                }
            });
        }else{
            loadingBar.setVisibility(View.GONE);
        }
    }
}

