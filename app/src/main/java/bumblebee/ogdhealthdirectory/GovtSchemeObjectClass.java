package bumblebee.ogdhealthdirectory;

/**
 * Created by anuragsharma on 07/10/17.
 */

public class GovtSchemeObjectClass {

    private String name, details;

    public GovtSchemeObjectClass(String n, String d){
        name = n;
        details = d;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }
}
