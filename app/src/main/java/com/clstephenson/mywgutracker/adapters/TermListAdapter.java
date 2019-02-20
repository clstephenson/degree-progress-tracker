package com.clstephenson.mywgutracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clstephenson.mywgutracker.DateUtils;
import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.data.TermStatus;
import com.clstephenson.mywgutracker.data.models.Term;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TermListAdapter extends RecyclerView.Adapter<TermListAdapter.TermViewHolder> {

    private final LayoutInflater inflater;
    private List<Term> terms;

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
            String statusText;
            if (DateUtils.isDateBeforeToday(context, current.getEndDate())) {
                statusText = TermStatus.COMPLETED.getFriendlyName();
            } else if (DateUtils.isDateAfterToday(context, current.getStartDate())) {
                statusText = TermStatus.NOT_STARTED.getFriendlyName();
            } else {
                statusText = TermStatus.IN_PROGRESS.getFriendlyName();
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

    public class TermViewHolder extends RecyclerView.ViewHolder {
        private final TextView termNameView;
        private final TextView termStatusView;
        private final TextView termDatesView;

        private TermViewHolder(View itemView) {
            super(itemView);
            termNameView = itemView.findViewById(R.id.term_list_text_name);
            termStatusView = itemView.findViewById(R.id.term_list_text_status);
            termDatesView = itemView.findViewById(R.id.term_list_text_dates);
        }
    }
}