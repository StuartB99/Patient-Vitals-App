package ModelClasses;

import java.io.Serializable;

/**
 * The base class for any type of staff, including health practitioner
 */
public abstract class Staff implements Serializable
{
    private final String identifier;

    public Staff(String identifier)
    {
        this.identifier = identifier;
    }

    public String getIdentifier(){
        return this.identifier;
    }
}
