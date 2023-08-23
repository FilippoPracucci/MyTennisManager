package it.unibo.controller.db.views;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import it.unibo.controller.db.View;
import it.unibo.model.Circolo;
import it.unibo.model.Giocatore;
import it.unibo.model.TorneiWithEditions;
import it.unibo.model.Torneo.Tipo;
import it.unibo.utils.Pair;
import it.unibo.utils.Utils;

public class ViewTornei implements View<TorneiWithEditions, Pair<Integer, Integer>> {

    public static final String VIEW_NAME = "TORNEI_CON_EDIZIONI";

    private final Connection connection; 

    public ViewTornei(final Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }

    @Override
    public boolean createView() {
        try (final Statement statement = this.connection.createStatement()) {
            statement.executeUpdate(
                "CREATE VIEW " + VIEW_NAME + " AS (" +
                "SELECT t.Id_Torneo, et.Numero_Edizione, t.Tipo, et.Data_Inizio, et.Data_Fine, t.Limite_Categoria, t.Limite_Eta, t.Montepremi, et.Id_Circolo " +
                "FROM TORNEI t " +
                "JOIN EDIZIONE_TORNEI et " +
                "ON (t.Id_Torneo = et.Id_Torneo))");
            return true;
        } catch (final SQLException e) {
            return false;
        }
    }

    @Override
    public boolean dropView() {
        try (final Statement statement = this.connection.createStatement()) {
            statement.executeUpdate("DROP VIEW " + VIEW_NAME);
            return true;
        } catch (final SQLException e) {
            return false;
        }
    }

    @Override
    public Optional<TorneiWithEditions> findByPrimaryKey(final Pair<Integer, Integer> key) {
        final String query = "SELECT * FROM " + VIEW_NAME + " WHERE Id_Torneo = ? AND Numero_Edizione = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, key.getX());
            statement.setInt(2, key.getY());
            final ResultSet resultSet = statement.executeQuery();
            return readTorneiWithEditionsFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<TorneiWithEditions> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + VIEW_NAME);
            return readTorneiWithEditionsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<TorneiWithEditions> findAllEdizioniByCircolo(final Circolo circolo) {
        final String query =
            "SELECT * FROM " + VIEW_NAME +
            " WHERE Id_Circolo = ?";
        try (final PreparedStatement statement = this.getConnection().prepareStatement(query)) {
            statement.setInt(1, circolo.getId());
            final ResultSet resultSet = statement.executeQuery();
            return this.readTorneiWithEditionsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<TorneiWithEditions> findAllSingolariEligibleByPlayer (final Giocatore giocatore) {
        String t;
        final char first = giocatore.getClassifica().charAt(0);
        final int cat;
        if (giocatore.getSesso().contentEquals("M")) {
            t = Tipo.SINGOLARE_MASCHILE.getNome();
        } else {
            t = Tipo.SINGOLARE_FEMMINILE.getNome();
        }
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
        final String query =
            "SELECT et.* FROM " + VIEW_NAME + " et LEFT JOIN ISCRIZIONI i " +
            "ON (et.Id_Torneo = i.Id_Torneo AND et.Numero_Edizione = i.Numero_Edizione) " +
            "WHERE (Limite_Eta IS NULL OR Limite_Eta >= ?) " +
            "AND (Limite_Categoria IS NULL OR Limite_Categoria <= ?) " +
            "AND Tipo = ? " +
            "AND ? not in (" +
                "SELECT Id_Utente FROM ISCRIZIONI WHERE Id_Torneo = i.Id_Torneo AND Numero_Edizione = i.Numero_Edizione)";
        try (final PreparedStatement statement = this.getConnection().prepareStatement(query)) {
            statement.setInt(1, giocatore.getEta());
            statement.setInt(2, cat);
            statement.setString(3, t);
            statement.setInt(4, giocatore.getId());
            final ResultSet resultSet = statement.executeQuery();
            return this.readTorneiWithEditionsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<TorneiWithEditions> findAllDoppiEligibleByCoppia (final Pair<Giocatore, Giocatore> coppia, final Integer id) {
        final char first1 = coppia.getX().getClassifica().charAt(0);
        final char first2 = coppia.getY().getClassifica().charAt(0);
        final int cat;
        final String t;
        if (coppia.getX().getSesso().contentEquals("M")) {
            t = Tipo.DOPPIO_MASCHILE.getNome();
        } else {
            t = Tipo.DOPPIO_FEMMINILE.getNome();
        }
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
        final String query =
            /*"SELECT et.*, i.Id_Coppia FROM " + VIEW_NAME + " et JOIN ISCRIZIONI i " +
            "ON (et.Id_Torneo = i.Id_Torneo AND et.Numero_Edizione = i.Numero_Edizione) " +
            "WHERE i.Id_Utente NOT IN (" +
                "SELECT cu.Id_Utente FROM COMPAGNO_UNIONI cu " +
                "WHERE cu.Id_Coppia = ?) " +
            "AND (Limite_Eta IS NULL OR Limite_Eta >= ?) " +
            "AND (Limite_Categoria IS NULL OR Limite_Categoria <= ?) " +
            "AND Tipo = ?";*/
            "SELECT et.* FROM " + VIEW_NAME + " et LEFT JOIN ISCRIZIONI i " +
            "ON (et.Id_Torneo = i.Id_Torneo AND et.Numero_Edizione = i.Numero_Edizione) " +
            "WHERE (Limite_Eta IS NULL OR Limite_Eta >= ?) " +
            "AND (Limite_Categoria IS NULL OR Limite_Categoria <= ?) " +
            "AND Tipo = ?";
        try (final PreparedStatement statement = this.getConnection().prepareStatement(query)) {
            
            //statement.setInt(1, id);
            statement.setInt(1, (coppia.getX().getEta() < coppia.getY().getEta()) ? coppia.getY().getEta() : coppia.getX().getEta());
            statement.setInt(2, cat);
            statement.setString(3, t);
            final ResultSet resultSet = statement.executeQuery();
            return this.readTorneiWithEditionsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<TorneiWithEditions> findAllFiltered(final Giocatore giocatore, final Optional<Integer> categoria, final Optional<Integer> eta, final Optional<String> data) {
        String t;
        final char first = giocatore.getClassifica().charAt(0);
        final int cat;
        if (giocatore.getSesso().contentEquals("M")) {
            t = Tipo.SINGOLARE_MASCHILE.getNome();
        } else {
            t = Tipo.SINGOLARE_FEMMINILE.getNome();
        }
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
        String query = "SELECT * FROM " + VIEW_NAME + " WHERE Tipo = ? ";
        final StringBuilder sBuilder = new StringBuilder(query);
        if (eta.isPresent()) {
            sBuilder.append("AND Limite_Eta = ? ");
        } else {
            sBuilder.append("AND (Limite_Eta IS NULL OR Limite_Eta >= ?) ");
        }
        if (categoria.isPresent()) {
            sBuilder.append("AND Limite_Categoria = ? ");
        } else {
            sBuilder.append("AND (Limite_Categoria IS NULL OR Limite_Categoria <= ?) ");
        }
        if (data.isPresent()) {
            sBuilder.append("AND YEAR(Data_Inizio) = ? ");
        }
        query = sBuilder.toString();
        try (final PreparedStatement statement = this.getConnection().prepareStatement(query)) {
            statement.setString(1, t);
            if (eta.isPresent()) {
                statement.setInt(2, eta.get());
            } else {
                statement.setInt(2, giocatore.getEta());
            }
            if (categoria.isPresent()) {
                statement.setInt(3, categoria.get());
            } else {
                statement.setInt(3, cat);
            }
            if (data.isPresent()) {
                statement.setString(4, data.get());
            }
            final ResultSet resultSet = statement.executeQuery();
            return this.readTorneiWithEditionsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<TorneiWithEditions> readTorneiWithEditionsFromResultSet(final ResultSet resultSet) {
        final List<TorneiWithEditions> tornei = new ArrayList<>();
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
                final Integer idCircolo = resultSet.getInt("Id_Circolo");
                // After retrieving all the data we create a Student object
                final TorneiWithEditions torneo = new TorneiWithEditions(idTorneo, nEdizione, tipo, dInizio, dFine, limite_categoria, limite_eta, montepremi, idCircolo);
                tornei.add(torneo);
            }
        } catch (final SQLException e) {}
        return tornei;
    }
}
