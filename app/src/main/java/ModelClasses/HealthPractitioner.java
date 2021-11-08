package ModelClasses;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Health practitioner who will sign in and check up on their respective patients
 */
public class HealthPractitioner extends Staff
{
    private ArrayList<Patient> patients = new ArrayList<Patient>(); // List of patients under the health practitioner

    public HealthPractitioner(String id)
    {
        super(id);
    }

    public HealthPractitioner(String id, ArrayList<Patient> patients)
    {
        super(id);
        this.patients = patients;
    }

    /**
     * Finds all the patients from the server that are under the care of the health practitioner
     * @param practitionerIdentifier the identifier of the patient
     * @return a list of all the patients
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws JSONException
     */
    public ArrayList<Patient> findPatientsOfPractitionerFromURL(String practitionerIdentifier) throws ExecutionException, InterruptedException, JSONException {
        ArrayList<String> jsonPatientsId = new ArrayList<>();
        ArrayList<Patient> patients = new ArrayList<>();
        String encountersUrl = FetchJsonData.rootUrl + "Encounter?participant.identifier=http://hl7.org/fhir/sid/us-npi%7C" + practitionerIdentifier + "&_include=Encounter.participant.individual&_include=Encounter.patient&_count=200";

        // Fetch the data from the server
        FetchJsonData fetchPractitionerPatientsData = new FetchJsonData();
        JSONObject jsonEncounters = (JSONObject) fetchPractitionerPatientsData.execute(encountersUrl).get();

        // ensure they are patients, return early with empty list if there are no patients
        try{
            if(jsonEncounters.getInt("total") <= 0){    // return empty array list if practitioner has no patients
                return patients;
            }
        } catch (JSONException e) {
            ;
        }

        JSONArray jsonEncountersArray = jsonEncounters.getJSONArray("entry");

        for(int i = 0 ; i < jsonEncountersArray.length() ; i++){    //looping through and creating new patient objects for new patients found
            JSONObject jsonEncounter = jsonEncountersArray.getJSONObject(i).getJSONObject("resource");
            String patientId = jsonEncounter.getJSONObject("subject").getString("reference").split("/")[1];

            if(!jsonPatientsId.contains(patientId)){    // ensures that there are only unique patients
                jsonPatientsId.add(patientId);

                String jsonTitleAndName = jsonEncounter.getJSONObject("subject").getString("display");
                String[] titleAndName = jsonTitleAndName.split(" ");
                String givenName = titleAndName[0];
                String familyName = titleAndName[1];
                if(titleAndName.length > 2){
                    givenName = titleAndName[1];
                    familyName = titleAndName[2];
                }

                patients.add(new MonitoredPatient(patientId, givenName, familyName));
            }
        }
        this.setPatients(patients);

        return patients;
    }

    // Add a patient to the list of the practitioner
    public boolean addPatient(Patient patient){
        return this.patients.add(patient);
    }

    // Remove a patient from the list of the practitioner
    public boolean removePatient(Patient patient){
        return this.patients.remove(patient);
    }

    // Getters and setters
    public void setPatients(ArrayList<Patient> patients){
        this.patients = patients;
    }

    public ArrayList getPatients(){return this.patients;}
}
