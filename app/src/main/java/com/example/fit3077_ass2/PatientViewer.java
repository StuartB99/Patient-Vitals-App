package com.example.fit3077_ass2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ModelClasses.Patient;

/**
 * Class that takes the patient data and puts it into the view of the recycler view
 */
public class PatientViewer extends RecyclerView.Adapter<PatientViewer.MyViewHolder> {

    private ArrayList<Patient> patients;
    private Context context;
    private ArrayList<Patient> selectedPatients;

    /**
     * Constructor for the class
     * @param ct context
     * @param patientList the list of patients
     * @param selectedPatientList patients who are to be selected
     */
    public PatientViewer(Context ct, ArrayList<Patient> patientList, ArrayList<Patient> selectedPatientList) {
        context = ct;
        patients = patientList;
        selectedPatients = selectedPatientList;

    }

    /**
     * Called whenever a view holder is being created
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.patient_row, parent, false);
        return new MyViewHolder(view);
    }

    /**
     * Binds the information to the view holder
     * @param holder view holder
     * @param position position of the current view holder being bind
     */
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        // set rows equal to names of patients
        holder.patientNameTextView.setText(patients.get(position).getGivenName() + " " + patients.get(position).getFamilyName());

        // add/remove from list based on what checkboxes selected
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPatients.contains(patients.get(position))) {
                    System.out.println("Removing Patient");
                    selectedPatients.remove(patients.get(position));
                } else {
                    selectedPatients.add(patients.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    /**
     * Class of the viewholder
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView patientNameTextView;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            patientNameTextView = itemView.findViewById(R.id.patientNameTextView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }

    }
}
