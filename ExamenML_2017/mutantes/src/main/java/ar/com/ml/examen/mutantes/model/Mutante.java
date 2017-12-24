package ar.com.ml.examen.mutantes.model;

import javax.persistence.*;

@Entity
@Table( name="mutante" )
public class Mutante {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_mutante")
    private int id_mutante;
    @Column(name = "dna")
    private String dna;
    @Column(name = "es_mutante")
    private boolean mutante;

    public Mutante(){}

    public Mutante(int id_mutante, String dna, boolean mutante) {
        this.id_mutante = id_mutante;
        this.dna = dna;
        this.mutante = mutante;
    }

    public Mutante(String dna, boolean mutante) {
        this.dna = dna;
        this.mutante = mutante;
    }

    public int getId_mutante() {
        return id_mutante;
    }


    public void setId_mutante(int id_mutante) {
        this.id_mutante = id_mutante;
    }

    public String getDna() {
        return dna;
    }

    public void setDna(String dna) {
        this.dna = dna;
    }

    public boolean isMutante() {
        return mutante;
    }

    public void setMutante(boolean mutante) {
        this.mutante = mutante;
    }


}
