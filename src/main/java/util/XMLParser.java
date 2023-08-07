package util;

import model.Intersection;
import model.PlanningRequest;
import model.Request;
import model.Segment;
import model.graphs.Key;
import model.graphs.Plan;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class XMLParser: parse xml files and translate the content in Plan and PlanningRequest object.
 * @// TODO: 25/11/2021 transform it into a static class.
 * @see model.graphs.Plan
 * @see model.PlanningRequest
 */
public class XMLParser {

    /**
     * Parse the document into a DOM document
     * @param filePath the filepath of the file to parse
     * @return
     */
    private Document parseXMLFile(String filePath) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            // optional, but recommended
            // process XML securely, avoid attacks like XML External Entities (XXE)
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(new File(filePath));

            // optional, but recommended
            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            return doc;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String formatDepartureTime(String departureTime){
        return "0" + departureTime.replaceAll(":",":0");
    }

    public float getMercatorY(float latitude){
        // convert from degrees to radians
        float latRad = (float) ((latitude*Math.PI)/180);

        // get y value
        return (float) Math.log(Math.tan((Math.PI/4)+(latRad/2)));
    }

    /** Read a map xml file composed of intersections and segments.
     *
     * @param filePath the path of the file to read
     * @return the graph resulting of the parsing. (to change to Plan)
     */
    public Plan readMap(String filePath) throws Exception {
        if(!filePath.contains(".xml")){
            throw new Exception("Please only select .xml files");
        }
        Document doc = parseXMLFile(filePath);
        Map<String, Intersection> intersectionMap = new HashMap<>();
        Map<String, List<String>> adjacentsMap = new HashMap<>();
        Map<Key,Segment> segmentMap = new HashMap<>();

        float maxLatitude = Integer.MIN_VALUE;
        float minLatitude = Integer.MAX_VALUE;
        float maxLongitude = Integer.MIN_VALUE;
        float minLongitude = Integer.MAX_VALUE;

        // Get all intersections
        NodeList intersections = doc.getElementsByTagName("intersection");
        if(intersections.getLength() ==0){
            throw new Exception("No intersections found in file");
        }
        for (int i = 0; i < intersections.getLength(); i++) {
            Node node = intersections.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String id = element.getAttribute("id");
                String latitudeString = element.getAttribute("latitude");
                String longitudeString = element.getAttribute("longitude");

                // If we lack important data we ignore the intersection
                if(id.isEmpty() || latitudeString.isEmpty() || longitudeString.isEmpty()){
                    continue;
                }
                float latitude = getMercatorY(Float.parseFloat(latitudeString));
                float longitude = Float.parseFloat(longitudeString);


                maxLatitude = Math.max(maxLatitude,latitude);
                minLatitude = Math.min(minLatitude,latitude);
                maxLongitude= Math.max(maxLongitude,longitude);
                minLongitude= Math.min(minLongitude,longitude);

                Intersection intersection = new Intersection(id, latitude, longitude);
                intersectionMap.put(id, intersection);
                adjacentsMap.put(id,new ArrayList<>());
            }
        }

        if(intersectionMap.isEmpty()){
            throw new Exception ("All intersections in the file lack important data");
        }

        // Get all segments
        NodeList segments = doc.getElementsByTagName("segment");
        if(segments.getLength()==0){
            throw new Exception("No segments found in file");
        }

        for (int i = 0; i < segments.getLength(); i++) {
            Node node = segments.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String origin = element.getAttribute("origin");
                String destination = element.getAttribute("destination");
                String lengthString = element.getAttribute("length");
                String name = element.getAttribute("name").isEmpty() ? "Unspecified" : element.getAttribute("name");

                // If we lack important data we ignore the segment
                if(origin.isEmpty() || destination.isEmpty() || lengthString.isEmpty()){
                    continue;
                }

                float length = Float.parseFloat(lengthString);
                Segment segment = new Segment(origin,destination, length, name);
                adjacentsMap.get(origin).add(destination);

                Key key = new Key(origin,destination);
                segmentMap.put(key,segment);
            }

        }

        if(segmentMap.isEmpty()){
            throw new Exception("All segments in the file lack important data");
        }
        
        Plan plan = new Plan(intersectionMap,adjacentsMap,segmentMap,maxLatitude,minLatitude,maxLongitude,minLongitude);

        return plan;
    }

    /**Read a requests file.
     *
     * @param filePath the path to the file to read.
     * @param intersectionMap the intersection map containing all intersections loaded in the map
     * @return
     */
    public PlanningRequest readRequests (String filePath, Map<String,Intersection> intersectionMap) throws Exception{
        if(!filePath.contains(".xml")){
            throw new Exception("Please only select .xml files");
        }
        Document doc = parseXMLFile(filePath);

        // Get depot
        Element depot = (Element) doc.getElementsByTagName("depot").item(0);
        if(depot==null){
            throw new Exception("Depot not found");
        }
        String startId = depot.getAttribute("address");
        String departureTime = formatDepartureTime(depot.getAttribute("departureTime"));

        if(startId.isEmpty()){
            throw  new Exception("Depot address not specified");
        }
        if(depot.getAttribute("departureTime").isEmpty()){
            throw new Exception("Departure time not specified");
        }

        PlanningRequest planningRequest = new PlanningRequest(startId,departureTime);

        // Get all requests
        NodeList requests = doc.getElementsByTagName("request");
        if(requests.getLength()==0){
            throw new Exception("No requests found in file");
        }
        for (int i = 0; i < requests.getLength(); i++) {
            Node node = requests.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String pickupId = element.getAttribute("pickupAddress");
                String deliveryId = !element.getAttribute("deliveryAddress").isEmpty() ? element.getAttribute("deliveryAddress") : element.getAttribute("adresseLivraison");

                // If we lack important data we ignore the request
                if(pickupId.isEmpty() || deliveryId.isEmpty()){
                    continue;
                }

                // If pickup or delivery duration is not specified we assume that it's 0
                int pickupDuration = element.getAttribute("pickupDuration").isEmpty()  ? 0 : Integer.parseInt(element.getAttribute("pickupDuration"));
                int deliveryDuration = element.getAttribute("deliveryDuration").isEmpty() ? 0 : Integer.parseInt(element.getAttribute("deliveryDuration"));

                Request request = new Request(pickupId,deliveryId,pickupDuration,deliveryDuration);
                planningRequest.addRequest(request);

            }
        }

        if(planningRequest.getRequests().isEmpty()){
            throw new Exception("All requests in file lack important data");
        }

        // We check that all the requests are actually present in the map (sometimes the map may be too small for the incoming requests) else we throw an error
        if(intersectionMap!=null){
            List<Request> finalRequests = planningRequest.getRequests();
            for(Request request:finalRequests){
                if(!intersectionMap.containsKey(request.getPickupId()) || !intersectionMap.containsKey(request.getDeliveryId())){
                    throw new Exception("The map is too small for the requests imported, please use a bigger map.");
                }
            }

        }

        return planningRequest;
    }


}
