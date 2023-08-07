package view;

import model.Request;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PointOfInterestPanel extends JPanel {
    private boolean isPickUp;
    private String timePassage;
    private Request request;
    private int i;

    public PointOfInterestPanel(boolean isPickUp, String timePassage, Request request,int i) {
        this.isPickUp = isPickUp;
        this.timePassage = timePassage;
        this.request = request;
        this.i = i;
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));

        JLabel label1 = new JLabel((isPickUp ? "Pickup":"Delivery")+" n°"+i);
        label1.setFont(new Font("Verdana",1,14));
        label1.setForeground(request.getColor());
        add(label1);
        JLabel label2 = new JLabel("Intersection id: "+(isPickUp? request.getPickupId() : request.getDeliveryId()));
        label2.setFont(new Font("Verdana",1,14));
        add(label2);
        JLabel label3  = new JLabel("Time of passage: "+timePassage);
        label3.setFont(new Font("Verdana",1,14));
        add(label3);

        add(Box.createVerticalStrut(15));
        EmptyBorder emptyBorder = new EmptyBorder(0,10,0,0);
        setBorder(emptyBorder);

    }

    public boolean isPickUp() {
        return isPickUp;
    }

    public Request getRequest() {
        return request;
    }
}
