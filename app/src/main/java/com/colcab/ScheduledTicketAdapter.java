package com.colcab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScheduledTicketAdapter extends FirestoreRecyclerAdapter<Ticket, ScheduledTicketAdapter.TicketHolder> {
    private ScheduledTicketAdapter.onItemClickListener listener;

    public ScheduledTicketAdapter(FirestoreRecyclerOptions<Ticket> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ScheduledTicketAdapter.TicketHolder holder, int position, @NonNull Ticket model) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.ENGLISH);
        Date loggedDate = model.getLoggedDate().toDate();
        holder.tfCustomer.setText(model.getCustomer());
        holder.tfDescription.setText(model.getCaseDescription());
        holder.tfLoggedDate.setText(sdf.format(loggedDate));
        holder.tfScheduledDate.setText(model.getScheduledDate());
    }

    @NonNull
    @Override
    public ScheduledTicketAdapter.TicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.scheduled_card_view, parent, false);
        return new ScheduledTicketAdapter.TicketHolder(v);
    }

    class TicketHolder extends RecyclerView.ViewHolder {

        TextView tfCustomer, tfDescription, tfLoggedDate,tfScheduledDate;

        public TicketHolder(View itemView) {
            super(itemView);
            tfCustomer = itemView.findViewById(R.id.ticket_sch_cust);
            tfDescription = itemView.findViewById(R.id.ticket_sch_desc);
            tfLoggedDate = itemView.findViewById(R.id.ticket_sch_logged);
            tfScheduledDate = itemView.findViewById(R.id.ticket_sch_sched);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }

    }

    public interface onItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(ScheduledTicketAdapter.onItemClickListener listener){
        this.listener = listener;
    }
}
