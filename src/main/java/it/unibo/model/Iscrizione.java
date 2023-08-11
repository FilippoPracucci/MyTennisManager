package it.unibo.model;

import java.util.Objects;
import java.util.Optional;

public class Iscrizione {
    private final int id;
    private final Optional<String> preferenzaOrario;
    private final Optional<Integer> idUtente;
    private final Optional<Integer> idCoppia;
    private final int idTorneo;
    private final int numeroEdizione;

    public Iscrizione(final int id,
            final Optional<String> pref_orario,
            final int torneo,
            final int n_edizione,
            final Optional<Integer> utente,
            final Optional<Integer> coppia) {

        this.id = id;
        this.preferenzaOrario = Objects.requireNonNull(pref_orario);
        this.idTorneo = torneo;
        this.numeroEdizione = n_edizione;
        this.idUtente = Objects.requireNonNull(utente);
        this.idCoppia = Objects.requireNonNull(coppia);
    }

    public Iscrizione(final int id,
            final String preferenza_orario,
            final int torneo,
            final int n_edizione,
            final int utente,
            final int coppia) {
        this(id, Optional.ofNullable(preferenza_orario), torneo, n_edizione, Optional.ofNullable(utente), Optional.ofNullable(coppia));
   }

   public Iscrizione(final int id,
            final int torneo,
            final int n_edizione,
            final int utente,
            final int coppia) {
        this(id, Optional.empty(), torneo, n_edizione, Optional.ofNullable(utente), Optional.ofNullable(coppia));
   }

    public int getId() {
        return this.id;
    }

    public Optional<String> getPreferenzaOrario() {
        return this.preferenzaOrario;
    }

    public Optional<Integer> getIdUtente() {
        return this.idUtente;
    }

    public Optional<Integer> getIdCoppia() {
        return this.idCoppia;
    }

    public int getIdTorneo() {
        return this.idTorneo;
    }

    public int getNumeroEdizione() {
        return this.numeroEdizione;
    }
}
