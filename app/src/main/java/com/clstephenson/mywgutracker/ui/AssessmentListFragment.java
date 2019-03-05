package com.clstephenson.mywgutracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.data.models.Assessment;
import com.clstephenson.mywgutracker.ui.adapters.AssessmentListAdapter;
import com.clstephenson.mywgutracker.ui.viewmodels.AssessmentListViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AssessmentListFragment extends Fragment {

    private AssessmentListViewModel viewModel;
    private String title;

    public AssessmentListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assessment_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //configure floating action button
        //todo uncomment once edit assessment edit form is implemented
//        FloatingActionButton fab = getActivity().findViewById(R.id.fab_add_assessment);
//        fab.setOnClickListener(this::openAssessmentEditActivityForNewAssessment);
//        fab.show();

        RecyclerView recyclerView = getView().findViewById(R.id.assessment_recyclerview);

        final AssessmentListAdapter adapter = new AssessmentListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        viewModel = ViewModelProviders.of(this).get(AssessmentListViewModel.class);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(MainActivity.TITLE_RESOURCE_ID)) {
                title = getString(getArguments().getInt(MainActivity.TITLE_RESOURCE_ID));
            }
            if (bundle.containsKey(CourseActivity.EXTRA_COURSE_ID)) {
                long courseId = bundle.getLong(CourseActivity.EXTRA_COURSE_ID);
                viewModel.getAssessmentsByCourseId(courseId).observe(getActivity(), adapter::setAssessments);
            } else {
                viewModel.getAllAssessments().observe(getActivity(), adapter::setAssessments);
            }
        }

        adapter.setOnItemInteractionListener(((view, position) -> {
            Intent intent = new Intent(getActivity(), AssessmentEditActivity.class);
            Assessment selectedAssessment = viewModel.getAssessment(position);
            intent.putExtra(CourseActivity.EXTRA_ASSESSMENT_ID, selectedAssessment.getId());
            startActivity(intent);
        }));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(title)) {
            getActivity().setTitle(title);
        }
    }

}
