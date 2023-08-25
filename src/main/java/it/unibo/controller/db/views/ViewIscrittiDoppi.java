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
import it.unibo.controller.db.tables.TabellaGiocatori;
import it.unibo.controller.db.tables.TabellaUnioni;
import it.unibo.model.CoppieIscritte;
import it.unibo.model.Giocatore;
import it.unibo.utils.Pair;
import it.unibo.utils.Tern;

public class ViewIscrittiDoppi implements View<CoppieIscritte, Tern<Integer, Integer, Integer>>{

    public static final String VIEW_NAME = "COPPIE_ISCRITTE";

    private final Connection connection;
    private final TabellaUnioni unione;
    private final TabellaGiocatori giocatore;

    public ViewIscrittiDoppi(final Connection connection) {
        this.connection = Objects.requireNonNull(connection);
        this.unione = new TabellaUnioni(this.connection);
        this.giocatore = new TabellaGiocatori(this.connection);
    }

    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }

    @Override
    public Optional<CoppieIscritte> findByPrimaryKey(final Tern<Integer, Integer, Integer> key) {
        final String query = "SELECT * FROM " + VIEW_NAME + " WHERE Id_Coppia = ? AND Id_Torneo = ? AND Numero_Edizione = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, key.getX());
            statement.setInt(2, key.getY());
            statement.setInt(3, key.getZ());
            final ResultSet resultSet = statement.executeQuery();
            return readCoppieIscritteFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<CoppieIscritte> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + VIEW_NAME);
            return readCoppieIscritteFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public Pair<Giocatore, Giocatore> findGiocatoriOfCoppia(final Integer coppia) {
        final Pair<Integer, Integer> ids = this.unione.findIdGiocatoriOfCoppia(coppia);
        return new Pair<Giocatore, Giocatore>(this.giocatore.findByPrimaryKey(ids.getX()).get(), this.giocatore.findByPrimaryKey(ids.getY()).get());
    }

    public List<CoppieIscritte> findAllIscrittiByEdizioneTorneo(final Integer idTorneo, final Integer nEdizione) {
        final String query = "SELECT * FROM " + VIEW_NAME + " WHERE Id_Torneo = ? AND Numero_Edizione = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, idTorneo);
            statement.setInt(2, nEdizione);
            final ResultSet resultSet = statement.executeQuery();
            return readCoppieIscritteFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<CoppieIscritte> findAllIscrittiByPreferenzaOrario(final String timePreference, final Pair<Integer, Integer> edition) {
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
            return readCoppieIscritteFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<CoppieIscritte> readCoppieIscritteFromResultSet(final ResultSet resultSet) {
        final List<CoppieIscritte> iscritte = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final Integer idCoppia = resultSet.getInt("Id_Coppia");
                final Pair<Giocatore, Giocatore> giocatori = this.findGiocatoriOfCoppia(idCoppia);
                final Integer g1 = giocatori.getX().getId();
                final Integer g2 = giocatori.getY().getId();
                final String tessera1 = giocatori.getX().getTessera();
                final String tessera2 = giocatori.getY().getTessera();
                final String classifica1 = giocatori.getX().getClassifica();
                final String classifica2 = giocatori.getY().getClassifica();
                final String telefono1 = giocatori.getX().getTelefono();
                final String telefono2 = giocatori.getY().getTelefono();
                final Integer idTorneo = resultSet.getInt("Id_Torneo");
                final Integer nEdizione = resultSet.getInt("Numero_Edizione");
                final String prefOrario = resultSet.getString("Preferenza_Orario");

                final CoppieIscritte iscritta = new CoppieIscritte(idCoppia,
                        g1,
                        g2,
                        tessera1,
                        tessera2,
                        classifica1,
                        classifica2,
                        telefono1,
                        telefono2,
                        idTorneo,
                        nEdizione,
                        prefOrario);
                iscritte.add(iscritta);
            }
        } catch (final SQLException e) {}
        return iscritte;
    }
}
