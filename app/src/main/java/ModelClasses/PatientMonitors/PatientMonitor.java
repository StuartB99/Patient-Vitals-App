package ModelClasses.PatientMonitors;

/**
 * Base class for any monitors for a patient. Is always updatable with the latest info.
 */
public abstract class PatientMonitor implements UpdatablePatientMonitor{
    private String effectiveDateTime;

    // Constructors
    public PatientMonitor(String effectiveDateTime) {
        this.effectiveDateTime = effectiveDateTime;
    }

    protected PatientMonitor() {
    }

    //Getter and Setter
    public String getEffectiveDateTime() {
        return effectiveDateTime;
    }

    public void setEffectiveDateTime(String effectiveDateTime) {
        this.effectiveDateTime = effectiveDateTime;
    }
}
