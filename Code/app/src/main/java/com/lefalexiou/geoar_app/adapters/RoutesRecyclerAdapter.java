package com.lefalexiou.geoar_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.lefalexiou.geoar_app.R;
import com.lefalexiou.geoar_app.models.Route;

public class RoutesRecyclerAdapter extends FirestoreRecyclerAdapter<Route, RoutesRecyclerAdapter.RoutesHolder> {
    private OnItemClickListener listener;

    public RoutesRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Route> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RoutesHolder holder, int position, @NonNull Route model) {
        holder.textViewTitle.setText(model.getTitle());
    }

    @NonNull
    @Override
    public RoutesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.routes_list_item,
                parent, false);
        return new RoutesHolder(v);
    }

    class RoutesHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;

        public RoutesHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.routes_list_item_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
