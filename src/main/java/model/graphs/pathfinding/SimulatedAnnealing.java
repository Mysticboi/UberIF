package model.graphs.pathfinding;

import model.PlanningRequest;
import model.Request;
import observer.Observer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;


/**
 * Class SimulatedAnnealing. Represent the Simulated Annealing algorithm.
 */
public class SimulatedAnnealing extends TemplateTSP implements TSP {


    /**
     * the initial temperature.
     */
    private float t0;
    /**
     * the cooling rate of the temperature.
     */
    private final float alpha = 0.96f;

    /**
     * the increasing rate of iterations at temperature.
     */
    private final float beta = 1.001f;
    /**
     * the initial iteration rate.
     */
    private final float beta0 = 0.0001f;
    /**
     * the list of all delivery points.
     */
    private List<String> deliveryPoints;
    /**
     * the list of all pickup points.
     */
    private List<String> pickupPoints;
    /**
     * the current generated permutation.
     */
    private String[] permutation;


    /**
     * count of total rejections.
     */
    private int rejected;

    /**
     * constructor with observer.
     *
     * @param mainWindow the main observer to be notified
     */
    public SimulatedAnnealing(final Observer mainWindow) {
        addObserver(mainWindow);
    }

    /**
     * default constructor.
     */
    public SimulatedAnnealing() {
    }

    @Override
    public void computeSolution(final PlanningRequest planningRequest) {

        if (timeLimit <= 0) {
            return;
        }

        rejected = 0;
        deliveryPoints = new ArrayList<>();
        pickupPoints = new ArrayList<>();

        List<Request> requests = planningRequest.getRequests();
        for (Request request : requests) {
            deliveryPoints.add(request.getDeliveryId());
            pickupPoints.add(request.getPickupId());
        }

        startTime = System.currentTimeMillis();


        float curCost = randomPermutation(planningRequest.getStartId());

        bestSol = Arrays.copyOf(permutation, permutation.length);
        bestSolCost = getPermutationCost();


        //heat();
        t0 = 200000;
        float temp = t0;


        int m = (int) Math.floor(beta0 * timeLimit);
        int timer = m;


        int nbIter = 0;
        final float minTemperature = 0.1f;

        while (nbIter < timeLimit && temp > minTemperature) {

            int totalRejected = 0;
            while (timer >= 0) {
                curCost = saStep(curCost, temp);
                totalRejected += rejected;
                timer--;
            }

            temp *= alpha;
            m = (int) Math.floor(beta * m);
            timer = m;
            nbIter++;

        }


    }

    /**
     * @return the cost of the current permutation
     */
    private float getPermutationCost() {
        float cost = 0;

        for (int i = 1; i < permutation.length; i++) {

            cost += g.getCost(permutation[i - 1], permutation[i]);
        }
        cost += g.getCost(permutation[permutation.length - 1], permutation[0]);
        return cost;
    }


    /**
     * Simulated Annealing step : move a vertex randomly, check if it improved
     * the route and accept it
     * if it doesn't improve, accept it with a probability
     * of <code>Math.exp(-deltaCost/temp))</code>.
     *
     * @param curCost the current cost of the permutation
     * @param temp    the current temperature
     * @return the cost of the accepted permutation
     */
    float saStep(float curCost, final float temp) {

        float newCost;
        float deltaCost;
        String[] current = Arrays.copyOf(permutation, permutation.length);

        randomMove();
        newCost = getPermutationCost();
        deltaCost = newCost - curCost;

        if (deltaCost < 0) {
            curCost = newCost;
            //System.out.println(curCost);
            if (curCost < bestSolCost) {
                bestSolCost = curCost;
                bestSol = Arrays.copyOf(permutation, permutation.length);
                rejected = 0;
            }
        } else {
            Random rd = new Random();
            if (rd.nextFloat() < Math.exp(-deltaCost / temp)) {
                curCost = newCost;
                rejected = 0;
            } else {
                rejected = 1;
                permutation = current;
                //undo(permutation);

            }
        }
        return curCost;
    }

    /**
     * increase the temperature so we have enough rejections.
     */
    private void heat() {
        float curCost = getPermutationCost();
        float temp = 1.0f;
        float prctReject = 1.0f;
        float rejectionThreshold = 0.05f;
        int timer = 100;
        int nbReject;

        int maxIter = 200;

        for (int k = 0; k < maxIter && prctReject > rejectionThreshold; k++) {
            nbReject = 0;
            for (int i = 0; i < timer; i++) {
                curCost = saStep(curCost, temp);
                nbReject += rejected;
            }
            prctReject = (float) nbReject / (float) timer;

            temp *= 1.1;
        }

        t0 = temp;
    }

    /**
     * swap 2 elements in an array.
     *
     * @param a           the index of the first node to be swapped
     * @param b           the index of the second node to be swapped
     */
    private void swap(final int a, final int b) {
        String tmp = permutation[a];
        String curr = permutation[b];
        permutation[a] = curr;
        permutation[b] = tmp;
    }


    /**
     * @param source the source index to me moved
     * @param dest   the destination where the source should be moved
     */
    private void moveElement(final int source,
                             final int dest) {

        // Converting array to ArrayList
        List<String> list = new ArrayList<>(
                Arrays.asList(permutation));

        // Does the move by removing the source and adding it at the
        // destination index
        list.add(dest, list.remove(source));


        // Converting the list back to array
        permutation = list.toArray(permutation);


    }

    /**
     * move one vertex randomly across the tour.
     */
    public void randomMove() {

        int size = permutation.length;
        Random rd = new Random();


        boolean isValid = false;
        while (!isValid) {

            int lastI = rd.nextInt(size - 1) + 1;
            int lastJ = rd.nextInt(size - 1) + 1;

            moveElement(lastI, lastJ);
            if (checkIsValid() && lastJ != lastI) {
                isValid = true;
            }

        }


    }


    /**
     * @return true if the current generated permutation has all the pickup and
     * delivery precedence respected
     */
    private boolean checkIsValid() {
        for (int j = 0; j < permutation.length; j++) {

            if (deliveryPoints.contains(permutation[j])) {
                boolean isValid = false;
                int deliveryIndex = deliveryPoints.indexOf(permutation[j]);
                String pickup = pickupPoints.get(deliveryIndex);
                for (int k = 0; k < j; k++) {

                    if (permutation[k].equals(pickup)) {
                        isValid = true;
                        break;
                    }
                }
                if (!isValid) {
                    return false;
                }

            }
        }
        return true;
    }

    /**
     * create a random permutation of the tour.
     *
     * @param startNode the starting point
     * @return the cost of the generated permutation
     */
    public float randomPermutation(final String startNode) {

        int size = g.getNbVertices();
        permutation = new String[size];

        permutation[0] = startNode;

        Set<String> vertices = g.getVertices();

        int i = 1;
        for (String node : vertices) {
            if (!node.equals(startNode)) {
                permutation[i] = node;
                i++;
            }
        }

        Random rd = new Random();

        do {
            int b = rd.nextInt(size - 1) + 1;
            for (int j = 1; j < permutation.length; j++) {
                swap(j, b);
            }
        } while (!checkIsValid());


        return getPermutationCost();
    }
}
