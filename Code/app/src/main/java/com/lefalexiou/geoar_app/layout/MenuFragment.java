package com.lefalexiou.geoar_app.layout;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lefalexiou.geoar_app.R;
import com.lefalexiou.geoar_app.adapters.RoutesListAdapter;
import com.lefalexiou.geoar_app.adapters.RoutesRecyclerAdapter;
import com.lefalexiou.geoar_app.models.Place;
import com.lefalexiou.geoar_app.models.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {
    private static final String TAG = "MenuFragment";
    private RecyclerView recyclerView;
    private RoutesListAdapter routesListAdapter;
    private RoutesRecyclerAdapter routesRecyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Route> routes = new ArrayList<>();
    private MenuFragmentListener listener;

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
        Log.d(TAG, "RoutesListAdapter: onCreate: " + routes);
    }

    private void initDataSet() {
/*        ArrayList<String> toadd = new ArrayList<>();
        toadd.add("Acropolis of Athens");
        toadd.add("Temple of Olympian Zeus");
        addData(new Route("Central Athens Tour",toadd));*/
        Log.d(TAG, "initDataSet: pre-db");
//        db.collection("routes")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        routes.clear();
//                        Log.d(TAG, "initDataSet: onComplete: onComplete start");
//                        for (QueryDocumentSnapshot document: task.getResult()){
//                            Map<String,Object> data = document.getData();
//                            ArrayList<String> placesData = new ArrayList<>();
//                            placesData.addAll(0,(ArrayList)data.get("places"));
//                            Route route = new Route((String) data.get("title"),placesData);
//                            routes.add(route);
//                        }
//                        Log.d(TAG, "initDataSet: onComplete: routeData: " + routes);
////                        routesListAdapter.notifyItemRangeInserted(0,routes.size());
//                        routesListAdapter.notifyDataSetChanged();
//                    }
//                });
//        Log.d(TAG, "initDataSet: route: " + routes);
        //TODO: Change dat thing to the firebase recycler (maybe also change and the pagination to firebase)
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        initDataSet();
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        /*recyclerView = (RecyclerView) v.findViewById(R.id.routes_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);


        Log.d(TAG, "RoutesListAdapter: onfragment: "+ routes);
        routesListAdapter = new RoutesListAdapter(routes);
        recyclerView.setAdapter(routesListAdapter);*/
        Query query = db.collection("routes");

        FirestoreRecyclerOptions<Route> options = new FirestoreRecyclerOptions.Builder<Route>()
                .setQuery(query, Route.class)
                .build();

        routesRecyclerAdapter = new RoutesRecyclerAdapter(options);

        RecyclerView recyclerView = v.findViewById(R.id.routes_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(routesRecyclerAdapter);

        routesRecyclerAdapter.setOnItemClickListener(new RoutesRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Route route = documentSnapshot.toObject(Route.class);
                Log.d(TAG, "onItemClick: position: " + position + ", title: " + route.getTitle());
                listener.onDataTransfer(route);
            }
        });

        return v;
    }

    public  interface MenuFragmentListener{
        void onDataTransfer(Route route);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof MenuFragmentListener){
            listener = (MenuFragmentListener) context;
        }else {
            throw new RuntimeException(context.toString()
            + " must implement MenuFragmentListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        routesRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        routesRecyclerAdapter.stopListening();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        listener = null;
    }

    public void addData(Route route) {
        db.collection("routes")
                .add(route);
    }
}