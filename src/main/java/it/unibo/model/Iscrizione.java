package it.unibo.model;

import java.util.Objects;
import java.util.Optional;

public class Iscrizione {
    private final Integer id;
    private final Optional<String> preferenzaOrario;
    private final Optional<Integer> idGiocatore;
    private final Optional<Integer> idCoppia;
    private final Integer idTorneo;
    private final Integer numeroEdizione;

    public Iscrizione(final Integer id,
            final Optional<String> pref_orario,
            final Integer torneo,
            final Integer n_edizione,
            final Optional<Integer> utente,
            final Optional<Integer> coppia) {

        this.id = id;
        this.preferenzaOrario = Objects.requireNonNull(pref_orario);
        this.idTorneo = torneo;
        this.numeroEdizione = n_edizione;
        this.idGiocatore = Objects.requireNonNull(utente);
        this.idCoppia = Objects.requireNonNull(coppia);
    }

    public Integer getId() {
        return this.id;
    }

    public Optional<String> getPreferenzaOrario() {
        return this.preferenzaOrario;
    }

    public Optional<Integer> getIdUtente() {
        return this.idGiocatore;
    }

    public Optional<Integer> getIdCoppia() {
        return this.idCoppia;
    }

    public Integer getIdTorneo() {
        return this.idTorneo;
    }

    public Integer getNumeroEdizione() {
        return this.numeroEdizione;
    }
}
