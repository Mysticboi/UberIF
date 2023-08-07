package view;

import view.plan.PlanPanel;

import java.awt.event.*;

public class MouseListenerPlanPanel implements MouseListener, MouseWheelListener, MouseMotionListener {

    private PlanPanel plan;
    public MouseListenerPlanPanel(PlanPanel planInit){
        this.plan = planInit;

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e){
        System.out.println("mouse wheel used");
        int notches = e.getWheelRotation();
        System.out.println("wheel moved "+notches+" bits!");
        plan.onMouseWheel(notches);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        final int x = e.getX();
        final int y = e.getY();

        plan.onMouseClicked(x, y);
    }

    @Override
    public void mousePressed(MouseEvent e){
        System.out.println("Mouse pressed!");
        int yMove = (int) e.getPoint().getY();
        int xMove = (int) e.getPoint().getX();
        System.out.println(" x : "+ xMove+" and y : "+yMove);
        plan.onMousePressed(xMove,yMove);

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        System.out.println("Mouse dragged!");
        int yMove = (int) e.getPoint().getY();
        int xMove = (int) e.getPoint().getX();
        System.out.println(" x : "+ xMove+" and y : "+yMove);
        plan.onMouseDragged(xMove,yMove);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
