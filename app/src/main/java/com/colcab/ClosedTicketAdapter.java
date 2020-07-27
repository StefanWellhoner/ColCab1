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

public class ClosedTicketAdapter extends FirestoreRecyclerAdapter<ClosedTickets, ClosedTicketAdapter.TicketHolder> {

    public ClosedTicketAdapter(FirestoreRecyclerOptions<ClosedTickets> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TicketHolder holder, int position, @NonNull ClosedTickets model) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.ENGLISH);
        Date loggedDate = model.getLoggedDate().toDate();
        Date closedDate = model.getClosedDate().toDate();
        holder.tfCustomer.setText(model.getCustomer());
        holder.tfRootCause.setText(model.getRootCause());
        holder.tfLoggedDate.setText(sdf.format(loggedDate));
        holder.tfClosedDate.setText(sdf.format(closedDate));
    }

    @NonNull
    @Override
    public TicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.closed_card_view, parent, false);
        return new TicketHolder(v);
    }

    class TicketHolder extends RecyclerView.ViewHolder {

        TextView tfCustomer, tfRootCause, tfLoggedDate, tfClosedDate;

        public TicketHolder(View itemView) {
            super(itemView);
            tfCustomer = itemView.findViewById(R.id.ticket_closed_cust);
            tfRootCause = itemView.findViewById(R.id.ticket_closed_rootCause);
            tfLoggedDate = itemView.findViewById(R.id.ticket_closed_logged);
            tfClosedDate = itemView.findViewById(R.id.ticket_closed_closed);
        }

    }

}
