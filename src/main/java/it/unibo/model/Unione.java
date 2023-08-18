package it.unibo.model;

public class Unione {
    private final Integer idCoppia;
    private final Integer idGiocatore;

    public Unione(final Integer coppia, final Integer giocatore) {
        this.idCoppia = coppia;
        this.idGiocatore = giocatore;
    }

    public Integer getIdCoppia() {
        return this.idCoppia;
    }

    public Integer getIdGiocatore() {
        return this.idGiocatore;
    }
}
