package com.example.fit3077_ass2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ModelClasses.MonitoredPatient;
import ModelClasses.Patient;
import ModelClasses.PatientMonitors.BloodPressure;
import ModelClasses.PatientMonitors.Cholesterol;

/**
 * Class that takes the patient monitor data and puts it into the view of the recycler view
 */
public class MonitorViewer extends RecyclerView.Adapter<MonitorViewer.MyViewHolder> {

    private ArrayList<Patient> patients;
    private Context context;
    private ArrayList<Cholesterol> monitoredPatientsCholesterol;
    private ArrayList<BloodPressure> monitoredPatientsBP;
    private Double averageCholesterol;
    private Double highSystolicBloodPressure;
    private Double highDiastolicBloodPressure;

    /**
     * constructor
     * @param ct context
     * @param selectedPatients patients that have been chosen by the user
     * @param monPatientsCholesterol their cholesterol
     * @param monPatientsBP their blood pressure
     * @param avCholesterol the average cholesterol
     * @param highSystolicBloodPressure the value for high systolic blood pressure
     * @param highDiastolicBloodPressure the value for high diastolic blood pressure
     */
    public MonitorViewer(Context ct, ArrayList<Patient> selectedPatients, ArrayList<Cholesterol> monPatientsCholesterol, ArrayList<BloodPressure> monPatientsBP, Double avCholesterol, Double highSystolicBloodPressure, Double highDiastolicBloodPressure) {
        this.context = ct;
        this.patients = selectedPatients;
        this.monitoredPatientsCholesterol = monPatientsCholesterol;
        this.monitoredPatientsBP = monPatientsBP;
        this.averageCholesterol = avCholesterol;
        this.highSystolicBloodPressure = highSystolicBloodPressure;
        this.highDiastolicBloodPressure = highDiastolicBloodPressure;
    }

    /**
     * Method that is called when the view holder is created
     * @param parent the parent view of the view holder
     * @param viewType type of view
     * @return the view holder which is used by onBindViewHolder
     */
    
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.total_cholesterol_row, parent, false);

        return new MyViewHolder(view);
    }

    /**
     * Binds the information and things to display to the view holder
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Set the name of the patient
        holder.patientNameTextView.setText(patients.get(position).getGivenName() + " " + patients.get(position).getFamilyName());

        if(SelectPatients.displayFormat.equals("Cholesterol")){
            setCholesterolValuesUI(holder, position);
        }
        else if (SelectPatients.displayFormat.equals("Blood Pressure")){
            setBloodPressureValuesUI(holder, position);
        }
        else{
            setCholesterolValuesUI(holder, position);
            setBloodPressureValuesUI(holder, position);
        }

    }

    /**
     * Gets the count of the item. Is used in the background by the monitorAdapter
     * @return the number of items
     */
    @Override
    public int getItemCount() {
        return patients.size();
    }

    /**
     * Set up the ui for the display of the cholesterol on the list
     * @param holder holder that holds the information and which is displayed
     * @param position position of the current holder being set up
     */
    public void setCholesterolValuesUI(@NonNull MyViewHolder holder, int position){
        // Set the cholesterol level
        if(monitoredPatientsCholesterol.get(position).getCholesterolLevel() == null){
            holder.totalCholesterolTextView.setText(" - ");
        }
        else{
            holder.totalCholesterolTextView.setText(Double.toString(monitoredPatientsCholesterol.get(position).getCholesterolLevel()) + " mg/dL");
            System.out.println(monitoredPatientsCholesterol.get(position).getCholesterolLevel());
            // If the cholesterol level is higher than the average, set the text colour to red
            if (monitoredPatientsCholesterol.get(position).getCholesterolLevel() > averageCholesterol) {
                holder.totalCholesterolTextView.setTextColor(context.getColor(R.color.highCholesterolHighlightColor));
            }
            // Set the effective datetime of the cholesterol
            holder.dateTimeCholesterol.setText(monitoredPatientsCholesterol.get(position).getEffectiveDateTime());
        }
    }

    /**
     * Set up the ui for the display of the blood pressures on the list
     * @param holder holder that holds the information and which is displayed
     * @param position position of the current holder being set up
     */
    public void setBloodPressureValuesUI(@NonNull MyViewHolder holder, int position){
        // Set the blood pressure level, both systolic and diastolic, only set the value if its available. -1 indicates the value was not obtained
        if(monitoredPatientsBP.get(position).getSystolicBloodPressure() == -1.00){
            holder.systolicBloodPressureTextView.setText(" - ");
            holder.diastolicBloodPressureTextView.setText(" - ");
        }
        else{
            holder.systolicBloodPressureTextView.setText(Double.toString(monitoredPatientsBP.get(position).getSystolicBloodPressure()) + " mmHg");
            holder.diastolicBloodPressureTextView.setText(Double.toString(monitoredPatientsBP.get(position).getDiastolicBloodPressure()) + " mmHg");
            // If either of the blood pressure is higher than the set amount, set the text to purple
            if (monitoredPatientsBP.get(position).getSystolicBloodPressure() > highSystolicBloodPressure){
                holder.systolicBloodPressureTextView.setTextColor(context.getColor(R.color.highBloodPressureHighlightColor));
            }
            if (monitoredPatientsBP.get(position).getDiastolicBloodPressure() > highDiastolicBloodPressure){
                holder.diastolicBloodPressureTextView.setTextColor(context.getColor(R.color.highBloodPressureHighlightColor));
            }
            // Set the effective date time of the blood pressures
            holder.dateTimeBloodPressure.setText(monitoredPatientsBP.get(position).getEffectiveDateTime());
        }
    }

    /**
     * nested class of the view holder
     */
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView patientNameTextView;
        TextView totalCholesterolTextView;
        TextView dateTimeCholesterol;
        TextView dateTimeBloodPressure;
        TextView systolicBloodPressureTextView;
        TextView diastolicBloodPressureTextView;
        TextView cholesterolDisplay;
        TextView diastolicBloodPressureDisplay;
        TextView systolicBloodPressureDisplay;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            cholesterolDisplay = itemView.findViewById(R.id.textViewCholesterolDisplay);
            diastolicBloodPressureDisplay = itemView.findViewById(R.id.textViewBPDiaDisplay);
            systolicBloodPressureDisplay = itemView.findViewById(R.id.textViewBPSysDisplay);
            patientNameTextView = itemView.findViewById(R.id.patientNameTextView);
            totalCholesterolTextView = itemView.findViewById(R.id.cholesterolTextView);
            dateTimeCholesterol = itemView.findViewById(R.id.dateTimeCholesterolTextView);
            dateTimeBloodPressure = itemView.findViewById(R.id.dateTimeBPTextView);
            systolicBloodPressureTextView = itemView.findViewById(R.id.textViewBloodPressureSys);
            diastolicBloodPressureTextView = itemView.findViewById(R.id.textViewBloodPressureDia);

            itemView.setOnCreateContextMenuListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Context context = v.getContext();
                    Intent intent = new Intent(context, PatientDetails.class);

                    Patient patient = patients.get(pos);
                    Cholesterol patientCholesterol = null;
                    BloodPressure patientBloodPressure = null;
                    if (monitoredPatientsCholesterol.size() > 0){
                         patientCholesterol = monitoredPatientsCholesterol.get(pos);
                    }
                    if (monitoredPatientsBP.size() > 0){
                        patientBloodPressure = monitoredPatientsBP.get(pos);
                    }
                    intent.putExtra("Patient", patient);

                    if (patientCholesterol != null){
                        intent.putExtra("Cholesterol", patientCholesterol);
                        intent.putExtra("CholesterolDateTime", patientCholesterol.getEffectiveDateTime());
                    }
                    if (patientBloodPressure != null) {
                        intent.putExtra("BloodPressure", patientBloodPressure);
                        intent.putExtra("BloodPressureDateTime", patientBloodPressure.getEffectiveDateTime());
                    }

                    context.startActivity(intent);
                }
            });
        }

        /**
         * Method called whenever a context menu is created. Adds the options of the menu as well as what happens
         * when they are clicked on.
         * @param menu
         * @param v
         * @param menuInfo
         */
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select The Action");
            //groupId, itemId, order, title
            menu.add(0, v.getId(), 0, "Add/Remove Blood Pressure").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int pos = getAdapterPosition();
                    // checks whether anything is displayed on the ui for systolic blood pressure
                    if (systolicBloodPressureTextView.getText().equals(" - ")){
                        if (monitoredPatientsBP.size() == 0){
                            for(int i = 0 ; i < patients.size() ; i++){
                                monitoredPatientsBP.add(null);
                            }
                        }
                        if (monitoredPatientsBP.get(pos) == null){
                            MonitoredPatient patient = (MonitoredPatient) patients.get(pos);
                            BloodPressure bloodPressure = new BloodPressure();
                            try {
                                bloodPressure.updatePatientMonitorLevel(patient.getId());
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            patient.addPatientMonitor(bloodPressure);

                            monitoredPatientsBP.add(pos, bloodPressure);
                        }
                        if (monitoredPatientsBP.get(pos).getSystolicBloodPressure() != -1.00){  // if the blood pressure is available
                            systolicBloodPressureTextView.setText(Double.toString(monitoredPatientsBP.get(pos).getSystolicBloodPressure()) + " mmHg");
                            diastolicBloodPressureTextView.setText(Double.toString(monitoredPatientsBP.get(pos).getDiastolicBloodPressure())+ " mmHg");
                            dateTimeBloodPressure.setText(monitoredPatientsBP.get(pos).getEffectiveDateTime());
                            if (monitoredPatientsBP.get(pos).getSystolicBloodPressure()>highSystolicBloodPressure){
                                systolicBloodPressureTextView.setTextColor(Color.parseColor("#7217e8"));
                            }
                            if (monitoredPatientsBP.get(pos).getDiastolicBloodPressure()>highDiastolicBloodPressure){
                                diastolicBloodPressureTextView.setTextColor(Color.parseColor("#7217e8"));
                            }
                        }
                    }
                    else{   // when the blood pressure is monitored, stop monitoring
                        systolicBloodPressureTextView.setText(" - ");
                        systolicBloodPressureTextView.setTextColor(Color.BLACK);
                        diastolicBloodPressureTextView.setText(" - ");
                        diastolicBloodPressureTextView.setTextColor(Color.BLACK);
                        dateTimeBloodPressure.setText(" - ");
                    }

                    return true;
                }
            });
            // Add the add/remove cholesterol option to the menu as well as what happens when it is clicked on
            menu.add(0, v.getId(), 0, "Add/Remove Cholesterol").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int pos = getAdapterPosition();

                    if (totalCholesterolTextView.getText().equals(" - ")){  // if cholesterol not monitored, start monitoring
                        if (monitoredPatientsCholesterol.size() == 0){
                            for(int i = 0 ; i < patients.size() ; i++){
                                monitoredPatientsCholesterol.add(null);
                            }
                        }
                        if (monitoredPatientsCholesterol.get(pos) == null){
                            MonitoredPatient patient = (MonitoredPatient) patients.get(pos);
                            Cholesterol cholesterol = new Cholesterol();
                            try {
                                cholesterol.updatePatientMonitorLevel(patient.getId());
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            patient.addPatientMonitor(cholesterol);

                            monitoredPatientsCholesterol.add(pos, cholesterol);
                            double totalCholesterol = 0.00;
                            double numOfCholesterol = 0;
                            for(int i = 0 ; i < monitoredPatientsCholesterol.size() ; i++){
                                if(monitoredPatientsCholesterol.get(i) != null){
                                    totalCholesterol+=monitoredPatientsCholesterol.get(i).getCholesterolLevel();
                                    numOfCholesterol++;
                                }
                            }
                            averageCholesterol = totalCholesterol/numOfCholesterol;
                        }
                        if (monitoredPatientsCholesterol.get(pos).getCholesterolLevel() != null){
                            totalCholesterolTextView.setText(monitoredPatientsCholesterol.get(pos).getCholesterolLevel().toString() + " mg/dL");
                            dateTimeCholesterol.setText(monitoredPatientsCholesterol.get(pos).getEffectiveDateTime());
                            if (monitoredPatientsCholesterol.get(pos).getCholesterolLevel() > averageCholesterol){
                                totalCholesterolTextView.setTextColor(Color.parseColor("#ff0000"));
                            }
                        }
                    }

                    else{       // if it is monitored, remove it
                        totalCholesterolTextView.setText(" - ");
                        totalCholesterolTextView.setTextColor(Color.BLACK);
                        dateTimeCholesterol.setText(" - ");
                    }

                    return true;
                }
            });
        }
    }
}
