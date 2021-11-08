package com.example.fit3077_ass2;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import ModelClasses.Patient;
import ModelClasses.PatientMonitors.BloodPressure;

/**
 * Activity that shows the systolic blood pressure line graph
 * This activity is started whenever a patient is chosen from the HighBloodPressureChanges activity
 */
public class SystolicBloodPressureGraph extends AppCompatActivity {

    private BloodPressure bloodPressure;
    private ArrayList<Double> bloodPressureValues = new ArrayList<>();

    /**
     * Called when the page is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systolic_blood_pressure_graph);

        // get intent from previous activity for patient details
        bloodPressure = (BloodPressure)getIntent().getSerializableExtra("BloodPressure");
        Patient patient = (Patient) getIntent().getSerializableExtra("Patient");

        // fill blood pressure values list
        displayPreviousBloodPressures();

        // create and display graph
        createGraph();

        TextView patientName = findViewById(R.id.systolicPatientNameTextView);

        patientName.setText(patient.getGivenName() + " " + patient.getFamilyName());

    }

    /**
     * Called in onCreate() to create the line graph
     * Using MPAndroidChart library for graph
     */
    public void createGraph() {
        LineChart lineChart = findViewById(R.id.lineChart);

        // create array of entries from bloodPressureValues added to in displayPreviousBloodPressures()
        ArrayList<Entry> values = new ArrayList<>();
        double bloodPressure;
        for (int i = 0; i < bloodPressureValues.size(); i++) {
            bloodPressure = bloodPressureValues.get(i);
            int barBloodPressure = (int) bloodPressure;
            values.add(new Entry(i, barBloodPressure));
        }

        // set data of line graph and format
        LineDataSet lineDataSet = new LineDataSet(values, "This is y axis");
        lineDataSet.setLineWidth(5);
        lineDataSet.setColor(Color.CYAN);

        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);
        Description description = lineChart.getDescription();
        description.setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(20);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTextSize(20);

        ArrayList<ILineDataSet> iLineDataSetArrayList =  new ArrayList<>();
        iLineDataSetArrayList.add(lineDataSet);

        LineData lineData = new LineData(iLineDataSetArrayList);

        lineChart.setData(lineData);
        lineData.setValueTextSize(20);
        lineChart.invalidate();

    }

    /**
     * Display the previous blood pressures of patients with high systolic blood pressure on the UI
     */
    private void displayPreviousBloodPressures(){
            for (int j = 0; j < bloodPressure.getBloodPressureHistory().size() ; j++){
                System.out.println(bloodPressure.getBloodPressureHistory().get(j).getSystolicBloodPressure());
                bloodPressureValues.add(bloodPressure.getBloodPressureHistory().get(j).getSystolicBloodPressure());
        }
    }

}
