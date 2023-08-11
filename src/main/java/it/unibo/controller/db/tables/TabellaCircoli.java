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
import it.unibo.model.Circolo;

public class TabellaCircoli implements Table<Circolo, Integer> {

    public static final String TABLE_NAME = "CIRCOLI";

    private final Connection connection; 

    public TabellaCircoli(final Connection connection) {
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
                        "Id_Circolo INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                        "Organizzatore INT NOT NULL," +
                        "Nome VARCHAR(40) NOT NULL," + 
                        "Citta VARCHAR(40) NOT NULL," + 
                        "Indirizzo VARCHAR(40) NOT NULL," + 
                        "Telefono VARCHAR(12) NOT NULL UNIQUE" +
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
    public Optional<Circolo> findByPrimaryKey(final Integer id) {
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE Id_Circolo = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            final ResultSet resultSet = statement.executeQuery();
            return readCircoliFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public Optional<Circolo> findCircoloByOrganizzatore(final Integer organizzatore) {
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE Organizzatore = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, organizzatore);
            final ResultSet resultSet = statement.executeQuery();
            return readCircoliFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Circolo> readCircoliFromResultSet(final ResultSet resultSet) {
        final List<Circolo> circoli = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns 
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get methods 
                final int id = resultSet.getInt("Id_Circolo");
                final int organizzatore = resultSet.getInt("Organizzatore");
                final String nome = resultSet.getString("Nome");
                final String citta = resultSet.getString("Citta");
                final String indirizzo = resultSet.getString("Indirizzo");
                final String telefono = resultSet.getString("Telefono");
                // After retrieving all the data we create a Student object
                final Circolo circolo = new Circolo(id, organizzatore, nome, citta, indirizzo, telefono);
                circoli.add(circolo);
            }
        } catch (final SQLException e) {}
        return circoli;
    }

    @Override
    public List<Circolo> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readCircoliFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Circolo circolo) {
        final String query = "INSERT INTO " + TABLE_NAME +
                "(Organizzatore, Nome, Citta, Indirizzo, Telefono) VALUES (?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, circolo.getOrganizzatore());
            statement.setString(2, circolo.getNome());
            statement.setString(3, circolo.getCitta());
            statement.setString(4, circolo.getIndirizzo());
            statement.setString(5, circolo.getTelefono());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Circolo circolo) {
        final String query =
            "UPDATE " + TABLE_NAME + " SET " +
                "Organizzatore = ?," +
                "Nome = ?," + 
                "Citta = ?," +
                "Indirizzo = ?," + 
                "Telefono = ?" +
            "WHERE Id_Circolo = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, circolo.getOrganizzatore());
            statement.setString(2, circolo.getNome());
            statement.setString(3, circolo.getCitta());
            statement.setString(4, circolo.getIndirizzo());
            statement.setString(5, circolo.getTelefono());
            statement.setInt(6, circolo.getId());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final Integer id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE Id_Circolo = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
