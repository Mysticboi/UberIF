package model.graphs.pathfinding;

import model.DeliveryTour;
import model.PlanningRequest;
import model.Segment;
import model.graphs.Graph;
import observer.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * Class TemplateTSP. a template class with all shared values between
 * algorithms
 */
public abstract class TemplateTSP extends Observable implements TSP {
    /**
     * the best computed solution.
     */
    protected String[] bestSol;
    /**
     * the graph.
     */
    protected Graph g;
    /**
     * the best computed solution cost.
     */
    protected float bestSolCost;
    /**
     * the time limit
     */
    protected int timeLimit;
    /**
     * the starting time
     */
    protected long startTime;


    /**
     * Call the <code>computerSolution</code> of the specific algorithm.
     *
     * @param timeLimit       the time limit
     * @param g               the graph
     * @param planningRequest the planing request with the deliveries, the
     *                        pickups and the starting point
     */
    @Override
    public void searchSolution(final int timeLimit,
                               final Graph g,
                               final PlanningRequest planningRequest) {
        if (timeLimit <= 0) {
            return;
        }
        startTime = System.currentTimeMillis();
        this.timeLimit = timeLimit;
        this.g = g;
        bestSol = new String[g.getNbVertices()];

        computeSolution(planningRequest);
        notifyObservers(getDeliveryTour());
    }

    /**
     * The specific implementation of the algorithm.
     *
     * @param planningRequest   the planning request
     */
    protected abstract void computeSolution(PlanningRequest
                                                    planningRequest);

    /**
     * @return a DeliveryTour object that contains, all the computed information
     */
    @Override
    public DeliveryTour getDeliveryTour() {

        List<Segment> segmentList = new ArrayList<>();

        int solutionSize = bestSol.length;
        for (int i = 1; i < solutionSize; i++) {
            Edge edge = g.getEdge(bestSol[i - 1], bestSol[i]);
            List<Segment> edgeSegmentList = edge.getSegmentList();
            if (edgeSegmentList != null) {
                segmentList.addAll(edgeSegmentList);
            }
        }
        Edge edge = g.getEdge(bestSol[solutionSize - 1], bestSol[0]);
        List<Segment> edgeSegmentList = edge.getSegmentList();
        if (edgeSegmentList != null) {
            segmentList.addAll(edgeSegmentList);
        }

        //System.out.println(bestSolCost);
        //System.out.println(Arrays.toString(bestSol));
        return new DeliveryTour(segmentList, bestSolCost, bestSol);
    }

    /**
     * @return the Array of string of the computed tour
     */
    @Override
    public String[] getSolution() {
        return bestSol;
    }

    /**
     * @return the cost of the computed tour
     */
    @Override
    public float getSolutionCost() {
        if (g != null) {
            return bestSolCost;
        }
        return -1;
    }


}
