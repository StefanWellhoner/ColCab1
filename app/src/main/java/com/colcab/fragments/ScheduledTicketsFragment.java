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
import android.widget.FrameLayout;

import com.colcab.R;
import com.colcab.models.Ticket;
import com.colcab.adapters.ScheduledTicketAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ScheduledTicketsFragment extends Fragment {

    private NavController navController;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference ticketsRef = db.collection("tickets");
    private ScheduledTicketAdapter adapter;

    public ScheduledTicketsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_scheduled_tickets, container, false);
        Query query = ticketsRef.orderBy("loggedDate", Query.Direction.DESCENDING).whereEqualTo("scheduled", true);
        FirestoreRecyclerOptions<Ticket> options = new FirestoreRecyclerOptions.Builder<Ticket>().setQuery(query, Ticket.class).build();
        adapter = new ScheduledTicketAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ScheduledTicketAdapter.onItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Bundle args = new Bundle();
                args.putString(ViewOpenTicketFragment.TICKET_ID, documentSnapshot.getId());
                navController.navigate(R.id.action_openTicketsFragment_to_viewOpenTicketFragment,args);
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
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}