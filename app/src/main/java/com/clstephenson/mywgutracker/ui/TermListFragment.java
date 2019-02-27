package com.clstephenson.mywgutracker.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clstephenson.mywgutracker.R;
import com.clstephenson.mywgutracker.data.models.Term;
import com.clstephenson.mywgutracker.ui.adapters.TermListAdapter;
import com.clstephenson.mywgutracker.ui.viewmodels.TermListViewModel;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TermListFragment extends Fragment {

    private TermListViewModel termListViewModel;
    private String title;

    public TermListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_term_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView recyclerView = getView().findViewById(R.id.term_recyclerview);

        final TermListAdapter adapter = new TermListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        termListViewModel = ViewModelProviders.of(getActivity()).get(TermListViewModel.class);
        termListViewModel.getAllTerms().observe(getActivity(), adapter::setTerms);

        adapter.setOnItemInteractionListener(((view, position) -> {
            Intent intent = new Intent(getActivity(), TermActivity.class);
            Term selectedTerm = termListViewModel.getTerm(position);
            intent.putExtra(TermActivity.EXTRA_TERM_ID, selectedTerm.getId());
            startActivity(intent);
        }));

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
        getActivity().setTitle(title);
    }

}
