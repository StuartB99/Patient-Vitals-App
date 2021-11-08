package com.example.fit3077_ass2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ModelClasses.MonitoredPatient;
import ModelClasses.Patient;
import ModelClasses.PatientMonitors.BloodPressure;
import ModelClasses.PatientMonitors.Cholesterol;
import ModelClasses.PatientMonitors.PatientMonitor;

/**
 * Page that shows the details of the selected patients. Also the page where it is possible to go to
 * the graphs, history of patients with high blood pressure, and more details
 */
public class SelectPatients extends AppCompatActivity {

    private RecyclerView monitorsRecyclerView;
    private ArrayList<Patient> selectedPatients;
    private ArrayList<Cholesterol> monitoredPatientsCholesterol = new ArrayList<>();
    private ArrayList<BloodPressure> monitoredPatientsBloodPressure = new ArrayList<>();
    private Double averageCholesterol = 0.0;
    private Double highSystolicBP;
    private Double highDiastolicBP;
    private MonitorViewer monitorAdapter;
    private Boolean refreshChanged = false;
    private Handler handler = new Handler();
    private Runnable runnable;
    private EditText refreshRateEditText;
    private TextView refreshTextView;
    public static int delay = 30000;
    public static String displayFormat;

    /**
     * Called when the page is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_patients);

        selectedPatients = PatientList.selectedPatients;

        Intent intent = getIntent();
        // Get the display format
        displayFormat = intent.getCharSequenceExtra("displayFormat").toString();

        // Get the high blood pressure levels from the patient list page
        highSystolicBP = intent.getDoubleExtra("HighSystolicBP", 140.00);
        highDiastolicBP = intent.getDoubleExtra("HighDiastolicBP", 90.00);

        System.out.println(displayFormat);
        if (displayFormat.equals("Blood Pressure")){
            fillPatientBloodPressure();
        }
        else if(displayFormat.equals("Cholesterol")){
            fillPatientCholesterolList();
        }
        else{
            fillPatientCholesterolList();
            fillPatientBloodPressure();
        }

        refreshRateEditText = findViewById(R.id.refreshEditText);
        refreshTextView = findViewById(R.id.refreshRateTextView);

        monitorsRecyclerView = findViewById(R.id.cholesterolRecyclerView);

        monitorAdapter = new MonitorViewer(this, selectedPatients, monitoredPatientsCholesterol, monitoredPatientsBloodPressure, averageCholesterol, highSystolicBP, highDiastolicBP);
        monitorsRecyclerView.setAdapter(monitorAdapter);
        monitorsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Called whenever the option menu is created
     * @param menu menu to be created
     * @return true if menu was created, false otherwise
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Method that is called whenever one of the items in the menu is selected
     * @param item the item that was chose
     * @return true if an item was selected, false if not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.barGraphOption:   // when the cholesterol bar graph option is chosen
                System.out.println(monitoredPatientsCholesterol.size());
                if (monitoredPatientsCholesterol.size() == 0) {
                    Toast toast = Toast.makeText(this, "Cholesterol Levels have not been monitored", Toast.LENGTH_LONG);
                    toast.show();
                    return true;
                }
                else {
                    Intent intent = new Intent(this, TotalCholesterolGraph.class);
                    intent.putExtra("Patients", selectedPatients);
                    startActivity(intent);
                    return true;
                }

            case R.id.highBPChangesOption:  // when the changes of high blood pressure option is chosen
                ArrayList<BloodPressure> highBloodPressureOfPatients = new ArrayList<>();
                ArrayList<Patient> patientsWithHighBloodPressure = new ArrayList<>();
                if (displayFormat.equals("Cholesterol")){
                    String msgNoBloodPressure = "This view only displays Cholesterol. Please choose either 'Both' or 'Blood Pressure' in the previous screen.";
                    Toast toast = Toast.makeText(this, msgNoBloodPressure, Toast.LENGTH_LONG);
                    toast.show();
                    return true;
                }
                else{
                    Intent intentToHighBPChanges = new Intent(this, HighBloodPressureChanges.class);

                    for(int i = 0 ; i < monitoredPatientsBloodPressure.size() ; i++){
                        if(monitoredPatientsBloodPressure.get(i).getSystolicBloodPressure() > highSystolicBP){
                            highBloodPressureOfPatients.add(monitoredPatientsBloodPressure.get(i));
                            patientsWithHighBloodPressure.add(selectedPatients.get(i));
                        }
                    }

                    intentToHighBPChanges.putExtra("HighBloodPressureOfPatients", highBloodPressureOfPatients);
                    intentToHighBPChanges.putExtra("PatientsWithHighBloodPressure", patientsWithHighBloodPressure);
                    startActivity(intentToHighBPChanges);
                    return true;
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Fills all the cholesterol details of the selected patients
     */
    public void fillPatientCholesterolList() {
        Double totalCholesterol = 0.0;
        int numOfPatientsWithCholesterol = 0;
        for (int i = 0; i < selectedPatients.size(); i++) {
            MonitoredPatient monitoredPatient = (MonitoredPatient) selectedPatients.get(i);

            PatientMonitor patientMonitor = new Cholesterol();

            monitoredPatient.addPatientMonitor(patientMonitor);

            for(int j = 0 ; j < monitoredPatient.getPatientMonitors().size() ; j++){
                if (monitoredPatient.getPatientMonitors().get(j) instanceof Cholesterol){
                    patientMonitor = monitoredPatient.getPatientMonitors().get(j);
                }
            }
            try {
                patientMonitor.updatePatientMonitorLevel(monitoredPatient.getId());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Cholesterol cholesterol = (Cholesterol) patientMonitor;
            monitoredPatientsCholesterol.add(cholesterol);
            if (cholesterol.getCholesterolLevel() != null){
                totalCholesterol += cholesterol.getCholesterolLevel();
                numOfPatientsWithCholesterol ++;
            }
        }

        averageCholesterol = totalCholesterol / numOfPatientsWithCholesterol;
    }

    /**
     * Fills all the blood pressure details of the patient
     */
    public void fillPatientBloodPressure(){
        for (int i = 0; i < selectedPatients.size(); i++) {
            MonitoredPatient monitoredPatient = (MonitoredPatient) selectedPatients.get(i);

            PatientMonitor patientMonitor = new BloodPressure();

            monitoredPatient.addPatientMonitor(patientMonitor);

            for(int j = 0 ; j < monitoredPatient.getPatientMonitors().size() ; j++){
                if (monitoredPatient.getPatientMonitors().get(j) instanceof BloodPressure){
                    patientMonitor = monitoredPatient.getPatientMonitors().get(j);
                }
            }
            try {
                patientMonitor.updatePatientMonitorLevel(monitoredPatient.getId());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            BloodPressure bloodPressure = (BloodPressure) patientMonitor;
            monitoredPatientsBloodPressure.add(bloodPressure);
        }
    }

    /**
     * Method called whenever the change refresh rate is called
     * @param view the view
     */
    public void refreshRateChanged(View view) {
        refreshTextView.setText("Current Refresh Rate: Every " + delay/1000 + " seconds.");

        if (refreshChanged == false) {
            handler.postDelayed(runnable = new Runnable() {
                @Override
                public void run() {
                    if (displayFormat.equals("Blood Pressure")){
                        fillPatientBloodPressure();
                        if(monitoredPatientsCholesterol.size() > 0){
                            fillPatientCholesterolList();
                        }
                    }
                    else if(displayFormat.equals("Cholesterol")){
                        fillPatientCholesterolList();
                        if(monitoredPatientsBloodPressure.size() > 0){
                            fillPatientBloodPressure();
                        }
                    }
                    else{
                        fillPatientCholesterolList();
                        fillPatientBloodPressure();
                    }
                    monitorAdapter.notifyDataSetChanged();
                    System.out.println("Refresh");
                    handler.postDelayed(this, delay);
                }
            }, delay);
            refreshChanged = true;
        }
        else {
            // stop delay refresh
            // start a new delay refresh
            handler.removeCallbacksAndMessages(runnable);
            String newDelay = refreshRateEditText.getText().toString();
            if (newDelay.equals("")){
                Toast toast = Toast.makeText(this, "No new number of seconds entered.", Toast.LENGTH_LONG);
                toast.show();
            }
            else{
                delay = Integer.parseInt(newDelay)*1000;
                refreshTextView.setText("Current Refresh Rate: Every " + delay/1000 + " seconds.");
                System.out.println("Refresh rate changed. New refresh rate is every " + delay/1000 + " seconds.");
            }
        }
    }
}
