package it.unibo.controller.db;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import it.unibo.controller.db.tables.TabellaCircoli;
import it.unibo.controller.db.tables.TabellaCoppie;
import it.unibo.controller.db.tables.TabellaEdizioniTorneo;
import it.unibo.controller.db.tables.TabellaGiocatori;
import it.unibo.controller.db.tables.TabellaIscrizioni;
import it.unibo.controller.db.tables.TabellaOrganizzatori;
import it.unibo.controller.db.tables.TabellaTornei;
import it.unibo.controller.db.tables.TabellaUnioni;
import it.unibo.model.Circolo;
import it.unibo.model.EdizioneTorneo;
import it.unibo.model.Giocatore;
import it.unibo.model.Organizzatore;
import it.unibo.model.Torneo;
import it.unibo.model.Torneo.Tipo;

public class QueryManager {

    private final TabellaCircoli circolo;
    private final TabellaCoppie coppia;
    private final TabellaEdizioniTorneo edizioneTorneo;
    private final TabellaGiocatori giocatore;
    private final TabellaIscrizioni iscrizione;
    private final TabellaOrganizzatori organizzatore;
    private final TabellaTornei torneo;
    private final TabellaUnioni unione;

    public QueryManager(final ConnectionProvider connectionProvider) {
        this.circolo = new TabellaCircoli(connectionProvider.getMySQLConnection());
        this.coppia = new TabellaCoppie(connectionProvider.getMySQLConnection());
        this.edizioneTorneo = new TabellaEdizioniTorneo(connectionProvider.getMySQLConnection());
        this.giocatore = new TabellaGiocatori(connectionProvider.getMySQLConnection());
        this.iscrizione = new TabellaIscrizioni(connectionProvider.getMySQLConnection());
        this.organizzatore = new TabellaOrganizzatori(connectionProvider.getMySQLConnection());
        this.torneo = new TabellaTornei(connectionProvider.getMySQLConnection());
        this.unione = new TabellaUnioni(connectionProvider.getMySQLConnection());
    }

    public Organizzatore createOrganizzatore(final String nome,
            final String cognome,
            final String email,
            final String password) {

        return new Organizzatore(nome, cognome, email, password);
    }

    public void addOrganizzatore(final Organizzatore organizzatore) {
        this.organizzatore.save(organizzatore);
    }

    public Integer getIdOrganizzatore(final Organizzatore organizzatore) {
        return organizzatore.getId();
    }

    public Optional<Organizzatore> findOrganizzatore(final Integer id) {
        return this.organizzatore.findByPrimaryKey(id);
    }

    public Optional<Organizzatore> findOrganizzatoreByCredentials(final String email, final String password) {
        return this.organizzatore.findOrganizzatoreByCredentials(email, password);
    }

    public Giocatore createGiocatore(final String nome,
            final String cognome,
            final String email,
            final String password,
            final String tessera,
            final String classifica,
            final int eta,
            final String sesso,
            final String telefono,
            final Integer circolo) {

        return new Giocatore(nome, cognome, email, password, tessera, classifica, eta, sesso, telefono, circolo);
    }

    public void addGiocatore(final Giocatore giocatore) {
        this.giocatore.save(giocatore);
    }

    public Integer getIdGiocatore(final Giocatore giocatore) {
        return giocatore.getId();
    }

    public Optional<Giocatore> findGiocatore(final Integer id) {
        return this.giocatore.findByPrimaryKey(id);
    }

    public Optional<Giocatore> findGiocatoreByCredentials(final String email, final String password) {
        return this.giocatore.findGiocatoreByCredentials(email, password);
    }

    public Circolo createCircolo(
        final Integer organizzatore,
        final String nome,
        final String citta,
        final String indirizzo,
        final String telefono) {
        
        return new Circolo(organizzatore, nome, citta, indirizzo, telefono);
    }

    public void addCircolo(final Circolo circolo) {
        this.circolo.save(circolo);
    }

    public Optional<Circolo> findCircolo(final Integer id) {
        return this.circolo.findByPrimaryKey(id);
    }

    public Optional<Circolo> findCircoloByOrganizzatore(final int organizzatore) {
        return this.circolo.findCircoloByOrganizzatore(organizzatore);
    }

    public List<Circolo> findAllCircolo() {
        return this.circolo.findAll();
    }

    public Torneo createTorneo(final Tipo tipo,
        final Optional<Integer> limiteCategoria,
        final Optional<Integer> limiteEta,
        final Optional<Integer> montepremi) {

        return new Torneo(tipo, limiteCategoria, limiteEta, montepremi);
    }

    public void addTorneo(final Torneo torneo) {
        this.torneo.save(torneo);
    }

    public Optional<Torneo> findTorneo(final Integer id) {
        return this.torneo.findByPrimaryKey(id);
    }

    public List<Torneo> findAllTorneo() {
        return this.torneo.findAll();
    }

    public List<Torneo> findAllTorneoByCircolo(final Circolo circolo) {
        return this.torneo.findAllByCircolo(circolo.getId());
    }

    public EdizioneTorneo createEdizioneTorneo(final Integer id_torneo,
            final Integer n_edizione,
            final Date d_inizio,
            final Date d_fine,
            final Integer circolo) {

        return new EdizioneTorneo(id_torneo, n_edizione, d_inizio, d_fine, circolo);
    }

    public void addEdizioneTorneo(final EdizioneTorneo eTorneo) {
        this.edizioneTorneo.save(eTorneo);
    }

    public int getNumeroEdizione(final Torneo torneo) {
        return this.edizioneTorneo.getEdizioneTorneo(torneo.getId()) + 1;
    }
}
