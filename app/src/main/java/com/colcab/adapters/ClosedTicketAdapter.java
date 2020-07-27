package com.colcab.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.colcab.R;
import com.colcab.models.ClosedTicket;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ClosedTicketAdapter  extends FirestoreRecyclerAdapter<ClosedTicket, ClosedTicketAdapter.TicketHolder> {

    public ClosedTicketAdapter(FirestoreRecyclerOptions<ClosedTicket> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TicketHolder holder, int position, @NonNull ClosedTicket model) {
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
