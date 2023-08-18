package it.unibo.controller.db.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import it.unibo.controller.db.Table;
import it.unibo.model.EdizioneTorneo;
import it.unibo.utils.Pair;
import it.unibo.utils.Utils;

public class TabellaEdizioniTorneo implements Table<EdizioneTorneo, Pair<Integer, Integer>> {

    public static final String TABLE_NAME = "EDIZIONE_TORNEI";

    private final Connection connection; 

    public TabellaEdizioniTorneo(final Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public boolean createTable() {
        try (final Statement statement = this.connection.createStatement()) {
            statement.executeUpdate(
                "CREATE TABLE " + TABLE_NAME + " (" +
                        "Id_Torneo INT NOT NULL," +
                        "Numero_Edizione INT NOT NULL," + 
                        "Data_Inizio DATE NOT NULL," + 
                        "Data_Fine DATE NOT NULL," +
                        "Id_Circolo INT NOT NULL," +
                        "PRIMARY KEY(Id_Torneo, Numero_Edizione)," +
                        "UNIQUE(Id_Torneo, Numero_Edizione, Id_Circolo)" +
                    ")");
            return true;
        } catch (final SQLException e) {
            return false;
        }
    }

    @Override
    public boolean dropTable() {
        try (final Statement statement = this.connection.createStatement()) {
            statement.executeUpdate("DROP TABLE " + TABLE_NAME);
            return true;
        } catch (final SQLException e) {
            return false;
        }
    }

    @Override
    public Optional<EdizioneTorneo> findByPrimaryKey(final Pair<Integer, Integer> key) {
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE Id_Torneo = ? AND Numero_Edizione = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, key.getX());
            statement.setInt(2, key.getY());
            final ResultSet resultSet = statement.executeQuery();
            return readEdizioniTorneoFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<EdizioneTorneo> readEdizioniTorneoFromResultSet(final ResultSet resultSet) {
        final List<EdizioneTorneo> edizioniTorneo = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns 
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get methods 
                final int id = resultSet.getInt("Id_Torneo");
                final int numeroEdizione = resultSet.getInt("Numero_Edizione");
                final Date dataInizio = Utils.sqlDateToDate(resultSet.getDate("Data_Inizio"));
                final Date dataFine = Utils.sqlDateToDate(resultSet.getDate("Data_Fine"));
                final int circolo = resultSet.getInt("Id_Circolo");
                // After retrieving all the data we create a Student object
                final EdizioneTorneo edizioneTorneo = new EdizioneTorneo(id, numeroEdizione, dataInizio, dataFine, circolo);
                edizioniTorneo.add(edizioneTorneo);
            }
        } catch (final SQLException e) {}
        return edizioniTorneo;
    }

    @Override
    public List<EdizioneTorneo> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readEdizioniTorneoFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final EdizioneTorneo edizioneTorneo) {
        final String query = "INSERT INTO " + TABLE_NAME +
                "(Id_Torneo, Numero_Edizione, Data_Inizio, Data_Fine, Id_Circolo) VALUES (?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, edizioneTorneo.getIdTorneo());
            statement.setInt(2, edizioneTorneo.getNumeroEdizione());
            statement.setDate(3, Utils.dateToSqlDate(edizioneTorneo.getDataInizio()));
            statement.setDate(4, Utils.dateToSqlDate(edizioneTorneo.getDataFine()));
            statement.setInt(5, edizioneTorneo.getIdCircolo());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final EdizioneTorneo edizioneTorneo) {
        final String query =
            "UPDATE " + TABLE_NAME + " SET " +
                "Data_Inizio = ?," + 
                "Data_Fine = ?," +
                "Id_Circolo = ?" +
            "WHERE Id_Torneo = ? AND Numero_Edizione = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDate(1, Utils.dateToSqlDate(edizioneTorneo.getDataInizio()));
            statement.setDate(2, Utils.dateToSqlDate(edizioneTorneo.getDataFine()));
            statement.setInt(3, edizioneTorneo.getIdCircolo());
            statement.setInt(4, edizioneTorneo.getIdTorneo());
            statement.setInt(5, edizioneTorneo.getNumeroEdizione());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final Pair<Integer, Integer> key) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE Id_Torneo = ? AND Numero_Edizione = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, key.getX());
            statement.setInt(2, key.getY());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public int getEdizioneTorneo(final Integer idTorneo) {
        final String query = "SELECT MAX(Numero_Edizione) AS Numero_Edizione FROM " + TABLE_NAME + " WHERE Id_Torneo = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, idTorneo);
            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("Numero_Edizione");
            } else {
                return 0;
            }
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<EdizioneTorneo> findByCircolo(final Integer idCircolo) {
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE Id_Circolo = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, idCircolo);
            final ResultSet resultSet = statement.executeQuery();
            return readEdizioniTorneoFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<EdizioneTorneo> findAllByTorneo(final Integer idTorneo) {
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE Id_Torneo = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, idTorneo);
            final ResultSet resultSet = statement.executeQuery();
            return readEdizioniTorneoFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
