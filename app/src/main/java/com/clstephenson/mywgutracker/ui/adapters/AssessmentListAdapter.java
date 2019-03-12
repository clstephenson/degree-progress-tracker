package com.clstephenson.mywgutracker.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.data.models.Assessment;
import com.clstephenson.mywgutracker.utils.DateUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class AssessmentListAdapter extends RecyclerView.Adapter<AssessmentListAdapter.AssessmentViewHolder> {

    private final LayoutInflater inflater;
    private List<Assessment> assessments;
    private OnItemInteractionListener listener;
    private Context context;

    public AssessmentListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_item_assessment, parent, false);
        return new AssessmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentViewHolder holder, int position) {
        if (assessments != null) {
            Assessment current = assessments.get(position);
            holder.assessmentNameView.setText(current.getName());
            holder.assessmentTypeView.setText(current.getType().getFriendlyName());
            holder.assessmentGoalView.setText(
                    String.format("%s: %s",
                            context.getString(R.string.due),
                            DateUtils.getFormattedDate(current.getGoalDate())));
        }
    }

    public void setAssessments(List<Assessment> assessments) {
        this.assessments = assessments;
        if (assessments.size() == 0) {
            TextView message = ((AppCompatActivity) context).findViewById(R.id.no_assessments_message);
            message.setVisibility(View.VISIBLE);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (assessments != null) {
            return assessments.size();
        } else {
            return 0;
        }
    }

    public void setOnItemInteractionListener(OnItemInteractionListener listener) {
        this.listener = listener;
    }

    public class AssessmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView assessmentNameView;
        private final TextView assessmentTypeView;
        private final TextView assessmentGoalView;

        private AssessmentViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            assessmentNameView = itemView.findViewById(R.id.assessment_list_text_name);
            assessmentTypeView = itemView.findViewById(R.id.assessment_list_text_type);
            assessmentGoalView = itemView.findViewById(R.id.assessment_list_text_goal);
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
