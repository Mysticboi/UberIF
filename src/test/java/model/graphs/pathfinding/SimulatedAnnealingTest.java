package model.graphs.pathfinding;

import javafx.util.Pair;
import model.PlanningRequest;
import model.graphs.Graph;
import model.graphs.Plan;
import org.junit.Before;
import org.junit.Test;
import util.XMLParser;
import view.state.CalculatingTourState;
import view.state.LoadingFileState;
import view.state.ReadyState;
import view.state.State;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javax.swing.JOptionPane.showMessageDialog;
import static org.junit.Assert.*;

public class SimulatedAnnealingTest {

    Graph g = new Graph();

    @Before
    public void setUp() throws Exception {
        List<Edge> edgeList = new ArrayList<>();
        edgeList.add(new Edge("2","1",null,1.0f));
        edgeList.add(new Edge("7","6",null,6.0f));
        edgeList.add(new Edge("6","7",null,3.0f));
        edgeList.add(new Edge("1","2",null,3.0f));
        edgeList.add(new Edge("4","6",null,2.0f));
        edgeList.add(new Edge("2","4",null,2.0f));
        edgeList.add(new Edge("4","7",null,5.0f));
        edgeList.add(new Edge("1","4",null,3.5f));
        edgeList.add(new Edge("2","6",null,4.0f));
        edgeList.add( new Edge("2","7",null,7.0f));
        edgeList.add( new Edge("1","6",null,5.5f));
        edgeList.add( new Edge("1","7",null,8.5f));
        edgeList.add( new Edge("7","1",null,11.0f));
        edgeList.add( new Edge("6","1",null,5.0f));
        edgeList.add( new Edge("7","2",null,10.0f));
        edgeList.add( new Edge("6","2",null,4.0f));
        edgeList.add( new Edge("4","1",null,4.0f));
        edgeList.add( new Edge("7","4",null,7.0f));
        edgeList.add( new Edge("4","2",null,3.0f));
        edgeList.add( new Edge("6","4",null,1.0f));

        for (Edge e:edgeList ) {
            g.addEdge(e.getOrigin(), e.getDestination(), e);
        }
    }

    @Test
    public void searchSol() {
        SimulatedAnnealing sa = new SimulatedAnnealing();
        PlanningRequest planningRequest = new PlanningRequest("1","0202");


        sa.searchSolution(20000,g,planningRequest);
        assert (Math.abs(sa.getSolutionCost() - 19.5)/19.5 < 0.1);
        System.out.println(Arrays.toString(sa.getSolution()));
        System.out.println(sa.getSolutionCost());
    }



    @Test
    public void saStats(){
        System.out.println("Calculating Simulated Annealing :");
        String filesFolder = "./files/";
        File mapFile = new File(filesFolder+ "largeMap.xml");
        File request1 = new File(filesFolder+ "requestsSmall1.xml");
        File request2 = new File(filesFolder+ "requestsSmall2.xml");
        File request3 = new File(filesFolder+ "requestsMedium3.xml");
        File request5 = new File(filesFolder+ "requestsMedium5.xml");
        File request7 = new File(filesFolder+ "requestsLarge7.xml");
        File request9 = new File(filesFolder+ "requestsLarge9.xml");


        List<Float> costs = new ArrayList<>();
        List<Long> times = new ArrayList<>();
        for (int i = 0; i < 15; i++) {

            Pair<Long, Float> result =  compareSol(mapFile,request1, new SimulatedAnnealing());
            costs.add(result.getValue());
            times.add(result.getKey());
        }
        System.out.println(Arrays.toString(costs.toArray()));
        System.out.println(Arrays.toString(times.toArray()));
        System.out.println();

        costs = new ArrayList<>();
        times = new ArrayList<>();
        for (int i = 0; i < 15; i++) {

            Pair<Long, Float> result =  compareSol(mapFile,request2, new SimulatedAnnealing());
            costs.add(result.getValue());
            times.add(result.getKey());
        }
        System.out.println(Arrays.toString(costs.toArray()));
        System.out.println(Arrays.toString(times.toArray()));
        System.out.println();

        costs = new ArrayList<>();
        times = new ArrayList<>();
        for (int i = 0; i < 15; i++) {


            Pair<Long, Float> result =  compareSol(mapFile,request3, new SimulatedAnnealing());
            costs.add(result.getValue());
            times.add(result.getKey());
        }
        System.out.println(Arrays.toString(costs.toArray()));
        System.out.println(Arrays.toString(times.toArray()));
        System.out.println();

        costs = new ArrayList<>();
        times = new ArrayList<>();
        for (int i = 0; i < 15; i++) {


            Pair<Long, Float> result =  compareSol(mapFile,request5, new SimulatedAnnealing());
            costs.add(result.getValue());
            times.add(result.getKey());
        }
        System.out.println(Arrays.toString(costs.toArray()));
        System.out.println(Arrays.toString(times.toArray()));
        System.out.println();

        costs = new ArrayList<>();
        times = new ArrayList<>();
        for (int i = 0; i < 15; i++) {


            Pair<Long, Float> result =  compareSol(mapFile,request7, new SimulatedAnnealing());
            costs.add(result.getValue());
            times.add(result.getKey());
        }
        System.out.println(Arrays.toString(costs.toArray()));
        System.out.println(Arrays.toString(times.toArray()));
        System.out.println();

        costs = new ArrayList<>();
        times = new ArrayList<>();
        for (int i = 0; i < 15; i++) {


            Pair<Long, Float> result =  compareSol(mapFile,request9, new SimulatedAnnealing());
            costs.add(result.getValue());
            times.add(result.getKey());
        }
        System.out.println(Arrays.toString(costs.toArray()));
        System.out.println(Arrays.toString(times.toArray()));
        System.out.println();

    }

    @Test
    public void stats(){
        System.out.println("Calculating Branch and Bound :");
        String filesFolder = "./files/";
        File mapFile = new File(filesFolder+ "largeMap.xml");
        File request1 = new File(filesFolder+ "requestsSmall1.xml");
        File request2 = new File(filesFolder+ "requestsSmall2.xml");
        File request3 = new File(filesFolder+ "requestsMedium3.xml");
        File request5 = new File(filesFolder+ "requestsMedium5.xml");
        File request7 = new File(filesFolder+ "requestsLarge7.xml");
        File request9 = new File(filesFolder+ "requestsLarge9.xml");


        System.out.println("Calcul request 1 :");
        Pair<Long, Float> result =  compareSol(mapFile,request1, new BranchAndBound());
        System.out.println("Completed in : " +result.getKey() + " with Score : "+result.getValue());

        System.out.println("Calcul request 2 :");
        result =  compareSol(mapFile,request2, new BranchAndBound());
        System.out.println("Completed in : " +result.getKey() + " with Score : "+result.getValue());

        System.out.println("Calcul request 3 :");
        result =  compareSol(mapFile,request3, new BranchAndBound());
        System.out.println("Completed in : " +result.getKey() + " with Score : "+result.getValue());

        System.out.println("Calcul request 5 :");
        result =  compareSol(mapFile,request5, new BranchAndBound());
        System.out.println("Completed in : " +result.getKey() + " with Score : "+result.getValue());

        System.out.println("Calcul request 7 :");
        result =  compareSol(mapFile,request7, new BranchAndBound());
        System.out.println("Completed in : " +result.getKey() + " with Score : "+result.getValue());

        System.out.println("Calcul request 9 :");
        result =  compareSol(mapFile,request9, new BranchAndBound());
        System.out.println("Completed in : " +result.getKey() + " with Score : "+result.getValue());

    }



    public Pair<Long,Float> compareSol(File planFile, File xmlFile, TSP tspMethod){
        XMLParser parser = new XMLParser();
        Plan planData = null;
        try {
            planData = parser.readMap(planFile.getAbsolutePath());
        }
        // Case where we fail to read the map
        catch(Exception e){
            String msg = "Error importing map: "+e.getMessage();
            System.out.println(msg);
        }

        XMLParser xmlParser = new XMLParser();
        PlanningRequest request;
        Graph graph;
        long startTime = 0;
        long stopTime = 0;
        try{
            request = xmlParser.readRequests(xmlFile.getPath(),planData.getIntersectionMap());
            if(request!=null) {
                planData.setPlanningRequest(request);
                graph = Graph.generateCompleteGraphFromPlan(planData);


                // Calling TSP to calculate the best tour
                PlanningRequest finalRequest = request;


                startTime = System.currentTimeMillis();
                tspMethod.searchSolution(600000, graph, finalRequest);
                stopTime = System.currentTimeMillis();
                return new Pair<Long,Float>(stopTime-startTime, tspMethod.getSolutionCost());
            }
        }
        catch(Exception e){
            request = null;
            String msg = "Error importing tour: "+e.getMessage();
            System.out.println(msg);
        }
        return new Pair<Long,Float>(stopTime-startTime, tspMethod.getSolutionCost());




    }
}