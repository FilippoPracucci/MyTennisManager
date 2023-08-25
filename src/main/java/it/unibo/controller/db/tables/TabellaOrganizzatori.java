package it.unibo.controller.db.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import it.unibo.controller.db.Table;
import it.unibo.model.Organizzatore;

public class TabellaOrganizzatori implements Table<Organizzatore, Integer> {

    public static final String TABLE_NAME = "ORGANIZZATORI";

    private final Connection connection; 

    public TabellaOrganizzatori(final Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Optional<Organizzatore> findByPrimaryKey(final Integer id) {
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE Id_Utente = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            final ResultSet resultSet = statement.executeQuery();
            return readOrganizzatoriFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public Optional<Organizzatore> findOrganizzatoreByCredentials(final String email, final String password) {
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE Email = ? AND Password = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);
            final ResultSet resultSet = statement.executeQuery();
            return readOrganizzatoriFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<Organizzatore> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readOrganizzatoriFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Organizzatore organizzatore) {
        final String query = "INSERT INTO " + TABLE_NAME +
                "(Nome, Cognome, Email, Password) VALUES (?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, organizzatore.getNome());
            statement.setString(2, organizzatore.getCognome());
            statement.setString(3, organizzatore.getEmail());
            statement.setString(4, organizzatore.getPassword());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Organizzatore organizzatore) {
        final String query =
            "UPDATE " + TABLE_NAME + " SET " +
                "Nome = ?," + 
                "Cognome = ?," +
                "Email = ?," + 
                "Password = ?" +
            "WHERE Id_Utente = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, organizzatore.getNome());
            statement.setString(2, organizzatore.getCognome());
            statement.setString(3, organizzatore.getEmail());
            statement.setString(4, organizzatore.getPassword());
            statement.setInt(5, organizzatore.getId());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final Integer id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE Id_Utente = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public Integer getNextId() {
        final String query = "SELECT TOP(1) Id_Utente FROM " + TABLE_NAME + " ORDER BY Id_Utente DESC";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            return statement.executeUpdate();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Organizzatore> readOrganizzatoriFromResultSet(final ResultSet resultSet) {
        final List<Organizzatore> organizzatori = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final int id = resultSet.getInt("Id_Utente");
                final String nome = resultSet.getString("Nome");
                final String cognome = resultSet.getString("Cognome");
                final String email = resultSet.getString("Email");
                final String password = resultSet.getString("Password");

                final Organizzatore organizzatore = new Organizzatore(id, nome, cognome, email, password);
                organizzatori.add(organizzatore);
            }
        } catch (final SQLException e) {}
        return organizzatori;
    }
}
