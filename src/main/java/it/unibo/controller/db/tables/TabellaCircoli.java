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

    public List<Circolo> findTopCircoli(final Integer sYear, final Integer eYear) {
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE Id_Circolo IN (" +
            "SELECT * FROM (" +
                "SELECT Id_Circolo FROM EDIZIONI_TORNEO " +
                "WHERE YEAR (Data_Inizio) BETWEEN ? AND ? " +
                "GROUP BY Id_Circolo ORDER BY COUNT(Id_Circolo) DESC " +
                "LIMIT 3) AS topCircoli)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, sYear);
            statement.setInt(2, eYear);
            final ResultSet resultSet = statement.executeQuery();
            return readCircoliFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Circolo> readCircoliFromResultSet(final ResultSet resultSet) {
        final List<Circolo> circoli = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final int id = resultSet.getInt("Id_Circolo");
                final int organizzatore = resultSet.getInt("Organizzatore");
                final String nome = resultSet.getString("Nome");
                final String citta = resultSet.getString("Citta");
                final String indirizzo = resultSet.getString("Indirizzo");
                final String telefono = resultSet.getString("Telefono");

                final Circolo circolo = new Circolo(id, organizzatore, nome, citta, indirizzo, telefono);
                circoli.add(circolo);
            }
        } catch (final SQLException e) {}
        return circoli;
    }
}
