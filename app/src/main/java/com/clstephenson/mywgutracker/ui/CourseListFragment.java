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

        //configure floating action button
        //todo uncomment once edit course edit form is implemented
//        FloatingActionButton fab = getActivity().findViewById(R.id.fab_add_course);
//        fab.setOnClickListener(this::openCourseEditActivityForNewCourse);
//        fab.show();

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
        if (getArguments().containsKey(TermActivity.EXTRA_TERM_ID)) {
            termId = getArguments().getLong(TermActivity.EXTRA_TERM_ID);
            viewModel.getCoursesByTermId(termId).observe(getActivity(), adapter::setCourses);
        } else {
            viewModel.getAllCourses().observe(getActivity(), adapter::setCourses);
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(MainActivity.TITLE_RESOURCE_ID)) {
                title = getString(getArguments().getInt(MainActivity.TITLE_RESOURCE_ID));
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
