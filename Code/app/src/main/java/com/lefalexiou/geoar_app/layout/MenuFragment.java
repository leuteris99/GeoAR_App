package com.lefalexiou.geoar_app.layout;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lefalexiou.geoar_app.R;
import com.lefalexiou.geoar_app.adapters.RoutesListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private RoutesListAdapter routesListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String[] dataSet;
    private static final int DATASET_COUNT = 60;
    public MenuFragment() {
        // Required empty public constructor
    }

    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDataSet();
    }

    private void initDataSet() {
        dataSet = new String[DATASET_COUNT];
        for (int i = 0; i < DATASET_COUNT; i++) {
            dataSet[i] = "This is element #" + i;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.routes_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        routesListAdapter = new RoutesListAdapter(dataSet);
        recyclerView.setAdapter(routesListAdapter);


        return v;
    }
}