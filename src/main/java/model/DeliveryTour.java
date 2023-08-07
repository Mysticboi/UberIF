package model;

import model.graphs.Graph;
import model.graphs.pathfinding.Edge;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the computed Delivery tour. result of the TSP.
 * @see Segment
 */
public class DeliveryTour {
    List<Segment> segmentList;
    String[] bestTour;

    float globalTime;
    List<String> pointsOfInterest;


    public List<Segment> getSegmentList() {
        return segmentList;
    }

    public void setSegmentList(List<Segment> segmentList) {
        this.segmentList = segmentList;
    }

    public float getGlobalTime() {
        return globalTime;
    }

    /**
     * Constructor of the delivery tour.
     *
     * @param segmentList the list of segments, in order, which compose the tour.
     * @param globalTime  the estimed time for the tour.
     */
    public DeliveryTour(List<Segment> segmentList, float globalTime, String[] bestSol) {
        this.segmentList = segmentList;
        this.globalTime = globalTime;
        pointsOfInterest = new ArrayList<>();
        for(int i=0; i<bestSol.length; i++){
            pointsOfInterest.add(bestSol[i]);
        }
    }

    public List<String> getPointsOfInterest() {
        return pointsOfInterest;
    }

    /**
     * Remove request and updates the tour
     * @param request the request to be removed
     * @param graph the already calculated graph
     */
    public void removeRequestAndChangeTour(Request request, Graph graph) {
        String pickupId = request.getPickupId();
        String deliveryId = request.getDeliveryId();

        pointsOfInterest.remove(pickupId);
        pointsOfInterest.remove(deliveryId);

        List<Segment> segmentList = new ArrayList<>();

        int solutionSize = pointsOfInterest.size();
        for (int i = 1; i < solutionSize; i++) {
            Edge edge = graph.getEdge(pointsOfInterest.get(i - 1), pointsOfInterest.get(i));
            segmentList.addAll(edge.getSegmentList());
        }
        Edge edge = graph.getEdge(pointsOfInterest.get(solutionSize - 1), pointsOfInterest.get(0));
        segmentList.addAll(edge.getSegmentList());

        this.segmentList = segmentList;
    }



}
