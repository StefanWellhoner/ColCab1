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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ViewTicketAdminFragment extends Fragment implements View.OnClickListener {

    private Spinner spnContractors;

    private TextInputEditText tfDatePicker;

    private ArrayList<String> contractors;
    private ArrayAdapter<String> conAdapter;

    public ViewTicketAdminFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ScrollView scrollView = (ScrollView) inflater.inflate(R.layout.fragment_view_ticket_admin, container, false);
        initComponents(scrollView);

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
        conAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, contractors);
        conAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnContractors.setAdapter(conAdapter);
        contractors.add("Select a Contractor");
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
        tfDatePicker = v.findViewById(R.id.tfDatePicker);

        TextInputLayout lDatePicker = v.findViewById(R.id.lDatePicker);

        //Spinner for contractors
        spnContractors = v.findViewById(R.id.spinner2);
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
                    Map<String, Object> fullName = (Map<String, Object>)contractor.get("fullName");
                    String firstName = fullName.get("firstName").toString();
                    String lastName = fullName.get("lastName").toString();
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
    }
}