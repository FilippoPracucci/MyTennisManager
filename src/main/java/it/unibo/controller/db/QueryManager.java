package it.unibo.controller.db;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import it.unibo.model.Circolo;
import it.unibo.model.CompagnoUnioni;
import it.unibo.model.Coppia;
import it.unibo.model.CoppieIscritte;
import it.unibo.model.EdizioneTorneo;
import it.unibo.model.Giocatore;
import it.unibo.model.GiocatoriIscritti;
import it.unibo.model.Iscrizione;
import it.unibo.model.IscrizioniWithTorneo;
import it.unibo.model.Organizzatore;
import it.unibo.model.TorneiWithEdizioni;
import it.unibo.model.Torneo;
import it.unibo.model.Torneo.Tipo;
import it.unibo.model.Unione;
import it.unibo.utils.Pair;

public interface QueryManager {

    /**
     * Creates a new {@code Organizzatore}.
     * 
     * @param nome the name
     * @param cognome the surname
     * @param email the email
     * @param password the password
     * 
     * @return the {@code Organizzatore} created
     */
    Organizzatore createOrganizzatore(String nome,
            String cognome,
            String email,
            String password
    );

    /**
     * Inserts the {@code Organizzatore} into the database.
     * 
     * @param organizzatore the organizer
     */
    void addOrganizzatore(Organizzatore organizzatore);

    /**
     * Gets the id of the {@code Organizzatore} given.
     * 
     * @param organizzatore the organizer
     * 
     * @return the id of the organizer given
     */
    Integer getIdOrganizzatore(Organizzatore organizzatore);

    /**
     * Gets the {@code Organizzatore} by his id.
     * 
     * @param id the id of the organizer to find
     * 
     * @return the {@code Organizzatore} with the id given
     */
    Optional<Organizzatore> findOrganizzatore(Integer id);

    /**
     * Gets the {@code Organizzatore} by his credentials.
     * 
     * @param email the email of the organizer to find
     * @param password the password of the organizer to find
     * 
     * @return the {@code Organizzatore} with the credentials given
     */
    Optional<Organizzatore> findOrganizzatoreByCredentials(String email, String password);

    /**
     * Update the current organizer's data.
     * 
     * @param nome the new name
     * @param cognome the new surname
     * @param email the new email
     * @param password the new password
     */
    void updateOrganizzatore(Integer id,
        String nome,
        String cognome,
        String email,
        String password
    );

    /**
     * Creates a new {@code Giocatore}.
     * 
     * @param nome the name
     * @param cognome the surname
     * @param email the email
     * @param password the password
     * @param tessera the badge number
     * @param classifica the ranking
     * @param eta the age
     * @param sesso the gender
     * @param telefono the phone number
     * @param circolo the club
     * 
     * @return the {@code Giocatore} created
     */
    Giocatore createGiocatore(String nome,
            String cognome,
            String email,
            String password,
            String tessera,
            String classifica,
            int eta,
            String sesso,
            String telefono,
            Integer circolo
    );

    /**
     * Inserts the {@code Giocatore} into the database.
     * 
     * @param giocatore the player
     */
    void addGiocatore(Giocatore giocatore);

    /**
     * Gets the id of the {@code Giocatore} given.
     * 
     * @param giocatore the player
     * 
     * @return the id of the player given
     */
    Integer getIdGiocatore(Giocatore giocatore);

    /**
     * Gets the {@code Giocatore} by his id.
     * 
     * @param id the id of the player to find
     * 
     * @return the {@code Giocatore} with the id given
     */
    Optional<Giocatore> findGiocatore(Integer id);

    /**
     * Gets the {@code Giocatore} by his credentials.
     * 
     * @param email the email of the player to find
     * @param password the password of the player to find
     * 
     * @return the {@code Giocatore} with the credentials given
     */
    Optional<Giocatore> findGiocatoreByCredentials(String email, String password);

    /**
     * Gets the list of the 5 {@code Giocatore} with the most tournaments played in the filtered period.
     * 
     * @param annoI the start year of the filter
     * @param annoF the end year of the filter
     * 
     * @return the list of {@code Giocatore} filtered
     */
    List<Giocatore> findTopGiocatori(Integer annoI, Integer annoF);

    /**
     * Update the current player's data.
     * 
     * @param nome the new name
     * @param cognome the new surname
     * @param email the new email
     * @param password the new password
     * @param tessera the new badge number
     * @param classifica the new ranking
     * @param eta the new age
     * @param sesso the gender
     * @param telefono the new phone number
     * @param circolo the new club
     */
    void updateGiocatore(Integer id,
        String nome,
        String cognome,
        String email,
        String password,
        String tessera,
        String classifica,
        int eta,
        String sesso,
        String telefono,
        Integer circolo
    );

    /**
     * Converts the list of {@code Giocatore} into a matrix of objects.
     * So it can be shown in gui tables.
     * 
     * @param list the list of players
     * @param col the number of columns
     * 
     * @return the matrix of players
     */
    Object[][] listGiocatoriToMatrix(List<Giocatore> list, int col);

    /**
     * Creates a new {@code Circolo}.
     * 
     * @param organizzatore the organizer's id
     * @param nome the name
     * @param citta the city
     * @param indirizzo the address
     * @param telefono the phone number
     * 
     * @return the {@code Circolo} created
     */
    Circolo createCircolo(Integer organizzatore,
        String nome,
        String citta,
        String indirizzo,
        String telefono
    );

    /**
     * Inserts the {@code Circolo} into the database.
     * 
     * @param circolo the club
     */
    void addCircolo(Circolo circolo);

    /**
     * Gets the {@code Circolo} by his id.
     * 
     * @param id the id of the club to find
     * 
     * @return the {@code Circolo} with the id given
     */
    Optional<Circolo> findCircolo(Integer id);

    /**
     * Gets the {@code Circolo} by his organizer.
     * 
     * @param organizzatore the organizer's id
     * 
     * @return the {@code Circolo} of the organizer given
     */
    Optional<Circolo> findCircoloByOrganizzatore(int organizzatore);

    /**
     * Gets the list of all {@code Circolo}.
     * 
     * @return the list of all {@code Circolo} in the database.
     */
    List<Circolo> findAllCircolo();

    /**
     * Gets the list of the 3 {@code Circolo} with the most tournaments organized in the filtered period.
     * 
     * @param annoI the start year of the filter
     * @param annoF the end year of the filter
     * 
     * @return the list of {@code Circolo} filtered
     */
    List<Circolo> findTopCircoli(Integer annoI, Integer annoF);

    /**
     * Converts the list of {@code Circolo} into a matrix of objects.
     * So it can be shown in gui tables.
     * 
     * @param list the list of clubs
     * @param col the number of columns
     * 
     * @return the matrix of clubs
     */
    Object[][] listCircoliToMatrix(List<Circolo> list, int col);

    /**
     * Creates a new {@code Torneo}.
     * 
     * @param tipo the type
     * @param limiteCategoria the category limit (optional)
     * @param limiteEta the age limit (optional)
     * @param montepremi the prize (optional)
     * 
     * @return the {@code Torneo} created
     */
    Torneo createTorneo(Tipo tipo,
        Optional<Integer> limiteCategoria,
        Optional<Integer> limiteEta,
        Optional<Integer> montepremi
    );

    /**
     * Inserts the {@code Torneo} into the database.
     * 
     * @param torneo the tournament
     */
    void addTorneo(Torneo torneo);

    /**
     * Gets the {@code Torneo} by his id.
     * 
     * @param id the id of the tournament to find
     * 
     * @return the {@code Torneo} with the id given
     */
    Optional<Torneo> findTorneo(Integer id);

    /**
     * Gets the list of all {@code Torneo}.
     * 
     * @return the list of all {@code Torneo} in the database.
     */
    List<Torneo> findAllTorneo();

    /**
     * Gets the list of all {@code Torneo} organized by a club.
     * 
     * @param circolo the club
     * 
     * @return the list of all {@code Torneo} organized by the club given
     */
    List<Torneo> findAllTorneoByCircolo(Circolo circolo);

    /**
     * Gets the list of all signles {@code Torneo} eligible for a player.
     * 
     * @param giocatore the player
     * 
     * @return the list of all singles {@code Torneo} eligible for the player given
     */
    List<Torneo> findAllTorneoSingolareEligible(Giocatore giocatore);

    /**
     * Gets the list of all doubles {@code Torneo} eligible for a couple.
     * 
     * @param coppia the couple
     * 
     * @return the list of all {@code Torneo} eligible for the couple given
     */
    List<Torneo> findAllTorneoDoppioEligible(Pair<Giocatore, Giocatore> coppia);

    /**
     * Gets the id of the last tournament created.
     * 
     * @return the last id
     */
    Integer getIdLastTorneo();

    /**
     * Deletes the {@code Torneo} with the id given from the database.
     * 
     * @param id the tournament's id
     */
    void deleteTorneo(Integer id);

    /**
     * Creates a new {@code EdizioneTorneo}.
     * 
     * @param idTorneo the tournament's id
     * @param nEdizione the edition number
     * @param dInizio  the start date
     * @param dFine the end date
     * @param circolo the club's id
     * 
     * @return the {@code EdizioneTorneo} created
     */
    EdizioneTorneo createEdizioneTorneo(Integer idTorneo,
            Integer nEdizione,
            Date dInizio,
            Date dFine,
            Integer circolo
    );

    /**
     * Inserts the {@code EdizioneTorneo} into the database.
     * 
     * @param eTorneo the tournament edition
     */
    void addEdizioneTorneo(EdizioneTorneo eTorneo);

    /**
     * Gets the {@code EdizioneTorneo} by his key.
     * 
     * @param key the primary key
     * 
     * @return the {@code EdizioneTorneo} with the key given
     */
    EdizioneTorneo findEdizioneTorneo(Pair<Integer, Integer> key);

    /**
     * Gets the edition number.
     * 
     * @param torneo the tournament
     * 
     * @return the edition number of the tournament given
     */
    int getNumeroEdizione(Torneo torneo);

    /**
     * Deletes the {@code EdizioneTorneo} with the key given from the database.
     * 
     * @param key the primary key
     */
    void deleteEdizioneTorneo(Pair<Integer, Integer> key);

    /**
     * Gets the list of all {@code EdizioneTorneo} of a tournament
     * 
     * @param torneo the tournament
     * 
     * @return the list of all {@code EdizioneTorneo} of the tournament given
     */
    List<EdizioneTorneo> findAllEdizioneByTorneo(Torneo torneo);

    /**
     * Converts the list of {@code TorneiWithEdizioni} into a matrix of objects.
     * So it can be shown in gui tables.
     * 
     * @param list the list of tournaments' editions
     * @param col the number of columns
     * 
     * @return the matrix of tournaments' editions
     */
    Object[][] listTorneiToMatrix(final List<TorneiWithEdizioni> list, final int col);

    /**
     * Creates a new {@code Iscrizione}.
     * 
     * @param idIscrizione the id
     * @param prefOrario the time preference (optional)
     * @param idTorneo the tournament's id
     * @param nEdizione the edition number
     * @param idUtente the player's id
     * @param idCoppia the couple's id
     * 
     * @return the {@code Iscrizione} created
     */
    Iscrizione createIscrizione(Integer idIscrizione,
            Optional<String> prefOrario,
            Integer idTorneo,
            Integer nEdizione,
            Optional<Integer> idUtente,
            Optional<Integer> idCoppia
    );

    /**
     * Inserts the {@code Iscrizione} into the database.
     * 
     * @param iscrizione the registration
     */
    void addIscrizione(Iscrizione iscrizione);

    /**
     * Gets the list of all {@code GiocatoriIscritti} to a tournament edition.
     * 
     * @param edizioneTorneo the tournament's edition
     * 
     * @return the list of all {@code GiocatoriIscritti} to the tournament edition given
     */
    List<GiocatoriIscritti> findAllGiocatoriIscrittiByEdizioneTorneo(EdizioneTorneo edizioneTorneo);

    /**
     * Gets the list of all {@code GiocatoriIscritti} to a tournament edition with the same time preference.
     * 
     * @param prefOrario the time preference
     * @param edizione the pair of tournament's id and edition's number
     * 
     * @return the list of all {@code GiocatoriIscritti} to the tournament edition given with the time preference given
     */
    List<GiocatoriIscritti> findAllIscrittiByPreferenzaOrario(String prefOrario, Pair<Integer, Integer> edizione);

    /**
     * Gets the list of all {@code GiocatoriIscritti} to a tournament edition ordered by ranking.
     * 
     * @param edizione the pair of tournament's id and edition's number
     * 
     * @return the list of all {@code GiocatoriIscritti} to the tournament edition given ordered by ranking
     */
    List<GiocatoriIscritti> findAllIscrittiOrderByClassifica(Pair<Integer, Integer> edizione);

    /**
     * Converts the list of {@code GiocatoriIscritti} into a matrix of objects.
     * So it can be shown in gui tables.
     * 
     * @param list the list of players registered
     * @param col the number of columns
     * 
     * @return the matrix of players registered
     */
    Object[][] listGiocatoriIscrittiToMatrix(List<GiocatoriIscritti> list, int col);

    /**
     * Gets the list of all {@code CoppieIscritte} to a tournament edition.
     * 
     * @param edizioneTorneo the tournament's edition
     * 
     * @return the list of all {@code CoppieIscritte} to the tournament edition given
     */
    List<CoppieIscritte> findAllCoppieIscritteByEdizioneTorneo(EdizioneTorneo edizioneTorneo);

    /**
     * Gets the list of all {@code CoppieIscritte} to a tournament edition with same time preference.
     * 
     * @param prefOrario the time preference
     * @param edizione the pair of tournament's id and edition's number
     * 
     * @return the list of all {@code CoppieIscritte} to the tournament edition given with the time preference given.
    */
    List<CoppieIscritte> findAllCoppieIscritteByPreferenzaOrario(String prefOrario, Pair<Integer, Integer> edizione);

    /**
     * Converts the list of {@code CoppieIscritte} into a matrix of objects.
     * So it can be shown in gui tables.
     * 
     * @param list the list of couples registered
     * @param col the number of columns
     * 
     * @return the matrix of couples registered
     */
    Object[][] listCoppieIscritteToMatrix(List<CoppieIscritte> list, int col);

    /**
     * Gets the list of all {@code TorneiWithEdizioni} organized by a club.
     * 
     * @param circolo the club
     * 
     * @return the list of all {@code TorneiWithEdizioni} organized by the club given
     */
    List<TorneiWithEdizioni> findAllEdizioniByCircolo(Circolo circolo);

    /**
     * Gets the list of all singles {@code TorneiWithEdizioni} eligible for a player.
     * 
     * @param giocatore the player
     * 
     * @return the list of all {@code TorneiWithEdizioni} eligible for the player given
     */
    List<TorneiWithEdizioni> findAllSingolariEligibleByPlayer(Giocatore giocatore);

    /**
     * Gets the list of all doubles {@code TorneiWithEdizioni} eligible for a couple.
     * 
     * @param coppia the couple
     * @param id the id of the player that had done the access
     * 
     * @return the list of all doubles {@code TorneiWithEdizioni} eligible for the couple given
     */
    List<TorneiWithEdizioni> findAllDoppiEligibleByCoppia(Pair<Giocatore, Giocatore> coppia, Integer id);

    /**
     * Gets the list of all {@code TorneiWithEdizioni} filtered.
     * 
     * @param giocatore the player
     * @param cat the category limit (optional)
     * @param eta the age limit (optional)
     * @param data the date (optional)
     * 
     * @return the list of all {@code TorneiWithEdizioni} filtered
     */
    List<TorneiWithEdizioni> findAllFiltered(Giocatore giocatore,
        Optional<Integer> cat,
        Optional<Integer> eta,
        Optional<String> data
    );

    /**
     * Creates a new {@code Coppia}.
     * 
     * @param id the couple's id
     * 
     * @return the {@code Coppia} created
     */
    Coppia createCoppia(final Integer id);

    /**
     * Inserts the {@code Coppia} into the database.
     * Gets the id of the couple created.
     * 
     * @param coppia the couple
     * 
     * @return the id of the couple created
     */
    int addCoppia(final Coppia coppia);

    /**
     * Gets the {@code Coppia} by his id.
     * 
     * @param id the couple's id
     * 
     * @return the {@code Coppia} with the id given
     */
    Optional<Coppia> findCoppia(final Integer id);

    /**
     * Creates a new {@code Unione}.
     * 
     * @param idCoppia the couple's id
     * @param idUtente the player's id
     * 
     * @return the {@code Unione} created
     */
    Unione createUnione(Integer idCoppia, Integer idUtente);

    /**
     * Inserts the {@code Unione} into the database.
     * 
     * @param unione the union
     */
    void addUnione(Unione unione);

    /**
     * Gets the list of all couple's id eligible for union.
     * 
     * @param giocatore the player
     * 
     * @return the list of all eligible couple's id for the player given
     */
    List<Integer> findAllEligibleUnioni(Giocatore giocatore);

    /**
     * Gets the pair of players composing a couple.
     * 
     * @param coppia the couple
     * 
     * @return the pair of {@code Giocatore} composing the couple given
     */
    Pair<Giocatore, Giocatore> findGiocatoriOfCoppia(Coppia coppia);

    /**
     * Gets the list of all {@code CompagnoUnioni} of a player.
     * 
     * @param giocatore the player
     * 
     * @return the list of all {@code CompagnoUnioni} of the player given
     */
    List<CompagnoUnioni> findAllUnioniByGiocatore(Giocatore giocatore);

    /**
     * Gets the list of all {@code CompagnoUnioni} eligible for a player.
     * 
     * @param giocatore the player
     * 
     * @return the list of all {@code CompagnoUnioni} eligible for the player given
     */
    List<CompagnoUnioni> findAllEligibleCompagniUnioneForGiocatore(Giocatore giocatore);

    /**
     * Converts the list of {@code CompagnoUnioni} into a matrix of objects.
     * So it can be shown in gui tables.
     * 
     * @param list the list of players composing unions
     * @param col the number of columns
     * 
     * @return the matrix of unions
     */
    Object[][] listUnioniToMatrix(List<CompagnoUnioni> list, int col);

    /**
     * Gets the list of all {@code IscrizioniWithTorneo} of a player.
     * 
     * @param giocatore the player
     * 
     * @return the list of all {@code IscrizioniWithTorneo} of the player given
     */
    List<IscrizioniWithTorneo> findAllIscrizioniByGiocatore(Giocatore giocatore);

    /**
     * Gets the list of all {@code IscrizioniWithTorneo} of a couple.
     * 
     * @param coppia the couple
     * 
     * @return the list of all {@code IscrizioniWithTorneo} of the couple given
     */
    List<IscrizioniWithTorneo> findAllIscrizioniByCoppia(Coppia coppia);

    /**
     * Converts the list of {@code IscrizioniWithTorneo} into a matrix of objects.
     * So it can be shown in gui tables.
     * 
     * @param list the list of registration and their tournament
     * @param col the number of columns
     * 
     * @return the matrix of registrations
     */
    Object[][] listIscrizioniToMatrix(List<IscrizioniWithTorneo> list, int col);

    /**
     * Deletes the {@code Iscrizione} of a player or couple by the tournament edition.
     * 
     * @param edizione the pair of tournament's id and edition's number
     * @param idUtente the player's id
     * @param idCoppia the couple's id
     */
    void deleteIscrizioneByEdizione(Pair<Integer, Integer> edizione, Optional<Integer> idUtente, Optional<Integer> idCoppia);
}
