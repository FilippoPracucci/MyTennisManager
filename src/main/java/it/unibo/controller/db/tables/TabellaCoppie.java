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
import it.unibo.model.Coppia;

public class TabellaCoppie implements Table<Coppia, Integer> {

    public static final String TABLE_NAME = "COPPIE";

    private final Connection connection; 

    public TabellaCoppie(final Connection connection) {
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
                        "Id_Coppia INT NOT NULL AUTO_INCREMENT PRIMARY KEY" +
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
    public Optional<Coppia> findByPrimaryKey(final Integer id) {
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE Id_Coppia = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            final ResultSet resultSet = statement.executeQuery();
            return readCoppieFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<Coppia> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
            return readCoppieFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean save(final Coppia coppia) {
        final String query = "INSERT INTO " + TABLE_NAME + "() VALUES ()";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.executeUpdate();
            return true;
        } catch (final SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /*
     * Impossible to update couples.
     */
    @Override
    public boolean update(final Coppia coppia) {
        return false;
    }

    @Override
    public boolean delete(final Integer id) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE Id_Coppia = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public Integer getLastId() {
        final String query = "SELECT MAX(Id_Coppia) AS Id_Coppia FROM " + TABLE_NAME + " ORDER BY Id_Coppia DESC";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            final ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("Id_Coppia");
            } else {
                return 0;
            }
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Coppia> readCoppieFromResultSet(final ResultSet resultSet) {
        final List<Coppia> coppie = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final int id = resultSet.getInt("Id_Coppia");

                final Coppia coppia = new Coppia(id);
                coppie.add(coppia);
            }
        } catch (final SQLException e) {}
        return coppie;
    }
}
