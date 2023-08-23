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
import it.unibo.controller.db.views.ViewIscrittiDoppi;
import it.unibo.controller.db.views.ViewIscrittiSingoli;
import it.unibo.controller.db.views.ViewIscrizioniGiocatore;
import it.unibo.controller.db.views.ViewTornei;
import it.unibo.controller.db.views.ViewUnioni;
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
    private final ViewTornei viewTornei;
    private final ViewUnioni viewUnioni;
    private final ViewIscrittiSingoli viewIscrittiSingoli;
    private final ViewIscrittiDoppi viewIscrittiDoppi;
    private final ViewIscrizioniGiocatore viewIscrizioniGiocatore;
    private QueryManager queryManager;

    private MainFrame frame;

    public App() {
        this.connectionProvider = new ConnectionProvider(username, password, dbName);
        this.tabellaCircoli  = new TabellaCircoli(this.connectionProvider.getMySQLConnection());
        this.tabellaCoppie = new TabellaCoppie(this.connectionProvider.getMySQLConnection());
        this.tabellaEdizioniTorneo = new TabellaEdizioniTorneo(this.connectionProvider.getMySQLConnection());
        this.tabellaGiocatori = new TabellaGiocatori(this.connectionProvider.getMySQLConnection());
        this.tabellaIscrizioni = new TabellaIscrizioni(this.connectionProvider.getMySQLConnection());
        this.tabellaOrganizzatori = new TabellaOrganizzatori(this.connectionProvider.getMySQLConnection());
        this.tabellaTornei = new TabellaTornei(this.connectionProvider.getMySQLConnection());
        this.tabellaUnioni = new TabellaUnioni(this.connectionProvider.getMySQLConnection());
        this.viewTornei = new ViewTornei(this.connectionProvider.getMySQLConnection());
        this.viewUnioni = new ViewUnioni(this.connectionProvider.getMySQLConnection());
        this.viewIscrittiSingoli = new ViewIscrittiSingoli(this.connectionProvider.getMySQLConnection());
        this.viewIscrittiDoppi = new ViewIscrittiDoppi(this.connectionProvider.getMySQLConnection());
        this.viewIscrizioniGiocatore = new ViewIscrizioniGiocatore(this.connectionProvider.getMySQLConnection());
        this.queryManager = new QueryManager(this.connectionProvider);
        this.frame = new MainFrame(this.queryManager);
        
        /*this.tabellaCircoli.dropTable();
        this.tabellaCircoli.createTable();
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
        this.tabellaCoppie.dropTable();
        this.tabellaCoppie.createTable();
        this.tabellaUnioni.dropTable();
        this.tabellaUnioni.createTable();
        this.viewTornei.dropView();
        this.viewTornei.createView();
        this.viewUnioni.dropView();
        this.viewUnioni.createView();
        this.viewIscrittiSingoli.dropView();
        this.viewIscrittiSingoli.createView();
        this.viewIscrizioniGiocatore.dropView();
        this.viewIscrizioniGiocatore.createView();*/
    }

    public void setup() {
        this.tabellaCircoli.dropTable();
        this.tabellaCircoli.createTable();
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
        this.tabellaCoppie.dropTable();
        this.tabellaCoppie.createTable();
        this.tabellaUnioni.dropTable();
        this.tabellaUnioni.createTable();
        this.viewTornei.dropView();
        this.viewTornei.createView();
        this.viewUnioni.dropView();
        this.viewUnioni.createView();
        this.viewIscrittiSingoli.dropView();
        this.viewIscrittiSingoli.createView();
        this.viewIscrittiDoppi.dropView();
        this.viewIscrittiDoppi.createView();
        this.viewIscrizioniGiocatore.dropView();
        this.viewIscrizioniGiocatore.createView();
    }

    public static void main(String[] args) {
        App app = new App();
        //app.setup();
    }
}
