package model;

import model.graphs.Graph;
import model.graphs.Plan;
import model.graphs.pathfinding.TSP;
import model.graphs.pathfinding.BranchAndBound;
import org.junit.Test;
import util.XMLParser;

import java.time.LocalTime;
import java.util.List;

import static org.junit.Assert.*;

public class PlanningRequestTest{
    XMLParser xmlParser = new XMLParser();
    Plan plan = readMap("files/largeMap.xml");
    DeliveryTour deliveryTour;

    private Plan readMap(String mapPath){
        Plan plan;
        try{
            plan = xmlParser.readMap(mapPath);
        }
        catch(Exception e){
            plan = null;
        }
        return plan;
    }
    private void setUp(String requestPath){
        PlanningRequest planningRequest;
        try {
            planningRequest = xmlParser.readRequests(requestPath,null);
        }
        catch(Exception e){
            planningRequest =null;
        }

        try {
            plan.setPlanningRequest(planningRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Calling TSP to calculate the best tour
        TSP tsp = new BranchAndBound();
        Graph graph = Graph.generateCompleteGraphFromPlan(plan);
        tsp.searchSolution(20000,graph,planningRequest);
        deliveryTour = tsp.getDeliveryTour();
    }

    @Test
    public void calculateTimes() {
        setUp("files/requestsSmall1.xml");
        PlanningRequest planningRequest = plan.getPlanningRequest();
        planningRequest.calculateTimes(deliveryTour);

        Request request = planningRequest.getRequests().get(0);
        assertEquals("08:01:22",request.getPickupTimePassage());
        assertEquals("08:13:38",request.getDeliveryTimePassage());

        assertEquals("08:28:16",planningRequest.getFinishTime());
    }

    @Test
    public void calculateTimesWellSorted(){
        setUp("files/requestsMedium5.xml");
        PlanningRequest planningRequest  = plan.getPlanningRequest();
        planningRequest.calculateTimes(deliveryTour);

        List<Request> requests = planningRequest.getRequests();
        assertEquals(5,requests.size());

        for(int i =0; i< requests.size()-1; i++){
            LocalTime pickUpTimePassage1 = LocalTime.parse(requests.get(i).getPickupTimePassage());
            LocalTime pickUpTimePassage2 = LocalTime.parse(requests.get(i+1).getPickupTimePassage());

            // Using compareTo method of LocalTime: Returns 1 when pickUpTimePassage2 is superior
            assertEquals(1,pickUpTimePassage2.compareTo(pickUpTimePassage1));
        }
    }
}