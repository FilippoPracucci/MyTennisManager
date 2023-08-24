package it.unibo.model;

import java.util.Objects;

public class Circolo {
    private final Integer id;
    private final Integer organizzatore;
    private final String nome;
    private final String citta;
    private final String indirizzo;
    private final String telefono;

    public Circolo(final Integer id,
            final Integer organizzatore,
            final String nome,
            final String citta,
            final String indirizzo,
            final String telefono) {

        this.id = id;
        this.organizzatore = organizzatore;
        this.nome = Objects.requireNonNull(nome);
        this.citta = Objects.requireNonNull(citta);
        this.indirizzo = Objects.requireNonNull(indirizzo);
        this.telefono = Objects.requireNonNull(telefono);
    }

    /*
     * Constructor without id.
     */
    public Circolo(final Integer organizzatore,
            final String nome,
            final String citta,
            final String indirizzo,
            final String telefono) {

        this(null, organizzatore, nome, citta, indirizzo, telefono);
    }

    public Integer getId() {
        return this.id;
    }

    public Integer getOrganizzatore() {
        return this.organizzatore;
    }

    public String getNome() {
        return this.nome;
    }

    public String getCitta() {
        return this.citta;
    }

    public String getIndirizzo() {
        return this.indirizzo;
    }

    public String getTelefono() {
        return this.telefono;
    }
}
