package model.graphs;

import model.DeliveryTour;
import model.Intersection;
import model.PlanningRequest;
import model.Segment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class Plan. It represents the plan currently opened by the user.
 * TODO: 25/11/2021 Allow the deliveries and pickup points to be null and adjust later. Fusing it with graph.
 */
public class Plan {
    /**
     *  the intersection map.
     */
    private final Map<String, Intersection> intersectionMap;
    /**
     *  the adjacents map.
     */
    private final Map<String, List<String>> adjacentsMap;
    /**
     *  the segment map, for a pair of intersection give an intersection.
     */
    private final Map<Key, Segment> segmentMap;
    /**
     * the planning request
     */
    private PlanningRequest planningRequest = null;
    /**
     * the currently selected street name
     */
    private String selectedStreetName = "";
    /**
     * the delivery tour
     */
    private DeliveryTour deliveryTour = null;

    private float maxLatitude;
    private float minLatitude;
    private float maxLongitude;
    private float minLongitude;





    /**
     * the constructor of the class Plan.
     * @param intersectionMap where the string is the id of
     *                        the intersection object.
     * @param adjacentsMap where the string is the id of
     *                     an intersection object.
     *                      the list<String> is the list of all the id of
     *                     the adjacent intersections.
     * @param segmentMap where the key is composed of the id of
     *                   origin and the id of destination.
     *                    the segment object is representing the line between
     *                   the two intersections.
     * @see Key
     * @see Intersection
     * @see Segment
     */
    public Plan(final Map<String, Intersection> intersectionMap,
                final Map<String, List<String>> adjacentsMap,
                final Map<Key, Segment> segmentMap) {
        this.intersectionMap = intersectionMap;
        this.adjacentsMap = adjacentsMap;
        this.segmentMap = segmentMap;
    }

    /**
     * @return the adjacents map
     */
    public Map<String, List<String>> getAdjacentsMap() {
        return adjacentsMap;
    }

    public Plan(final Map<String, Intersection> intersectionMap,
                final Map<String, List<String>> adjacentsMap,
                final Map<Key, Segment> segmentMap,
                final float maxLatitude,
                final float minLatitude,
                final float maxLongitude,
                final float minLongitude) {
        this.intersectionMap = intersectionMap;
        this.adjacentsMap = adjacentsMap;
        this.segmentMap = segmentMap;
        this.maxLatitude = maxLatitude;
        this.minLatitude = minLatitude;
        this.maxLongitude = maxLongitude;
        this.minLongitude = minLongitude;
    }

    /**
     * @return the intersections map
     */
    public Map<String, Intersection> getIntersectionMap() {
        return intersectionMap;
    }


    /**
     * @return the segment map
     */
    public Map<Key, Segment> getSegmentMap() {
        return segmentMap;
    }

    /**
     * Add an intersection to the map.
     * @param id the id of the intersection.
     * @param intersection the intersection object representing it.
     * @see Intersection
     */
    public void addIntersection(final String id,
                                final Intersection intersection) {
        intersectionMap.put(id, intersection);
    }

    /**
     * get all the adjacent intersections from an intersection.
     * @param node the intersection.
     * @return a list of adjacent intersections.
     * @see Intersection
     */
    public List<String> getAdjacentsNodes(final String node) {
        return adjacentsMap.get(node);

    }

    /**
     * Get the length of a segment between two intersections.
     * @param origin the intersection at the beginning of the segment.
     * @param destination the intersection at the end of the segment.
     * @return the length of the segment
     * @see Segment
     */
    public float getSegmentWeight(final String origin,
                                  final String destination) {
        Segment segment = segmentMap.get(new Key(origin, destination));
        return segment.getLength();
    }

    public float getMaxLatitude() {
        return maxLatitude;
    }

    public float getMinLatitude() {
        return minLatitude;
    }

    public float getMaxLongitude() {
        return maxLongitude;
    }

    public float getMinLongitude() {
        return minLongitude;
    }

    public PlanningRequest getPlanningRequest() {
        return planningRequest;
    }

    public void setPlanningRequest(final PlanningRequest planningRequest) {
        this.planningRequest = planningRequest;
    }


    public Segment getSegment(final String origin, final String destination) {
        return segmentMap.get(new Key(origin, destination));
    }

    public String getSelectedStreetName() {
        return selectedStreetName;
    }

    public void setSelectedStreetName(final String selectedStreetName) {
        this.selectedStreetName = selectedStreetName;
    }

    public DeliveryTour getDeliveryTour() {
        return deliveryTour;
    }

    public void setDeliveryTour(final DeliveryTour deliveryTour) {
        this.deliveryTour = deliveryTour;
    }

    /**
     * @param origin the concerned vertex
     * @return       the adjacent segments of the vertex
     */
    public List<Segment> getSegmentsFromIntersection(final String origin) {
        List<String> adjacents = adjacentsMap.get(origin);
        List<Segment> segments = new ArrayList<>();
        for (String destination: adjacents) {
            Segment segment = segmentMap.get(new Key(origin, destination));
            segments.add(segment);
        }

        return segments;
    }

    @Override
    public String toString() {
        return "Plan{"
                + "intersectionMap=" + intersectionMap
                + ", adjacentsMap=" + adjacentsMap
                + ", segmentMap=" + segmentMap
                + ", planningRequest=" + planningRequest
                + ", maxLatitude=" + maxLatitude
                + ", minLatitude=" + minLatitude
                + ", maxLongitude=" + maxLongitude
                + ", minLongitude=" + minLongitude
                + '}';
    }

}
