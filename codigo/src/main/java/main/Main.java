package main;

import controller.ListarEstacionamentoController;
import exceptions.EstacionamentoDAOException;
import view.MainView;

public class Main {
    public static void main(String[] args) throws EstacionamentoDAOException {
        MainView mainView = new MainView();
        mainView.setVisible(true);
    }
}
