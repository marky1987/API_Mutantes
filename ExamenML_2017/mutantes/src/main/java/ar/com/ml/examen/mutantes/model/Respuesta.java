package ar.com.ml.examen.mutantes.model;

public class Respuesta {

    private Long contadorMutantes;
    private Long contadorNoMutantes;
    private Double ratio;

    public Respuesta() {}

    public Respuesta(Long contadorMutantes, Long contadorNoMutantes, Double ratio) {
        this.contadorMutantes = contadorMutantes;
        this.contadorNoMutantes = contadorNoMutantes;
        this.ratio = ratio;
    }

    public Long getContadorMutantes() {
        return contadorMutantes;
    }

    public void setContadorMutantes(Long contadorMutantes) {
        this.contadorMutantes = contadorMutantes;
    }

    public Long getContadorNoMutantes() {
        return contadorNoMutantes;
    }

    public void setContadorNoMutantes(Long contadorNoMutantes) {
        this.contadorNoMutantes = contadorNoMutantes;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    @Override
    public String toString() {
        return '{' + "\"ADN\":{" +
                "\"count_mutant_dna\":" + contadorMutantes +
                ", \"count_human_dna\":" + contadorNoMutantes  +
                ", \"ratio\":" + ratio +
                '}' +
                '}';
    }
}
