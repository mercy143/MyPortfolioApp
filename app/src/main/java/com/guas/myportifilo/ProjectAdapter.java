package com.guas.myportifilo;
import android.widget.Button;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    private final List<Project> projectList;
    private final OnProjectActionListener listener;

    public ProjectAdapter(List<Project> projectList, OnProjectActionListener listener) {
        this.projectList = projectList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_project, parent, false);  // Your project item layout
        return new ProjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Project project = projectList.get(position);
        holder.tvTitle.setText(project.getTitle());
        holder.tvDescription.setText(project.getDescription());

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(project, position);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(project, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    public static class ProjectViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDescription;
        Button btnEdit, btnDelete;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvProjectTitle);
            tvDescription = itemView.findViewById(R.id.tvProjectDescription);
            btnEdit = itemView.findViewById(R.id.btnEditProject);
            btnDelete = itemView.findViewById(R.id.btnDeleteProject);
        }
    }

    public interface OnProjectActionListener {
        void onEdit(Project project, int position);
        void onDelete(Project project, int position);
    }
}
