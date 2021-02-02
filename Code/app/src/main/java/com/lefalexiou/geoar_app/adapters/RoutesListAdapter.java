package com.lefalexiou.geoar_app.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lefalexiou.geoar_app.R;
import com.lefalexiou.geoar_app.models.Route;

import java.util.ArrayList;

//TODO: I thing this file is deprecated (not 100% sure, save it and test the app before deletion)

public class RoutesListAdapter extends RecyclerView.Adapter<RoutesListAdapter.ViewHolder> {
    private ArrayList<String> localDataSet;
    private static final String TAG = "RoutesListAdapter";

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);

            textView = (TextView) view.findViewById(R.id.routes_list_item_text);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public RoutesListAdapter(ArrayList<Route> dataSet) {
        localDataSet = new ArrayList<>();
        Log.d(TAG, "RoutesListAdapter: pre-: " + dataSet);
        for (Route route : dataSet) {
            localDataSet.add(route.getTitle());
        }
        Log.d(TAG, "RoutesListAdapter: " + localDataSet);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.routes_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextView().setText(localDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
