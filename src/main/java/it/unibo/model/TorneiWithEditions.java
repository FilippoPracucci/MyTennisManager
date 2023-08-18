package it.unibo.model;

import java.util.Date;
import java.util.Optional;

import it.unibo.model.Torneo.Tipo;

public class TorneiWithEditions {

    private final Integer idTorneo;
    private final Integer nEdizione;
    private final Tipo tipo;
    private final Date dInizio;
    private final Date dFine;
    private final Optional<Integer> limCategoria;
    private final Optional<Integer> limEta;
    private final Optional<Integer> montepremi;
    private final Integer idCircolo;

    public TorneiWithEditions(final Integer idTorneo,
            final Integer nEdizione,
            final Tipo tipo,
            final Date dInizio,
            final Date dFine,
            final Optional<Integer> lCat,
            final Optional<Integer> lEta,
            final Optional<Integer> montepremi,
            final Integer idCircolo) {

        this.idTorneo = idTorneo;
        this.nEdizione = nEdizione;
        this.tipo = tipo;
        this.dInizio = dInizio;
        this.dFine = dFine;
        this.limCategoria = lCat;
        this.limEta = lEta;
        this.montepremi = montepremi;
        this.idCircolo = idCircolo;
    }

    public Integer getIdTorneo() {
        return this.idTorneo;
    }

    public Integer getNumEdizione() {
        return this.nEdizione;
    }

    public Tipo getTipo() {
        return this.tipo;
    }

    public Date getDataInizio() {
        return this.dInizio;
    }

    public Date getDataFine() {
        return this.dFine;
    }

    public Optional<Integer> getLimCategoria() {
        return this.limCategoria;
    }

    public Optional<Integer> getLimEta() {
        return this.limEta;
    }

    public Optional<Integer> getMontepremi() {
        return this.montepremi;
    }

    public Integer getIdCircolo() {
        return this.idCircolo;
    }
}
