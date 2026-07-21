package br.com.trackfit.model;

import java.io.Serializable;

public abstract class Exercicio implements Serializable, CargaCalculavel {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private String grupo;

    public Exercicio(int id, String nome, String grupo) {
        this.id = id;
        this.nome = nome;
        this.grupo = grupo;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public abstract String getTipo();

    @Override
    public String toString() {
        return id + " - " + nome + " (" + getTipo() + ")";
    }
}
