package com.colcab.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.colcab.R;
import com.colcab.adapters.NotesAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewTicketNotesFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private NotesAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DocumentReference currentTicketRef;
    private FirebaseFirestore db;
    private TextInputEditText tfNotes;
    private List<String> notes;

    public ViewTicketNotesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_ticket_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.notesRecycler);
        tfNotes = view.findViewById(R.id.tfAddNote);
        view.findViewById(R.id.btnNotes).setOnClickListener(this);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        db = FirebaseFirestore.getInstance();
        currentTicketRef = db.collection("tickets").document(ViewOpenTicketFragment.ticketID);
    }

    public void listenForNotes() {
        currentTicketRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d("Firebase Listener Error", error.getMessage());
                }
                notes = (value.get("notes") != null) ? (List<String>) value.get("notes") : new ArrayList<String>();
                mAdapter = new NotesAdapter(notes);
                recyclerView.setAdapter(mAdapter);
                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        currentTicketRef.update("notes", FieldValue.arrayRemove(notes.get(viewHolder.getAdapterPosition())));
                    }
                }).attachToRecyclerView(recyclerView);
            }
        });
    }

    @Override
    public void onStart() {
        listenForNotes();
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnNotes) {
            currentTicketRef.update("notes", FieldValue.arrayUnion(tfNotes.getText().toString()));
            tfNotes.setText(null);
        }
    }
}