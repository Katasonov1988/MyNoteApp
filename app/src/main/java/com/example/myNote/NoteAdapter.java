package com.example.myNote;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DataBase.Notes;
import com.example.DataBase.NotesDataBase;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    public static List<Notes> notes;
    public OnNoteClickListener onNoteClickListener;

    public interface OnNoteClickListener {
        void onNoteClick(Notes note);
    }

    public void setOnNoteClickListener(OnNoteClickListener onNoteClickListener) {
        this.onNoteClickListener = onNoteClickListener;
    }
    public NoteAdapter(List<Notes> notes) {
        this.notes = notes;
    }


    public void setNotes(List<Notes> note) {
        notes.clear();
        notes.addAll(note);
        notifyDataSetChanged();
    }

    public void clearDataFromAdapter() {
        notes.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Notes note = notes.get(position);
        holder.textViewHeader.setText(note.getHeader());
        if (note.getHeader().isEmpty()) {
            holder.textViewHeader.setVisibility(View.GONE);
        } else {
            holder.textViewHeader.setVisibility(View.VISIBLE);
        }
        holder.textViewDescription.setText(note.getDescription());
        if (note.getDescription().isEmpty()) {
            holder.textViewDescription.setVisibility(View.GONE);
        } else {
            holder.textViewDescription.setVisibility(View.VISIBLE);
        }
        String date = note.getDate().substring(0, note.getDate().length() - 5);
        holder.textViewTime.setText(date);
        String color = note.getColor();
        Log.i("color", color);
        holder.cardView.setCardBackgroundColor(Color.parseColor(color));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onNoteClickListener != null) {
                    onNoteClickListener.onNoteClick(note);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewHeader;
        private final TextView textViewDescription;
        private final TextView textViewTime;
        private final CardView cardView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHeader = itemView.findViewById(R.id.textViewHeader);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewTime = itemView.findViewById(R.id.textViewDate);
            cardView = itemView.findViewById(R.id.cardViewNote);
        }
    }
}
