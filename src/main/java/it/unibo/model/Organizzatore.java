package it.unibo.model;

import java.util.Objects;

public class Organizzatore {
    private final Integer id;
    private final String nome;
    private final String cognome;
    private final String email;
    private final String password;

    public Organizzatore(final Integer id,
            final String nome,
            final String cognome,
            final String email,
            final String password) {

        this.id = id;
        this.nome = Objects.requireNonNull(nome);
        this.cognome = Objects.requireNonNull(cognome);
        this.email = Objects.requireNonNull(email);
        this.password = Objects.requireNonNull(password);
    }

    /*
     * Constructor without id.
     */
    public Organizzatore(final String nome,
            final String cognome,
            final String email,
            final String password) {

        this(null, nome, cognome, email, password);
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
}
