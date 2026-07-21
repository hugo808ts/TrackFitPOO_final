package br.com.trackfit.model;

public interface CargaCalculavel {
    double calcularCarga(ItemTreino item);

    String descreverFormula();
}
