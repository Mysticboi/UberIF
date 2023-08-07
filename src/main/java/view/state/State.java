package view.state;

import view.MainWindow;

public interface State {
    void execute(MainWindow context);
}
