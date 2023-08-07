package view.plan;

import model.DeliveryTour;
import model.Intersection;
import model.Segment;
import model.graphs.Key;
import model.graphs.Plan;
import view.MainWindow;
import view.MouseListenerPlanPanel;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

/**
 * The visualisation of the map. Updated when the data changes.
 */
public final class PlanPanel extends JPanel {
    /** Parent component of the panel. **/
    private final MainWindow parent;
    /** Data of the current context. **/
    private Plan planData;
    /** Data structure containing the segments of the map. **/
    private Map<Key, Segment> segmentMap;
    /** Data structure containing the intersections of the map. **/
    private Map<String, Intersection> intersectionMap;
    /** Value of the zoom on the graphical component. **/
    private int currentScale = 1;
    /** x coordinate on the map if it is zoomed. **/
    private int xPosition = -1;
    /** y coordinate on the map if it is zoomed. **/
    private int yPosition = -1;
    /** Is the map currently zoomed. **/
    private boolean zoom = false;
    /** Does the user try to move on the map. **/
    private boolean move = false;
    /** Maximum latitude given by the intersections of the map. **/
    private float maxLatitude;
    /** Minimum latitude given by the intersections of the map. **/
    private float minLatitude;
    /** Maximum longitude given by the intersections of the map. **/
    private float maxLongitude;
    /** Minimum longitude given by the intersections of the map. **/
    private float minLongitude;
    /** Width of the component. **/
    private int width;
    /** Height of the component. **/
    private int height;

    Map<Tuple2<Intersection, Integer>, Tuple2<Intersection, Intersection>> clickablePOIMap;
    Tuple2<Intersection, Intersection> highlightedPath;
    private Intersection selectedPOI;

    /**
     * Creates a new instance of the graphical component and initializes its
     * listeners.
     *
     * @param parent Parent component of the panel
     */
    public PlanPanel(final MainWindow parent) {
        super();
        this.parent = parent;
        this.setBackground(Color.LIGHT_GRAY);
        MouseListenerPlanPanel mouseEvent = new MouseListenerPlanPanel(this);
        this.addMouseListener(mouseEvent);
        this.addMouseWheelListener(mouseEvent);
        this.addMouseMotionListener(mouseEvent);
        clickablePOIMap = new HashMap<>();
        highlightedPath = null;
        selectedPOI = null;

        setVisible(true);
    }

    public Intersection getSelectedPOI() {
        return selectedPOI;
    }

    public void setSelectedPOI(final Intersection selectedPOI) {
        this.selectedPOI = selectedPOI;
        repaint();
    }

    /**
     * Returns the data of the current context.
     *
     * @return the data of the current context
     */
    public Plan getPlanData() {
        return planData;
    }

    /**
     * Sets a new context to the visualization of the map.
     *
     * @param data The data of the new context
     */
    public void setPlanData(final Plan data) {
        planData = data;
        segmentMap = planData.getSegmentMap();
        intersectionMap = planData.getIntersectionMap();
        maxLatitude = planData.getMaxLatitude();
        minLatitude = planData.getMinLatitude();
        maxLongitude = planData.getMaxLongitude();
        minLongitude = planData.getMinLongitude();
        this.repaint();
    }

    @Override
    public void setSize(final int w, final int h) {
        super.setSize(w, h);
        width = w;
        height = h;
    }

    /**
     * Detects if the user is zooming on the map.
     *
     * @param notches Amount of zoom applied
     */
    public void onMouseWheel(final int notches) {
        if ((this.currentScale + notches) >= 1) {
            this.currentScale += notches;
            zoom = true;
            this.repaint();
        } else {
            this.xPosition = width / 2;
            this.yPosition = height / 2;
        }
    }

    /**
     * Detects if the user is moving a zoomed map.
     *
     * @param xMove x coordinate of the mouse after the drag
     * @param yMove y coordinate of the mouse after the drag
     */
    public void onMouseDragged(final int xMove, final int yMove) {
        if (xPosition < 0 && yPosition < 0) {
            this.xPosition = xMove;
            this.yPosition = yMove;
        } else {
            int xMovement = xMove - this.xPosition;
            int yMovement = yMove - this.yPosition;
            this.xPosition += xMovement;
            this.yPosition += yMovement;
        }
        this.move = true;
        this.repaint();

    }

    /**
     * Detects if the user has selected something on the map.
     *
     * @param xMouse x coordinate of the mouse
     * @param yMouse y coordinate of the mouse
     */
    public void onMouseClicked(final int xMouse, final int yMouse) {
        highlightedPath = null;

        for (Tuple2<Intersection, Integer> t : clickablePOIMap.keySet()) {
            if(Math.sqrt(Math.pow(yMouse - scaleYCoordinateToPlan(t._1.getLatitude()), 2D) + Math.pow(xMouse - scaleXCoordinateToPlan(t._1.getLongitude()), 2D)) <= t._2) {
                selectedPOI = t._1;
                highlightedPath = clickablePOIMap.get(t);
                parent.setHighlighted(selectedPOI.getId());
            }
        }

        if(highlightedPath==null)
            identifyStreet(xMouse, yMouse);
        repaint();
    }

    /**
     * Detects if the user clicked on the panel, but didn't release.
     *
     * @param xMove x coordinate of the mouse
     * @param yMove y coordinate of the mouse
     */
    public void onMousePressed(final int xMove, final int yMove) {
    }

    /**
     * Scales an x coordinate to the current size of the graphical component.
     *
     * @param x Coordinate to scale
     * @return the scaled x coordinate
     */
    public int scaleXCoordinateToPlan(final float x) {
        if (x < minLongitude || x > maxLongitude) {
            return -1;
        }
        return (int) Math.floor((width * (x - minLongitude)
                / (maxLongitude - minLongitude)));
    }

    /**
     * Scales and flips the y coordinate to fit the current size and
     * orientation of the graphical component.
     *
     * @param y Coordinate to convert
     * @return the converted y coordinate
     */
    public int scaleYCoordinateToPlan(final float y) {
        if (y < minLatitude || y > maxLatitude) {
            return -1;
        }
        return flipYAxis((int) Math.floor((height * (y - minLatitude)
                / (maxLatitude - minLatitude))));
    }

    /**
     * Flips a y coordinate to fit the current orientation of the graphical
     * component.
     *
     * @param y The coordinate to flip
     * @return the flipped coordinate
     */
    private int flipYAxis(final int y) {
        return height - y;
    }

    /**
     * Identifies a street selected by the user.
     * Checks, for all segments of the map, whether the x and y coordinates
     * of the mouse are inside of one, which sets the name of the street.
     *
     * @param xMouse x coordinate of the mouse
     * @param yMouse y coordinate of the mouse
     */
    public void identifyStreet(final int xMouse, final int yMouse) {
        planData.setSelectedStreetName("");
        for (Key value : segmentMap.keySet()) {
            Segment segment = segmentMap.get(value);
            Intersection origine = intersectionMap.get(segment.getOrigin());
            Intersection destination =
                    intersectionMap.get(segment.getDestination());

            int yOrigine = scaleYCoordinateToPlan(origine.getLatitude());
            int xOrigine = scaleXCoordinateToPlan(origine.getLongitude());

            Point2D pointOrigine = new Point(xOrigine, yOrigine);

            int yDestination =
                    scaleYCoordinateToPlan(destination.getLatitude());
            int xDestination =
                    scaleXCoordinateToPlan(destination.getLongitude());
            Point2D pointDestination = new Point(xDestination, yDestination);

            Point2D pointMouse = new Point(xMouse, yMouse);

            if ((int) (pointOrigine.distance(pointMouse))
                    + (int) (pointDestination.distance(pointMouse))
                    == (int) (pointDestination.distance(pointOrigine))
                    && !planData.getSelectedStreetName().equals(segment.getName())
            ) {
                planData.setSelectedStreetName(segment.getName());
                parent.setSystemInfoText(segment.getName());
                break;
            }
        }
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        width = this.getWidth();
        height = this.getHeight();

        if (this.zoom) {
            if (this.xPosition < 0 && this.yPosition < 0) {
                g2.translate(width / 2, height / 2);
                g2.scale(this.currentScale, this.currentScale);
                g2.translate(-width / 2, -height / 2);
            } else {
                g2.translate(this.xPosition, this.yPosition);
                g2.scale(this.currentScale, this.currentScale);
                g2.translate(-this.xPosition, -this.yPosition);
            }
            this.zoom = false;

        } else if (this.move) {
            g2.translate(this.xPosition, this.yPosition);
            g2.scale(this.currentScale, this.currentScale);
            g2.translate(-this.xPosition, -this.yPosition);
            this.move = false;
        }

        if (planData != null) {
            // Sélection et ordonnancement des logiques de dessin
            PlanDrawing planDrawing = new PlanDrawing(planData, this, g);
            planDrawing.drawPlan();
            final DeliveryTour deliveryTour = planData.getDeliveryTour();
            if (deliveryTour != null) {
                planDrawing.drawRequestsRoute(deliveryTour, selectedPOI);
            }
            if (planData.getPlanningRequest() != null) {
                planDrawing.drawPOI(new ClickablePOI() {
                    @Override
                    public void updateTrack(Intersection origin, int radiusOrigin, Intersection destination, int radiusDestination) {
                        if(deliveryTour != null) { //beatriz
                            clickablePOIMap.put(new Tuple2<Intersection, Integer>(origin, radiusOrigin), new Tuple2<Intersection, Intersection>(origin, destination));
                            clickablePOIMap.put(new Tuple2<Intersection, Integer>(destination, radiusDestination), new Tuple2<Intersection, Intersection>(origin, destination));
                        }
                    }
                });
            }
        }
        highlightedPath = null;
    }
}
