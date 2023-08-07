package view;

import controller.ControllerMainWindow;
import model.Request;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteButtonListener implements ActionListener {
    private ControllerMainWindow controllerMainWindow;
    private Request request;
    private boolean shouldChangeTour;

    public DeleteButtonListener(ControllerMainWindow controllerMainWindow, Request request, boolean shouldChangeTour) {
        this.controllerMainWindow = controllerMainWindow;
        this.request = request;
        this.shouldChangeTour = shouldChangeTour;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        // Remove request without changing tour
        if(!shouldChangeTour) {
            System.out.println("Removing request without changing tour...");
            controllerMainWindow.removeRequest(request,false);
        }
        else{
            System.out.println("Removing request with changing the tour...");
            controllerMainWindow.removeRequest(request,true);
        }
    }
}
