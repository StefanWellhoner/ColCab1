package com.colcab;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ClosedTicketsActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference ticketsRef = db.collection("closed tickets");
    private ClosedTicketAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closed_tickets);

        RecyclerView closedTicketRv = (RecyclerView) findViewById(R.id.recyclerViewClosed);
        Query query = ticketsRef.orderBy("customer", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ClosedTickets> options = new FirestoreRecyclerOptions.Builder<ClosedTickets>().setQuery(query, ClosedTickets.class).build();
        adapter = new ClosedTicketAdapter(options);
        closedTicketRv.setHasFixedSize(true);
        closedTicketRv.setLayoutManager(new LinearLayoutManager(this));
        closedTicketRv.setAdapter(adapter);
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