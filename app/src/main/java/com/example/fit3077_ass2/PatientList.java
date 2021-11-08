package com.example.fit3077_ass2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ModelClasses.HealthPractitioner;
import ModelClasses.MonitoredPatient;
import ModelClasses.Patient;
import ModelClasses.PatientMonitors.BloodPressure;
import ModelClasses.PatientMonitors.Cholesterol;
import ModelClasses.PatientMonitors.PatientMonitor;

/**
 * Page of the patients that can be selected and the monitors to be had
 */
public class PatientList extends AppCompatActivity {

    public static ArrayList<Patient> selectedPatients = new ArrayList<>();

    /**
     * Method called when the page is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get list of patients from health practitioner
        HealthPractitioner healthPractitioner = (HealthPractitioner) getIntent().getSerializableExtra("HealthPractitioner");
        ArrayList<Patient> patientList = healthPractitioner.getPatients();

        // only populate list with patients that have a monitor value
        ArrayList<Patient> patientListWithMonitors = new ArrayList<>();
        for (int patient = 0; patient < patientList.size(); patient++) {
            MonitoredPatient monitoredPatient = new MonitoredPatient(patientList.get(patient).getId(), patientList.get(patient).getGivenName(), patientList.get(patient).getFamilyName());

            PatientMonitor patientMonitor1 = new Cholesterol();
            PatientMonitor patientMonitor2 = new BloodPressure();
            monitoredPatient.addPatientMonitor(patientMonitor1);
            monitoredPatient.addPatientMonitor(patientMonitor2);

            // update the values for all the monitors of the patient
            for (int monitor = 0 ; monitor < monitoredPatient.getPatientMonitors().size() ; monitor++){
                try {
                    monitoredPatient.getPatientMonitors().get(monitor).updatePatientMonitorLevel(monitoredPatient.getId());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // if patients have effectivedatetime for either monitors, add them to list to give to adapter
            for (int monitor = 0 ; monitor < monitoredPatient.getPatientMonitors().size() ; monitor++ ){
                if (monitoredPatient.getPatientMonitors().get(monitor).getEffectiveDateTime() != null) {
                    patientListWithMonitors.add(patientList.get(patient));
                    break;
                }
            }
        }

        setContentView(R.layout.activity_patient_list);

        RecyclerView patientRecyclerView = findViewById(R.id.patientRecyclerView);

        PatientViewer patientAdapter = new PatientViewer(this, patientListWithMonitors, selectedPatients);
        patientRecyclerView.setAdapter(patientAdapter);
        patientRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * start next activity with chosen patients. called when the select patient button is pressed. Takes the provided
     * high systolic and diastolic blood pressure with it.
      */
    public void selectPatients(View view) {
        Intent intent = new Intent(this, SelectPatients.class);
        // get the high systolic and diastolic blood pressure and send it over the intent
        EditText highSystolicBPEditText = findViewById(R.id.editTextHighSysBP);
        EditText highDiastolicBPEditText = findViewById(R.id.editTextHighDiaBP);

        Double highSystolicBP = 140.00;
        if (!highSystolicBPEditText.getText().toString().equals("")){
            highSystolicBP = Double.parseDouble(highSystolicBPEditText.getText().toString());
        }
        Double highDiastolicBP = 90.00;
        if (!highDiastolicBPEditText.getText().toString().equals("")){
            highDiastolicBP = Double.parseDouble(highDiastolicBPEditText.getText().toString());
        }

        intent.putExtra("HighSystolicBP", highSystolicBP);
        intent.putExtra("HighDiastolicBP", highDiastolicBP);

        // Get the selected display format
        RadioGroup radioGroup = findViewById(R.id.radioGroupObservationDisplayed);
        int selectedDisplayFormat = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedDisplayFormat);
        intent.putExtra("displayFormat", radioButton.getText());
        // start the intent
        startActivity(intent);

    }
}
