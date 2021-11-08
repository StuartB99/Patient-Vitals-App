package com.example.fit3077_ass2;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import ModelClasses.Patient;
import ModelClasses.PatientMonitors.BloodPressure;

/**
 * Class that takes the patient data from the SelectedPatients class
 * Puts data into recyclerview in HighBloodPressureChanges
 */
public class SystolicPressureViewer extends RecyclerView.Adapter<SystolicPressureViewer.MyViewHolder> {

    private ArrayList<String> patientsHistory;
    private ArrayList<BloodPressure> highBloodPressureOfPatients;
    private ArrayList<Patient> patients;
    private LayoutInflater layoutInflater;

    /**
     * Constructor for the class
     * @param context context
     * @param patientsHistory blood pressure history of patient to be displayed in recyclerview
     * @param highBloodPressureOfPatients blood pressure of patient to be passed through to systolic line graph activity
     * @param patients list of patients
     */
    SystolicPressureViewer(Context context, ArrayList<String> patientsHistory, ArrayList<BloodPressure> highBloodPressureOfPatients, ArrayList<Patient> patients) {
        this.layoutInflater = LayoutInflater.from(context);
        this.patientsHistory = patientsHistory;
        this.highBloodPressureOfPatients = highBloodPressureOfPatients;
        this.patients = patients;
    }

    /**
     * Called whenever a view holder is being created
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public SystolicPressureViewer.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.blood_pressure_row, parent, false);
        return new MyViewHolder(view);
    }

    /**
     * Binds the information to the view holder
     * @param holder view holder
     * @param position position of the current view holder being bind
     */
    @Override
    public void onBindViewHolder(@NonNull SystolicPressureViewer.MyViewHolder holder, int position) {
        // set row text to patients blood pressure history
        String bloodPressureHistory = patientsHistory.get(position);
        holder.bloodPressureHistoryTV.setText(bloodPressureHistory);

    }

    @Override
    public int getItemCount() {
        return patientsHistory.size();
    }

    /**
     * Class of the viewholder
     */
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView bloodPressureHistoryTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            bloodPressureHistoryTV = itemView.findViewById(R.id.bloodPressureHistoryTV);

            itemView.setOnCreateContextMenuListener(this);

            // when a patient is selected in the recyclerview, pass the blood pressure data through to
            // line graph activity
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Context context = v.getContext();
                    Intent intent = new Intent(context, SystolicBloodPressureGraph.class);

                    BloodPressure bloodPressure = highBloodPressureOfPatients.get(pos);
                    Patient patient = patients.get(pos);
                    intent.putExtra("BloodPressure", bloodPressure);
                    intent.putExtra("Patient", patient);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }
    }

}
