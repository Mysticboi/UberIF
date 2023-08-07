package model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * the class simulating the requests to compute in the tour.
 * @see Request
 */
public class PlanningRequest {
    private String startId;
    private String departureTime;
    private List<Request> requests;
    // Finish time calculated for the tour
    private String finishTime;
    /**
     * the constructor of the object PlanningRequest.
     * @param startId the id of the intersection of the deposit.
     * @param departureTime the time of departure from the deliveryman.
     */
    public PlanningRequest(String startId, String departureTime) {
        this.startId = startId;
        this.departureTime = departureTime;
        requests = new ArrayList<>();
    }

    /**
     * Add a request to the planning.
     * @param request the request added.
     */
    public void addRequest(Request request){
        requests.add(request);
    }

    public String getStartId() {
        return startId;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public String toString(){
        String print = "departure time: "+this.departureTime+"\n"+
                "id of the deposit: "+this.startId+
                "\nrequests : [\n";
        int i = 0;
        for(Request req : requests){
            i++;
            print +="   request "+i+": "+ req.toString()+"\n";
        }
        print +="]";
        return print;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    /**
     * Method that calculate for each request(pickup and delivery) the time of passage of the cyclist
     * @param deliveryTour the computed tour to use for calculation
     */

    public void calculateTimes(DeliveryTour deliveryTour){
        List<Segment> segmentList = deliveryTour.getSegmentList();
        LocalTime currentTime = LocalTime.parse(departureTime);
        // Speed of the cyclist in m/s
        float speed = (float)(15/3.6) ;

        for (Segment segment: segmentList){
            String origin = segment.getOrigin();

            // The origin of the segment is a pickup
            Request requestPick = requests.stream().filter(request->request.getPickupId().equals(origin)).findFirst().orElse(null);
            if(requestPick!=null){
                requestPick.setPickupTimePassage(currentTime.toString());
                currentTime = currentTime.plusSeconds(requestPick.getPickupDuration());
            }

            // The origin of the segment is a delivery
            Request requestDelivery = requests.stream().filter(request->request.getDeliveryId().equals(origin)).findFirst().orElse(null);
            if(requestDelivery!=null){
                requestDelivery.setDeliveryTimePassage(currentTime.toString());
                currentTime = currentTime.plusSeconds(requestDelivery.getDeliveryDuration());
            }


            // Time needed in seconds to go through the segment
            int segmentDuration = (int) (segment.getLength()/speed);
            currentTime = currentTime.plusSeconds(segmentDuration);
        }

        this.finishTime = currentTime.toString();

        // Sort request by pickUpTimePassage
        List<Request> sortedRequests = requests.stream().sorted((request1,request2)->{
            return LocalTime.parse(request1.getPickupTimePassage()).compareTo(LocalTime.parse(request2.getPickupTimePassage()));
        }).collect(Collectors.toList());
        this.requests = sortedRequests;
    }

    public void removeRequest(Request request){
        requests.remove(request);
    }
}
