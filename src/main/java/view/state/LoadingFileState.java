package view.state;

import view.MainWindow;

public class LoadingFileState implements State {
    @Override
    public void execute(MainWindow context) {
        context.setModifyPlanData(false);
        context.setCurrentState(this);
        context.setSystemInfoText("Loading file...");
    }
}
