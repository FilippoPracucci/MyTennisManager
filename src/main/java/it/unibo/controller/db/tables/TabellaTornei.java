package it.unibo.controller.db.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import it.unibo.controller.db.Table;
import it.unibo.model.Torneo;
import it.unibo.model.Torneo.Tipo;

public class TabellaTornei implements Table<Torneo, Integer> {

    public static final String TABLE_NAME = "TORNEI";

    private final Connection connection; 

    public TabellaTornei(final Connection connection) {
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
                        "Id_Torneo INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                        "Tipo VARCHAR(20) NOT NULL CHECK(Tipo in('Singolare maschile', 'Singolare femminile', 'Doppio maschile', 'Doppio femminile'))," +
                        "Limite_Categoria INT," +
                        "Limite_Eta INT," +
                        "Montepremi INT" +
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
    public Optional<Torneo> findByPrimaryKey(final Integer id) {
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE Id_Torneo = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            final ResultSet resultSet = statement.executeQuery();
            return readTorneiFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Torneo> readTorneiFromResultSet(final ResultSet resultSet) {
        final List<Torneo> tornei = new ArrayList<>();
        try {
            // ResultSet encapsulate a pointer to a table with the results: it starts with the pointer
            // before the first row. With next the pointer advances to the following row and returns 
            // true if it has not advanced past the last row
            while (resultSet.next()) {
                // To get the values of the columns of the row currently pointed we use the get methods 
                final Integer id = resultSet.getInt("Id_Torneo");
                final Tipo tipo = Tipo.getTipo(resultSet.getString("Tipo"));
                final Optional<Integer> limite_categoria = Optional.ofNullable(resultSet.getInt("Limite_Categoria"));
                final Optional<Integer> limite_eta = Optional.ofNullable(resultSet.getInt("Limite_Eta"));
                final Optional<Integer> montepremi = Optional.ofNullable(resultSet.getInt("Montepremi"));
                // After retrieving all the data we create a Student object
                final Torneo torneo = new Torneo(id, tipo, limite_categoria, limite_eta, montepremi);
                tornei.add(torneo);
            }
        } catch (final SQLException e) {}
        return tornei;
    }

    @Override
    public List<Torneo> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readTorneiFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<Torneo> findAllByCircolo(final Integer idCircolo) {
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE Id_Torneo IN " +
                "(SELECT EDIZIONE_TORNEI.Id_Torneo FROM EDIZIONE_TORNEI WHERE Id_Circolo = ?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, idCircolo);
            final ResultSet resultSet = statement.executeQuery();
            return readTorneiFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Torneo torneo) {
        final String query = "INSERT INTO " + TABLE_NAME +
                "(Tipo, Limite_Categoria, Limite_Eta, Montepremi) VALUES (?,?,?,?)";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, torneo.getTipo().getNome());
            if (torneo.getLimiteCategoria().isPresent()) {
                statement.setInt(2, torneo.getLimiteCategoria().get());
            } else {
                statement.setNull(2, Types.NULL);
            }
            if (torneo.getLimiteEta().isPresent()) {
                statement.setInt(3, torneo.getLimiteEta().get());
            } else {
                statement.setNull(3, Types.NULL);
            }
            if (torneo.getMontepremi().isPresent()) {
                statement.setInt(4, torneo.getMontepremi().get());
            } else {
                statement.setNull(4, Types.NULL);
            }
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean update(final Torneo torneo) {
        final String query =
            "UPDATE " + TABLE_NAME + " SET " +
                "Tipo = ?," + 
                "Limite_Categoria = ?," +
                "Limite_Eta = ?," + 
                "Montepremi = ?" +
            "WHERE Id_Torneo = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, torneo.getTipo().getNome());
            statement.setInt(2, torneo.getLimiteCategoria().get());
            statement.setInt(3, torneo.getLimiteEta().get());
            statement.setInt(4, torneo.getMontepremi().get());
            statement.setInt(5, torneo.getId());
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean delete(final Integer id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE Id_Torneo = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public Integer getLastId() {
        final String query = "SELECT MAX(Id_Torneo) AS Id_Torneo FROM " + TABLE_NAME + " ORDER BY Id_Torneo DESC";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("Id_Torneo");
            } else {
                return 0;
            }
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
