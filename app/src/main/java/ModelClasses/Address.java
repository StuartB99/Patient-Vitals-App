package ModelClasses;

/**
 * Address which is decomposed to street, city, state, postcode and country. A POJO.
 */
public class Address
{
    private String street;
    private String city;
    private String state;
    private String postcode;
    private String country;

    // constructor with postcode
    public Address(String street, String city, String state, String postcode, String country)
    {
        this.street = street;
        this.city = city;
        this.state = state;
        this.postcode = postcode;
        this.country = country;
    }

    // constructor without postcode
    public Address(String street, String city, String state, String country) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    // Getters and Setters
    public String getStreet()
    {
        return street;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getPostcode()
    {
        return postcode;
    }

    public void setPostcode(String postcode)
    {
        this.postcode = postcode;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }
}
