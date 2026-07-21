package br.com.trackfit.model;

public class Atleta extends Pessoa {
    private static final long serialVersionUID = 1L;

    private double pesoKg;
    private ObjetivoTreino objetivo;
    private Treinador treinador;

    public Atleta(int id, String nome, String email, double pesoKg, ObjetivoTreino objetivo, Treinador treinador) {
        super(id, nome, email);
        this.pesoKg = pesoKg;
        this.objetivo = objetivo;
        this.treinador = treinador;
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(double pesoKg) {
        this.pesoKg = pesoKg;
    }

    public ObjetivoTreino getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(ObjetivoTreino objetivo) {
        this.objetivo = objetivo;
    }

    public Treinador getTreinador() {
        return treinador;
    }

    public void setTreinador(Treinador treinador) {
        this.treinador = treinador;
    }
}
