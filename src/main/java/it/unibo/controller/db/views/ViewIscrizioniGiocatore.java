package it.unibo.controller.db.views;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import it.unibo.controller.db.View;
import it.unibo.controller.db.tables.TabellaIscrizioni;
import it.unibo.model.Giocatore;
import it.unibo.model.IscrizioniWithTorneo;
import it.unibo.model.Torneo.Tipo;
import it.unibo.utils.Pair;
import it.unibo.utils.Utils;

public class ViewIscrizioniGiocatore implements View<IscrizioniWithTorneo, Pair<Integer, Integer>> {

    public static final String VIEW_NAME = "ISCRIZIONI_CON_TORNEO";

    private final Connection connection;

    private final TabellaIscrizioni iscrizioni;

    public ViewIscrizioniGiocatore(final Connection connection) {
        this.connection = Objects.requireNonNull(connection);
        this.iscrizioni = new TabellaIscrizioni(this.connection);
    }

    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public String getViewName() {
        return VIEW_NAME;
    }

    @Override
    public Optional<IscrizioniWithTorneo> findByPrimaryKey(final Pair<Integer, Integer> key) {
        final String query = "SELECT * FROM " + VIEW_NAME + " WHERE Id_Torneo = ? AND Numero_Edizione = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, key.getX());
            statement.setInt(2, key.getY());
            final ResultSet resultSet = statement.executeQuery();
            return readIscrizioniWithTorneoFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<IscrizioniWithTorneo> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + VIEW_NAME);
            return readIscrizioniWithTorneoFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<IscrizioniWithTorneo> findByGiocatore(final Giocatore giocatore) {
        final String query =
            "SELECT * FROM " + VIEW_NAME +
            " WHERE Id_Utente = ?";
        try (final PreparedStatement statement = this.getConnection().prepareStatement(query)) {
            statement.setInt(1, giocatore.getId());
            final ResultSet resultSet = statement.executeQuery();
            return this.readIscrizioniWithTorneoFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<IscrizioniWithTorneo> findByCoppia(final Integer id) {
        final String query =
            "SELECT * FROM " + VIEW_NAME +
            " WHERE Id_Coppia = ?";
        try (final PreparedStatement statement = this.getConnection().prepareStatement(query)) {
            statement.setInt(1, id);
            final ResultSet resultSet = statement.executeQuery();
            return this.readIscrizioniWithTorneoFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public void deleteIscrizioneGiocatoreByEdizione(final Pair<Integer, Integer> edizione, final Integer id) {
        this.iscrizioni.delete(this.iscrizioni.findByEdizioneToGiocatore(edizione, id).get().getId());
    }

    public void deleteIscrizioneCoppiaByEdizione(final Pair<Integer, Integer> edizione, final Integer id) {
        this.iscrizioni.delete(this.iscrizioni.findByEdizioneToCoppia(edizione, id).get().getId());
    }

    private List<IscrizioniWithTorneo> readIscrizioniWithTorneoFromResultSet(final ResultSet resultSet) {
        final List<IscrizioniWithTorneo> iscrizioni = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final Integer idTorneo = resultSet.getInt("Id_Torneo");
                final Integer nEdizione = resultSet.getInt("Numero_Edizione");
                final Tipo tipo = Tipo.getTipo(resultSet.getString("Tipo"));
                final Date dInizio = Utils.sqlDateToDate(resultSet.getDate("Data_Inizio"));
                final Date dFine = Utils.sqlDateToDate(resultSet.getDate("Data_Fine"));
                final String nCircolo = resultSet.getString("Circolo");
                final Integer limite_categoria = resultSet.getInt("Limite_Categoria");
                final Integer limite_eta = resultSet.getInt("Limite_Eta");
                final Integer montepremi = resultSet.getInt("Montepremi");
                final String pOrario = resultSet.getString("Preferenza_Orario");
                final Integer idUtente = resultSet.getInt("Id_Utente");
                final Integer idCoppia = resultSet.getInt("Id_Coppia");

                final IscrizioniWithTorneo iscrizione = new IscrizioniWithTorneo(idTorneo, nEdizione, tipo, dInizio, dFine, nCircolo, limite_categoria, limite_eta, montepremi, pOrario, idUtente, idCoppia);
                iscrizioni.add(iscrizione);
            }
        } catch (final SQLException e) {}
        return iscrizioni;
    }
}
