package view;

import controller.ControllerMainWindow;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The button listener of the main window.
 * Act and call for the controller according to which button was pressed.
 */
public class ButtonListenerMainWindow implements ActionListener {
    private ControllerMainWindow controllerMainWindow;
    private MainWindow mainWindow;
    private JFileChooser chooser;

    public ButtonListenerMainWindow(ControllerMainWindow controllerMainWindow, MainWindow mainWindow) {
        this.controllerMainWindow = controllerMainWindow;
        this.mainWindow = mainWindow;
        chooser = new JFileChooser("files");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int returnVal;
        switch(e.getActionCommand()){

            //if the button to import a tour is pressed
            case "Import tour":
                System.out.println("Importer un tour");
                chooser.setAcceptAllFileFilterUsed(false);
                chooser.addChoosableFileFilter(new FileNameExtensionFilter("xml files (.xml)", "xml"));
                returnVal = chooser.showOpenDialog(this.mainWindow);
                if(returnVal == JFileChooser.APPROVE_OPTION){
                    System.out.println("You chose to open this file: "+chooser.getSelectedFile());
                    controllerMainWindow.importTour(chooser.getSelectedFile());
                }
                break;

            //If the button to import a map is pressed
            case "Import map":
                returnVal = chooser.showOpenDialog(this.mainWindow);
                if(returnVal == JFileChooser.APPROVE_OPTION){
                    controllerMainWindow.importMap(chooser.getSelectedFile());
                }
                break;

            //If the button to add a request is pressed
            case "Add a request":System.out.println("Add a request : not implemented");break;

            //If an unpredictable or unimplemented event happen.
            default : System.out.println("Not Implemented");break;
        }
    }
}
