package com.colcab.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.colcab.R;
import com.colcab.helpers.Validator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddContractorFragment extends Fragment implements View.OnClickListener {

    private TextInputLayout lFullName;
    private TextInputLayout lBusinessNumber;
    private TextInputLayout lMobileNumber;
    private TextInputLayout lCompanyName;
    private TextInputLayout lCountryRegion;
    private TextInputEditText tfFullName, tfBusinessNumber, tfMobileNumber, tfCompanyName, tfCountryRegion;

    public AddContractorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_contractor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        lFullName = view.findViewById(R.id.lFullName);
        lBusinessNumber = view.findViewById(R.id.lBusinessNumber);
        lMobileNumber = view.findViewById(R.id.lMobileNumber);
        lCompanyName = view.findViewById(R.id.lCompanyName);
        lCountryRegion = view.findViewById(R.id.lCountryRegion);
        view.findViewById(R.id.btnAddContractor).setOnClickListener(this);
        tfFullName = view.findViewById(R.id.tfFullName);
        tfBusinessNumber = view.findViewById(R.id.tfBusinessNumber);
        tfMobileNumber = view.findViewById(R.id.tfMobileNumber);
        tfCountryRegion = view.findViewById(R.id.tfCountryRegion);
        tfCompanyName = view.findViewById(R.id.tfCompanyName);
    }

    /**
     * Validation method for Contractor information
     *
     * @param fullName       full name of contractor
     * @param businessNumber company Number
     * @param mobileNumber   mobile number
     * @param companyName    company Name
     * @param countryRegion  where the contractor is from
     * @return valid whether all the fields are filled with valid information
     */
    private boolean isContractorValid(String fullName, String businessNumber, String mobileNumber, String companyName, String countryRegion) {
        int errorCounter = 0;
        if (!Validator.isCustomerValid(fullName)) {
            errorCounter++;
            lFullName.setError("Contractor Name can't be empty");
        }
        if (!Validator.isNumberValid(businessNumber)) {
            errorCounter++;
            lBusinessNumber.setError("Business Number must be 10 digits");
        }
        if (!Validator.isNumberValid(mobileNumber)) {
            errorCounter++;
            lMobileNumber.setError("Mobile Number must be 10 digits");
        }
        if (!Validator.isCustomerValid(companyName)) {
            errorCounter++;
            lCompanyName.setError("Company Name can't be empty");
        }
        if (!Validator.isRegionValid(countryRegion)) {
            errorCounter++;
            lCountryRegion.setError("Country/Region can't be empty");
        }
        return errorCounter > 0;
    }

    /**
     * When the Add Contractor button is clicked a new contractor will be saved in firebase
     * void onAddContractor(Button)
     */
    private void addContractor() {
        // Get text from text fields entered by user
        String fullName = String.valueOf(tfFullName.getText());
        String businessNumber = String.valueOf(tfBusinessNumber.getText());
        String mobileNumber = String.valueOf(tfMobileNumber.getText());
        String companyName = String.valueOf(tfCompanyName.getText());
        String countryRegion = String.valueOf(tfCountryRegion.getText());

        if (!isContractorValid(fullName, businessNumber, mobileNumber, companyName, countryRegion)) {

            Map<String, String> fName = splitFullName(fullName);

            // Add entered text to Hashmap for Firebase
            Map<String, Object> contractorData = new HashMap<>();
            contractorData.put("fullName", fName);
            contractorData.put("businessNumber", businessNumber);
            contractorData.put("mobileNumber", mobileNumber);
            contractorData.put("company", companyName);
            contractorData.put("countryRegion", countryRegion);

            // Create database instance
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Insert data from user into "Contractor" collection
            db.collection("contractors").add(contractorData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                // OnSuccess => when ticket has been saved in Firebase
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getContext(), "Contractor was Added", Toast.LENGTH_LONG).show();
                    System.out.println("Document ID: " + documentReference.getId());
                    clearTextFields();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                // OnFailure => Error has occurred (TAG: Firebase Error:)
                public void onFailure(@NonNull Exception e) {
                    Log.d("Firebase Error: ", e.getMessage());
                    clearTextFields();
                }
            });
        } else {
            Toast.makeText(getContext(), "Please fill in all information", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Map<String, String> splitFullName(String) a method that splits a fullName into 2 parts with key value pair 'firstName' and 'lastName'
     *
     * @param fullName full name of a person
     * @return Map<Key, Value>
     */
    private Map<String, String> splitFullName(String fullName) {
        String lastName = "";
        String firstName = "";
        if (fullName.split("\\w+").length > 1) {

            lastName = fullName.substring(fullName.lastIndexOf(" ") + 1);
            firstName = fullName.substring(0, fullName.lastIndexOf(' '));
        } else {
            firstName = fullName;
        }
        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("firstName", firstName);
        nameMap.put("lastName", lastName);
        return nameMap;
    }

    private void clearTextFields(){
        tfFullName.setText(null);
        tfBusinessNumber.setText(null);
        tfMobileNumber.setText(null);
        tfCompanyName.setText(null);
        tfCountryRegion.setText(null);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnAddContractor) {
            addContractor();
        }
    }
}