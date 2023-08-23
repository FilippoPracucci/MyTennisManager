package it.unibo.model;

public class CoppieIscritte {

    private final Integer idCoppia;
    private final Integer giocatore1;
    private final Integer giocatore2;
    private final String tessera1;
    private final String tessera2;
    private final String classifica1;
    private final String classifica2;
    private final String telefono1;
    private final String telefono2;
    private final Integer idTorneo;
    private final Integer nEdizione;
    private final String prefOrario;

    public CoppieIscritte(final Integer idCoppia,
        final Integer giocatore1,
        final Integer giocatore2,
        final String tessera1,
        final String tessera2,
        final String classifica1,
        final String classifica2,
        final String telefono1,
        final String telefono2,
        final Integer idTorneo,
        final Integer nEdizione,
        final String prefOrario) {

        this.idCoppia = idCoppia;
        this.giocatore1 = giocatore1;
        this.giocatore2 = giocatore2;
        this.tessera1 = tessera1;
        this.tessera2 = tessera2;
        this.classifica1 = classifica1;
        this.classifica2 = classifica2;
        this.telefono1 = telefono1;
        this.telefono2 = telefono2;
        this.idTorneo = idTorneo;
        this.nEdizione = nEdizione;
        this.prefOrario = prefOrario;
    }

    public Integer getIdCoppia() {
        return this.idCoppia;
    }

    public Integer getGiocatore1() {
        return this.giocatore1;
    }

    public Integer getGiocatore2() {
        return this.giocatore2;
    }

    public String getTessera1() {
        return this.tessera1;
    }

    public String getTessera2() {
        return this.tessera2;
    }

    public String getClassifica1() {
        return this.classifica1;
    }

    public String getClassifica2() {
        return this.classifica2;
    }

    public String getTelefono1() {
        return this.telefono1;
    }

    public String getTelefono2() {
        return this.telefono2;
    }

    public Integer getIdTorneo() {
        return this.idTorneo;
    }

    public Integer getnEdizione() {
        return this.nEdizione;
    }

    public String getPrefOrario() {
        return this.prefOrario;
    }
}
