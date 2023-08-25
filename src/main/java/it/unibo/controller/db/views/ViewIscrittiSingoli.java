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
import it.unibo.model.GiocatoriIscritti;
import it.unibo.utils.Pair;
import it.unibo.utils.Tern;

public class ViewIscrittiSingoli implements View<GiocatoriIscritti, Tern<Integer, Integer, Integer>> {

    public static final String VIEW_NAME = "GIOCATORI_ISCRITTI";

    private final Connection connection; 

    public ViewIscrittiSingoli(final Connection connection) {
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
    public Optional<GiocatoriIscritti> findByPrimaryKey(final Tern<Integer, Integer, Integer> key) {
        final String query = "SELECT * FROM " + VIEW_NAME + " WHERE Id_Utente = ? AND Id_Torneo = ? AND Numero_Edizione = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, key.getX());
            statement.setInt(2, key.getY());
            statement.setInt(3, key.getZ());
            final ResultSet resultSet = statement.executeQuery();
            return readGiocatoriIscrittiFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<GiocatoriIscritti> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + VIEW_NAME);
            return readGiocatoriIscrittiFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<GiocatoriIscritti> findAllIscrittiByEdizioneTorneo(final Integer idTorneo, final Integer nEdizione) {
        final String query = "SELECT * FROM " + VIEW_NAME + " WHERE Id_Torneo = ? AND Numero_Edizione = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, idTorneo);
            statement.setInt(2, nEdizione);
            final ResultSet resultSet = statement.executeQuery();
            return readGiocatoriIscrittiFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<GiocatoriIscritti> findAllIscrittiByPreferenzaOrario(final String timePreference, final Pair<Integer, Integer> edition) {
        final String query;
        if (timePreference == "Nessuna") {
            query = "SELECT DISTINCT * FROM " + VIEW_NAME  + " WHERE Id_Torneo = ? AND Numero_Edizione = ?";
        } else {
            query = "SELECT DISTINCT * FROM " + VIEW_NAME + " WHERE Id_Torneo = ? AND Numero_Edizione = ? " +
                "AND (Preferenza_Orario IS NULL OR Preferenza_Orario = ?)";
        }
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, edition.getX());
            statement.setInt(2, edition.getY());
            if (timePreference != "Nessuna") {
                statement.setString(3, timePreference);
            }
            final ResultSet resultSet = statement.executeQuery();
            return readGiocatoriIscrittiFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<GiocatoriIscritti> orderAllIscrittiByClassifica(final Pair<Integer, Integer> edition) {
        final String query = "SELECT * FROM " + VIEW_NAME +
            " WHERE Id_Torneo = ? AND Numero_Edizione = ? " +
            "ORDER BY Classifica ASC";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, edition.getX());
            statement.setInt(2, edition.getY());
            final ResultSet resultSet = statement.executeQuery();
            return readGiocatoriIscrittiFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<GiocatoriIscritti> readGiocatoriIscrittiFromResultSet(final ResultSet resultSet) {
        final List<GiocatoriIscritti> iscritti = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final Integer idUtente = resultSet.getInt("Id_Utente");
                final String nome = resultSet.getString("Nome");
                final String cognome = resultSet.getString("Cognome");
                final String email = resultSet.getString("Email");
                final String tessera = resultSet.getString("Tessera");
                final String classifica = resultSet.getString("Classifica");
                final int eta = resultSet.getInt("Eta");
                final String telefono = resultSet.getString("Telefono");
                final Integer idTorneo = resultSet.getInt("Id_Torneo");
                final Integer nEdizione = resultSet.getInt("Numero_Edizione");
                final String prefOrario = resultSet.getString("Preferenza_Orario");

                final GiocatoriIscritti iscritto = new GiocatoriIscritti(idUtente,
                        nome,
                        cognome,
                        email,
                        tessera,
                        classifica,
                        eta,
                        telefono,
                        idTorneo,
                        nEdizione,
                        prefOrario);
                iscritti.add(iscritto);
            }
        } catch (final SQLException e) {}
        return iscritti;
    }
}
