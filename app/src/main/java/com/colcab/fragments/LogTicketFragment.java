package com.colcab.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.colcab.R;
import com.colcab.helpers.Validator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LogTicketFragment extends Fragment implements View.OnClickListener {

    private TextInputLayout lCustomer, lSerialNumber, lCaseModel, lCaseDesc, lReqBy, lCustomerPO;
    private TextInputEditText tfCustomer, tfSerialNum, tfCaseModel, tfCaseDesc, tfRequestedBy, tfCustomerPO;
    private AutoCompleteTextView spnWarranty;
    private FrameLayout loadingBar;

    public LogTicketFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log_ticket, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initComponents(view);
        initOnKeyListeners();
        view.findViewById(R.id.btn_log_ticket).setOnClickListener(this);

        loadingBar = getActivity().findViewById(R.id.loadingBar);
    }

    /**
     * void initComponents()
     * Initializes the components to grab data from them
     */
    public void initComponents(View v) {
        // text fields containing user's entered data
        tfCustomer = v.findViewById(R.id.tfCustomer);
        tfSerialNum = v.findViewById(R.id.tfSerialNum);
        tfCaseModel = v.findViewById(R.id.tfCaseModel);
        tfCaseDesc = v.findViewById(R.id.tfCaseDesc);
        tfRequestedBy = v.findViewById(R.id.tfRequestedBy);
        tfCustomerPO = v.findViewById(R.id.tfCustomerPO);
        spnWarranty = v.findViewById(R.id.spnWarranty);

        // Layouts for setting errors
        lCustomer = v.findViewById(R.id.lCustomer);
        lSerialNumber = v.findViewById(R.id.lSerialNumber);
        lCaseModel = v.findViewById(R.id.lCaseModel);
        lCaseDesc = v.findViewById(R.id.lCaseDesc);
        lReqBy = v.findViewById(R.id.lReqBy);
        lCustomerPO = v.findViewById(R.id.lCustomerPO);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.warranty));
        spnWarranty.setAdapter(adapter);
    }

    /**
     * void initOnKeyListeners()
     * Initializes the on key listeners to remove error messages from the text fields
     */
    private void initOnKeyListeners() {
        tfCustomer.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (Validator.isCustomerValid(tfCustomer.getText().toString())) {
                    lCustomer.setError(null);
                }
                return false;
            }
        });
        tfSerialNum.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (Validator.isSerialNumberValid(tfSerialNum.getText().toString())) {
                    lSerialNumber.setError(null);
                }
                return false;
            }
        });
        tfCaseModel.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (Validator.isCaseModelValid(tfCaseModel.getText().toString())) {
                    lCaseModel.setError(null);
                }
                return false;
            }
        });
        tfCaseDesc.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (Validator.isCaseDescValid(tfCaseDesc.getText().toString())) {
                    lCaseDesc.setError(null);
                }
                return false;
            }
        });
        tfRequestedBy.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (Validator.isRequestedByValid(tfRequestedBy.getText().toString())) {
                    lReqBy.setError(null);
                }
                return false;
            }
        });
        tfCustomerPO.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (Validator.isCustomerPOValid(tfCustomerPO.getText().toString())) {
                    lCustomerPO.setError(null);
                }
                return false;
            }
        });
    }

    /**
     * void getCurrentDate()
     * A method that get the current date and time from the system to be logged for each ticket
     *
     * @return return the current date in format MM/dd/yyyy HH:mm:ss
     */
    private Timestamp getCurrentDate() {
        return Timestamp.now();
    }

    /**
     * @param customer   customer name
     * @param serialNum  serial number
     * @param caseModel  case model type
     * @param caseDesc   case description
     * @param reqBy      who requested the ticket
     * @param customerPO customer purchase order
     * @return valid whether all the fields are filled with valid information
     */
    private boolean isTicketInfoValid(String customer, String serialNum, String caseModel, String caseDesc, String reqBy, String customerPO) {
        int errorCounter = 0;
        if (!Validator.isCustomerValid(customer)) {
            errorCounter++;
            lCustomer.setError("Customer can't be empty");
        }
        if (!Validator.isSerialNumberValid(serialNum)) {
            errorCounter++;
            lSerialNumber.setError("Serial Number can't be empty");
        }
        if (!Validator.isCaseModelValid(caseModel)) {
            errorCounter++;
            lCaseModel.setError("Case Model can't be empty");
        }
        if (!Validator.isCaseDescValid(caseDesc)) {
            errorCounter++;
            lCaseDesc.setError("Case Description can't be empty");
        }
        if (!Validator.isRequestedByValid(reqBy)) {
            errorCounter++;
            lReqBy.setError("Requested By can't be empty");
        }
        if (!Validator.isCustomerPOValid(customerPO)) {
            errorCounter++;
            lCustomerPO.setError("Customer PO can't be empty");
        }
        return errorCounter > 0;
    }

    /**
     * void onLogTicket(Button)
     * When the log ticket button is clicked a ticket will be logged and saved in firebase
     */
    public void onLogTicket() {
        loadingBar.setVisibility(View.VISIBLE);
        // Get text from text fields entered by user
        String customer = tfCustomer.getText().toString();
        String warranty = spnWarranty.getText().toString();
        String serialNum = tfSerialNum.getText().toString();
        String caseModel = tfCaseModel.getText().toString();
        String caseDesc = tfCaseDesc.getText().toString();
        String reqBy = tfRequestedBy.getText().toString();
        String customerPO = tfCustomerPO.getText().toString();

        if (!isTicketInfoValid(customer, serialNum, caseModel, caseDesc, reqBy, customerPO)) {
            Map<String, Object> fullName = splitFullName(reqBy);

            // Add entered text to HashMap for Firebase
            Map<String, Object> ticketData = new HashMap<>();
            ticketData.put("loggedDate", getCurrentDate());
            ticketData.put("customer", customer);
            ticketData.put("warranty", warranty);
            ticketData.put("serialNumber", serialNum);
            ticketData.put("caseModel", caseModel);
            ticketData.put("caseDescription", caseDesc);
            ticketData.put("requestedBy", fullName);
            ticketData.put("customerPO", customerPO);
            ticketData.put("scheduled", false);

            // Create database instance
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            // Insert data from user into "Tickets" collection
            db.collection("tickets").add(ticketData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                // OnSuccess => when ticket has been saved in Firebase
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getContext(), "Ticket was Logged", Toast.LENGTH_LONG).show();
                    System.out.println("Document ID: " + documentReference.getId());
                    clearTextFields();
                    loadingBar.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                // OnFailure => Error has occurred (TAG: Firebase Error:)
                public void onFailure(@NonNull Exception e) {
                    Log.d("Firebase Error: ", e.getMessage());
                    clearTextFields();
                    loadingBar.setVisibility(View.GONE);
                }
            });
        } else {
            clearTextFields();
            loadingBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Please fill in all information", Toast.LENGTH_LONG).show();
        }
    }

    private void clearTextFields(){
        tfCustomer.setText("");
        tfSerialNum.setText("");
        tfCaseModel.setText("");
        tfCaseDesc.setText("");
        tfRequestedBy.setText("");
        tfCustomerPO.setText("");
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_log_ticket) {
            onLogTicket();
        }
    }
}