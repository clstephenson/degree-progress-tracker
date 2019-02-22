package com.clstephenson.mywgutracker.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.data.TermStatus;
import com.clstephenson.mywgutracker.data.models.Term;
import com.clstephenson.mywgutracker.utils.DateUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TermListAdapter extends RecyclerView.Adapter<TermListAdapter.TermViewHolder> {

    private final LayoutInflater inflater;
    private List<Term> terms;
    private OnItemInteractionListener listener;

    public TermListAdapter(Context context) {
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
            Context context = holder.termDatesView.getContext();
            Term current = terms.get(position);
            holder.termNameView.setText(current.getName());
            StringBuilder statusText = new StringBuilder()
                    .append(context.getString(R.string.status))
                    .append(":  ");
            if (DateUtils.isDateBeforeToday(context, current.getEndDate())) {
                statusText.append(TermStatus.COMPLETED.getFriendlyName());
            } else if (DateUtils.isDateAfterToday(context, current.getStartDate())) {
                statusText.append(TermStatus.NOT_STARTED.getFriendlyName());
            } else {
                statusText.append(TermStatus.IN_PROGRESS.getFriendlyName());
            }
            holder.termStatusView.setText(statusText);
            holder.termDatesView.setText(
                    DateUtils.getFormattedDateRange(
                            context, current.getStartDate(), current.getEndDate()));
        } else {
            //todo add message stating that there are no terms to display
        }
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
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
