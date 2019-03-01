package com.clstephenson.mywgutracker.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.data.models.Course;
import com.clstephenson.mywgutracker.utils.DateUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseViewHolder> {

    private final LayoutInflater inflater;
    private List<Course> courses;
    private OnItemInteractionListener listener;

    public CourseListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_item_course, parent, false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        if (courses != null) {
            Context context = holder.courseDatesView.getContext();
            Course current = courses.get(position);
            holder.courseNameView.setText(current.getName());
            StringBuilder statusText = new StringBuilder()
                    .append(context.getString(R.string.status))
                    .append(":  ")
                    .append(current.getStatus().getFriendlyName());
            holder.courseStatusView.setText(statusText);
            holder.courseDatesView.setText(
                    DateUtils.getFormattedDateRange(current.getStartDate(), current.getEndDate()));
        } else {
            //todo add message stating that there are no courses to display
        }
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (courses != null) {
            return courses.size();
        } else {
            return 0;
        }
    }

    public void setOnItemInteractionListener(OnItemInteractionListener listener) {
        this.listener = listener;
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView courseNameView;
        private final TextView courseStatusView;
        private final TextView courseDatesView;

        private CourseViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            courseNameView = itemView.findViewById(R.id.course_list_text_name);
            courseStatusView = itemView.findViewById(R.id.course_list_text_status);
            courseDatesView = itemView.findViewById(R.id.course_list_text_dates);
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
