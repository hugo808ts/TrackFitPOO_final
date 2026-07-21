package br.com.trackfit.model;

import java.io.Serializable;

public class ItemTreino implements Serializable {
    private static final long serialVersionUID = 1L;

    private Exercicio exercicio;
    private int series;
    private int repeticoes;
    private double cargaKg;
    private double duracaoMinutos;
    private double distanciaKm;
    private int intensidade;

    public ItemTreino(Exercicio exercicio, int series, int repeticoes, double cargaKg,
            double duracaoMinutos, double distanciaKm, int intensidade) {
        this.exercicio = exercicio;
        this.series = series;
        this.repeticoes = repeticoes;
        this.cargaKg = cargaKg;
        this.duracaoMinutos = duracaoMinutos;
        this.distanciaKm = distanciaKm;
        this.intensidade = intensidade;
    }

    public Exercicio getExercicio() {
        return exercicio;
    }

    public int getSeries() {
        return series;
    }

    public int getRepeticoes() {
        return repeticoes;
    }

    public double getCargaKg() {
        return cargaKg;
    }

    public double getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public int getIntensidade() {
        return intensidade;
    }

    public double calcularCarga() {
        return exercicio.calcularCarga(this);
    }

    public String resumo() {
        return exercicio.getNome() + " - carga " + String.format("%.1f", calcularCarga());
    }
}
