package it.unibo.model;

public class Unione {
    private final int idCoppia;
    private final int idUtente;

    public Unione(final int coppia, final int utente) {
        this.idCoppia = coppia;
        this.idUtente = utente;
    }

    public int getIdCoppia() {
        return this.idCoppia;
    }

    public int getIdUtente() {
        return this.idUtente;
    }
}
