package it.unibo.start;

import it.unibo.controller.db.ConnectionProvider;
import it.unibo.controller.db.QueryManagerImpl;
import it.unibo.view.MainFrame;

public class App {
    private final String username = "filippo";
    private final String password = "FPracucciDB123!";
    private final String dbName = "mytennismanager";

    private final ConnectionProvider connectionProvider;
    private QueryManagerImpl queryManager;

    private MainFrame frame;

    public App() {
        this.connectionProvider = new ConnectionProvider(username, password, dbName);
        this.queryManager = new QueryManagerImpl(this.connectionProvider);
        this.frame = new MainFrame(this.queryManager);
    }

    public static void main(String[] args) {
        App app = new App();
    }
}
