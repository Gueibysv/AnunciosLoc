package com.anunciosloc.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anunciosloc.R;
import com.anunciosloc.data.MockDataSource;
import com.anunciosloc.models.Local;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private List<Local> locationList;

    public LocationAdapter(List<Local> locationList) {
        this.locationList = locationList;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Local local = locationList.get(position);
        holder.locationName.setText(local.getNome());
        holder.locationDetails.setText(local.toString());

        holder.removeButton.setOnClickListener(v -> {
            if (MockDataSource.removeLocation(local.getId())) {
                locationList.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(v.getContext(), "Local removido: " + local.getNome(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(v.getContext(), "Erro ao remover local.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView locationName;
        TextView locationDetails;
        Button removeButton;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            locationName = itemView.findViewById(R.id.locationNameTextView);
            locationDetails = itemView.findViewById(R.id.locationDetailsTextView);
            removeButton = itemView.findViewById(R.id.removeLocationButton);
        }
    }
}
