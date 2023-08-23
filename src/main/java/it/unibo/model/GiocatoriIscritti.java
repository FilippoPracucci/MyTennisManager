package it.unibo.model;

public class GiocatoriIscritti {

    private final Integer idUtente;
    private final String nome;
    private final String cognome;
    private final String email;
    private final String tessera;
    private final String classifica;
    private final int eta;
    private final String telefono;
    private final Integer idTorneo;
    private final Integer nEdizione;
    private final String prefOrario;

    public GiocatoriIscritti(final Integer idUtente,
            final String nome,
            final String cognome,
            final String email,
            final String tessera,
            final String classifica,
            final int eta,
            final String telefono,
            final Integer idTorneo,
            final Integer nEdizione,
            final String prefOrario) {

        this.idUtente = idUtente;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.tessera = tessera;
        this.classifica = classifica;
        this.eta = eta;
        this.telefono = telefono;
        this.idTorneo = idTorneo;
        this.nEdizione = nEdizione;
        this.prefOrario = prefOrario;
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

    public String getEmail() {
        return this.email;
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

    public String getTelefono() {
        return this.telefono;
    }

    public Integer getIdTorneo() {
        return this.idTorneo;
    }

    public Integer getNumEdizione() {
        return this.nEdizione;
    }

    public String getPrefOrario() {
        return this.prefOrario;
    }
}
