package com.clstephenson.mywgutracker.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.data.models.Note;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteViewHolder> {

    private final LayoutInflater inflater;
    private List<Note> notes;
    private OnItemInteractionListener listener;
    Context context;

    public NoteListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_item_note, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        if (notes != null) {
            Note current = notes.get(position);
            holder.noteView.setText(current.getNote());
        }
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        TextView message = ((AppCompatActivity) context).findViewById(R.id.no_notes_message);
        if (notes.size() == 0) {
            message.setText(R.string.no_notes_message);
            message.setVisibility(View.VISIBLE);
        } else {
            message.setVisibility(View.INVISIBLE);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (notes != null) {
            return notes.size();
        } else {
            return 0;
        }
    }

    public void setOnItemInteractionListener(OnItemInteractionListener listener) {
        this.listener = listener;
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView noteView;

        private NoteViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            noteView = itemView.findViewById(R.id.note_list_note);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onClick(v, position);
            }
        }
    }
}
