package ModelClasses.PatientMonitors;

import org.json.JSONException;
import java.util.concurrent.ExecutionException;

/**
 * Interface that ensures that a particular monitor is always updatable
 */
public interface UpdatablePatientMonitor
{
    boolean updatePatientMonitorLevel(String patientId) throws ExecutionException, InterruptedException, JSONException;
}
