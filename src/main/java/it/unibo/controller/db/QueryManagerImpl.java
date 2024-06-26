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
import it.unibo.controller.db.views.ViewIscrittiDoppi;
import it.unibo.controller.db.views.ViewIscrittiSingoli;
import it.unibo.controller.db.views.ViewIscrizioniGiocatore;
import it.unibo.controller.db.views.ViewTornei;
import it.unibo.controller.db.views.ViewUnioni;
import it.unibo.model.Circolo;
import it.unibo.model.CompagnoUnioni;
import it.unibo.model.Coppia;
import it.unibo.model.CoppieIscritte;
import it.unibo.model.EdizioneTorneo;
import it.unibo.model.Giocatore;
import it.unibo.model.GiocatoriIscritti;
import it.unibo.model.Iscrizione;
import it.unibo.model.IscrizioniWithTorneo;
import it.unibo.model.Organizzatore;
import it.unibo.model.Torneo;
import it.unibo.model.Unione;
import it.unibo.model.TorneiWithEdizioni;
import it.unibo.model.Torneo.Tipo;
import it.unibo.utils.Pair;

public class QueryManagerImpl implements QueryManager {

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
    private final ViewIscrittiSingoli viewIscrittiSingoli;
    private final ViewIscrittiDoppi viewIscrittiDoppi;
    private final ViewIscrizioniGiocatore viewIscrizioniGiocatore;

    public QueryManagerImpl(final ConnectionProvider connectionProvider) {
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
        this.viewIscrittiSingoli = new ViewIscrittiSingoli(connectionProvider.getMySQLConnection());
        this.viewIscrittiDoppi = new ViewIscrittiDoppi(connectionProvider.getMySQLConnection());
        this.viewIscrizioniGiocatore = new ViewIscrizioniGiocatore(connectionProvider.getMySQLConnection());
    }

    @Override
    public Organizzatore createOrganizzatore(final String nome,
            final String cognome,
            final String email,
            final String password) {

        return new Organizzatore(nome, cognome, email, password);
    }

    @Override
    public void addOrganizzatore(final Organizzatore organizzatore) {
        this.organizzatore.save(organizzatore);
    }

    @Override
    public Integer getIdOrganizzatore(final Organizzatore organizzatore) {
        return organizzatore.getId();
    }

    @Override
    public Optional<Organizzatore> findOrganizzatore(final Integer id) {
        return this.organizzatore.findByPrimaryKey(id);
    }

    @Override
    public Optional<Organizzatore> findOrganizzatoreByCredentials(final String email, final String password) {
        return this.organizzatore.findOrganizzatoreByCredentials(email, password);
    }

    @Override
    public void updateOrganizzatore(final Integer id,
        final String nome,
        final String cognome,
        final String email,
        final String password) {

        this.organizzatore.update(new Organizzatore(id, nome, cognome, email, password));
    }

    @Override
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

    @Override
    public void addGiocatore(final Giocatore giocatore) {
        this.giocatore.save(giocatore);
    }

    @Override
    public Integer getIdGiocatore(final Giocatore giocatore) {
        return giocatore.getId();
    }

    @Override
    public Optional<Giocatore> findGiocatore(final Integer id) {
        return this.giocatore.findByPrimaryKey(id);
    }

    @Override
    public Optional<Giocatore> findGiocatoreByCredentials(final String email, final String password) {
        return this.giocatore.findGiocatoreByCredentials(email, password);
    }

    @Override
    public List<Giocatore> findTopGiocatori(final Integer annoI, final Integer annoF) {
        return this.giocatore.findTopGiocatori(annoI, annoF);
    }

    @Override
    public void updateGiocatore(final Integer id,
        final String nome,
        final String cognome,
        final String email,
        final String password,
        final String tessera,
        final String classifica,
        final int eta,
        final String sesso,
        final String telefono,
        final Integer circolo) {

        this.giocatore.update(new Giocatore(
            id, nome, cognome, email, password, tessera, classifica, eta, sesso, telefono, circolo)
        );
    }

    @Override
    public Object[][] listGiocatoriToMatrix(final List<Giocatore> list, final int col) {
        Object[][] matrix = new Object[list.size()][col];
        Giocatore g;
        int j = 0;

        for (int i = 0; i < list.size(); i++) {
            g = list.get(i);
            matrix[i][j++] = g.getId();
            matrix[i][j++] = g.getNome();
            matrix[i][j++] = g.getCognome();
            matrix[i][j++] = g.getEmail();
            matrix[i][j++] = g.getTessera();
            matrix[i][j++] = g.getClassifica();
            matrix[i][j++] = g.getEta();
            matrix[i][j++] = g.getSesso();
            matrix[i][j++] = g.getTelefono();
            matrix[i][j++] = g.getIdCircolo();
            j = 0;
        }

        return matrix;
    }

    @Override
    public Circolo createCircolo(
        final Integer organizzatore,
        final String nome,
        final String citta,
        final String indirizzo,
        final String telefono) {
        
        return new Circolo(organizzatore, nome, citta, indirizzo, telefono);
    }

    @Override
    public void addCircolo(final Circolo circolo) {
        this.circolo.save(circolo);
    }

    @Override
    public Optional<Circolo> findCircolo(final Integer id) {
        return this.circolo.findByPrimaryKey(id);
    }

    @Override
    public Optional<Circolo> findCircoloByOrganizzatore(final int organizzatore) {
        return this.circolo.findCircoloByOrganizzatore(organizzatore);
    }

    @Override
    public List<Circolo> findAllCircolo() {
        return this.circolo.findAll();
    }

    @Override
    public List<Circolo> findTopCircoli(final Integer annoI, final Integer annoF) {
        return this.circolo.findTopCircoli(annoI, annoF);
    }

    @Override
    public Object[][] listCircoliToMatrix(final List<Circolo> list, final int col) {
        Object[][] matrix = new Object[list.size()][col];
        Circolo c;
        int j = 0;

        for (int i = 0; i < list.size(); i++) {
            c = list.get(i);
            matrix[i][j++] = c.getId();
            matrix[i][j++] = c.getOrganizzatore();
            matrix[i][j++] = c.getNome();
            matrix[i][j++] = c.getCitta();
            matrix[i][j++] = c.getIndirizzo();
            matrix[i][j++] = c.getTelefono();
            j = 0;
        }

        return matrix;
    }

    @Override
    public Torneo createTorneo(final Tipo tipo,
        final Optional<Integer> limiteCategoria,
        final Optional<Integer> limiteEta,
        final Optional<Integer> montepremi) {

        return new Torneo(tipo, limiteCategoria, limiteEta, montepremi);
    }

    @Override
    public void addTorneo(final Torneo torneo) {
        this.torneo.save(torneo);
    }

    @Override
    public Optional<Torneo> findTorneo(final Integer id) {
        return this.torneo.findByPrimaryKey(id);
    }

    @Override
    public List<Torneo> findAllTorneo() {
        return this.torneo.findAll();
    }

    @Override
    public List<Torneo> findAllTorneoByCircolo(final Circolo circolo) {
        return this.torneo.findAllByCircolo(circolo.getId());
    }

    @Override
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

    @Override
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

    @Override
    public Integer getIdLastTorneo() {
        return this.torneo.getLastId();
    }

    @Override
    public void deleteTorneo(final Integer id) {
        this.torneo.delete(id);
    }

    @Override
    public EdizioneTorneo createEdizioneTorneo(final Integer idTorneo,
            final Integer nEdizione,
            final Date dInizio,
            final Date dFine,
            final Integer circolo) {

        return new EdizioneTorneo(idTorneo, nEdizione, dInizio, dFine, circolo);
    }

    @Override
    public void addEdizioneTorneo(final EdizioneTorneo eTorneo) {
        this.edizioneTorneo.save(eTorneo);
    }

    @Override
    public EdizioneTorneo findEdizioneTorneo(final Pair<Integer, Integer> key) {
        return this.edizioneTorneo.findByPrimaryKey(key).get();
    }

    @Override
    public int getNumeroEdizione(final Torneo torneo) {
        return this.edizioneTorneo.getEdizioneTorneo(torneo.getId());
    }

    @Override
    public void deleteEdizioneTorneo(final Pair<Integer, Integer> key) {
        this.edizioneTorneo.delete(key);
    }

    @Override
    public List<EdizioneTorneo> findAllEdizioneByTorneo(final Torneo torneo) {
        return this.edizioneTorneo.findAllByTorneo(torneo.getId());
    }

    @Override
    public Object[][] listTorneiToMatrix(final List<TorneiWithEdizioni> list, final int col) {
        Object[][] matrix = new Object[list.size()][col];
        DateFormat df = new SimpleDateFormat("dd-MM-YYYY");
        TorneiWithEdizioni vt;
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
            if (col == 9) {
                matrix[i][j++] = vt.getIdCircolo();
            }
            j = 0;
        }

        return matrix;
    }

    @Override
    public Iscrizione createIscrizione(final Integer idIscrizione,
            final Optional<String> prefOrario,
            final Integer idTorneo,
            final Integer nEdizione,
            final Optional<Integer> idUtente,
            final Optional<Integer> idCoppia) {

        return new Iscrizione(idIscrizione, prefOrario, idTorneo, nEdizione, idUtente, idCoppia);
    }

    @Override
    public void addIscrizione(final Iscrizione iscrizione) {
        this.iscrizione.save(iscrizione);
    }

    @Override
    public List<GiocatoriIscritti> findAllGiocatoriIscrittiByEdizioneTorneo(final EdizioneTorneo edizioneTorneo) {
        return this.viewIscrittiSingoli.findAllIscrittiByEdizioneTorneo(edizioneTorneo.getIdTorneo(), edizioneTorneo.getNumeroEdizione());
    }

    @Override
    public List<GiocatoriIscritti> findAllIscrittiByPreferenzaOrario(final String prefOrario, final Pair<Integer, Integer> edizione) {
        return this.viewIscrittiSingoli.findAllIscrittiByPreferenzaOrario(prefOrario, edizione);
    }

    @Override
    public List<GiocatoriIscritti> findAllIscrittiOrderByClassifica(final Pair<Integer, Integer> edizione) {
        return this.viewIscrittiSingoli.orderAllIscrittiByClassifica(edizione);
    }

    @Override
    public Object[][] listGiocatoriIscrittiToMatrix(final List<GiocatoriIscritti> list, final int col) {
        Object[][] matrix = new Object[list.size()][col];
        GiocatoriIscritti gI;
        int j = 0;

        for (int i = 0; i < list.size(); i++) {
            gI = list.get(i);
            matrix[i][j++] = gI.getIdUtente();
            matrix[i][j++] = gI.getNome();
            matrix[i][j++] = gI.getCognome();
            matrix[i][j++] = gI.getEmail();
            matrix[i][j++] = gI.getTessera();
            matrix[i][j++] = gI.getClassifica();
            matrix[i][j++] = gI.getEta();
            matrix[i][j++] = gI.getTelefono();
            matrix[i][j++] = gI.getPrefOrario();
            j = 0;
        }

        return matrix;
    }

    @Override
    public List<CoppieIscritte> findAllCoppieIscritteByEdizioneTorneo(final EdizioneTorneo edizioneTorneo) {
        return this.viewIscrittiDoppi.findAllIscrittiByEdizioneTorneo(edizioneTorneo.getIdTorneo(), edizioneTorneo.getNumeroEdizione());
    }

    @Override
    public List<CoppieIscritte> findAllCoppieIscritteByPreferenzaOrario(final String prefOrario, final Pair<Integer, Integer> edizione) {
        return this.viewIscrittiDoppi.findAllIscrittiByPreferenzaOrario(prefOrario, edizione);
    }

    @Override
    public Object[][] listCoppieIscritteToMatrix(final List<CoppieIscritte> list, final int col) {
        Object[][] matrix = new Object[list.size()][col];
        CoppieIscritte cI;
        int j = 0;

        for (int i = 0; i < list.size(); i++) {
            cI = list.get(i);
            matrix[i][j++] = cI.getIdCoppia();
            matrix[i][j++] = cI.getGiocatore1();
            matrix[i][j++] = cI.getGiocatore2();
            matrix[i][j++] = cI.getTessera1();
            matrix[i][j++] = cI.getTessera2();
            matrix[i][j++] = cI.getClassifica1();
            matrix[i][j++] = cI.getClassifica2();
            matrix[i][j++] = cI.getTelefono1();
            matrix[i][j++] = cI.getTelefono2();
            matrix[i][j++] = cI.getPrefOrario();
            j = 0;
        }

        return matrix;
    }

    @Override
    public List<TorneiWithEdizioni> findAllEdizioniByCircolo(final Circolo circolo) {
        return this.viewTornei.findAllEdizioniByCircolo(circolo);
    }

    @Override
    public List<TorneiWithEdizioni> findAllSingolariEligibleByPlayer(final Giocatore giocatore) {
        return this.viewTornei.findAllSingolariEligibleByPlayer(giocatore);
    }

    @Override
    public List<TorneiWithEdizioni> findAllDoppiEligibleByCoppia(final Pair<Giocatore, Giocatore> coppia, final Integer id) {
        return this.viewTornei.findAllDoppiEligibleByCoppia(coppia, id);
    }

    @Override
    public List<TorneiWithEdizioni> findAllFiltered(final Giocatore giocatore,
            final Optional<Integer> cat,
            final Optional<Integer> eta,
            final Optional<String> data) {
        return this.viewTornei.findAllFiltered(giocatore, cat, eta, data);
    }

    @Override
    public Coppia createCoppia(final Integer id) {
        return new Coppia(id);
    }

    @Override
    public int addCoppia(final Coppia coppia) {
        this.coppia.save(coppia);
        return this.coppia.getLastId();
    }

    @Override
    public Optional<Coppia> findCoppia(final Integer id) {
        return this.coppia.findByPrimaryKey(id);
    }

    @Override
    public Unione createUnione(final Integer idCoppia, final Integer idUtente) {
        return new Unione(idCoppia, idUtente);
    }

    @Override
    public void addUnione(final Unione unione) {
        this.unione.save(unione);
    }

    @Override
    public List<Integer> findAllEligibleUnioni(final Giocatore giocatore) {
        return this.viewUnioni.findAllEligibleUnioni(giocatore.getId(), giocatore.getSesso())
            .stream()
            .map(u -> u.getIdCoppia())
            .toList();
    }

    @Override
    public Pair<Giocatore, Giocatore> findGiocatoriOfCoppia(final Coppia coppia) {
        final Pair<Integer, Integer> ids = this.unione.findIdGiocatoriOfCoppia(coppia.getId());
        return new Pair<Giocatore, Giocatore>(this.findGiocatore(ids.getX()).get(), this.findGiocatore(ids.getY()).get());
    }

    @Override
    public List<CompagnoUnioni> findAllUnioniByGiocatore(final Giocatore giocatore) {
        return this.viewUnioni.findAllMyUnioni(giocatore.getId());
    }

    @Override
    public List<CompagnoUnioni> findAllEligibleCompagniUnioneForGiocatore(final Giocatore giocatore) {
        return this.viewUnioni.findAllEligibleUnioni(giocatore.getId(), giocatore.getSesso());
    }

    @Override
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

    @Override
    public List<IscrizioniWithTorneo> findAllIscrizioniByGiocatore(final Giocatore giocatore) {
        return this.viewIscrizioniGiocatore.findByGiocatore(giocatore);
    }

    @Override
    public List<IscrizioniWithTorneo> findAllIscrizioniByCoppia(final Coppia coppia) {
        return this.viewIscrizioniGiocatore.findByCoppia(coppia.getId());
    }

    @Override
    public Object[][] listIscrizioniToMatrix(final List<IscrizioniWithTorneo> list, final int col) {
        Object[][] matrix = new Object[list.size()][col];
        DateFormat df = new SimpleDateFormat("dd-MM-YYYY");
        IscrizioniWithTorneo iT;
        int j = 0;

        for (int i = 0; i < list.size(); i++) {
            iT = list.get(i);
            matrix[i][j++] = iT.getIdTorneo();
            matrix[i][j++] = iT.getNumEdizione();
            matrix[i][j++] = iT.getTipo();
            matrix[i][j++] = df.format(iT.getDataInizio());
            matrix[i][j++] = df.format(iT.getDataFine());
            matrix[i][j++] = iT.getNomeCircolo();
            matrix[i][j++] = iT.getLimCategoria();
            matrix[i][j++] = iT.getLimEta();
            matrix[i][j++] = iT.getMontepremi();
            matrix[i][j++] = iT.getPreferenzaOrario();
            j = 0;
        }

        return matrix;
    }

    @Override
    public void deleteIscrizioneByEdizione(final Pair<Integer, Integer> edizione, final Optional<Integer> idUtente, final Optional<Integer> idCoppia) {
        if (idCoppia.isPresent()) {
            this.viewIscrizioniGiocatore.deleteIscrizioneCoppiaByEdizione(edizione, idCoppia.get());
        } else {
            this.viewIscrizioniGiocatore.deleteIscrizioneGiocatoreByEdizione(edizione, idUtente.get());
        }
    }
}
