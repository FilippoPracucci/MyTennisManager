package it.unibo.model;

import java.util.Objects;

public class Giocatore {
    private final Integer id;
    private final String nome;
    private final String cognome;
    private final String email;
    private final String password;
    private final String tessera;
    private final String classifica;
    private final int eta;
    private final String sesso;
    private final String telefono;
    private final Integer idCircolo;

    public Giocatore(final Integer id,
            final String nome,
            final String cognome,
            final String email,
            final String password,
            final String tessera,
            final String classifica,
            final int eta,
            final String sesso,
            final String telefono,
            final Integer id_circolo) {

        this.id =  id;
        this.nome = Objects.requireNonNull(nome);
        this.cognome = Objects.requireNonNull(cognome);
        this.email = Objects.requireNonNull(email);
        this.password = Objects.requireNonNull(password);
        this.tessera = Objects.requireNonNull(tessera);
        this.classifica = Objects.requireNonNull(classifica);
        this.eta = eta;
        this.sesso = Objects.requireNonNull(sesso);
        this.telefono = Objects.requireNonNull(telefono);
        this.idCircolo = Objects.requireNonNull(id_circolo);
    }

    /*
     * Constructor without id.
     */
    public Giocatore(final String nome,
            final String cognome,
            final String email,
            final String password,
            final String tessera,
            final String classifica,
            final int eta,
            final String sesso,
            final String telefono,
            final Integer circolo) {

        this(null, nome, cognome, email, password, tessera, classifica, eta, sesso, telefono, circolo);
    }

    public Integer getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public String getCognome() {
        return this.cognome;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getTessera() {
        return this.tessera;
    }

    public String getClassifica() {
        return this.classifica;
    }

    public int getEta() {
        return this.eta;
    }

    public String getSesso() {
        return this.sesso;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Integer getIdCircolo() {
        return this.idCircolo;
    }
}
