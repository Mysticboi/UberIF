package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class simulating an intersection.
 */
public class Intersection {
    private String id;
    private float latitude;
    private float longitude;


    /**
     * The constructor of copy for the object Intersection.
     * @param intersection the copied intersection.
     */
    public Intersection(Intersection intersection){
        this.id = intersection.id;
        this.latitude = intersection.latitude;
        this.longitude = intersection.longitude;
    }

    /**
     * The constructor of the object Intersection.
     * @param id the id of the intersection.
     * @param latitude the latitude of the intersection.
     * @param longitude the longitude of the intersection.
     */
    public Intersection(String id, float latitude, float longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLongitude() {
        return longitude;
    }


}
