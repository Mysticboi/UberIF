package model.graphs.pathfinding;

import model.PlanningRequest;
import model.graphs.Graph;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BranchAndBoundTest {
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
        TSP sa = new BranchAndBound();
        PlanningRequest planningRequest = new PlanningRequest("1","0202");

        sa.searchSolution(20000,g,planningRequest);
        System.out.println(Arrays.toString(sa.getSolution()));
        String[] solution =  {"1","7","6","4","2"};
        assert(Arrays.equals(sa.getSolution(), solution));
        System.out.println(sa.getSolutionCost());
    }
}