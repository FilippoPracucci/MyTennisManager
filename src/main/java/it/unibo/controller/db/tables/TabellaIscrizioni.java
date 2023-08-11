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
import it.unibo.model.Iscrizione;

public class TabellaIscrizioni implements Table<Iscrizione, Integer> {

    public static final String TABLE_NAME = "ISCRIZIONI";

    private final Connection connection; 

    public TabellaIscrizioni(final Connection connection) {
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
                        "Id_Iscrizione INT NOT NULL PRIMARY KEY," +
                        "Preferenza_Orario VARCHAR(10) CHECK (Preferenza_Orario in ('9:00-13:00', '15:00-21:00'))," +
                        "Id_Torneo INT NOT NULL," + 
                        "Numero_Edizione INT NOT NULL," + 
                        "Id_Utente INT," +
                        "Id_Coppia INT" +
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
    public Optional<Iscrizione> findByPrimaryKey(final Integer id) {
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE Id_Iscrizione = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            final ResultSet resultSet = statement.executeQuery();
            return readIscrizioniFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Iscrizione> readIscrizioniFromResultSet(final ResultSet resultSet) {
        final List<Iscrizione> iscrizioni = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns 
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get methods 
                final int id = resultSet.getInt("Id_Iscrizione");
                final Optional<String> pref_orario = Optional.ofNullable(resultSet.getString("Preferenza_Orario"));
                final int torneo = resultSet.getInt("Id_Torneo");
                final int num_edizione = resultSet.getInt("Numero_Edizione");
                final Optional<Integer> utente = Optional.ofNullable(resultSet.getInt("Id_Utente"));
                final Optional<Integer> coppia = Optional.ofNullable(resultSet.getInt("Id_Coppia"));
                // After retrieving all the data we create a Student object
                final Iscrizione iscrizione = new Iscrizione(id, pref_orario, torneo, num_edizione, utente, coppia);
                iscrizioni.add(iscrizione);
            }
        } catch (final SQLException e) {}
        return iscrizioni;
    }

    @Override
    public List<Iscrizione> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readIscrizioniFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Iscrizione iscrizione) {
        final String query = "INSERT INTO " + TABLE_NAME +
                "(id, preferenza_orario, torneo, numero_edizione, utente, coppia) VALUES (?,?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, iscrizione.getId());
            statement.setString(2, iscrizione.getPreferenzaOrario().orElse(null));
            statement.setInt(3, iscrizione.getIdTorneo());
            statement.setInt(4, iscrizione.getNumeroEdizione());
            statement.setInt(5, iscrizione.getIdUtente().orElse(null));
            statement.setInt(6, iscrizione.getIdCoppia().orElse(null));
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Iscrizione iscrizione) {
        final String query =
            "UPDATE " + TABLE_NAME + " SET " +
                "Preferenza_Orario = ?," + 
                "Id_Torneo = ?," +
                "Numero_Edizione = ?," +
                "Id_Utente = ?," + 
                "Id_Coppia = ?" +
            "WHERE Id_Iscrizione = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, iscrizione.getPreferenzaOrario().get());
            statement.setInt(2, iscrizione.getIdTorneo());
            statement.setInt(3, iscrizione.getNumeroEdizione());
            statement.setInt(4, iscrizione.getIdUtente().get());
            statement.setInt(5, iscrizione.getIdCoppia().get());
            statement.setInt(6, iscrizione.getId());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final Integer id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE Id_Iscrizione = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
