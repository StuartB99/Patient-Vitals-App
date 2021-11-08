package com.example.fit3077_ass2;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ModelClasses.Patient;
import ModelClasses.PatientMonitors.BloodPressure;

/**
 * UI class of the high blood pressure changes of patients with high blood pressure
 */
public class HighBloodPressureChanges extends AppCompatActivity {
    private Boolean refreshChanged = false;
    private Handler handler = new Handler();
    private Runnable runnable;
    private ArrayList<Patient> patients;
    private ArrayList<BloodPressure> highBloodPressureOfPatients;
    private ArrayList<String> bloodPressureHistories = new ArrayList<>();

    /**
     * Method called when the page is created
     * @param savedInstanceState the previous saved instance
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_bloodpressure_observations);

        highBloodPressureOfPatients = (ArrayList<BloodPressure>)getIntent().getSerializableExtra("HighBloodPressureOfPatients");
        patients = (ArrayList<Patient>) getIntent().getSerializableExtra("PatientsWithHighBloodPressure");

        refreshBloodPressureHistory();

        TextView titleTextView = findViewById(R.id.high_bp_observations_title);

        if(patients.size() > 0){
            displayPreviousBloodPressures();
        }
        else{       // when no patients with high blood pressure
            titleTextView.setText("There are no patients being monitored with high blood pressure.");
        }

        RecyclerView systolicPressureHistoryRecyclerView = findViewById(R.id.systolicResultsRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        systolicPressureHistoryRecyclerView.setLayoutManager(layoutManager);

        SystolicPressureViewer systolicPressureViewer = new SystolicPressureViewer(this, bloodPressureHistories, highBloodPressureOfPatients, patients);
        systolicPressureHistoryRecyclerView.setAdapter(systolicPressureViewer);
        systolicPressureHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Display the previous blood pressures of patients with high systolic blood pressure on the UI
     */
    private void displayPreviousBloodPressures(){
        String textOfHighBloodPressures = "";
        for(int i = 0 ; i < patients.size() ; i++){ // loops through the patients who have high blood pressure
            textOfHighBloodPressures = "";
            textOfHighBloodPressures += (patients.get(i).getGivenName() + " " + patients.get(i).getFamilyName()) + ": ";
            BloodPressure bloodPressure = highBloodPressureOfPatients.get(i);   // get their blood pressure
            for (int j = 0; j < bloodPressure.getBloodPressureHistory().size() ; j++){
                BloodPressure previousSystolicBP = bloodPressure.getBloodPressureHistory().get(j);
                String dateTime = previousSystolicBP.getEffectiveDateTime().substring(0, 10) + " " + previousSystolicBP.getEffectiveDateTime().substring(11, 19);

                if(j == (bloodPressure.getBloodPressureHistory().size()-1)){
                    textOfHighBloodPressures += previousSystolicBP.getSystolicBloodPressure()+" ("+ dateTime +")\n\n";
                    bloodPressureHistories.add(textOfHighBloodPressures);
                }
                else{
                    textOfHighBloodPressures += previousSystolicBP.getSystolicBloodPressure()+" ("+ dateTime +"), ";
                }
            }
        }

//        previousPressuresTextView.setText(textOfHighBloodPressures);    // display the history of high blood pressure to the ui
        for (int i = 0; i < bloodPressureHistories.size(); i++) {
            System.out.println(bloodPressureHistories.get(i));

        }
    }

    /**
     * Is called whenever the blood pressure history is refreshed. Calls to the fhir server to get the most
     * recent values.
     */
    private void refreshBloodPressureHistory(){
        for(int i = 0 ; i < highBloodPressureOfPatients.size() ; i++){
            try {
                highBloodPressureOfPatients.get(i).updatePatientMonitorLevel(patients.get(i).getId());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Is called whenever the change refresh rate button is clicked on. It changes the refresh rate which is shared
     * with all the other refreshes.
     * @param view
     */
    public void refreshRateChanged(View view) {
        TextView refreshBPTextView = findViewById(R.id.refreshRateTextViewHighBP);
        EditText refreshRateEditText = findViewById(R.id.refreshEditTextHighBP);
        refreshBPTextView.setText("Current Refresh Rate: Every " + SelectPatients.delay/1000 + " seconds.");

        if (refreshChanged == false) {  // if the refresh has not been changed
            handler.postDelayed(runnable = new Runnable() {
                @Override
                public void run() {
                    refreshBloodPressureHistory();  // refresh the data
                    // Display the refreshed data to the UI
                    displayPreviousBloodPressures();
                    System.out.println("Refresh");
                    handler.postDelayed(this, SelectPatients.delay);
                }
            }, SelectPatients.delay);
            refreshChanged = true;
        }
        else {  // the refresh duration has been changed
            // stop delay refresh
            // start a new delay refresh
            handler.removeCallbacksAndMessages(runnable);
            String newDelay = refreshRateEditText.getText().toString();
            if (newDelay.equals("")){
                Toast toast = Toast.makeText(this, "No new number of seconds entered.", Toast.LENGTH_LONG);
                toast.show();
            }
            else{
                SelectPatients.delay = Integer.parseInt(newDelay)*1000;
                refreshBPTextView.setText("Current Refresh Rate: Every " + SelectPatients.delay/1000 + " seconds.");
                System.out.println("Refresh rate changed. New refresh rate is every " + SelectPatients.delay/1000 + " seconds.");
            }
        }
    }
}
