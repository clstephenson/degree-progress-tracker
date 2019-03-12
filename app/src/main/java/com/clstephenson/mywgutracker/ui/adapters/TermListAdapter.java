package com.clstephenson.mywgutracker.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.data.models.Term;
import com.clstephenson.mywgutracker.utils.DateUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class TermListAdapter extends RecyclerView.Adapter<TermListAdapter.TermViewHolder> {

    private final LayoutInflater inflater;
    private List<Term> terms;
    private OnItemInteractionListener listener;
    Context context;

    public TermListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_item_term, parent, false);
        return new TermViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TermViewHolder holder, int position) {
        if (terms != null) {
            Term current = terms.get(position);
            holder.termNameView.setText(current.getName());
            String statusText = String.format(
                    "%s: %s",
                    context.getString(R.string.status),
                    current.getStatus().getFriendlyName());
            holder.termStatusView.setText(statusText);
            holder.termDatesView.setText(
                    DateUtils.getFormattedDateRange(current.getStartDate(), current.getEndDate()));
        }
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
        if (terms.size() == 0) {
            TextView message = ((AppCompatActivity) context).findViewById(R.id.no_main_message);
            message.setText(R.string.no_terms_message);
            message.setVisibility(View.VISIBLE);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (terms != null) {
            return terms.size();
        } else {
            return 0;
        }
    }

    public void setOnItemInteractionListener(OnItemInteractionListener listener) {
        this.listener = listener;
    }

    public class TermViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView termNameView;
        private final TextView termStatusView;
        private final TextView termDatesView;

        private TermViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            termNameView = itemView.findViewById(R.id.term_list_text_name);
            termStatusView = itemView.findViewById(R.id.term_list_text_status);
            termDatesView = itemView.findViewById(R.id.term_list_text_dates);
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
