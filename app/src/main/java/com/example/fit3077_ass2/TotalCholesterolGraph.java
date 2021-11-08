package com.example.fit3077_ass2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ModelClasses.MonitoredPatient;
import ModelClasses.Patient;
import ModelClasses.PatientMonitors.Cholesterol;
import ModelClasses.PatientMonitors.PatientMonitor;

/**
 * Activity that shows the cholesterol bar chart
 * This activity is started from the SelectedPatients activity
 * The bar chart is created with the patients with selected checkboxes
 */
public class TotalCholesterolGraph extends AppCompatActivity {

    private ArrayList<Patient> selectedPatients;
    private ArrayList<Patient> barPatients = new ArrayList<>();
    private ArrayList<Cholesterol> monitoredPatients = new ArrayList<>();

    /**
     * Called when the page is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_cholesterol_graph);

        // get patients from intent
        selectedPatients = (ArrayList<Patient>) getIntent().getSerializableExtra("Patients");

        // fill list with cholesterol based on patients
        fillPatientCholesterolList();

        // display and create grapg
        createGraph();

    }

    /**
     * Called in onCreate() to create the bar chart
     * Using MPAndroidChart library for graph
     */
    public void createGraph() {
        BarChart barChart = findViewById(R.id.barChart);

        // create array of entries from bloodPressureValues added to in displayPreviousBloodPressures()
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        double cholesterol;
        for (int i = 0; i < barPatients.size(); i++) {

            cholesterol = monitoredPatients.get(i).getCholesterolLevel();
            int barCholesterol = (int) cholesterol;
            barEntries.add(new BarEntry(i, barCholesterol));
            labels.add(barPatients.get(i).getFamilyName());

        }

        // change x-axis label sizes depending on how many bars in graph
        XAxis xAxis = barChart.getXAxis();
        if (barPatients.size() > 8) {
            xAxis.setTextSize(10);
        } else {
            xAxis.setTextSize(15);
        }

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        xAxis.setCenterAxisLabels(false);
        xAxis.setLabelRotationAngle(-90);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        BarDataSet bardataset = new BarDataSet(barEntries, "Cholesterol");

        // set data of line graph and format
        BarData data = new BarData(bardataset);
        data.setValueTextSize(20);
        barChart.setData(data); // set the data and list of labels into chart
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
        Description description = barChart.getDescription();
        description.setEnabled(false);

        YAxis axisRight = barChart.getAxisRight();
        axisRight.setEnabled(false);

        YAxis axisLeft = barChart.getAxisLeft();
        axisLeft.setTextSize(20);

    }

    /**
     * Fills all the cholesterol details of the selected patients
     */
    public void fillPatientCholesterolList() {
        for (int i = 0; i < selectedPatients.size(); i++) {
            MonitoredPatient monitoredPatient = (MonitoredPatient) selectedPatients.get(i);

            PatientMonitor patientMonitor = new Cholesterol();

            monitoredPatient.addPatientMonitor(patientMonitor);

            try {
                monitoredPatient.getPatientMonitors().get(0).updatePatientMonitorLevel(monitoredPatient.getId());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Cholesterol cholesterol = (Cholesterol) monitoredPatient.getPatientMonitors().get(0);
            if (cholesterol.getCholesterolLevel() != null) {
                monitoredPatients.add(cholesterol);
                barPatients.add(selectedPatients.get(i));
            }

        }

    }

}
