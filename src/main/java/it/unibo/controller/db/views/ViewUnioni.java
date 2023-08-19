package it.unibo.controller.db.views;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import it.unibo.controller.db.View;
import it.unibo.model.CompagnoUnioni;
import it.unibo.utils.Pair;

public class ViewUnioni implements View<CompagnoUnioni, Pair<Integer, Integer>> {

    public static final String VIEW_NAME = "COMPAGNO_UNIONI";

    private final Connection connection; 

    public ViewUnioni(final Connection connection) {
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
                "SELECT u.Id_Coppia, g.Id_Utente, g.Nome, g.Cognome, g.Classifica, g.Sesso " +
                "FROM UNIONI u " + "JOIN GIOCATORI g " +
                "ON (u.Id_Giocatore = g.Id_Utente))");
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
    public Optional<CompagnoUnioni> findByPrimaryKey(final Pair<Integer, Integer> key) {
        final String query = "SELECT * FROM " + VIEW_NAME + " WHERE Id_Coppia = ? AND Id_Giocatore = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, key.getX());
            statement.setInt(2, key.getY());
            final ResultSet resultSet = statement.executeQuery();
            return readCompagnoUnioniFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<CompagnoUnioni> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + VIEW_NAME);
            return readCompagnoUnioniFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<CompagnoUnioni> findAllMyUnioni(final Integer id) {
        final String query = "SELECT * FROM " + VIEW_NAME +
            " WHERE Id_Coppia IN (" +
                "SELECT Id_Coppia FROM " + VIEW_NAME +
                " WHERE Id_Utente = ?) " +
            "AND Id_Utente != ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.setInt(2, id);
            final ResultSet resultSet = statement.executeQuery();
            return readCompagnoUnioniFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<CompagnoUnioni> findAllEligibleUnioni(final Integer id, final String sesso) {
        final String query = "SELECT * FROM " + VIEW_NAME +
            " WHERE Id_Coppia IN (" +
                "SELECT Id_Coppia FROM " + VIEW_NAME +
                " WHERE Sesso = ? " +
                "GROUP BY Id_Coppia " +
                "HAVING COUNT(*) = 1" +
                ") " +
            "AND Id_Utente != ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, sesso);
            statement.setInt(2, id);
            final ResultSet resultSet = statement.executeQuery();
            return readCompagnoUnioniFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<CompagnoUnioni> readCompagnoUnioniFromResultSet(final ResultSet resultSet) {
        final List<CompagnoUnioni> unioni = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns 
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get methods 
                final Integer idCoppia = resultSet.getInt("Id_Coppia");
                final Integer idUtente = resultSet.getInt("Id_Utente");
                final String nome = resultSet.getString("Nome");
                final String cognome = resultSet.getString("Cognome");
                final String classifica = resultSet.getString("Classifica");
                final String sesso = resultSet.getString("Sesso");
                // After retrieving all the data we create a Student object
                final CompagnoUnioni unione = new CompagnoUnioni(idCoppia, idUtente, nome, cognome, classifica, sesso);
                unioni.add(unione);
            }
        } catch (final SQLException e) {}
        return unioni;
    }
}
