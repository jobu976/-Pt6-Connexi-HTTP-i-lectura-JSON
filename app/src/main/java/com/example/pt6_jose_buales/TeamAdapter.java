package com.example.pt6_jose_buales;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {

    private final List<Team> teams;
    private final Context context;
    private final String lliga;

    public TeamAdapter(List<Team> teams, Context context, String lliga) {
        this.teams = teams;
        this.context = context;
        this.lliga = lliga;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_team, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Team team = teams.get(position);
        holder.txtNom.setText(team.getName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TeamDetailActivity.class);
            intent.putExtra("LLIGA", lliga);
            intent.putExtra("CODI", team.getCode());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNom;

        ViewHolder(View itemView) {
            super(itemView);
            txtNom = itemView.findViewById(R.id.txtNomEquip);
        }
    }
}
