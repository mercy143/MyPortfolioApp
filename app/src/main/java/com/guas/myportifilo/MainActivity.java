package com.guas.myportifilo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnAddProject, btnCancelProject, btnSaveProject;
    private LinearLayout skillButtonGroup, projectbuttonGroup;

    // Skill buttons
    private Button btnAddSkill, btnCancelSkill, btnSaveSkill;

    private EditText etProjectName, etProjectDesc;
    private List<Project> projectList = new ArrayList<>();
    private ProjectAdapter projectAdapter;

    private RecyclerView rvSkills, rvProjects;
    private Button btnShowSkills, btnShowProjects, btnContact;
    private EditText etSkillName, etSkillPercent;
    private PieChart pieChart;

    private boolean skillsVisible = false;
    private boolean projectsVisible = false;

    private boolean isAddingSkill = false;
    private int editingSkillPosition = -1;

    private boolean isEditingProject = false;
    private int editingProjectPosition = -1;

    private List<Skill> skillList = new ArrayList<>();
    private SkillAdapter skillAdapter;

    static class Skill {
        String name;
        float accuracy;

        Skill(String name, float accuracy) {
            this.name = name;
            this.accuracy = accuracy;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Project Views
        btnAddProject = findViewById(R.id.btnAddProject);
        btnCancelProject = findViewById(R.id.btnCancelProject);
        btnSaveProject = findViewById(R.id.btnSaveProject);

        etProjectName = findViewById(R.id.etProjectName);
        etProjectDesc = findViewById(R.id.etProjectDesc);
        skillButtonGroup = findViewById(R.id.skillButtonGroup);
        projectbuttonGroup = findViewById(R.id.projectButtonGroup);

        // Initialize Skill Views
        btnAddSkill = findViewById(R.id.btnAddSkill);
        btnCancelSkill = findViewById(R.id.btnCancelSkill);
        btnSaveSkill = findViewById(R.id.btnSaveSkill);

        etSkillName = findViewById(R.id.etSkillName);
        etSkillPercent = findViewById(R.id.etSkillPercent);

        // Hide inputs and Save/Cancel buttons initially
        btnCancelProject.setVisibility(View.GONE);
        btnSaveProject.setVisibility(View.GONE);
        etProjectName.setVisibility(View.GONE);
        etProjectDesc.setVisibility(View.GONE);
        projectbuttonGroup.setVisibility(View.GONE);

        btnCancelSkill.setVisibility(View.GONE);
        btnSaveSkill.setVisibility(View.GONE);
        etSkillName.setVisibility(View.GONE);
        etSkillPercent.setVisibility(View.GONE);
        skillButtonGroup.setVisibility(View.GONE);

        // Initialize RecyclerViews and other buttons
        rvSkills = findViewById(R.id.rvSkills);
        rvProjects = findViewById(R.id.rvProjects);
        pieChart = findViewById(R.id.pieChart);
        btnShowSkills = findViewById(R.id.btnShowSkills);
        btnShowProjects = findViewById(R.id.btnShowProjects);
        btnContact = findViewById(R.id.btnContact);

        Button btnAboutMe = findViewById(R.id.btnAboutMe);
        TextView tvAboutHeader = findViewById(R.id.tvAboutHeader);
        TextView tvAboutContent = findViewById(R.id.tvAboutContent);

        // About Me toggle animation
        btnAboutMe.setOnClickListener(v -> {
            boolean visible = tvAboutHeader.getVisibility() == View.VISIBLE;
            if (visible) {
                collapseView(tvAboutHeader);
                collapseView(tvAboutContent);
                btnAboutMe.setText("About Me");
            } else {
                expandView(tvAboutHeader);
                expandView(tvAboutContent);
                btnAboutMe.setText("Hide About Me");
            }
        });

        Button btnMyJourney = findViewById(R.id.btnMyJourney);
        TextView tvJourneyHeader = findViewById(R.id.tvJourneyHeader);
        TextView tvJourneyContent = findViewById(R.id.tvJourneyContent);

        btnMyJourney.setOnClickListener(v -> {
            boolean visible = tvJourneyHeader.getVisibility() == View.VISIBLE;
            if (visible) {
                collapseView(tvJourneyHeader);
                collapseView(tvJourneyContent);
                btnMyJourney.setText("My Journey");
            } else {
                expandView(tvJourneyHeader);
                expandView(tvJourneyContent);
                btnMyJourney.setText("Hide Journey");
            }
        });

        // Default projects
        projectList.add(new Project("Tax Reporting App", "Used by businesses to automate tax filing."));
        projectList.add(new Project("Invoice QR Validator", "Scans and validates ERCA invoice QR codes."));
        projectList.add(new Project("Chat Messenger", "Real-time Firebase-based messaging app."));
        projectList.add(new Project("Kebele Admin System", "Digital record management for local offices."));

        // Setup ProjectAdapter with edit/delete listener
        projectAdapter = new ProjectAdapter(projectList, new ProjectAdapter.OnProjectActionListener() {
            @Override
            public void onEdit(Project project, int position) {
                etProjectName.setText(project.getTitle());
                etProjectDesc.setText(project.getDescription());
                etProjectName.setVisibility(View.VISIBLE);
                etProjectDesc.setVisibility(View.VISIBLE);
                projectbuttonGroup.setVisibility(View.VISIBLE);
                btnCancelProject.setVisibility(View.VISIBLE);
                btnSaveProject.setVisibility(View.VISIBLE);
                btnAddProject.setVisibility(View.GONE);

                isEditingProject = true;
                editingProjectPosition = position;
            }

            @Override
            public void onDelete(Project project, int position) {
                projectList.remove(position);
                projectAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Project deleted", Toast.LENGTH_SHORT).show();
            }
        });

        rvProjects.setLayoutManager(new LinearLayoutManager(this));
        rvProjects.setAdapter(projectAdapter);

        // Add project button click: show input fields + Save/Cancel buttons
        btnAddProject.setOnClickListener(v -> {
            etProjectName.setVisibility(View.VISIBLE);
            etProjectDesc.setVisibility(View.VISIBLE);
            projectbuttonGroup.setVisibility(View.VISIBLE);
            btnCancelProject.setVisibility(View.VISIBLE);
            btnSaveProject.setVisibility(View.VISIBLE);
            btnAddProject.setVisibility(View.GONE);

            isEditingProject = false;
            editingProjectPosition = -1;
        });

        // Save project button click
        btnSaveProject.setOnClickListener(v -> {
            String name = etProjectName.getText().toString().trim();
            String desc = etProjectDesc.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(desc)) {
                Toast.makeText(MainActivity.this, "Fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isEditingProject) {
                projectList.set(editingProjectPosition, new Project(name, desc));
                Toast.makeText(MainActivity.this, "Project updated", Toast.LENGTH_SHORT).show();
                isEditingProject = false;
                editingProjectPosition = -1;
            } else {
                projectList.add(new Project(name, desc));
                Toast.makeText(MainActivity.this, "Project added", Toast.LENGTH_SHORT).show();
            }
            projectAdapter.notifyDataSetChanged();

            etProjectName.setText("");
            etProjectDesc.setText("");
            etProjectName.setVisibility(View.GONE);
            etProjectDesc.setVisibility(View.GONE);
            btnCancelProject.setVisibility(View.GONE);
            btnSaveProject.setVisibility(View.GONE);
            projectbuttonGroup.setVisibility(View.GONE);
            btnAddProject.setVisibility(View.VISIBLE);
        });

        btnCancelProject.setOnClickListener(v -> {
            etProjectName.setText("");
            etProjectDesc.setText("");
            etProjectName.setVisibility(View.GONE);
            etProjectDesc.setVisibility(View.GONE);
            btnCancelProject.setVisibility(View.GONE);
            btnSaveProject.setVisibility(View.GONE);
            projectbuttonGroup.setVisibility(View.GONE);
            btnAddProject.setVisibility(View.VISIBLE);
            isEditingProject = false;
            editingProjectPosition = -1;
        });

        // Default skills
        skillList.add(new Skill("Android", 95.6f));
        skillList.add(new Skill("Flutter", 85.5f));
        skillList.add(new Skill("Python & AI", 80.3f));
        skillList.add(new Skill("Node.js", 72.4f));
        skillList.add(new Skill("ReactJS", 71.4f));
        skillList.add(new Skill("Firebase", 70.4f));

        // Setup SkillAdapter with edit/delete listener
        skillAdapter = new SkillAdapter(skillList, new SkillAdapter.OnSkillActionListener() {
            @Override
            public void onDelete(Skill skill) {
                skillList.remove(skill);
                skillAdapter.notifyDataSetChanged();
                updatePieChart();
            }

            @Override
            public void onEdit(Skill skill, int position) {
                etSkillName.setText(skill.name);
                etSkillPercent.setText(String.valueOf(skill.accuracy));
                etSkillName.setVisibility(View.VISIBLE);
                etSkillPercent.setVisibility(View.VISIBLE);
                skillButtonGroup.setVisibility(View.VISIBLE);
                btnCancelSkill.setVisibility(View.VISIBLE);
                btnSaveSkill.setVisibility(View.VISIBLE);
                btnAddSkill.setVisibility(View.GONE);

                isAddingSkill = true;
                editingSkillPosition = position;
            }
        });

        rvSkills.setLayoutManager(new LinearLayoutManager(this));
        rvSkills.setAdapter(skillAdapter);

        // Add Skill button click
        btnAddSkill.setOnClickListener(v -> {
            etSkillName.setVisibility(View.VISIBLE);
            etSkillPercent.setVisibility(View.VISIBLE);
            skillButtonGroup.setVisibility(View.VISIBLE);
            btnCancelSkill.setVisibility(View.VISIBLE);
            btnSaveSkill.setVisibility(View.VISIBLE);
            btnAddSkill.setVisibility(View.GONE);

            isAddingSkill = false;
            editingSkillPosition = -1;
        });

        // Save skill button click
        btnSaveSkill.setOnClickListener(v -> {
            String name = etSkillName.getText().toString().trim();
            String percentStr = etSkillPercent.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(percentStr)) {
                Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            float percent;
            try {
                percent = Float.parseFloat(percentStr);
            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, "Invalid percentage", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isAddingSkill) {
                skillList.set(editingSkillPosition, new Skill(name, percent));
                Toast.makeText(MainActivity.this, "Skill updated", Toast.LENGTH_SHORT).show();
            } else {
                skillList.add(new Skill(name, percent));
                Toast.makeText(MainActivity.this, "Skill added", Toast.LENGTH_SHORT).show();
            }

            skillAdapter.notifyDataSetChanged();
            updatePieChart();

            // Reset & Hide
            etSkillName.setText("");
            etSkillPercent.setText("");
            etSkillName.setVisibility(View.GONE);
            etSkillPercent.setVisibility(View.GONE);
            skillButtonGroup.setVisibility(View.GONE);
            btnCancelSkill.setVisibility(View.GONE);
            btnSaveSkill.setVisibility(View.GONE);
            btnAddSkill.setVisibility(View.VISIBLE);

            isAddingSkill = false;
            editingSkillPosition = -1;
        });


        btnCancelSkill.setOnClickListener(v -> {
            etSkillName.setText("");
            etSkillPercent.setText("");
            etSkillName.setVisibility(View.GONE);
            etSkillPercent.setVisibility(View.GONE);
            skillButtonGroup.setVisibility(View.GONE);
            btnCancelSkill.setVisibility(View.GONE);
            btnSaveSkill.setVisibility(View.GONE);
            btnAddSkill.setVisibility(View.VISIBLE);

            isAddingSkill = false;
            editingSkillPosition = -1;
        });


        // Show/hide skills
        btnShowSkills.setOnClickListener(v -> {
            skillsVisible = !skillsVisible;
            rvSkills.setVisibility(skillsVisible ? View.VISIBLE : View.GONE);
            btnShowSkills.setText(skillsVisible ? "Hide Skills" : "Show Skills");
        });

        // Show/hide projects
        btnShowProjects.setOnClickListener(v -> {
            projectsVisible = !projectsVisible;
            rvProjects.setVisibility(projectsVisible ? View.VISIBLE : View.GONE);
            btnShowProjects.setText(projectsVisible ? "Hide Projects" : "Show Projects");
        });

        // Contact me dialog
        btnContact.setOnClickListener(v -> {
            String[] options = {"Phone", "Email"};
            new android.app.AlertDialog.Builder(MainActivity.this)
                    .setTitle("Contact me I'm Guash Berhe")
                    .setItems(options, (dialog, which) -> {
                        if (which == 0) {
                            Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
                            phoneIntent.setData(Uri.parse("tel:+251932330844"));
                            startActivity(phoneIntent);
                        } else {
                            String email = "guashberhe2019@gmail.com";
                            String subject = "Portfolio Contact - Guash";
                            String body = "Hello Guash,\n\nI saw your portfolio and want to reach out.";

                            Uri uri = Uri.parse("mailto:" + email +
                                    "?subject=" + Uri.encode(subject) +
                                    "&body=" + Uri.encode(body));

                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                            emailIntent.setData(uri);

                            try {
                                startActivity(Intent.createChooser(emailIntent, "Send Email"));
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(MainActivity.this, "No email clients installed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        // Social buttons
        findViewById(R.id.btnGithub).setOnClickListener(v -> openUrl("https://github.com/mercy143"));
        findViewById(R.id.btnTelegram).setOnClickListener(v -> openUrl("https://t.me/@zeberling"));
        findViewById(R.id.btnLinkedIn).setOnClickListener(v -> openUrl("https://www.linkedin.com/in/your_username"));
        findViewById(R.id.btnInstagram).setOnClickListener(v -> openUrl("https://www.instagram.com/your_username"));
        findViewById(R.id.btnYouTube).setOnClickListener(v -> openUrl("https://www.youtube.com/@Amerawi"));

        updatePieChart();
    }

    private void updatePieChart() {
        List<PieEntry> entries = new ArrayList<>();
        for (Skill skill : skillList) {
            entries.add(new PieEntry(skill.accuracy, skill.name));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Skill Accuracy");
        dataSet.setColors(new int[]{
                R.color.pie_android, R.color.pie_flutter, R.color.pie_python,
                R.color.pie_node, R.color.pie_react, R.color.pie_firebase
        }, this);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(12f);
        data.setValueTextColor(getResources().getColor(android.R.color.black));
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.1f%%", value);
            }
        });

        pieChart.setData(data);
        pieChart.setUsePercentValues(false);
        pieChart.setDrawHoleEnabled(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1000);
        pieChart.invalidate();
    }

    private void expandView(final View v) {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(0f);
        v.animate()
                .alpha(1f)
                .setDuration(300)
                .start();
    }

    private void collapseView(final View v) {
        v.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction(() -> v.setVisibility(View.GONE))
                .start();
    }

    private void openUrl(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }
}
