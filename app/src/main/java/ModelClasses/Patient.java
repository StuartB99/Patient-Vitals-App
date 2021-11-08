package ModelClasses;

import java.io.Serializable;
import java.time.LocalDate;

/**
 *  Abstract Patient class which will serve as a base for any other types of patient. Has just the basic
 *  info of a patient
 */
public abstract class Patient implements Serializable
{
    private String id;
    private String givenName;
    private String familyName;
    private LocalDate birthDate;
    private Gender gender;
    private Address address;

    // Constructor for with all info known
    public Patient(String id, String givenName, String familyName, LocalDate birthDate, Gender gender, Address address)
    {
        this.id = id;
        this.givenName = givenName;
        this.familyName = familyName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
    }

    // Constructor when only id and name is know
    public Patient(String id, String givenName, String familyName){
        this.id = id;
        this.givenName = givenName;
        this.familyName = familyName;
    }

    // getters and setters
    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getGivenName()
    {
        return givenName;
    }

    public void setGivenName(String givenName)
    {
        this.givenName = givenName;
    }

    public String getFamilyName()
    {
        return familyName;
    }

    public void setFamilyName(String familyName)
    {
        this.familyName = familyName;
    }

    public LocalDate getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate)
    {
        this.birthDate = birthDate;
    }

    public Gender getGender()
    {
        return gender;
    }

    public void setGender(Gender gender)
    {
        this.gender = gender;
    }

    public Address getAddress()
    {
        return address;
    }

    public void setAddress(Address address)
    {
        this.address = address;
    }
}
