package util;

import model.Intersection;
import model.PlanningRequest;
import model.Request;
import model.Segment;
import model.graphs.Plan;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class XMLParserTest {

    XMLParser xmlParser = new XMLParser();

    @Test
    public void readMap() {
        Plan plan = null;
        try{
            plan = xmlParser.readMap("files/map.xml");
        }
        catch(Exception e){

        }

        Map<String, Intersection> map = plan.getIntersectionMap();
        assertEquals(2,map.size());
        Intersection intersection = map.get("1");
        assertEquals("1",intersection.getId());
        assertEquals(xmlParser.getMercatorY((float)45.7),intersection.getLatitude(),0.1);
        assertEquals(4.8,intersection.getLongitude(),0.1);


        List<Segment> segments = plan.getSegmentsFromIntersection("1");
        assertEquals(1,segments.size());
        Segment segment = segments.get(0);
        assertEquals("2",segment.getDestination());
        assertEquals(72.60,segment.getLength(),0.1);
        assertEquals("Rue Saint-Victorien",segment.getName());

    }

    @Test
    public void readRequests(){
        PlanningRequest planningRequest;
        try {
            planningRequest = xmlParser.readRequests("files/requestsSmall1.xml",null);
        }
        catch(Exception e){
            planningRequest=null;
        }
        assertEquals("342873658",planningRequest.getStartId());
        assertEquals("08:00:00",planningRequest.getDepartureTime());

        List<Request> requests = planningRequest.getRequests();
        assertEquals(1,requests.size());

        Request request = requests.get(0);
        assertEquals("208769039",request.getPickupId());
        assertEquals("25173820",request.getDeliveryId());
        assertEquals(180,request.getPickupDuration());
        assertEquals(240,request.getDeliveryDuration());

    }

    @Test
    public void readLargeMap(){
        Plan plan = null;
        try{
            plan = xmlParser.readMap("files/largeMap.xml");
        }
        catch(Exception e){

        }
        assertEquals(3736,plan.getIntersectionMap().size());
        plan.getIntersectionMap().forEach((key,value)->{
            assertNotEquals("",key);
            assertNotNull(value);
        });
    }

    @Test
    public void testGetMercatorY() {
        assertEquals((float)0.01570860927, xmlParser.getMercatorY((float)0.9), 0.000001);
    }

    @Test
    public void testBadMap(){
        try{
            xmlParser.readMap("files/requestsSmall1.xml");
        }
        catch(Exception e){
            assertEquals("No intersections found in file",e.getMessage());
        }
    }

    @Test
    public void testBadRequests(){
        try{
            xmlParser.readRequests("files/largeMap.xml",null);
        }
        catch(Exception e){
            assertEquals("Depot not found",e.getMessage());
        }
    }
}