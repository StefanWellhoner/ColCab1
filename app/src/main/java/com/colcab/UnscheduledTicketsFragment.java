package com.colcab;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class UnscheduledTicketsFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference ticketsRef = db.collection("tickets");
    private TicketAdapter adapter;

    public UnscheduledTicketsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView unscheduledTicketRV = (RecyclerView) inflater.inflate(R.layout.fragment_unscheduled_tickets, container, false);
        Query query = ticketsRef.orderBy("customer", Query.Direction.DESCENDING).whereEqualTo("scheduled", false);
        FirestoreRecyclerOptions<UnscheduledTickets> options = new FirestoreRecyclerOptions.Builder<UnscheduledTickets>().setQuery(query, UnscheduledTickets.class).build();
        adapter = new TicketAdapter(options);
        unscheduledTicketRV.setHasFixedSize(true);
        unscheduledTicketRV.setLayoutManager(new LinearLayoutManager(getContext()));
        unscheduledTicketRV.setAdapter(adapter);
        return unscheduledTicketRV;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}