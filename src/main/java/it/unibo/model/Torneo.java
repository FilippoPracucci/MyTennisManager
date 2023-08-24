package it.unibo.model;

import java.util.Objects;
import java.util.Optional;
import java.util.List;

public class Torneo {
    public enum Tipo {
        SINGOLARE_MASCHILE("Singolare maschile"),
        SINGOLARE_FEMMINILE("Singolare femminile"),
        DOPPIO_MASCHILE("Doppio maschile"),
        DOPPIO_FEMMINILE("Doppio femminile");

        private String nome;

        private Tipo(final String nome) {
            this.nome = nome;
        }

        public static Tipo getTipo(final String nome) {
            return List.of(Tipo.values()).stream().filter(t -> t.getNome().equalsIgnoreCase(nome)).findFirst().get();
        }

        public String getNome() {
            return this.nome;
        }
    }

    private final Integer id;
    private final Tipo tipo;
    private final Optional<Integer> limiteCategoria;
    private final Optional<Integer> limiteEta;
    private final Optional<Integer> montepremi;

    /*
     * Constructor with all paramaters.
     */
    public Torneo(final Integer id,
            final Tipo tipo,
            final Optional<Integer> limite_cat,
            final Optional<Integer> limite_eta,
            final Optional<Integer> montepremi) {

        this.id = id;
        this.tipo = Objects.requireNonNull(tipo);
        this.limiteCategoria = limite_cat;
        this.limiteEta = limite_eta;
        this.montepremi = montepremi;
    }

    /*
     * Constructor without id.
     */
    public Torneo(final Tipo tipo,
            final Optional<Integer> limite_cat,
            final Optional<Integer> limite_eta,
            final Optional<Integer> montepremi) {

        this.id = null;
        this.tipo = Objects.requireNonNull(tipo);
        this.limiteCategoria = limite_cat;
        this.limiteEta = limite_eta;
        this.montepremi = montepremi;
    }

    public Integer getId() {
        return this.id;
    }

    public Tipo getTipo() {
        return this.tipo;
    }

    public Optional<Integer> getLimiteCategoria() {
        return this.limiteCategoria;
    }

    public Optional<Integer> getLimiteEta() {
        return this.limiteEta;
    }

    public Optional<Integer> getMontepremi() {
        return this.montepremi;
    }
}
