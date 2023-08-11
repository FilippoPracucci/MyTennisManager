package it.unibo.model;

import java.util.Date;
import java.util.Objects;

public class EdizioneTorneo {
    private final Integer idTorneo;
    private final Integer numeroEdizione;
    private final Date dataInizio;
    private final Date dataFine;
    private final Integer idCircolo;

    public EdizioneTorneo(final Integer id_torneo,
            final Integer n_edizione,
            final Date d_inizio,
            final Date d_fine,
            final Integer circolo) {

        this.idTorneo = id_torneo;
        this.numeroEdizione = n_edizione;
        this.dataInizio = Objects.requireNonNull(d_inizio);
        this.dataFine = Objects.requireNonNull(d_fine);
        this.idCircolo = circolo;
    }

    /*public EdizioneTorneo(final Integer id_torneo,
            final Date d_inizio,
            final Date d_fine,
            final Integer circolo) {

        this(id_torneo, null, d_inizio, d_fine, circolo);
    }*/

    public Integer getIdTorneo() {
        return this.idTorneo;
    }

    public int getNumeroEdizione() {
        return this.numeroEdizione;
    }

    public Date getDataInizio() {
        return this.dataInizio;
    }

    public Date getDataFine() {
        return this.dataFine;
    }

    public Integer getIdCircolo() {
        return this.idCircolo;
    }
}
