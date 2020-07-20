package com.colcab;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableRow;
import android.widget.Toast;

//Added Imports

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Map;
import java.util.HashMap;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TicketAdminFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton fabAddContractor;
    private TableRow addContractorPanel;
    private Button btnAddContractor;

    private TextInputLayout lFullName, lCompanyNumber, lMobileNumber, lEmailAddress, lCountryRegion;
    private TextInputEditText tfFullName, tfCompanyNumber, tfMobileNumber, tfEmailAddress, tfCountryRegion;



    public TicketAdminFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        setContentView(R.layout.fragment_ticket_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        initComponents();
        initOnKeyListeners();
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

    /**
     * void initComponents()
     * Initializes the components to grab data from them
     */
    private void initComponents() {
        // text fields containing user's entered data
        tfFullName = getView().findViewById(R.id.tfFullName);
        tfCompanyNumber = getView().findViewById(R.id.tfCompanyNumber);
        tfMobileNumber = getView().findViewById(R.id.tfMobileNumber);
        tfEmailAddress = getView().findViewById(R.id.tfEmailAddress);
        tfCountryRegion = getView().findViewById(R.id.tfCountryRegion);

        // Layouts for setting errors

        lFullName = getView().findViewById(R.id.lFullName);
        lCompanyNumber = getView().findViewById(R.id.lCompanyNumber);
        lMobileNumber = getView().findViewById(R.id.lMobileNumber);
        lEmailAddress = getView().findViewById(R.id.lEmailAddress);
        lCountryRegion = getView().findViewById(R.id.lCountryRegion);


    }


    /**
     * void initOnKeyListeners()
     * Initializes the on key listeners to remove error messages from the text fields
     */
    private void initOnKeyListeners() {
        tfFullName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (Validator.isCustomerValid(tfFullName.getText().toString())) {
                    lFullName.setError(null);
                }
                return false;
            }
        });
        tfCompanyNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (Validator.isSerialNumberValid(tfCompanyNumber.getText().toString())) {
                    lCompanyNumber.setError(null);
                }
                return false;
            }
        });
        tfMobileNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (Validator.isCaseModelValid(tfMobileNumber.getText().toString())) {
                    lMobileNumber.setError(null);
                }
                return false;
            }
        });
        tfEmailAddress.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (Validator.isCaseDescValid(tfEmailAddress.getText().toString())) {
                    lEmailAddress.setError(null);
                }
                return false;
            }
        });
        tfCountryRegion.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (Validator.isRequestedByValid(tfCountryRegion.getText().toString())) {
                    lCountryRegion.setError(null);
                }
                return false;
            }
        });
    }

    private Timestamp getCurrentDate() {
        return Timestamp.now();
    }

    /**
     * @param fullName   full name of contractor
     * @param companyNumber  company Number
     * @param mobileNumber  mobile number
     * @param email  email
     * @param countryRegion  where the contractor is from
     * @return valid whether all the fields are filled with valid information
     */
    private boolean isTicketInfoValid(String fullName, String companyNumber, String mobileNumber, String email, String countryRegion) {
        int errorCounter = 0;
        if (!Validator.isCustomerValid(fullName)) {
            errorCounter++;
            lFullName.setError("Name of Contractor can't be empty");
        }
        if (!Validator.isSerialNumberValid(companyNumber)) {
            errorCounter++;
            lCompanyNumber.setError("Company Number can't be empty");
        }
        if (!Validator.isCaseModelValid(mobileNumber)) {
            errorCounter++;
            lMobileNumber.setError("Mobile Number can't be empty");
        }
        if (!Validator.isCaseDescValid(email)) {
            errorCounter++;
            lEmailAddress.setError("Email can't be empty");
        }
        if (!Validator.isRequestedByValid(countryRegion)) {
            errorCounter++;
            lCountryRegion.setError("Country/ Region can't be empty");
        }

        return errorCounter > 0;
    }

    /**
     * void onAddContractor(Button)
     * When the Add Contractor button is clicked a new contractor will be saved in firebase
     */
    public void onAddContractor(View v) {
        // Get text from text fields entered by user
        String fullName = tfFullName.getText().toString();
        String companyNumber = tfCompanyNumber.getText().toString();
        String mobileNumber = tfMobileNumber.getText().toString();
        String emailAddress = tfEmailAddress.getText().toString();
        String countryRegion = tfCountryRegion.getText().toString();

        if (!isTicketInfoValid(fullName, companyNumber, mobileNumber, emailAddress, countryRegion)) {

            String lastName = "";
            String firstName = "";
            if (reqBy.split("\\w+").length > 1) {

                lastName = reqBy.substring(reqBy.lastIndexOf(" ") + 1);
                firstName = reqBy.substring(0, reqBy.lastIndexOf(' '));
            } else {
                firstName = reqBy;
            }
            Map<String, Object> fullName = new HashMap<>();
            fullName.put("firstName", firstName);
            fullName.put("lastName", lastName);

            // Add entered text to Hashmap for Firebase
            Map<String, Object> contractorData = new HashMap<>();
            contractorData.put("enteredDate", getCurrentDate());
            contractorData.put("fullName", fullName);
            contractorData.put("companyNumber", companyNumber);
            contractorData.put("mobileNumber", mobileNumber);
            contractorData.put("email", emailAddress);
            contractorData.put("countryRegion", countryRegion);



            // Create database instance
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Insert data from user into "Contractor" collection
            db.collection("tickets").add(ticketData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                // OnSuccess => when ticket has been saved in Firebase
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(TicketAdminFragment.this, "Contractor was Added", Toast.LENGTH_LONG).show();
                    System.out.println("Document ID: " + documentReference.getId());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                // OnFailure => Error has occurred (TAG: Firebase Error:)
                public void onFailure(@NonNull Exception e) {
                    Log.d("Firebase Error: ", e.getMessage());
                }
            });
        } else {
            Toast.makeText(TicketAdminFragment.this, "Please fill in all information", Toast.LENGTH_LONG).show();
        }
    }







}