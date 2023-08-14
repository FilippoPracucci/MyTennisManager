package it.unibo.controller.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import it.unibo.model.ViewTornei;
import it.unibo.model.Torneo.Tipo;
import it.unibo.utils.Pair;
import it.unibo.utils.Utils;

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

    public Integer getIdLastTorneo() {
        return this.torneo.getLastId();
    }

    public void deleteTorneo(final Integer id) {
        this.torneo.delete(id);
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

    public List<ViewTornei> findAllEdizioniByCircolo(final Circolo circolo) {
        final String query =
            "SELECT t.Id_Torneo, et.Numero_Edizione, t.Tipo, et.Data_Inizio, et.Data_Fine, t.Limite_Categoria, t.Limite_Eta, t.Montepremi " +
            "FROM " + this.torneo.getTableName() + " t " +
            "JOIN " + this.edizioneTorneo.getTableName() + " et " +
            "ON (t.Id_Torneo = et.Id_Torneo) " +
            "WHERE Id_Circolo = ?";
        try (final PreparedStatement statement = this.edizioneTorneo.getConnection().prepareStatement(query)) {
            statement.setInt(1, circolo.getId());
            final ResultSet resultSet = statement.executeQuery();
            return readViewTorneiFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<ViewTornei> readViewTorneiFromResultSet(final ResultSet resultSet) {
        final List<ViewTornei> tornei = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns 
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get methods 
                final Integer idTorneo = resultSet.getInt("Id_Torneo");
                final Integer nEdizione = resultSet.getInt("Numero_Edizione");
                final Tipo tipo = Tipo.getTipo(resultSet.getString("Tipo"));
                final Date dInizio = Utils.sqlDateToDate(resultSet.getDate("Data_Inizio"));
                final Date dFine = Utils.sqlDateToDate(resultSet.getDate("Data_Fine"));
                final Optional<Integer> limite_categoria = Optional.ofNullable(resultSet.getInt("Limite_Categoria"));
                final Optional<Integer> limite_eta = Optional.ofNullable(resultSet.getInt("Limite_Eta"));
                final Optional<Integer> montepremi = Optional.ofNullable(resultSet.getInt("Montepremi"));
                // After retrieving all the data we create a Student object
                final ViewTornei torneo = new ViewTornei(idTorneo, nEdizione, tipo, dInizio, dFine, limite_categoria, limite_eta, montepremi);
                tornei.add(torneo);
            }
        } catch (final SQLException e) {}
        return tornei;
    }

    public Object[][] listTorneiToMatrix(final List<ViewTornei> list, final int col) {
        Object[][] matrix = new Object[list.size()][col];
        DateFormat df = new SimpleDateFormat("dd-MM-YYYY");
        ViewTornei vt;
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
            j = 0;
        }

        return matrix;
    }
}
