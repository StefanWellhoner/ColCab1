package com.colcab.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FullScreenDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FullScreenDialogFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayAdapter<String> failures;
    private ArrayAdapter<String> conAdapter;
    private ArrayList<String> remedialCategory;
    private ArrayList<String> failureTypes;
    private ArrayList<String> clientCSATs;
    private String failureType;
    private String categoryType ;
    private String clientCSAT ;

    private TextView lblAmountDue, lblAmountDue1, lblCategoryType, lblFailureType, lblRootCause, lblClientFeedback, lblSaveAs;
    private EditText edtAmountDueCategory, edtAmountDueFailure, edtClientFeedback, edtSaveAs , edtRootCause;
    private Spinner spnCategoryTypes, spnFailureTypes, spnCSATs;
    private Button btnCloseTicket;

    public FullScreenDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FullScreenDialog.
     */
    // TODO: Rename and change types and number of parameters
    public static FullScreenDialog newInstance(String param1, String param2) {
        FullScreenDialog fragment = new FullScreenDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        btnCloseTicket.setOnClickListener((View.OnClickListener) this);
        btnCloseTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(this,MainActivity.class);
                addToFirebase(failureType, categoryType, clientCSAT);


            }

            private void addToFirebase(String failureType, String categoryType, String clientCSAT) {
                //adding values to firebase
                String failuretype = spnFailureTypes.getSelectedItem().toString();
                final String failureamount = edtAmountDueFailure.getText().toString();
                //final int failureamount = Integer.parseInt(edtAmountDueFailure);
                final String categorytype = spnCategoryTypes.getSelectedItem().toString();
                final String categoryamount = edtAmountDueCategory.getText().toString();
                //final int categoryamount = Integer.parseInt(edtAmountDueCategory);
                String rootCause = edtRootCause.getText().toString();
                final String clientfeedback = edtClientFeedback.getText().toString();
                final String clientSaveAs = edtSaveAs.getText().toString();
                final String clientcsat = spnCSATs.getSelectedItem().toString();

                // Create database instance
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                //Vaules of categories of failures
                // Create a reference to the  collection to retrieve
                CollectionReference item = db.collection("Categories of failures");
                // Create a query against the collection.
                Query query = item.whereEqualTo("categoryName", failuretype);
                // retrieve  query results asynchronously using query.get()
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> failures = new HashMap<>();
                            //failures.put("failureType", failuretype);
                            //failures.put("amount", +failureamount); //not int
                            failures.put("totalFaults", +1);
                        } else {
                            System.out.println("Query failed to load!");
                        }
                    }
                });
                //Vaules of Remedial Category
                // Create a reference to the  collection to retrieve
                CollectionReference item1 = db.collection("Remedial Category");
                // Create a query against the collection.
                Query query1 = item1.whereEqualTo("categoryName", categorytype);
                // retrieve  query results asynchronously using query.get()
                query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> categories = new HashMap<>();
                            //categories.put("categoryName", categorytype);
                            //categories.put("amount", +categoryamount);//not int
                            categories.put("total", +1);
                        } else {
                            System.out.println("Query failed to load!");
                        }
                    }
                });
                //Vaules of CSATs
                // Create a reference to the  collection to retrieve
                CollectionReference item2 = db.collection("Customer Satisfaction");
                // Create a query against the collection.
                Query query2 = item2.whereEqualTo("CSAT", clientcsat);
                // retrieve  query results asynchronously using query.get()
                query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> feedback = new HashMap<>();
                            feedback.put("clientFeedback", clientfeedback);
                            feedback.put("saveAs", clientSaveAs);
                            feedback.put("total", +1);
                        } else {
                            System.out.println("Query failed to load!");
                        }
                    }
                });

                //Coontractors
                Map<String, Object> closedTickets = new HashMap<>();
                //closedTickets.put("caseDescription", getCaseDescription);
                //closedTickets.put("closedDate", getCloseDate();
                ///closedTickets.put("customer", getCustomer());
                //closedTickets.put("loggedDate", getLoggedDate() );
                closedTickets.put("rootCause", rootCause);
                closedTickets.put("saveAs", clientSaveAs);

                // Create database instance
                //FirebaseFirestore db = FirebaseFirestore.getInstance();

                // Insert data from user into "Contractor" collection
                db.collection("closed tickets").add(closedTickets).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    // OnSuccess => when ticket has been saved in Firebase
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getContext(), "Closed Ticket was Added", Toast.LENGTH_LONG).show();
                        System.out.println("Document ID: " + documentReference.getId());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    // OnFailure => Error has occurred (TAG: Firebase Error:)
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firebase Error: ", e.getMessage());
                    }
                });
            }
        });
    }
            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
                ScrollView scrollView = (ScrollView) inflater.inflate(R.layout.fragment_full_screen_dialog, container, false);
                initComponents(scrollView);

                failureTypes = new ArrayList<>();
                remedialCategory = new ArrayList<>();
                clientCSATs = new ArrayList<>();

                conAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, failureTypes);
                conAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnCategoryTypes.setAdapter(conAdapter);
                failureTypes.add("Select a Category Type");

                conAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, remedialCategory);
                conAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnCategoryTypes.setAdapter(conAdapter);
                remedialCategory.add("Select a Failure Type");

                conAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, clientCSATs);
                conAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnCSATs.setAdapter(conAdapter);
                clientCSATs.add("Select a Client Satisfaction");


                return scrollView;
            }

            @Override
            public void onStart() {
                listenForChanges();
                super.onStart();
            }

            private void listenForChanges() {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                //add failure types tp spnFailureTypes
                db.collection("categories of failure").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.d("Error", "contractor listening error" + error);
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            QueryDocumentSnapshot category = dc.getDocument();
                            Map<String, String> failurename = (Map<String, String>) category.get("categoryName");
                            String failure  = failurename.get("categoryName");
                            switch (dc.getType()) {
                                case ADDED:
                                    remedialCategory.add(failure);
                                    conAdapter.notifyDataSetChanged();
                                    break;
                                case MODIFIED:
                                    remedialCategory.set(dc.getNewIndex() + 1, failure);
                                    conAdapter.notifyDataSetChanged();
                                    break;
                                case REMOVED:
                                    remedialCategory.remove(dc.getOldIndex() + 1);
                                    conAdapter.notifyDataSetChanged();
                                    break;
                            }
                        }
                    }
                });
                // add category types to spnCategoryTypes
                db.collection("Remedial Categories").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.d("Error", "contractor listening error" + error);
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            QueryDocumentSnapshot category = dc.getDocument();
                            Map<String, String> categoryname = (Map<String, String>) category.get("categoryName");
                            String categoryType = categoryname.get("categoryName");
                            switch (dc.getType()) {
                                case ADDED:
                                    remedialCategory.add(categoryType);
                                    conAdapter.notifyDataSetChanged();
                                    break;
                                case MODIFIED:
                                    remedialCategory.set(dc.getNewIndex() + 1, categoryType);
                                    conAdapter.notifyDataSetChanged();
                                    break;
                                case REMOVED:
                                    remedialCategory.remove(dc.getOldIndex() + 1);
                                    conAdapter.notifyDataSetChanged();
                                    break;
                            }
                        }
                    }
                });
            }

            private void initComponents(View v) {
                // Spinners
                spnFailureTypes = v.findViewById(R.id.spnFailureType);
                spnCategoryTypes = v.findViewById(R.id.spnCategoryType);
                spnCSATs = v.findViewById(R.id.spnCSAT);

                // Layouts for setting errors
                lblAmountDue = v.findViewById(R.id.lblAmountDue);
                lblAmountDue1 = v.findViewById(R.id.lblAmountDue1);
                lblCategoryType = v.findViewById(R.id.lblCategoryType);
                lblFailureType = v.findViewById(R.id.lblFailureType);
                lblRootCause = v.findViewById(R.id.lblRootCause);
                lblClientFeedback = v.findViewById(R.id.lblClientFeedback);
                lblSaveAs = v.findViewById(R.id.lblSaveAs);

                edtAmountDueCategory = v.findViewById(R.id.edtAmountDueCategory);
                edtAmountDueFailure = v.findViewById(R.id.edtAmountDueFailure);
                edtClientFeedback = v.findViewById(R.id.edtClientFeedback);
                edtSaveAs = v.findViewById(R.id.edtSaveAs);
                // Buttons for actions
                btnCloseTicket = v.findViewById(R.id.btnCloseTicket);


            }




    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        failureType = spnFailureTypes.getSelectedItem().toString();
        categoryType = spnCategoryTypes.getSelectedItem().toString();
        clientCSAT = spnCSATs.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}



