package view.state;

import view.MainWindow;

public class ReadyState implements State {
    @Override
    public void execute(MainWindow context) {
        context.setModifyPlanData(true);
        context.setCurrentState(this);
        context.setSystemInfoText("Ready");
    }
}
