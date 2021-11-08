package ModelClasses;

import org.json.JSONException;
import org.json.JSONObject;

import ModelClasses.PatientMonitors.PatientMonitor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * A type patient who's vitals is constantly being monitored
 */
public class MonitoredPatient extends Patient
{
    private ArrayList<PatientMonitor> patientMonitors = new ArrayList<PatientMonitor>();    // A list to hold all the monitors on the patient

    // Constructor for when the patient monitors are already available
    public MonitoredPatient(String id, String givenName, String familyName, LocalDate birthDate, Gender gender, Address address, ArrayList<PatientMonitor> patientMonitors)
    {
        super(id, givenName, familyName, birthDate, gender, address);
        this.patientMonitors =  patientMonitors;
    }

    // Constructor without patientMonitors known yet
    public MonitoredPatient(String id, String givenName, String familyName, LocalDate birthDate, Gender gender, Address address) {
        super(id, givenName, familyName, birthDate, gender, address);
    }

    // Constructor with only id and name of monitored patient known
    public MonitoredPatient(String id, String givenName, String familyName)
    {
        super(id, givenName, familyName);
    }

    /**
     * Called on the monitored patient to get all the personal info from the server.
     * @throws JSONException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void gettingAllInfoOfPatient() throws JSONException, ExecutionException, InterruptedException {
        String patientUrl = FetchJsonData.rootUrl + "Patient/" +this.getId();
        FetchJsonData fetchPatientData = new FetchJsonData();
        JSONObject jsonPatient = (JSONObject) fetchPatientData.execute(patientUrl).get();

        this.addMonitoredPatientInfoFromJson(jsonPatient);
    }

    /**
     * Take the json object from the server and convert it to individual attributes of the monitored patient
     * @param patient json object of the patient from the server
     * @throws JSONException
     */
    private void addMonitoredPatientInfoFromJson(JSONObject patient) throws JSONException {
        String jsonPatientGender = patient.getString("gender");    // String: male
        Gender patientGender = Gender.OTHERS;
        if (jsonPatientGender.equals("male")){
            patientGender = Gender.MALE;
        }
        else if (jsonPatientGender.equals("female")){
            patientGender = Gender.FEMALE;
        }

        // Getting the birthdate of the patient
        String jsonPatientBirthDate = patient.get("birthDate").toString();  // String: 1973-09-30
        Integer patientBirthYear = Integer.parseInt(jsonPatientBirthDate.substring(0,4));
        Integer patientBirthMonth = Integer.parseInt(jsonPatientBirthDate.substring(5,7));
        Integer patientBirthDay = Integer.parseInt(jsonPatientBirthDate.substring(8));
        LocalDate patientBirthDate = LocalDate.of(patientBirthYear, patientBirthMonth, patientBirthDay);

        // Getting the address
        JSONObject jsonPatientAddress = patient.getJSONArray("address").getJSONObject(0);
        String patientAddressStreet = jsonPatientAddress.getJSONArray("line").get(0).toString();
        String patientAddressCity = jsonPatientAddress.get("city").toString();
        String patientAddressState = jsonPatientAddress.get("state").toString();
        String patientAddressCountry = jsonPatientAddress.get("country").toString();
        Address patientAddress = new Address(patientAddressStreet, patientAddressCity, patientAddressState, patientAddressCountry);

        // Set all the attributes for the patient
        this.setBirthDate(patientBirthDate);
        this.setGender(patientGender);
        this.setAddress(patientAddress);
    }

    // Add a monitor to the patient's monitor list
    public void addPatientMonitor(PatientMonitor patientMonitor){
        this.patientMonitors.add(patientMonitor);
    }

    // Remove a monitor from the patient's monitor list
    public void removePatientMonitor(PatientMonitor patientMonitor){
        this.patientMonitors.remove(patientMonitor);
    }

    // Getters and setters
    public ArrayList<PatientMonitor> getPatientMonitors()
    {
        return patientMonitors;
    }

    public void setPatientMonitors(ArrayList<PatientMonitor> patientMonitors)
    {
        this.patientMonitors = patientMonitors;
    }
}
