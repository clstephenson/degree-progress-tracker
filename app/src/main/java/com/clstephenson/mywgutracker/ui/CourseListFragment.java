package com.clstephenson.mywgutracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.data.models.Course;
import com.clstephenson.mywgutracker.ui.adapters.CourseListAdapter;
import com.clstephenson.mywgutracker.ui.viewmodels.CourseListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CourseListFragment extends Fragment {

    private CourseListViewModel viewModel;
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

        adapter.setOnItemInteractionListener(((view, position) -> {
            Intent intent = new Intent(getActivity(), CourseActivity.class);
            Course selectedCourse = viewModel.getCourse(position);
            intent.putExtra(CourseActivity.EXTRA_COURSE_ID, selectedCourse.getId());
            startActivity(intent);
        }));

        viewModel = ViewModelProviders.of(this).get(CourseListViewModel.class);

        long termId;
        FloatingActionButton fab;
        if (getArguments().containsKey(TermActivity.EXTRA_TERM_ID)) {
            termId = getArguments().getLong(TermActivity.EXTRA_TERM_ID);
            fab = getActivity().findViewById(R.id.fab_add_course);
            viewModel.getCoursesByTermId(termId).observe(getActivity(), adapter::setCourses);
        } else {
            fab = getActivity().findViewById(R.id.fab_add_main);
            viewModel.getCourses().observe(getActivity(), adapter::setCourses);
        }

        //configure floating action button
        fab.setOnClickListener(this::openCourseEditActivityForNewCourse);
        fab.show();

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(MainActivity.TITLE_RESOURCE_ID)) {
                title = getString(getArguments().getInt(MainActivity.TITLE_RESOURCE_ID));
            }
        }
    }

    private void openCourseEditActivityForNewCourse(View view) {
        Intent intent = new Intent(getActivity(), CourseEditActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(title)) {
            getActivity().setTitle(title);
        }
    }

}
