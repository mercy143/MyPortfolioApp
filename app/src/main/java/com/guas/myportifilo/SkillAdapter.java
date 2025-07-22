package com.guas.myportifilo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.SkillViewHolder> {

    private List<MainActivity.Skill> skillList;
    private OnSkillActionListener listener;

    public interface OnSkillActionListener {
        void onDelete(MainActivity.Skill skill);
        void onEdit(MainActivity.Skill skill, int position);
    }

    public SkillAdapter(List<MainActivity.Skill> skillList, OnSkillActionListener listener) {
        this.skillList = skillList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SkillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_skill, parent, false);
        return new SkillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillViewHolder holder, int position) {
        MainActivity.Skill skill = skillList.get(position);
        holder.tvSkillName.setText(skill.name + " (" + skill.accuracy + "%)");

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(skill, position);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(skill);
        });
    }

    @Override
    public int getItemCount() {
        return skillList.size();
    }

    static class SkillViewHolder extends RecyclerView.ViewHolder {
        TextView tvSkillName;
        Button btnEdit, btnDelete;

        SkillViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSkillName = itemView.findViewById(R.id.tvSkillName);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
