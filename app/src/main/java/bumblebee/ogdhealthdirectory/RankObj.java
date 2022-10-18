package bumblebee.ogdhealthdirectory;

/**
 * Created by suhassangangire on 18/10/22.
 */

public class RankObj {
    private String name, stars, cleanliness, facilities, doc_behaviour, quality_of_service;

    public String getCleanliness() {
        return cleanliness;
    }

    public String getFacilities() {
        return facilities;
    }

    public String getDoc_behaviour() {
        return doc_behaviour;
    }

    public String getQuality_of_service() {
        return quality_of_service;
    }

    public RankObj(String n, String d, String cleanliness, String facilities, String doc_behaviour, String quality_of_service){
        name = n;
        stars = d;
        this.cleanliness = cleanliness;
        this.facilities = facilities;

        this.doc_behaviour = doc_behaviour;
        this.quality_of_service = quality_of_service;
    }

    public String getName() {
        return name;
    }

    public String getStars() {
        return stars;
    }
}
