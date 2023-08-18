package it.unibo.model;

public class CompagnoUnioni {

    private final Integer idCoppia;
    private final Integer idUtente;
    private final String nome;
    private final String cognome;
    private final String classifica;
    private final String sesso;

    public CompagnoUnioni(final Integer idCoppia,
            final Integer idUtente,
            final String nome,
            final String cognome,
            final String classifica,
            final String sesso) {

        this.idCoppia = idCoppia;
        this.idUtente = idUtente;
        this.nome = nome;
        this.cognome = cognome;
        this.classifica = classifica;
        this.sesso = sesso;
    }

    public Integer getIdCoppia() {
        return this.idCoppia;
    }

    public Integer getIdUtente() {
        return this.idUtente;
    }

    public String getNome() {
        return this.nome;
    }

    public String getCognome() {
        return this.cognome;
    }

    public String getClassifica() {
        return this.classifica;
    }

    public String getSesso() {
        return this.sesso;
    }
}
