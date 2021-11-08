package ModelClasses.PatientMonitors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;

import ModelClasses.FetchJsonData;

/**
 * The Cholesterol which is a type of Patientmonitor
 */
public class Cholesterol extends PatientMonitor implements Serializable
{
    private Double cholesterolLevel;

    // Basic constructor
    public Cholesterol(Double cholesterolLevel, String effectiveDateTime)
    {
        super(effectiveDateTime);
        this.cholesterolLevel = cholesterolLevel;
    }

    /**
     * Empty constructor which can have its value filled by calling updatePatientMonitorLevel. Used
     * when cholesterol and datetime is unknown
     */
    public Cholesterol(){}

    /**
     * Updates the patient's monitor level, which is cholesterol here, whenever called
     * @param patientId: the patient's identifier
     * @return true if successful, false if not
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws JSONException
     */
    @Override
    public boolean updatePatientMonitorLevel(String patientId) throws ExecutionException, InterruptedException, JSONException {
        // Url of observations(Cholesterol) with the most recent one shown first
        String observationUrl = FetchJsonData.rootUrl + "Observation?_count=5&_sort=-date&code=2093-3&patient="+ patientId;
        // Fetch the json data from the server
        FetchJsonData fetchJsonObservations = new FetchJsonData();
        JSONObject jsonObservations = (JSONObject) fetchJsonObservations.execute(observationUrl).get();

        // If there are no Cholesterol readings
        if(jsonObservations.getInt("total") == 0){
            return false;
        }

        // Get the most recent observation
        JSONArray jsonObservationsArray = jsonObservations.getJSONArray("entry");
        JSONObject mostRecentObservation = jsonObservationsArray.getJSONObject(0).getJSONObject("resource");

        // Get the effectiveDateTime and cholesterolLevel
        String effectiveDateTime = mostRecentObservation.getString("effectiveDateTime");
        Double cholesterolLevel = mostRecentObservation.getJSONObject("valueQuantity").getDouble("value");

        setCholesterolLevel(cholesterolLevel);
        setEffectiveDateTime(effectiveDateTime);
        return true;
    }

    // Getters and setters
    public Double getCholesterolLevel()
    {
        return this.cholesterolLevel;
    }

    public void setCholesterolLevel(Double cholesterolLevel)
    {
        this.cholesterolLevel = cholesterolLevel;
    }

}
