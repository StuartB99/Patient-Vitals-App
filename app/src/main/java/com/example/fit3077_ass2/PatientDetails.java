package com.example.fit3077_ass2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

import ModelClasses.Address;
import ModelClasses.MonitoredPatient;
import ModelClasses.Patient;
import ModelClasses.PatientMonitors.BloodPressure;
import ModelClasses.PatientMonitors.Cholesterol;

/**
 * Page that displays the patient's details when more detail is required
 */
public class PatientDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);

        // Getting info from intent
        Intent intent = getIntent();
        Patient patient = (Patient)intent.getSerializableExtra("Patient");
        Cholesterol patientCholesterol = (Cholesterol)intent.getSerializableExtra("Cholesterol");
        String cholesterolDateTime = intent.getStringExtra("CholesterolDateTime");
        if (cholesterolDateTime == null){
            cholesterolDateTime = " - ";
        }
        BloodPressure patientBloodPressure = (BloodPressure) intent.getSerializableExtra("BloodPressure") ;
        String bpDateTime = intent.getStringExtra("BloodPressureDateTime");
        if (bpDateTime == null){
            bpDateTime = " - ";
        }

        MonitoredPatient monitoredPatient = new MonitoredPatient(patient.getId(), patient.getGivenName(), patient.getFamilyName());
        try {
            monitoredPatient.gettingAllInfoOfPatient();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        monitoredPatient.addPatientMonitor(patientCholesterol);
        monitoredPatient.addPatientMonitor(patientBloodPressure);

        // Get the values to be added to the text view
        String gender = monitoredPatient.getGender().toString();
        String birthdate = monitoredPatient.getBirthDate().getDayOfMonth() + "/" + monitoredPatient.getBirthDate().getMonthValue()+"/"+monitoredPatient.getBirthDate().getYear();
        Address address = monitoredPatient.getAddress();
        String cholesterol = " - ";
        if(patientCholesterol != null || patientCholesterol.getCholesterolLevel() != null){
            cholesterol = Double.toString(patientCholesterol.getCholesterolLevel());
        }
        String systolicBP = " - ";
        String diastolicBP = " - ";
        if (patientBloodPressure != null || patientBloodPressure.getSystolicBloodPressure() > 1.00) {
            systolicBP = Double.toString(patientBloodPressure.getSystolicBloodPressure());
            diastolicBP = Double.toString(patientBloodPressure.getDiastolicBloodPressure());
        }

        // Find the text views
        TextView streetAddressTextView = findViewById(R.id.streetAddressTextView);
        TextView suburbCityTextView = findViewById(R.id.suburbCityTextView);
        TextView stateCountryTextView = findViewById(R.id.stateCountryTextView);
        TextView patientNameTextView = findViewById(R.id.patientNameTextView);
        TextView patientCholesterolTextView = findViewById(R.id.totalCholesterolTextView);
        TextView dataTimeCholesterolTextView = findViewById(R.id.dateTimeCholesterolTextView);
        TextView genderTextView = findViewById(R.id.genderTextView);
        TextView birthDateTextView = findViewById(R.id.birthdateTextView);
        TextView systolicBPTextView = findViewById(R.id.textViewSysBP_PatientDetail);
        TextView diastolicBPTextView = findViewById(R.id.textViewDiaBP_PatientDetail);
        TextView dateTimeBPTextView = findViewById(R.id.dateTimeBP_PatientDetail);

        // Set the text views to the values
        patientNameTextView.setText(patient.getGivenName() + " " + patient.getFamilyName());
        patientCholesterolTextView.setText(cholesterol + " mg/dL");
        dataTimeCholesterolTextView.setText(cholesterolDateTime);
        genderTextView.setText(gender);
        birthDateTextView.setText(birthdate);
        streetAddressTextView.setText(address.getStreet());
        suburbCityTextView.setText(address.getCity());
        stateCountryTextView.setText(address.getState() + ", " + address.getCountry());
        systolicBPTextView.setText(systolicBP + " mmHg");
        diastolicBPTextView.setText(diastolicBP + " mmHg");
        dateTimeBPTextView.setText(bpDateTime);
    }
}
