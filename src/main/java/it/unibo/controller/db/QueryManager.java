package it.unibo.controller.db;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import it.unibo.controller.db.views.ViewTornei;
import it.unibo.controller.db.views.ViewUnioni;
import it.unibo.model.Circolo;
import it.unibo.model.CompagnoUnioni;
import it.unibo.model.Coppia;
import it.unibo.model.EdizioneTorneo;
import it.unibo.model.Giocatore;
import it.unibo.model.Iscrizione;
import it.unibo.model.Organizzatore;
import it.unibo.model.Torneo;
import it.unibo.model.Unione;
import it.unibo.model.TorneiWithEditions;
import it.unibo.model.Torneo.Tipo;
import it.unibo.utils.Pair;

public class QueryManager {

    private final TabellaCircoli circolo;
    private final TabellaCoppie coppia;
    private final TabellaEdizioniTorneo edizioneTorneo;
    private final TabellaGiocatori giocatore;
    private final TabellaIscrizioni iscrizione;
    private final TabellaOrganizzatori organizzatore;
    private final TabellaTornei torneo;
    private final TabellaUnioni unione;
    private final ViewTornei viewTornei;
    private final ViewUnioni viewUnioni;

    public QueryManager(final ConnectionProvider connectionProvider) {
        this.circolo = new TabellaCircoli(connectionProvider.getMySQLConnection());
        this.coppia = new TabellaCoppie(connectionProvider.getMySQLConnection());
        this.edizioneTorneo = new TabellaEdizioniTorneo(connectionProvider.getMySQLConnection());
        this.giocatore = new TabellaGiocatori(connectionProvider.getMySQLConnection());
        this.iscrizione = new TabellaIscrizioni(connectionProvider.getMySQLConnection());
        this.organizzatore = new TabellaOrganizzatori(connectionProvider.getMySQLConnection());
        this.torneo = new TabellaTornei(connectionProvider.getMySQLConnection());
        this.unione = new TabellaUnioni(connectionProvider.getMySQLConnection());
        this.viewTornei = new ViewTornei(connectionProvider.getMySQLConnection());
        this.viewUnioni = new ViewUnioni(connectionProvider.getMySQLConnection());
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

    public List<Torneo> findAllTorneoSingolareEligible(final Giocatore giocatore) {
        final char first = giocatore.getClassifica().charAt(0);
        final int cat;
        switch (first) {
            case '2':
                cat = 2;
                break;
            case '3':
                cat = 3;
                break;
            default:
                cat = 4;
                break;
        }
        return this.torneo.findAllSingolariEligible(giocatore.getEta(), cat, giocatore.getSesso());
    }

    public List<Torneo> findAllTorneoDoppioEligible(final Pair<Giocatore, Giocatore> coppia) {
        final char first1 = coppia.getX().getClassifica().charAt(0);
        final char first2 = coppia.getY().getClassifica().charAt(0);
        final int cat;
        if (first1 < first2) {
            switch (first1) {
                case '2':
                    cat = 2;
                    break;
                case '3':
                    cat = 3;
                    break;
                default:
                    cat = 4;
                    break;
            }
        } else {
            switch (first2) {
                case '2':
                    cat = 2;
                    break;
                case '3':
                    cat = 3;
                    break;
                default:
                    cat = 4;
                    break;
            }
        }
        return this.torneo.findAllDoppiEligible(
            (coppia.getX().getEta() < coppia.getY().getEta()) ? coppia.getY().getEta() : coppia.getX().getEta(),
            cat,
            coppia.getX().getSesso());
    }

    public Integer getIdLastTorneo() {
        return this.torneo.getLastId();
    }

    public void deleteTorneo(final Integer id) {
        this.torneo.delete(id);
    }

    public EdizioneTorneo createEdizioneTorneo(final Integer idTorneo,
            final Integer nEdizione,
            final Date dInizio,
            final Date dFine,
            final Integer circolo) {

        return new EdizioneTorneo(idTorneo, nEdizione, dInizio, dFine, circolo);
    }

    public void addEdizioneTorneo(final EdizioneTorneo eTorneo) {
        this.edizioneTorneo.save(eTorneo);
    }

    public EdizioneTorneo findEdizioneByPrimaryKey(final Pair<Integer, Integer> key) {
        return this.edizioneTorneo.findByPrimaryKey(key).get();
    }

    public int getNumeroEdizione(final Torneo torneo) {
        return this.edizioneTorneo.getEdizioneTorneo(torneo.getId());
    }

    public void deleteEdizioneTorneo(final Pair<Integer, Integer> key) {
        this.edizioneTorneo.delete(key);
    }

    public List<EdizioneTorneo> findAllEdizioneByTorneo(final Torneo torneo) {
        return this.edizioneTorneo.findAllByTorneo(torneo.getId());
    }

    public Object[][] listTorneiToMatrix(final List<TorneiWithEditions> list, final int col) {
        Object[][] matrix = new Object[list.size()][col];
        DateFormat df = new SimpleDateFormat("dd-MM-YYYY");
        TorneiWithEditions vt;
        int j = 0;

        for (int i = 0; i < list.size(); i++) {
            vt = list.get(i);
            matrix[i][j++] = vt.getIdTorneo();
            matrix[i][j++] = vt.getNumEdizione();
            matrix[i][j++] = vt.getTipo();
            matrix[i][j++] = df.format(vt.getDataInizio());
            matrix[i][j++] = df.format(vt.getDataFine());
            matrix[i][j++] = vt.getLimCategoria().orElse(null);
            matrix[i][j++] = vt.getLimEta().orElse(null);
            matrix[i][j++] = vt.getMontepremi().orElse(null);
            matrix[i][j++] = vt.getIdCircolo();
            j = 0;
        }

        return matrix;
    }

    public Iscrizione createIscrizione(final Integer idIscrizione,
            final Optional<String> prefOrario,
            final Integer idTorneo,
            final Integer nEdizione,
            final Optional<Integer> idUtente,
            final Optional<Integer> idCoppia) {

        return new Iscrizione(idIscrizione, prefOrario, idTorneo, nEdizione, idUtente, idCoppia);
    }

    public void addIscrizione(final Iscrizione iscrizione) {
        this.iscrizione.save(iscrizione);
    }

    public List<TorneiWithEditions> findAllEdizioniByCircolo(final Circolo circolo) {
        return this.viewTornei.findAllEdizioniByCircolo(circolo);
    }

    public List<TorneiWithEditions> findAllEligibleByPlayer(final Giocatore giocatore) {
        return this.viewTornei.findAllEligibleByPlayer(giocatore);
    }

    public List<TorneiWithEditions> findAllFiltered(final Giocatore giocatore,
            final Optional<Integer> cat,
            final Optional<Integer> eta,
            final Optional<String> data) {
        return this.viewTornei.findAllFiltered(giocatore, cat, eta, data);
    }

    public Coppia createCoppia(final Integer id) {
        return new Coppia(id);
    }

    public int addCoppia(final Coppia coppia) {
        this.coppia.save(coppia);
        return coppia.getId();
    }

    public Optional<Coppia> findCoppia(final Integer id) {
        return this.coppia.findByPrimaryKey(id);
    }

    public Unione createUnione(final Integer idCoppia, final Integer idUtente) {
        return new Unione(idCoppia, idUtente);
    }

    public void addUnione(final Unione unione) {
        this.unione.save(unione);
    }

    public List<Integer> findAllEligibleUnioni(final Giocatore giocatore) {
        return this.unione.findAllEligible(giocatore.getSesso());
    }

    public List<Integer> findAllPlayerCouples(final Giocatore giocatore) {
        return this.unione.findAllCoppieOfGiocatore(giocatore.getId());
    }

    public Pair<Giocatore, Giocatore> findGiocatoriOfCoppia(final Coppia coppia) {
        final Pair<Integer, Integer> ids = this.unione.findIdGiocatoriOfCoppia(coppia.getId());
        return new Pair<Giocatore, Giocatore>(this.findGiocatore(ids.getX()).get(), this.findGiocatore(ids.getY()).get());
    }

    public List<CompagnoUnioni> findAllUnioniByGiocatore(final Giocatore giocatore) {
        return this.viewUnioni.findAllMyUnioni(giocatore.getId());
    }

    public List<CompagnoUnioni> findAllEligibleCompagniUnioneForGiocatore(final Giocatore giocatore) {
        return this.viewUnioni.findAllEligibleUnioni(giocatore.getId(), giocatore.getSesso());
    }

    public Object[][] listUnioniToMatrix(final List<CompagnoUnioni> list, final int col) {
        Object[][] matrix = new Object[list.size()][col];
        CompagnoUnioni cU;
        int j = 0;

        for (int i = 0; i < list.size(); i++) {
            cU = list.get(i);
            matrix[i][j++] = cU.getIdCoppia();
            matrix[i][j++] = cU.getIdUtente();
            matrix[i][j++] = cU.getNome();
            matrix[i][j++] = cU.getCognome();
            matrix[i][j++] = cU.getClassifica();
            matrix[i][j++] = cU.getSesso();
            j = 0;
        }

        return matrix;
    }
}
