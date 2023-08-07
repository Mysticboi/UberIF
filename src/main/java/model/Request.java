package model;

import java.awt.*;

/**
 * class Request, a request from a client. Object constituted from
 * the id of the pickup intersection, the id of the delivery intersection,
 * the time needed for picking up and the time needed for delivering.
 * @see Intersection
 */
public class Request {
    private String pickupId;
    private String deliveryId;
    private int pickupDuration;
    private int deliveryDuration;
    // The time the cyclist arrive to this pickup in the tour
    private String pickupTimePassage;
    // The time the cyclist arrive to this delivery in the tour
    private String deliveryTimePassage;

    // Color of the icons in the map
    private Color color;


    /**
     * The constructor of a request object.
     * @param pickupId the id of the pickup address
     * @param deliveryId the id of the delivery address
     * @param pickupDuration the time it takes to pickup in sec
     * @param deliveryDuration the time it takes to deliver in sec
     */
    public Request(String pickupId, String deliveryId, int pickupDuration, int deliveryDuration) {
        this.pickupId = pickupId;
        this.deliveryId = deliveryId;
        this.pickupDuration = pickupDuration;
        this.deliveryDuration = deliveryDuration;
        this.color = null;
    }

    /**
     * Getter of pickupId.
     * @return the id of the pickup address.
     */
    public String getPickupId() {
        return pickupId;
    }

    /**
     * Getter of deliveryId.
     * @return the id of the delivery address.
     */
    public String getDeliveryId() {
        return deliveryId;
    }

    /**
     * Getter of pickupDuration.
     * @return get the time it takes for the pickup.
     */
    public int getPickupDuration() {
        return pickupDuration;
    }

    /**
     * Getter of deliveryDuration.
     * @return get the time it takes for the delivery.
     */
    public int getDeliveryDuration() {
        return deliveryDuration;
    }

    public String toString(){
        return "[id of the pickup: "+this.pickupId+
                " pickup duration: "+this.pickupDuration+"\n"+
                "id of the delivery: "+this.deliveryId+
                " delivery duration: "+this.deliveryDuration+"]";
    }

    public String getPickupTimePassage() {
        return pickupTimePassage;
    }

    public void setPickupTimePassage(String pickupTimePassage) {
        this.pickupTimePassage = pickupTimePassage;
    }

    public String getDeliveryTimePassage() {
        return deliveryTimePassage;
    }

    public void setDeliveryTimePassage(String deliveryTimePassage) {
        this.deliveryTimePassage = deliveryTimePassage;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
