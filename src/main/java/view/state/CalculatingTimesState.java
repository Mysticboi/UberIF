package view.state;

import view.MainWindow;

public class CalculatingTimesState implements State {
    @Override
    public void execute(MainWindow context) {
        context.setModifyPlanData(false);
        context.setCurrentState(this);
        context.setSystemInfoText("Calculating delivery times...");
    }
}
