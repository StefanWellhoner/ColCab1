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
import android.widget.AdapterView;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ViewTicketAdminFragment extends Fragment implements View.OnClickListener {

    private Button btnScheduleTicket;
    private TextInputLayout lDatePicker;
    private Spinner spnContractors;
    private TextInputEditText tfDatePicker;
    private ArrayList<String> contractors;
    private ArrayList<String> contractorIDs;
    private ArrayAdapter<String> conAdapter;
    private FirebaseFirestore db;
    private String selectedDate;
    private DocumentReference selectedContractor;

    public ViewTicketAdminFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ScrollView scrollView = (ScrollView) inflater.inflate(R.layout.fragment_view_ticket_admin, container, false);
        initComponents(scrollView);
        btnScheduleTicket.setOnClickListener(this);
        db = FirebaseFirestore.getInstance();
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar);
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
                selectedDate = sdf.format(myCalendar.getTime());
                enableSchedule();
            }
        };

        tfDatePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        contractors = new ArrayList<>();
        contractorIDs = new ArrayList<>();
        conAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, contractors);
        conAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnContractors.setAdapter(conAdapter);
        spnContractors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedContractor = db.collection("contractors").document(contractorIDs.get(adapterView.getSelectedItemPosition()));
                enableSchedule();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return scrollView;
    }

    @Override
    public void onStart() {
        listenContractorChanges();
        super.onStart();
    }

    @Override
    public void onStop() {
        conAdapter.clear();
        super.onStop();
    }

    /**
     * Initializes the components to grab data from them
     * void initComponents()
     */
    private void initComponents(View v) {
        // Text fields containing user's entered data
        tfDatePicker = v.findViewById(R.id.tfDatePicker);
        btnScheduleTicket = v.findViewById(R.id.btnScheduleTicket);
        lDatePicker = v.findViewById(R.id.lDatePicker);
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
     * Schedules Ticket
     */
    private void scheduleTicket() {
        HashMap<String, Object> scheduleData = new HashMap<>();
        scheduleData.put("scheduled", true);
        scheduleData.put("scheduledDate", selectedDate);
        scheduleData.put("contractor", selectedContractor);
        db.collection("tickets").document(ViewOpenTicketFragment.ticketID).update(scheduleData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enableSchedule() {
        String date = String.valueOf(tfDatePicker.getText());
        String contractor = String.valueOf(spnContractors.getSelectedItem());
        if (isValidSchedule(date, contractor) && spnContractors.getSelectedItemPosition() != 0) {
            btnScheduleTicket.setEnabled(true);
        }
    }

    private boolean isValidSchedule(String date, String contractor) {
        return !(date.isEmpty() || contractor.isEmpty());
    }

    /**
     * Method that listens for changes made to contractors collection
     */
    @SuppressWarnings("unchecked")
    private void listenContractorChanges() {
        contractors.add("Select a Contractor");
        contractorIDs.add("Select a Contractor");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("contractors").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d("Error", "contractor listening error" + error);
                    Toast.makeText(getContext(), "An Error Occurred", Toast.LENGTH_SHORT).show();
                }
                try {
                    if (value != null) {
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            QueryDocumentSnapshot contractor = dc.getDocument();
                            String id = contractor.getId();
                            Map<String, Object> fullName = (Map<String, Object>) contractor.get("fullName");
                            String firstName = String.valueOf(fullName.get("firstName"));
                            String lastName = String.valueOf(fullName.get("lastName"));
                            String company = contractor.getString("company");
                            switch (dc.getType()) {
                                case ADDED:
                                    contractorIDs.add(id);
                                    contractors.add(firstName + " " + lastName + " - " + company);
                                    conAdapter.notifyDataSetChanged();
                                    break;
                                case MODIFIED:
                                    contractorIDs.set(dc.getNewIndex()+1,id);
                                    contractors.set(dc.getNewIndex() + 1, firstName + " " + lastName + " - " + company);
                                    conAdapter.notifyDataSetChanged();
                                    break;
                                case REMOVED:
                                    contractorIDs.remove(dc.getOldIndex()+1);
                                    contractors.remove(dc.getOldIndex() + 1);
                                    conAdapter.notifyDataSetChanged();
                                    break;
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.d("Firebase Exception", "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnScheduleTicket) {
            scheduleTicket();
        }
    }

}