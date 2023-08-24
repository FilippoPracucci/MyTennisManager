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
import it.unibo.model.Circolo;
import it.unibo.model.Giocatore;
import it.unibo.model.TorneiWithEdizioni;
import it.unibo.model.Torneo.Tipo;
import it.unibo.utils.Pair;
import it.unibo.utils.Utils;

public class ViewTornei implements View<TorneiWithEdizioni, Pair<Integer, Integer>> {

    public static final String VIEW_NAME = "TORNEI_CON_EDIZIONI";

    private final Connection connection; 

    public ViewTornei(final Connection connection) {
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
                "SELECT t.Id_Torneo, et.Numero_Edizione, t.Tipo, et.Data_Inizio, et.Data_Fine, t.Limite_Categoria, t.Limite_Eta, t.Montepremi, et.Id_Circolo " +
                "FROM TORNEI t " +
                "JOIN EDIZIONE_TORNEI et " +
                "ON (t.Id_Torneo = et.Id_Torneo))");
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
    public Optional<TorneiWithEdizioni> findByPrimaryKey(final Pair<Integer, Integer> key) {
        final String query = "SELECT * FROM " + VIEW_NAME + " WHERE Id_Torneo = ? AND Numero_Edizione = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, key.getX());
            statement.setInt(2, key.getY());
            final ResultSet resultSet = statement.executeQuery();
            return readTorneiWithEditionsFromResultSet(resultSet).stream().findFirst();
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<TorneiWithEdizioni> findAll() {
        try (final Statement statement = this.connection.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + VIEW_NAME);
            return readTorneiWithEditionsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<TorneiWithEdizioni> findAllEdizioniByCircolo(final Circolo circolo) {
        final String query =
            "SELECT * FROM " + VIEW_NAME +
            " WHERE Id_Circolo = ?";
        try (final PreparedStatement statement = this.getConnection().prepareStatement(query)) {
            statement.setInt(1, circolo.getId());
            final ResultSet resultSet = statement.executeQuery();
            return this.readTorneiWithEditionsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<TorneiWithEdizioni> findAllSingolariEligibleByPlayer (final Giocatore giocatore) {
        String t;
        final char first = giocatore.getClassifica().charAt(0);
        final int cat;
        if (giocatore.getSesso().contentEquals("M")) {
            t = Tipo.SINGOLARE_MASCHILE.getNome();
        } else {
            t = Tipo.SINGOLARE_FEMMINILE.getNome();
        }
        switch (first) {
            case '2':
                cat = 2;
                break;
            case '3':
                cat = 3;
                break;
            default:
                cat = 4;
                break;
        }
        final String query =
            "SELECT DISTINCT et.* FROM " + VIEW_NAME + " et LEFT JOIN ISCRIZIONI i " +
            "ON (et.Id_Torneo = i.Id_Torneo AND et.Numero_Edizione = i.Numero_Edizione) " +
            "WHERE (Limite_Eta IS NULL OR Limite_Eta >= ?) " +
            "AND (Limite_Categoria IS NULL OR Limite_Categoria <= ?) " +
            "AND Tipo = ? " +
            "AND ? not in (" +
                "SELECT Id_Utente FROM ISCRIZIONI WHERE Id_Torneo = i.Id_Torneo AND Numero_Edizione = i.Numero_Edizione) " +
            "AND NOT EXISTS (SELECT it.Id_Torneo, it.Numero_Edizione FROM ISCRIZIONI_CON_TORNEO it " +
                "WHERE it.Id_Utente = ? " +
                "AND ((et.Data_Inizio BETWEEN it.Data_Inizio AND it.Data_Fine) " +
                "OR (et.Data_Fine BETWEEN it.Data_Inizio AND it.Data_Fine)))";
        try (final PreparedStatement statement = this.getConnection().prepareStatement(query)) {
            statement.setInt(1, giocatore.getEta());
            statement.setInt(2, cat);
            statement.setString(3, t);
            statement.setInt(4, giocatore.getId());
            statement.setInt(5, giocatore.getId());
            final ResultSet resultSet = statement.executeQuery();
            return this.readTorneiWithEditionsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<TorneiWithEdizioni> findAllDoppiEligibleByCoppia (final Pair<Giocatore, Giocatore> coppia, final Integer id) {
        final char first1 = coppia.getX().getClassifica().charAt(0);
        final char first2 = coppia.getY().getClassifica().charAt(0);
        final int cat;
        final String t;
        if (coppia.getX().getSesso().contentEquals("M")) {
            t = Tipo.DOPPIO_MASCHILE.getNome();
        } else {
            t = Tipo.DOPPIO_FEMMINILE.getNome();
        }
        if (first1 < first2) {
            switch (first1) {
                case '2':
                    cat = 2;
                    break;
                case '3':
                    cat = 3;
                    break;
                default:
                    cat = 4;
                    break;
            }
        } else {
            switch (first2) {
                case '2':
                    cat = 2;
                    break;
                case '3':
                    cat = 3;
                    break;
                default:
                    cat = 4;
                    break;
            }
        }
        final String query =
            "SELECT et.* FROM " + VIEW_NAME + " et LEFT JOIN ISCRIZIONI i " +
            "ON (et.Id_Torneo = i.Id_Torneo AND et.Numero_Edizione = i.Numero_Edizione) " +
            "WHERE (Limite_Eta IS NULL OR Limite_Eta >= ?) " +
            "AND (Limite_Categoria IS NULL OR Limite_Categoria <= ?) " +
            "AND Tipo = ? " +
            "AND ? NOT IN (SELECT u.Id_Giocatore FROM ISCRIZIONI i2 RIGHT JOIN UNIONI u " +
                "ON (i2.Id_Coppia = u.Id_Coppia)" +
                "WHERE i2.Id_Torneo = i.Id_Torneo AND i2.Numero_Edizione = i.Numero_Edizione) " +
            "AND NOT EXISTS (SELECT it.Id_Torneo, it.Numero_Edizione FROM ISCRIZIONI_CON_TORNEO it " +
                "WHERE it.Id_Coppia IN (SELECT it2.Id_Coppia FROM ISCRIZIONI_CON_TORNEO it2 LEFT JOIN UNIONI u2 " +
                    "ON (it2.id_Coppia = u2.Id_Coppia) " +
                    "WHERE u2.Id_Giocatore = ?) " +
                "AND ((et.Data_Inizio BETWEEN it.Data_Inizio AND it.Data_Fine) " +
                "OR (et.Data_Fine BETWEEN it.Data_Inizio AND it.Data_Fine)))";
        try (final PreparedStatement statement = this.getConnection().prepareStatement(query)) {
            statement.setInt(1, (coppia.getX().getEta() < coppia.getY().getEta()) ? coppia.getY().getEta() : coppia.getX().getEta());
            statement.setInt(2, cat);
            statement.setString(3, t);
            statement.setInt(4, id);
            statement.setInt(5, id);
            final ResultSet resultSet = statement.executeQuery();
            return this.readTorneiWithEditionsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<TorneiWithEdizioni> findAllFiltered(final Giocatore giocatore, final Optional<Integer> categoria, final Optional<Integer> eta, final Optional<String> data) {
        String t;
        final char first = giocatore.getClassifica().charAt(0);
        final int cat;
        if (giocatore.getSesso().contentEquals("M")) {
            t = Tipo.SINGOLARE_MASCHILE.getNome();
        } else {
            t = Tipo.SINGOLARE_FEMMINILE.getNome();
        }
        switch (first) {
            case '2':
                cat = 2;
                break;
            case '3':
                cat = 3;
                break;
            default:
                cat = 4;
                break;
        }
        String query = "SELECT * FROM " + VIEW_NAME + " WHERE Tipo = ? ";
        final StringBuilder sBuilder = new StringBuilder(query);
        if (eta.isPresent()) {
            sBuilder.append("AND Limite_Eta = ? ");
        } else {
            sBuilder.append("AND (Limite_Eta IS NULL OR Limite_Eta >= ?) ");
        }
        if (categoria.isPresent()) {
            sBuilder.append("AND Limite_Categoria = ? ");
        } else {
            sBuilder.append("AND (Limite_Categoria IS NULL OR Limite_Categoria <= ?) ");
        }
        if (data.isPresent()) {
            sBuilder.append("AND YEAR(Data_Inizio) = ? ");
        }
        query = sBuilder.toString();
        try (final PreparedStatement statement = this.getConnection().prepareStatement(query)) {
            statement.setString(1, t);
            if (eta.isPresent()) {
                statement.setInt(2, eta.get());
            } else {
                statement.setInt(2, giocatore.getEta());
            }
            if (categoria.isPresent()) {
                statement.setInt(3, categoria.get());
            } else {
                statement.setInt(3, cat);
            }
            if (data.isPresent()) {
                statement.setString(4, data.get());
            }
            final ResultSet resultSet = statement.executeQuery();
            return this.readTorneiWithEditionsFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<TorneiWithEdizioni> readTorneiWithEditionsFromResultSet(final ResultSet resultSet) {
        final List<TorneiWithEdizioni> tornei = new ArrayList<>();
        try {
            while (resultSet.next()) {
                final Integer idTorneo = resultSet.getInt("Id_Torneo");
                final Integer nEdizione = resultSet.getInt("Numero_Edizione");
                final Tipo tipo = Tipo.getTipo(resultSet.getString("Tipo"));
                final Date dInizio = Utils.sqlDateToDate(resultSet.getDate("Data_Inizio"));
                final Date dFine = Utils.sqlDateToDate(resultSet.getDate("Data_Fine"));
                final Optional<Integer> limite_categoria = Optional.ofNullable(resultSet.getInt("Limite_Categoria"));
                final Optional<Integer> limite_eta = Optional.ofNullable(resultSet.getInt("Limite_Eta"));
                final Optional<Integer> montepremi = Optional.ofNullable(resultSet.getInt("Montepremi"));
                final Integer idCircolo = resultSet.getInt("Id_Circolo");

                final TorneiWithEdizioni torneo = new TorneiWithEdizioni(idTorneo, nEdizione, tipo, dInizio, dFine, limite_categoria, limite_eta, montepremi, idCircolo);
                tornei.add(torneo);
            }
        } catch (final SQLException e) {}
        return tornei;
    }
}
