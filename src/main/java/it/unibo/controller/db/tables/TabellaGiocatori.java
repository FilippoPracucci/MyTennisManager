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
import it.unibo.model.Giocatore;

public class TabellaGiocatori implements Table<Giocatore, Integer> {

    public static final String TABLE_NAME = "GIOCATORI";

    private final Connection connection; 

    public TabellaGiocatori(final Connection connection) {
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
                        "Id_Utente INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                        "Nome VARCHAR(40) NOT NULL," + 
                        "Cognome VARCHAR(40) NOT NULL," + 
                        "Email VARCHAR(40) NOT NULL UNIQUE," + 
                        "Password VARCHAR(12) NOT NULL," +
                        "Tessera VARCHAR(11) NOT NULL UNIQUE," +
                        "Classifica VARCHAR(4) NOT NULL," +
                        "Eta INT NOT NULL," +
                        "Sesso CHAR(1) NOT NULL CHECK(Sesso in ('M', 'F'))," +
                        "Telefono VARCHAR(11) NOT NULL UNIQUE," +
                        "Id_Circolo INT NOT NULL" +
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
    public Optional<Giocatore> findByPrimaryKey(final Integer id) {
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE Id_Utente = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            final ResultSet resultSet = statement.executeQuery();
            return readGiocatoriFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public Optional<Giocatore> findGiocatoreByCredentials(final String email, final String password) {
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE Email = ? AND Password = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);
            final ResultSet resultSet = statement.executeQuery();
            return readGiocatoriFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Giocatore> readGiocatoriFromResultSet(final ResultSet resultSet) {
        final List<Giocatore> giocatori = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns 
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get methods 
                final int id = resultSet.getInt("Id_Utente");
                final String nome = resultSet.getString("Nome");
                final String cognome = resultSet.getString("Cognome");
                final String email = resultSet.getString("Email");
                final String password = resultSet.getString("Password");
                final String tessera = resultSet.getString("Tessera");
                final String classifica = resultSet.getString("Classifica");
                final int eta = resultSet.getInt("Eta");
                final String sesso = resultSet.getString("Sesso");
                final String telefono = resultSet.getString("Telefono");
                final int id_circolo = resultSet.getInt("Id_Circolo");
                // After retrieving all the data we create a Student object
                final Giocatore giocatore = new Giocatore(id, nome, cognome, email, password, tessera, classifica, eta, sesso, telefono, id_circolo);
                giocatori.add(giocatore);
            }
        } catch (final SQLException e) {}
        return giocatori;
    }

    @Override
    public List<Giocatore> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readGiocatoriFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Giocatore giocatore) {
        final String query = "INSERT INTO " + TABLE_NAME +
                "(Nome, Cognome, Email, Password, Tessera, Classifica, Eta, Sesso, Telefono, Id_Circolo)" +
                "VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, giocatore.getNome());
            statement.setString(2, giocatore.getCognome());
            statement.setString(3, giocatore.getEmail());
            statement.setString(4, giocatore.getPassword());
            statement.setString(5, giocatore.getTessera());
            statement.setString(6, giocatore.getClassifica());
            statement.setInt(7, giocatore.getEta());
            statement.setString(8, giocatore.getSesso());
            statement.setString(9, giocatore.getTelefono());
            statement.setInt(10, giocatore.getIdCircolo());
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Giocatore giocatore) {
        final String query =
            "UPDATE " + TABLE_NAME + " SET " +
                "Nome = ?," + 
                "Cognome = ?," +
                "Email = ?," + 
                "Password = ?," +
                "Tessera = ?," +
                "Classifica = ?," +
                "Eta = ?," +
                "Sesso = ?," +
                "Telefono = ?," +
                "Id_Circolo = ?" +
            "WHERE Id_Utente = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, giocatore.getNome());
            statement.setString(2, giocatore.getCognome());
            statement.setString(3, giocatore.getEmail());
            statement.setString(4, giocatore.getPassword());
            statement.setString(5, giocatore.getTessera());
            statement.setString(6, giocatore.getClassifica());
            statement.setInt(7, giocatore.getEta());
            statement.setString(8, giocatore.getSesso());
            statement.setString(9, giocatore.getTelefono());
            statement.setInt(10, giocatore.getIdCircolo());
            statement.setInt(11, giocatore.getId());
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

}
