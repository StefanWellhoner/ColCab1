package com.colcab.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.colcab.R;
import com.colcab.adapters.ClosedTicketAdapter;
import com.colcab.models.ClosedTicket;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ClosedTicketsFragment extends Fragment {

    private NavController navController;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference ticketsRef = db.collection("closed tickets");
    private ClosedTicketAdapter adapter;

    public ClosedTicketsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_closed_tickets, container, false);
        Query query = ticketsRef.orderBy("customer", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ClosedTicket> options = new FirestoreRecyclerOptions.Builder<ClosedTicket>().setQuery(query, ClosedTicket.class).build();
        adapter = new ClosedTicketAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ClosedTicketAdapter.onItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Bundle args = new Bundle();
                args.putString(ViewOpenTicketFragment.TICKET_ID, documentSnapshot.getId());
                navController.navigate(R.id.action_closedTicketsFragment_to_viewClosedTicketFragment,args);
            }
        });
        return recyclerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
    }



    @Override
    public void onStart() {
        adapter.startListening();
        super.onStart();
    }

    @Override
    public void onStop() {
        adapter.stopListening();
        super.onStop();
    }

}