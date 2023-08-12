package it.unibo.start;

import it.unibo.controller.db.ConnectionProvider;
import it.unibo.controller.db.QueryManager;
import it.unibo.controller.db.tables.TabellaCircoli;
import it.unibo.controller.db.tables.TabellaCoppie;
import it.unibo.controller.db.tables.TabellaEdizioniTorneo;
import it.unibo.controller.db.tables.TabellaGiocatori;
import it.unibo.controller.db.tables.TabellaIscrizioni;
import it.unibo.controller.db.tables.TabellaOrganizzatori;
import it.unibo.controller.db.tables.TabellaTornei;
import it.unibo.controller.db.tables.TabellaUnioni;
import it.unibo.view.MainFrame;

public class App {
    private final String username = "root";
    private final String password = "Wipassatelli20!";
    private final String dbName = "mytennismanager";

    private final ConnectionProvider connectionProvider;

    private final TabellaCircoli tabellaCircoli;
    private final TabellaCoppie tabellaCoppie;
    private final TabellaEdizioniTorneo tabellaEdizioniTorneo;
    private final TabellaGiocatori tabellaGiocatori;
    private final TabellaIscrizioni tabellaIscrizioni;
    private final TabellaOrganizzatori tabellaOrganizzatori;
    private final TabellaTornei tabellaTornei;
    private final TabellaUnioni tabellaUnioni;
    private QueryManager queryManager;

    private MainFrame frame;

    public App() {
        this.connectionProvider = new ConnectionProvider(username, password, dbName);
        this.tabellaCircoli  = new TabellaCircoli(connectionProvider.getMySQLConnection());
        this.tabellaCoppie = new TabellaCoppie(connectionProvider.getMySQLConnection());
        this.tabellaEdizioniTorneo = new TabellaEdizioniTorneo(connectionProvider.getMySQLConnection());
        this.tabellaGiocatori = new TabellaGiocatori(connectionProvider.getMySQLConnection());
        this.tabellaIscrizioni = new TabellaIscrizioni(connectionProvider.getMySQLConnection());
        this.tabellaOrganizzatori = new TabellaOrganizzatori(connectionProvider.getMySQLConnection());
        this.tabellaTornei = new TabellaTornei(connectionProvider.getMySQLConnection());
        this.tabellaUnioni = new TabellaUnioni(connectionProvider.getMySQLConnection());
        this.queryManager = new QueryManager(connectionProvider);
        this.frame = new MainFrame(this.queryManager);
        
        this.tabellaTornei.dropTable();
        this.tabellaTornei.createTable();
        this.tabellaEdizioniTorneo.dropTable();
        this.tabellaEdizioniTorneo.createTable();
    }

    public void setup() {
        this.tabellaCircoli.dropTable();
        this.tabellaCircoli.createTable();
        this.tabellaCoppie.dropTable();
        this.tabellaCoppie.createTable();
        this.tabellaEdizioniTorneo.dropTable();
        this.tabellaEdizioniTorneo.createTable();
        this.tabellaGiocatori.dropTable();
        this.tabellaGiocatori.createTable();
        this.tabellaIscrizioni.dropTable();
        this.tabellaIscrizioni.createTable();
        this.tabellaOrganizzatori.dropTable();
        this.tabellaOrganizzatori.createTable();
        this.tabellaTornei.dropTable();
        this.tabellaTornei.createTable();
        this.tabellaUnioni.dropTable();
        this.tabellaUnioni.createTable();
    }

    public static void main(String[] args) {
        App app = new App();
        //app.setup();
    }
}
