package com.anunciosloc.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anunciosloc.R;
import com.anunciosloc.activities.ViewAdActivity;
import com.anunciosloc.dto.AdDTO;

import java.util.List;

public class AdAdapter extends RecyclerView.Adapter<AdAdapter.AdViewHolder> {

    private final List<AdDTO> adList;

    public AdAdapter(List<AdDTO> adList) {
        this.adList = adList;
    }

    // 🔹 CRIA A VIEW
    @NonNull
    @Override
    public AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ad, parent, false);
        return new AdViewHolder(view);
    }

    // 🔹 PREENCHE OS DADOS
    @Override
    public void onBindViewHolder(@NonNull AdViewHolder holder, int position) {
        AdDTO ad = adList.get(position);

        holder.adText.setText(ad.texto);
        holder.adDetails.setText(
                "Local: " + ad.localNome + " | Editor: " + ad.editor
        );

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ViewAdActivity.class);
            intent.putExtra("AD_ID", ad.id);
            v.getContext().startActivity(intent);
        });
    }

    // 🔹 TOTAL DE ITENS
    @Override
    public int getItemCount() {
        return adList.size();
    }

    // 🔹 VIEW HOLDER
    static class AdViewHolder extends RecyclerView.ViewHolder {

        TextView adText;
        TextView adDetails;

        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
            adText = itemView.findViewById(R.id.adTextTextView);
            adDetails = itemView.findViewById(R.id.adDetailsTextView);
        }
    }
}
