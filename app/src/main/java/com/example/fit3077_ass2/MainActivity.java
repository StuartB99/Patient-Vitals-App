package com.example.fit3077_ass2;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ModelClasses.HealthPractitioner;
import ModelClasses.Patient;

/**
 * First page of the application
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Called whenever the page is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Function called whenever confirm button on the first page is pressed
     * @param view
     * @throws JSONException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void toPatientList(View view) throws JSONException, ExecutionException, InterruptedException {
        // get practitionerId string from edit text
        // create a health practitioner instance using this ID
        EditText practitionerEditText = findViewById(R.id.practitionerEditText);
        String practitionerId = practitionerEditText.getText().toString();
        HealthPractitioner practitioner = new HealthPractitioner(practitionerId);

        // create list of patients based on practitioners ID
        ArrayList<Patient> patientList = practitioner.findPatientsOfPractitionerFromURL(practitionerId);

        // if the practitioner does not have any patients, do not move to the next screen
        // if they do, pass through the practitioner ID and the patient list
        if (patientList.size() == 0) {
            Toast toast = Toast.makeText(this, "You do not have any patients.", Toast.LENGTH_LONG);
            toast.show();
        } else {
            System.out.println(patientList.size());
            Intent intent = new Intent(this, PatientList.class);
            intent.putExtra("HealthPractitioner", practitioner);
            startActivity(intent);
        }
    }
}
