package model.graphs;

import model.PlanningRequest;
import model.Request;
import model.graphs.pathfinding.Dijkstra;
import model.graphs.pathfinding.Edge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Class Graph. For now represent a complete graph.
 */
public class Graph {
    /**
     * the graph vertices.
     */
    private final Set<String> vertices;
    /**
     * the graph edges.
     */
    private final HashMap<Key, Edge> edges;
    /**
     * the graph minimal edge cost.
     */
    private float minCost;
    /**
     * subgraphs minimal edge cost.
     */
    private HashMap<Integer, Float> minSubgraphCost;

    /**
     * @param   plan the plan of the city
     * @return  the complete graph of the point of interests from dijkstra
     */
    public static Graph generateCompleteGraphFromPlan(final Plan plan) {
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
                dijkstra.executeAlgorithm(plan,
                        currentPoint,
                        newGraph,
                        pointsOfInterests);

            } catch (Exception e) {
                System.err.println("Error creating the complete graph :"
                        + e.getMessage());
                return null;
            }
        }

        newGraph.calculateMinCost();
        return newGraph;
    }

    /**
     * calculate the minimal edge cost and save it.
     */
    public void calculateMinCost() {
        minCost = Float.MAX_VALUE;
        for (Map.Entry<Key, Edge> entry : edges.entrySet()) {
            float duration = entry.getValue().getDuration();
            minCost = Math.min(minCost, duration);
        }
    }

    /**
     * graph constructor.
     */
    public Graph() {
        vertices = new HashSet<>();
        edges = new HashMap<>();
        minSubgraphCost = new HashMap<>();
    }

    /**
     * @param origin        the source vertex
     * @param destination   the destination vertex
     * @return              the cost of the source -> destination edge
     */
    public float getCost(final String origin, final String destination) {
        Edge edge = edges.get(new Key(origin, destination));
        if (edge == null) {
            System.out.println(origin + " " + destination);

            return 0;
        }
        return edge.getDuration();
    }

    /**
     * @return the minimum edge cost of the graph
     */
    public float getMinCost() {
        return minCost;
    }


    /**
     * @param subGraph the subgraph
     * @return the minimum edge cost of the <code>subGraph</code>
     */
    public float getMinCost(final List<String> subGraph) {
        int hashcode = subGraph.stream()
                .mapToInt(Object::hashCode)
                .reduce(0, (left, right) -> left ^ right);
        if (minSubgraphCost.containsKey(hashcode)) {
            return minSubgraphCost.get(hashcode);
        }

        float subGraphMinCost = Float.MAX_VALUE;
        for (Map.Entry<Key, Edge> entry : edges.entrySet()) {
            String x = entry.getKey().getX();
            String y = entry.getKey().getY();
            if (subGraph.contains(x) || subGraph.contains(y)) {
                float duration = entry.getValue().getDuration();
                subGraphMinCost = Math.min(subGraphMinCost, duration);
            }
        }

        minSubgraphCost.put(hashcode, subGraphMinCost);
        return subGraphMinCost;
    }

    /**
     * @param verticesSet set of the graph vertices
     * @param edgesMap    all the edges of the graph
     */
    public Graph(final Set<String> verticesSet,
                 final HashMap<Key, Edge> edgesMap) {

        this.vertices = verticesSet;
        this.edges = edgesMap;
    }

    /**
     * @param origin      the source vertex
     * @param destination the destination vertex
     * @param edge        the edge object
     */
    public void addEdge(final String origin, final String destination,
                        final Edge edge) {
        vertices.add(origin);
        vertices.add(destination);
        edges.put(new Key(origin, destination), edge);
    }

    /**
     * @return the number of vertices in the graph
     */
    public int getNbVertices() {
        return vertices.size();
    }

    /**
     * @return the number of edges in the graph
     */
    public int getNbEdges() {
        return edges.size();
    }


    /**
     * @return the edges of the graph
     */
    public  HashMap<Key, Edge> getEdges() {
        return edges;
    }

    /**
     * @return the vertices of the graph
     */
    public Set<String> getVertices() {
        return vertices;
    }

    /**
     * @param node the current node to check neighbors of
     * @param currentVertices the vertices of the subgraph to check
     * @return the nearest node of the currentNode
     */
    public String getNearestNodeFromNode(final String node,
                                         final Set<String> currentVertices) {

        float min = Float.MAX_VALUE;
        String minNode = "";
        for (String vertex:currentVertices
        ) {
            if (!Objects.equals(node, vertex)) {

                float cost =  getCost(node, vertex);
                if (cost < min) {
                    min = cost;
                    minNode = vertex;
                }
            }
        }
        return minNode;
    }

    /**
     * @param origin        the source vertex
     * @param destination   the destination vertex
     * @return              true if we can go from
     *                      <code>origin</code> to <code>destination</code>
     */
    public boolean isArc(final String origin, final String destination) {
        return edges.containsKey(new Key(origin, destination));
    }


    /**
     * @param origin        the source vertex
     * @param destination   the destination vertex
     * @return              the edge of
     *                      <code>origin</code>-><code>destination</code>
     */
    public Edge getEdge(final String origin, final String destination) {
        return edges.get(new Key(origin, destination));
    }

}
