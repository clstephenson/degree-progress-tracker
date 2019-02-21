package com.clstephenson.mywgutracker.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.data.CourseStatus;
import com.clstephenson.mywgutracker.data.models.Course;
import com.clstephenson.mywgutracker.utils.DateUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseViewHolder> {

    private final LayoutInflater inflater;
    private List<Course> courses;

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
                    .append(":  ");
            if (DateUtils.isDateBeforeToday(context, current.getEndDate())) {
                statusText.append(CourseStatus.COMPLETED.getFriendlyName());
            } else if (DateUtils.isDateAfterToday(context, current.getStartDate())) {
                statusText.append(CourseStatus.NOT_STARTED.getFriendlyName());
            } else {
                statusText.append(CourseStatus.STARTED.getFriendlyName());
            }
            holder.courseStatusView.setText(statusText);
            holder.courseDatesView.setText(
                    DateUtils.getFormattedDateRange(
                            context, current.getStartDate(), current.getEndDate()));
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

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseNameView;
        private final TextView courseStatusView;
        private final TextView courseDatesView;

        private CourseViewHolder(View itemView) {
            super(itemView);
            courseNameView = itemView.findViewById(R.id.course_list_text_name);
            courseStatusView = itemView.findViewById(R.id.course_list_text_status);
            courseDatesView = itemView.findViewById(R.id.course_list_text_dates);
        }
    }
}