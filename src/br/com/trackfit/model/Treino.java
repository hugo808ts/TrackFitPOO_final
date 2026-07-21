package br.com.trackfit.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Treino implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private Atleta atleta;
    private LocalDate data;
    private String observacoes;
    private List<ItemTreino> itens;

    public Treino(int id, Atleta atleta, LocalDate data, String observacoes) {
        this.id = id;
        this.atleta = atleta;
        this.data = data;
        this.observacoes = observacoes;
        this.itens = new ArrayList<ItemTreino>();
    }

    public int getId() {
        return id;
    }

    public Atleta getAtleta() {
        return atleta;
    }

    public LocalDate getData() {
        return data;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void adicionarItem(ItemTreino item) {
        itens.add(item);
    }

    public List<ItemTreino> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public double calcularCargaTotal() {
        double total = 0.0;
        for (ItemTreino item : itens) {
            total += item.calcularCarga();
        }
        return total;
    }

    public String classificarCarga() {
        double carga = calcularCargaTotal();
        if (carga < 300) {
            return "Leve";
        }
        if (carga < 900) {
            return "Moderada";
        }
        if (carga < 1800) {
            return "Alta";
        }
        return "Muito alta";
    }
}
