package com.colcab.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.colcab.R;
import com.colcab.models.ClosedTicket;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ViewClosedTicketFragment extends Fragment {
    public static final String TICKET_ID = "ticketID";
    public static String ticketID;

    private static TextView customer, customerPo, reqBy, caseModel, serialNumber, catType, description, rootCause, warranty, loggedDate, closedDate;
    private FrameLayout loadingBar;
    private static View view;

    public ViewClosedTicketFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ticketID = getArguments().getString(TICKET_ID);
        loadingBar = getActivity().findViewById(R.id.loadingBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_closed_ticket, container, false);
        customer = view.findViewById(R.id.tvCustomer);
        customerPo = view.findViewById(R.id.tvCustomerPo);
        reqBy = view.findViewById(R.id.tvReqBy);
        caseModel = view.findViewById(R.id.tvCaseModel);
        serialNumber = view.findViewById(R.id.tvSerialNumber);
        catType = view.findViewById(R.id.tvCategorytype);
        description = view.findViewById(R.id.tvDescription);
        rootCause = view.findViewById(R.id.tvRootCause);
        warranty = view.findViewById(R.id.tvWarranty);
        loggedDate = view.findViewById(R.id.tvLoggedDate);
        closedDate = view.findViewById(R.id.tvClosedDate);
        setFields();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewClosedTicketFragment.view = view;
    }

    public void setFields() {
        loadingBar.setVisibility(View.VISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("closed tickets").document(ticketID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    Map<String, Object> data = document.getData();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.ENGLISH);
                    Timestamp ld = (Timestamp)data.get("loggedDate");
                    Timestamp cd = (Timestamp)data.get("closedDate");

                    HashMap<Object, String> req = (HashMap)data.get("requestedBy");

                    String reqName = req.get("firstName") + " " + req.get("lastName");

                    customer.setText(data.get("customer").toString());
                    customerPo.setText(data.get("customerPO").toString());
                    reqBy.setText(reqName);
                    caseModel.setText(data.get("caseModel").toString());
                    serialNumber.setText(data.get("serialNumber").toString());
                    catType.setText(data.get("categoryType").toString());
                    description.setText(data.get("caseDescription").toString());
                    rootCause.setText(data.get("rootCause").toString());
                    warranty.setText(data.get("warranty").toString());
                    loggedDate.setText(dateFormat.format(ld.toDate()));
                    closedDate.setText(dateFormat.format(cd.toDate()));

                    loadingBar.setVisibility(View.GONE);
                }
            }
        });
    }
}