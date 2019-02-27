package com.clstephenson.mywgutracker.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.ui.adapters.CourseListAdapter;
import com.clstephenson.mywgutracker.ui.viewmodels.CourseListViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CourseListFragment extends Fragment {

    private CourseListViewModel courseListViewModel;
    private String title;

    public CourseListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView recyclerView = getView().findViewById(R.id.course_recyclerview);
        final CourseListAdapter adapter = new CourseListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        courseListViewModel = ViewModelProviders.of(this).get(CourseListViewModel.class);
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(MainActivity.TITLE_RESOURCE_ID)) {
                title = getString(getArguments().getInt(MainActivity.TITLE_RESOURCE_ID));
            }
            if (bundle.containsKey(TermActivity.TERM_EXTRA_NAME)) {
                // list of courses for a specified term
                long termId = getArguments().getLong(TermActivity.TERM_EXTRA_NAME);
                courseListViewModel.getCoursesByTermId(termId).observe(getActivity(), adapter::setCourses);
            } else {
                // list of all courses
                courseListViewModel.getAllCourses().observe(getActivity(), adapter::setCourses);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(title)) {
            getActivity().setTitle(title);
        }
    }

}
