package com.clstephenson.mywgutracker;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clstephenson.mywgutracker.adapters.TermListAdapter;
import com.clstephenson.mywgutracker.viewmodels.TermListViewModel;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TermListFragment extends Fragment {

    private TermListViewModel termListViewModel;

    public TermListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_term_list, container, false);

        // Inflate the layout for this fragment
        return rootView;
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
    }
}
