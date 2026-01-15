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
import com.anunciosloc.dto.LocationDTO;

import java.util.List;

public class LocationAdapter
        extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private final List<LocationDTO> locationList;

    public LocationAdapter(List<LocationDTO> locationList) {
        this.locationList = locationList;
    }

    // ================= VIEW HOLDER =================
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
    // =================================================

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull LocationViewHolder holder,
            int position
    ) {
        LocationDTO loc = locationList.get(position);

        holder.locationName.setText(loc.nome);

        if ("GPS".equalsIgnoreCase(loc.tipoCoordenada)) {
            holder.locationDetails.setText(
                    "GPS • Lat: " + loc.latitude +
                            " | Lon: " + loc.longitude +
                            " | Raio: " + loc.raioMetros + "m"
            );
        } else {
            holder.locationDetails.setText(
                    "Wi-Fi • " +
                            (loc.wifiSSIDs != null ? loc.wifiSSIDs.size() : 0) +
                            " redes"
            );
        }

        holder.removeButton.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            locationList.remove(pos);
            notifyItemRemoved(pos);

            Toast.makeText(
                    v.getContext(),
                    "Local removido da lista",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }
}
