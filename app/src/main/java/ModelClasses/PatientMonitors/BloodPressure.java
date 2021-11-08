package ModelClasses.PatientMonitors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ModelClasses.FetchJsonData;

/**
 * BloodPressure class which is a type of PatientMonitor of a patient
 */
public class BloodPressure extends PatientMonitor implements Serializable {
    private Double systolicBloodPressure = -1.00;
    private Double diastolicBloodPressure = -1.00;
    private ArrayList<BloodPressure> bloodPressureHistory = new ArrayList<BloodPressure>();

    // Basic Constructor
    public BloodPressure(String effectiveDateTime, Double systolicBloodPressure, Double diastolicBloodPressure, ArrayList<BloodPressure> previousBloodPressureWithTimeAndSystolic) {
        super(effectiveDateTime);
        this.systolicBloodPressure = systolicBloodPressure;
        this.diastolicBloodPressure = diastolicBloodPressure;
        this.bloodPressureHistory = previousBloodPressureWithTimeAndSystolic;
    }

    // Constructor with just systolic blood pressure and effectivedatetiem
    public BloodPressure(String effectiveDateTime, Double systolicBloodPressure) {
        super(effectiveDateTime);
        this.systolicBloodPressure = systolicBloodPressure;
    }

    /**
     * Empty constructor which can have its value filled by calling updatePatientMonitorLevel. Used
     * when blood pressure and datetime is unknown
     */
    public BloodPressure() {}

    @Override
    public boolean updatePatientMonitorLevel(String patientId) throws ExecutionException, InterruptedException, JSONException {
        // Url of observations(Cholesterol) with the most recent one shown first
        String observationUrl = FetchJsonData.rootUrl + "Observation?_count=5&_sort=-date&code=55284-4&patient="+ patientId;
        System.out.println("url");
        // Fetch the json data from the server
        FetchJsonData fetchJsonObservations = new FetchJsonData();
        JSONObject jsonObservations = (JSONObject) fetchJsonObservations.execute(observationUrl).get();
        int totalObservation = jsonObservations.getInt("total");
        // If there are no Blood Pressure readings
        if(totalObservation == 0){
            return false;
        }

        // Get the most recent observation
        JSONArray jsonObservationsArray = jsonObservations.getJSONArray("entry");
        JSONObject mostRecentObservation = jsonObservationsArray.getJSONObject(0).getJSONObject("resource");

        // Get the effectiveDateTime and diastolic/systolic blood pressurees
        String effectiveDateTime = mostRecentObservation.getString("effectiveDateTime");
        Double diastolicPressureLevel = mostRecentObservation.getJSONArray("component").getJSONObject(0).getJSONObject("valueQuantity").getDouble("value");
        Double systolicPressureLevel = mostRecentObservation.getJSONArray("component").getJSONObject(1).getJSONObject("valueQuantity").getDouble("value");

        // Set the values to the class
        setEffectiveDateTime(effectiveDateTime);
        setDiastolicBloodPressure(diastolicPressureLevel);
        setSystolicBloodPressure(systolicPressureLevel);

        setBloodPressureHistory(new ArrayList<BloodPressure>());

        if(totalObservation <= 5){      // Take all the values if the total observations is less than 5
            for(int i = 1 ; i < jsonObservationsArray.length() ; i++){
                JSONObject currentObservation = jsonObservationsArray.getJSONObject(i).getJSONObject("resource");
                effectiveDateTime = currentObservation.getString("effectiveDateTime");
                systolicPressureLevel = currentObservation.getJSONArray("component").getJSONObject(1).getJSONObject("valueQuantity").getDouble("value");
                BloodPressure onlySystolic = new BloodPressure(effectiveDateTime, systolicPressureLevel);
                bloodPressureHistory.add(onlySystolic);
            }
        }
        else{
            for(int i = 1 ; i < 5 ; i++){   // Take only 4 observations, if total observation more than 5
                JSONObject currentObservation = jsonObservationsArray.getJSONObject(i).getJSONObject("resource");
                effectiveDateTime = currentObservation.getString("effectiveDateTime");
                systolicPressureLevel = currentObservation.getJSONArray("component").getJSONObject(1).getJSONObject("valueQuantity").getDouble("value");
                BloodPressure onlySystolic = new BloodPressure(effectiveDateTime, systolicPressureLevel);
                bloodPressureHistory.add(onlySystolic);
            }
        }

        return true;
    }

    // Getters and setters for the attributes
    public double getSystolicBloodPressure() {
        return systolicBloodPressure;
    }

    public void setSystolicBloodPressure(double systolicBloodPressure) {
        this.systolicBloodPressure = systolicBloodPressure;
    }

    public double getDiastolicBloodPressure() {
        return diastolicBloodPressure;
    }

    public void setDiastolicBloodPressure(double diastolicBloodPressure) {
        this.diastolicBloodPressure = diastolicBloodPressure;
    }

    public ArrayList<BloodPressure> getBloodPressureHistory() {
        return bloodPressureHistory;
    }

    public void setBloodPressureHistory(ArrayList<BloodPressure> bloodPressureHistory) {
        this.bloodPressureHistory = bloodPressureHistory;
    }
}
