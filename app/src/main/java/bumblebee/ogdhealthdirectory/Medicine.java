package bumblebee.ogdhealthdirectory;

/**
 * Created by suhassangangire on 18/10/22.
 */

public class Medicine {

    private String name, detailsHindi, detailsEnglish;

    public Medicine(String arr[]){
        name = arr[1];
        detailsEnglish = arr[2];
        detailsHindi = arr[3];

    }

    public String getName() {
        return name;
    }

    public String getDetailsHindi() {
        return detailsHindi;
    }

    public String getDetailsEnglish() {
        return detailsEnglish;
    }

}
