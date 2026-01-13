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
import com.anunciosloc.models.Perfil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProfileKeyAdapter extends RecyclerView.Adapter<ProfileKeyAdapter.ProfileKeyViewHolder> {

    private List<Map.Entry<String, String>> keyList;
    private Perfil userProfile;

    public ProfileKeyAdapter(Perfil userProfile) {
        this.userProfile = userProfile;
        this.keyList = new ArrayList<>(userProfile.getChaves().entrySet());
    }

    @NonNull
    @Override
    public ProfileKeyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_key, parent, false);
        return new ProfileKeyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileKeyViewHolder holder, int position) {
        Map.Entry<String, String> entry = keyList.get(position);
        String key = entry.getKey();
        String value = entry.getValue();

        holder.keyTextView.setText(key + ": " + value);

        holder.removeButton.setOnClickListener(v -> {
            userProfile.removerChave(key);
            keyList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(v.getContext(), "Chave '" + key + "' removida.", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return keyList.size();
    }

    public void updateData() {
        this.keyList = new ArrayList<>(userProfile.getChaves().entrySet());
        notifyDataSetChanged();
    }

    public static class ProfileKeyViewHolder extends RecyclerView.ViewHolder {
        TextView keyTextView;
        Button removeButton;

        public ProfileKeyViewHolder(@NonNull View itemView) {
            super(itemView);
            keyTextView = itemView.findViewById(R.id.keyTextView);
            removeButton = itemView.findViewById(R.id.removeKeyButton);
        }
    }
}
