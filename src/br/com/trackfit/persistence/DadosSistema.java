package br.com.trackfit.persistence;

import br.com.trackfit.model.Atleta;
import br.com.trackfit.model.Exercicio;
import br.com.trackfit.model.Treinador;
import br.com.trackfit.model.Treino;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DadosSistema implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Atleta> atletas = new ArrayList<Atleta>();
    private List<Treinador> treinadores = new ArrayList<Treinador>();
    private List<Exercicio> exercicios = new ArrayList<Exercicio>();
    private List<Treino> treinos = new ArrayList<Treino>();

    private int proximoAtletaId = 1;
    private int proximoTreinadorId = 1;
    private int proximoExercicioId = 1;
    private int proximoTreinoId = 1;

    public List<Atleta> getAtletas() {
        return atletas;
    }

    public List<Treinador> getTreinadores() {
        return treinadores;
    }

    public List<Exercicio> getExercicios() {
        return exercicios;
    }

    public List<Treino> getTreinos() {
        return treinos;
    }

    public int novoAtletaId() {
        return proximoAtletaId++;
    }

    public int novoTreinadorId() {
        return proximoTreinadorId++;
    }

    public int novoExercicioId() {
        return proximoExercicioId++;
    }

    public int novoTreinoId() {
        return proximoTreinoId++;
    }
}
