package com.colcab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class TicketAdapter extends FirestoreRecyclerAdapter<UnscheduledTickets, TicketAdapter.TicketHolder> {

    public TicketAdapter(FirestoreRecyclerOptions<UnscheduledTickets> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TicketHolder holder, int position, @NonNull UnscheduledTickets model) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.ENGLISH);
        Date loggedDate = model.getLoggedDate().toDate();
        holder.tfCustomer.setText(model.getCustomer());
        holder.tfDescription.setText(model.getCaseDescription());
        holder.tfLoggedDate.setText(sdf.format(loggedDate));
    }

    @NonNull
    @Override
    public TicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_card, parent, false);
        return new TicketHolder(v);
    }

    class TicketHolder extends RecyclerView.ViewHolder {

        TextView tfCustomer, tfDescription, tfLoggedDate;

        public TicketHolder(View itemView) {
            super(itemView);
            tfCustomer = itemView.findViewById(R.id.ticket_unsch_cust);
            tfDescription = itemView.findViewById(R.id.ticket_unsch_desc);
            tfLoggedDate = itemView.findViewById(R.id.ticket_unsch_logged);
        }

    }

}
