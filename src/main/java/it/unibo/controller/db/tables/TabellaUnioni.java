package it.unibo.controller.db.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import it.unibo.controller.db.Table;
import it.unibo.model.Unione;
import it.unibo.utils.Pair;

public class TabellaUnioni implements Table<Unione, Pair<Integer, Integer>> {

    public static final String TABLE_NAME = "UNIONI";

    private final Connection connection; 

    public TabellaUnioni(final Connection connection) {
        this.connection = Objects.requireNonNull(connection);
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
                        "Id_Coppia INT NOT NULL," +
                        "Id_Utente INT NOT NULL," +
                        "PRIMARY KEY (Id_Coppia, Id_Utente)" +
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
    public Optional<Unione> findByPrimaryKey(final Pair<Integer, Integer> key) {
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE Id_Coppia = ? AND Id_Utente = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, key.getX());
            statement.setInt(2, key.getY());
            final ResultSet resultSet = statement.executeQuery();
            return readUnioniFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Unione> readUnioniFromResultSet(final ResultSet resultSet) {
        final List<Unione> unioni = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns 
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get methods 
                final int idCoppia = resultSet.getInt("Id_Coppia");
                final int idUtente = resultSet.getInt("Id_Utente");
                // After retrieving all the data we create a Student object
                final Unione unione = new Unione(idCoppia, idUtente);
                unioni.add(unione);
            }
        } catch (final SQLException e) {}
        return unioni;
    }

    @Override
    public List<Unione> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readUnioniFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Unione unione) {
        final String query = "INSERT INTO " + TABLE_NAME + "(Id_Coppia, Id_Utente) VALUES (?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, unione.getIdCoppia());
            statement.setInt(2, unione.getIdUtente());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /*
     * Impossibile fare una query di UPDATE per un'unione.
     */
    @Override
    public boolean update(final Unione unione) {
        return false;
    }

    @Override
    public boolean delete(final Pair<Integer, Integer> key) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE Id_Coppia = ? AND Id_Utente = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, key.getX());
            statement.setInt(2, key.getY());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}