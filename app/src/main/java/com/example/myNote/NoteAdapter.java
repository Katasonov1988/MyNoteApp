package com.example.myNote;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> notes;

    public OnNoteClickListener onNoteClickListener;

   public interface OnNoteClickListener {
        void onNoteClick(int position);
    }

    public void setOnNoteClickListener(OnNoteClickListener onNoteClickListener) {
        this.onNoteClickListener = onNoteClickListener;
    }

    public NoteAdapter(List<Note> notes) {
        this.notes = notes;
    }


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.textViewHeader.setText(note.getHeader());
        if (note.getHeader().isEmpty()) {
            holder.textViewHeader.setVisibility(View.GONE);
        }
        holder.textViewDescription.setText(note.getDescription());
        if (note.getDescription().isEmpty()) {
            holder.textViewDescription.setVisibility(View.GONE);
        }
        String date = note.getDate().substring(0, note.getDate().length() - 5);
        holder.textViewTime.setText(date);
        String color = note.getColor();
        holder.cardView.setCardBackgroundColor(Color.parseColor(color));
        holder.textViewId.setText(note.getId());

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewHeader;
        private TextView textViewDescription;
        private TextView textViewTime;
        private TextView textViewId;
        private CardView cardView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHeader = itemView.findViewById(R.id.textViewHeader);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewTime = itemView.findViewById(R.id.textViewDate);
            textViewId = itemView.findViewById(R.id.textViewId);
            cardView = itemView.findViewById(R.id.cardviewNote);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onNoteClickListener != null) {
                        onNoteClickListener.onNoteClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
