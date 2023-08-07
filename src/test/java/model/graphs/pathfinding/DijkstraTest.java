package model.graphs.pathfinding;

import model.PlanningRequest;
import model.Request;
import model.graphs.Graph;
import model.graphs.Plan;
import model.graphs.Key;
import model.Intersection;
import model.Segment;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class DijkstraTest {
    Dijkstra dijkstra;

    Map<String, List<String>> adjacentsMap = new HashMap<>();
    Map<Key, Segment> segmentMap = new HashMap<>();
    Plan plan;

    private void connectNodes(String i1, String i2, float length){
        Segment segment = new Segment(i1, i2,length, i1+i2);
        List<String> adj;
        if(adjacentsMap.containsKey(i1)){
            adj = adjacentsMap.get(i1);

        }
        else{
            adj = new ArrayList<String>();

        }
        adj.add(i2);
        adjacentsMap.put(i1,adj);
        segmentMap.put(new Key(i1, i2), segment);
    }

    @Before
    public void setUp() throws Exception {
        dijkstra = new Dijkstra();
        Intersection i1 = new Intersection("1",0,0);
        Intersection i2 = new Intersection("2",0,0);
        Intersection i3 = new Intersection("3",0,0);
        Intersection i4 = new Intersection("4",0,0);
        Intersection i5 = new Intersection("5",0,0);
        Intersection i6 = new Intersection("6",0,0);
        Intersection i7 = new Intersection("7",0,0);


        Map<String, Intersection> intersectionMap = new HashMap<>();
        intersectionMap.put("1",i1);
        intersectionMap.put("2",i2);
        intersectionMap.put("3",i3);
        intersectionMap.put("4",i4);
        intersectionMap.put("5",i5);
        intersectionMap.put("6",i6);
        intersectionMap.put("7",i7);

        connectNodes("1","2",3);
        connectNodes("2","1",1);

        connectNodes("1","3",2);
        connectNodes("3","1",2.5f);

        connectNodes("2","3",4);
        connectNodes("3","2",4);

        connectNodes("2","4",2);
        connectNodes("4","2",3);

        connectNodes("3","4",1.5f);

        connectNodes("3","5",3);
        connectNodes("5","3",5);

        connectNodes("4","5",1);
        connectNodes("5","4",2);

        connectNodes("4","6",2);
        connectNodes("6","4",1);

        connectNodes("5","6",7);
        connectNodes("6","5",2);

        connectNodes("6","7",3);
        connectNodes("7","6",6);





        List<String> deliveries = new ArrayList<>();
        deliveries.add("4");
        deliveries.add("6");
        List<String> pickups =  new ArrayList<>();
        pickups.add("2");
        pickups.add("7");
        PlanningRequest planningRequest = new PlanningRequest("1","2:0:0");
        planningRequest.addRequest(new Request("2","4",5,4));
        planningRequest.addRequest(new Request("7","6",5,4));
        plan = new Plan(intersectionMap,adjacentsMap,segmentMap,0,0,0,0);
        plan.setPlanningRequest(planningRequest);
        //graph = new Plan(intersectionMap,adjacentsMap,segmentMap,0,0);

    }

    @Test
    public void getLowestDistanceNode() {
    }

    @Test
    public void calculateMinimumDistance() {
    }


    @Test
    public void executeAlgorithm() {


        Dijkstra dijkstra = new Dijkstra();
        List<String> pointsOfInterests = new ArrayList<>();

        PlanningRequest planningRequest = plan.getPlanningRequest();
        pointsOfInterests.add(planningRequest.getStartId());
        for (Request request: planningRequest.getRequests()) {
            pointsOfInterests.add(request.getDeliveryId());
            pointsOfInterests.add(request.getPickupId());
        }

        Graph newGraph = new Graph();


        for (String currentPoint:pointsOfInterests) {


            try {
                dijkstra.executeAlgorithm(plan,currentPoint,newGraph,pointsOfInterests );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //dijkstra.executeAlgorithm(plan,planningRequest.getStartId(),newGraph,pointsOfInterests );


        Set<String> correctValues = new HashSet<>(Arrays.asList("1", "2", "4","6","7"));
        System.out.println(newGraph.getEdges());
        assert(newGraph.getNbVertices() == 5);

        assert(newGraph.getNbEdges() == 20);
        assert(Objects.equals(newGraph.getVertices(), correctValues));


    }
}