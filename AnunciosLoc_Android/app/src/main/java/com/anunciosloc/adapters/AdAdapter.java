package com.anunciosloc.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anunciosloc.R;
import com.anunciosloc.activities.ViewAdActivity;
import com.anunciosloc.data.MockDataSource;
import com.anunciosloc.models.Anuncio;

import java.util.List;

public class AdAdapter extends RecyclerView.Adapter<AdAdapter.AdViewHolder> {

    private List<Anuncio> adList;

    public AdAdapter(List<Anuncio> adList) {
        this.adList = adList;
    }

    @NonNull
    @Override
    public AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ad, parent, false);
        return new AdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdViewHolder holder, int position) {
        Anuncio anuncio = adList.get(position);
        holder.adText.setText(anuncio.getTexto());
        holder.adDetails.setText("Local: " + anuncio.getLocalDestinoId() + " | Editor: " + anuncio.getEditor());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ViewAdActivity.class);
            intent.putExtra("AD_ID", anuncio.getId());
            v.getContext().startActivity(intent);
        });

        holder.removeButton.setOnClickListener(v -> {
            if (MockDataSource.removeAd(anuncio.getId())) {
                adList.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(v.getContext(), "Anúncio removido.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(v.getContext(), "Erro ao remover anúncio.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return adList.size();
    }

    public static class AdViewHolder extends RecyclerView.ViewHolder {
        TextView adText;
        TextView adDetails;
        Button removeButton;

        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
            adText = itemView.findViewById(R.id.adTextTextView);
            adDetails = itemView.findViewById(R.id.adDetailsTextView);
            removeButton = itemView.findViewById(R.id.removeAdButton);
        }
    }
}
