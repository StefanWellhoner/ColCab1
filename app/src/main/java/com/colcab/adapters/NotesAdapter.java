package com.colcab.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.colcab.R;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    private List<String> notes;

    public class NotesViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public NotesViewHolder(View v){
            super(v);
            textView = v.findViewById(R.id.ticket_note);
        }
    }

    public NotesAdapter(List<String> notes){
        this.notes = notes;
    }

    @NonNull
    @Override
    public NotesAdapter.NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_card_view, parent, false);
        NotesViewHolder vh = new NotesViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.textView.setText(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}
