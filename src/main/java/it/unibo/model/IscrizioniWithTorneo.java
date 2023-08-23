package it.unibo.model;

import java.util.Date;
import it.unibo.model.Torneo.Tipo;

public class IscrizioniWithTorneo {

    private final Integer idTorneo;
    private final Integer nEdizione;
    private final Tipo tipo;
    private final Date dInizio;
    private final Date dFine;
    private final String nomeCircolo;
    private final Integer limCategoria;
    private final Integer limEta;
    private final Integer montepremi;
    private final String preferenzaOrario;
    private final Integer idUtente;
    private final Integer idCoppia;

    public IscrizioniWithTorneo(final Integer idTorneo,
        final Integer nEdizione,
        final Tipo tipo,
        final Date dInizio,
        final Date dFine,
        final String nCircolo,
        final Integer limCategoria,
        final Integer limEta,
        final Integer montepremi,
        final String preferenzaOrario,
        final Integer idUtente,
        final Integer idCoppia) {

        this.idTorneo = idTorneo;
        this.nEdizione = nEdizione;
        this.tipo = tipo;
        this.dInizio = dInizio;
        this.dFine = dFine;
        this.nomeCircolo = nCircolo;
        this.limCategoria = limCategoria;
        this.limEta = limEta;
        this.montepremi = montepremi;
        this.preferenzaOrario = preferenzaOrario;
        this.idUtente = idUtente;
        this.idCoppia = idCoppia;
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

    public String getNomeCircolo() {
        return this.nomeCircolo;
    }

    public Integer getLimCategoria() {
        return this.limCategoria;
    }

    public Integer getLimEta() {
        return this.limEta;
    }

    public Integer getMontepremi() {
        return this.montepremi;
    }

    public String getPreferenzaOrario() {
        return this.preferenzaOrario;
    }

    public Integer getIdUtente() {
        return this.idUtente;
    }

    public Integer getIdCoppia() {
        return this.idCoppia;
    }
}
