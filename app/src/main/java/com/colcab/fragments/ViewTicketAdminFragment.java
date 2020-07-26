package com.colcab.fragments;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.colcab.R;
import com.colcab.helpers.Validator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ViewTicketAdminFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton fabAddContractor;
    private LinearLayout addContractorPanel;
    private Button btnAddContractor, btnCancel;
    private Spinner spnContractors;

    private TextInputLayout lFullName, lBusinessNumber, lMobileNumber, lCompanyName, lCountryRegion, lDatePicker;
    private TextInputEditText tfFullName, tfBusinessNumber, tfMobileNumber, tfCompanyName, tfCountryRegion, tfDatePicker;

    private ArrayList<String> contractors;
    private ArrayAdapter<String> conAdapter;

    public ViewTicketAdminFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ScrollView scrollView = (ScrollView) inflater.inflate(R.layout.fragment_view_ticket_admin, container, false);
        initComponents(scrollView);
        initOnKeyListeners();
        fabAddContractor.setOnClickListener(this);
        btnAddContractor.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar);
            }
        };

        tfDatePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        contractors = new ArrayList<>();
        conAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, contractors);
        conAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnContractors.setAdapter(conAdapter);
        contractors.add("Select a Contractor");
//        updateSpinner();
        return scrollView;
    }

    @Override
    public void onStart() {
        listenContractorChanges();
        super.onStart();
    }

    /**
     * Initializes the components to grab data from them
     * void initComponents()
     */
    private void initComponents(View v) {
        // Text fields containing user's entered data
        tfFullName = v.findViewById(R.id.tfFullName);
        tfBusinessNumber = v.findViewById(R.id.tfBusinessNumber);
        tfMobileNumber = v.findViewById(R.id.tfMobileNumber);
        tfCompanyName = v.findViewById(R.id.tfCompanyName);
        tfCountryRegion = v.findViewById(R.id.tfCountryRegion);
        tfDatePicker = v.findViewById(R.id.tfDatePicker);

        // Layouts for setting errors
        lFullName = v.findViewById(R.id.lFullName);
        lBusinessNumber = v.findViewById(R.id.lBusinessNumber);
        lMobileNumber = v.findViewById(R.id.lMobileNumber);
        lCompanyName = v.findViewById(R.id.lCompanyName);
        lCountryRegion = v.findViewById(R.id.lCountryRegion);
        lDatePicker = v.findViewById(R.id.lDatePicker);

        // Buttons for actions
        fabAddContractor = v.findViewById(R.id.fabAddContractor);
        addContractorPanel = v.findViewById(R.id.addContractorPanel);
        btnAddContractor = v.findViewById(R.id.btnAddContractor);
        btnCancel = v.findViewById(R.id.btnCancel);

        //Spinner for contractors
        spnContractors = v.findViewById(R.id.spinner2);
    }

    /**
     * Initializes the on key listeners to remove error messages from the text fields
     * void initOnKeyListeners()
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
        tfBusinessNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (Validator.isNumberValid(tfBusinessNumber.getText().toString())) {
                    lBusinessNumber.setError(null);
                }
                return false;
            }
        });
        tfMobileNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (Validator.isNumberValid(tfMobileNumber.getText().toString())) {
                    lMobileNumber.setError(null);
                }
                return false;
            }
        });
        tfCompanyName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (Validator.isCustomerValid(tfCompanyName.getText().toString())) {
                    lCompanyName.setError(null);
                }
                return false;
            }
        });
        tfCountryRegion.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (Validator.isRegionValid(tfCountryRegion.getText().toString())) {
                    lCountryRegion.setError(null);
                }
                return false;
            }
        });
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
    private boolean isTicketInfoValid(String fullName, String businessNumber, String mobileNumber, String companyName, String countryRegion) {
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
     * Map<String, String> splitFullname(String) a method that splits a fullname into 2 parts with key value pair 'firstName' and 'lastName'
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

    /**
     * When the Add Contractor button is clicked a new contractor will be saved in firebase
     * void onAddContractor(Button)
     */
    public void addContractor() {
        // Get text from text fields entered by user
        String fullName = tfFullName.getText().toString();
        String businessNumber = tfBusinessNumber.getText().toString();
        String mobileNumber = tfMobileNumber.getText().toString();
        String companyName = tfCompanyName.getText().toString();
        String countryRegion = tfCountryRegion.getText().toString();

        if (!isTicketInfoValid(fullName, businessNumber, mobileNumber, companyName, countryRegion)) {

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
                    addContractorPanel.setVisibility(View.GONE);
                    fabAddContractor.setVisibility(View.VISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                // OnFailure => Error has occurred (TAG: Firebase Error:)
                public void onFailure(@NonNull Exception e) {
                    Log.d("Firebase Error: ", e.getMessage());
                }
            });
        } else {
            Toast.makeText(getContext(), "Please fill in all information", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Sets date picker text field to date chosen in date picker
     */
    private void updateLabel(Calendar cal) {
        String myFormat = "dd MMMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        tfDatePicker.setText(sdf.format(cal.getTime()));
    }

    /**
     * Method that listens for changes made to contractors collection
     */
    private void listenContractorChanges() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("contractors").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d("Error", "contractor listening error" + error);
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    QueryDocumentSnapshot contractor = dc.getDocument();
                    Map<String, String> fullName = (Map<String, String>) contractor.get("fullName");
                    String firstName = fullName.get("firstName");
                    String lastName = fullName.get("lastName");
                    String company = contractor.getString("company");
                    switch (dc.getType()) {
                        case ADDED:
                            contractors.add(firstName + " " + lastName + " - " + company);
                            conAdapter.notifyDataSetChanged();
                            break;
                        case MODIFIED:
                            contractors.set(dc.getNewIndex() + 1, firstName + " " + lastName + " - " + company);
                            conAdapter.notifyDataSetChanged();
                            break;
                        case REMOVED:
                            contractors.remove(dc.getOldIndex() + 1);
                            conAdapter.notifyDataSetChanged();
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabAddContractor:
                addContractorPanel.setVisibility(View.VISIBLE);
                fabAddContractor.setVisibility(View.INVISIBLE);
                break;
            case R.id.btnAddContractor:
                addContractor();
                break;
            case R.id.btnCancel:
                addContractorPanel.setVisibility(View.GONE);
                fabAddContractor.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}