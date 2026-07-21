package br.com.trackfit.service;

import br.com.trackfit.exception.DadosInvalidosException;
import br.com.trackfit.exception.EntidadeNaoEncontradaException;
import br.com.trackfit.model.Atleta;
import br.com.trackfit.model.Exercicio;
import br.com.trackfit.model.ExercicioCardio;
import br.com.trackfit.model.ExercicioForca;
import br.com.trackfit.model.ItemTreino;
import br.com.trackfit.model.ObjetivoTreino;
import br.com.trackfit.model.Treinador;
import br.com.trackfit.model.Treino;
import br.com.trackfit.persistence.BancoDados;
import br.com.trackfit.persistence.DadosSistema;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AcademiaService {
    private final BancoDados bancoDados;
    private final DadosSistema dados;

    public AcademiaService(BancoDados bancoDados) {
        this.bancoDados = bancoDados;
        this.dados = bancoDados.carregar();
        if (dados.getTreinadores().isEmpty() && dados.getAtletas().isEmpty()) {
            popularExemplos();
        } else if (dados.getTreinos().isEmpty()) {
            popularTreinosExemploSePossivel();
        }
    }

    public static AcademiaService criarPadrao() {
        return new AcademiaService(new BancoDados("data/trackfit.ser"));
    }

    public Treinador cadastrarTreinador(String nome, String email, String especialidade, String cref)
            throws DadosInvalidosException {
        validarTexto(nome, "Nome do treinador");
        validarTexto(especialidade, "Especialidade");
        Treinador treinador = new Treinador(dados.novoTreinadorId(), nome.trim(), email.trim(),
                especialidade.trim(), cref.trim());
        dados.getTreinadores().add(treinador);
        salvar();
        return treinador;
    }

    public Atleta cadastrarAtleta(String nome, String email, double pesoKg, ObjetivoTreino objetivo,
            Integer treinadorId) throws DadosInvalidosException, EntidadeNaoEncontradaException {
        validarTexto(nome, "Nome do atleta");
        if (pesoKg <= 0) {
            throw new DadosInvalidosException("O peso do atleta deve ser maior que zero.");
        }
        Treinador treinador = treinadorId == null ? null : buscarTreinador(treinadorId);
        Atleta atleta = new Atleta(dados.novoAtletaId(), nome.trim(), email.trim(), pesoKg, objetivo, treinador);
        dados.getAtletas().add(atleta);
        salvar();
        return atleta;
    }

    public Exercicio cadastrarExercicioForca(String nome, String grupoMuscular) throws DadosInvalidosException {
        validarTexto(nome, "Nome do exercicio");
        validarTexto(grupoMuscular, "Grupo muscular");
        Exercicio exercicio = new ExercicioForca(dados.novoExercicioId(), nome.trim(), grupoMuscular.trim());
        dados.getExercicios().add(exercicio);
        salvar();
        return exercicio;
    }

    public Exercicio cadastrarExercicioCardio(String nome, String modalidade) throws DadosInvalidosException {
        validarTexto(nome, "Nome do exercicio");
        validarTexto(modalidade, "Modalidade");
        Exercicio exercicio = new ExercicioCardio(dados.novoExercicioId(), nome.trim(), modalidade.trim());
        dados.getExercicios().add(exercicio);
        salvar();
        return exercicio;
    }

    public ItemTreino criarItemTreino(int exercicioId, int series, int repeticoes, double cargaKg,
            double duracaoMinutos, double distanciaKm, int intensidade)
            throws DadosInvalidosException, EntidadeNaoEncontradaException {
        Exercicio exercicio = buscarExercicio(exercicioId);
        if (exercicio instanceof ExercicioForca) {
            if (series <= 0 || repeticoes <= 0 || cargaKg <= 0) {
                throw new DadosInvalidosException("Exercicios de forca exigem series, repeticoes e carga positivas.");
            }
        }
        if (exercicio instanceof ExercicioCardio) {
            if (duracaoMinutos <= 0 || intensidade <= 0) {
                throw new DadosInvalidosException("Exercicios de cardio exigem duracao e intensidade positivas.");
            }
        }
        if (intensidade < 0 || intensidade > 10) {
            throw new DadosInvalidosException("A intensidade deve ficar entre 0 e 10.");
        }
        return new ItemTreino(exercicio, series, repeticoes, cargaKg, duracaoMinutos, distanciaKm, intensidade);
    }

    public Treino registrarTreino(int atletaId, LocalDate data, String observacoes, List<ItemTreino> itens)
            throws DadosInvalidosException, EntidadeNaoEncontradaException {
        Atleta atleta = buscarAtleta(atletaId);
        if (data == null) {
            throw new DadosInvalidosException("A data do treino e obrigatoria.");
        }
        if (itens == null || itens.isEmpty()) {
            throw new DadosInvalidosException("Adicione pelo menos um exercicio ao treino.");
        }
        Treino treino = new Treino(dados.novoTreinoId(), atleta, data, observacoes == null ? "" : observacoes);
        for (ItemTreino item : itens) {
            treino.adicionarItem(item);
        }
        dados.getTreinos().add(treino);
        salvar();
        return treino;
    }

    public void removerTreino(int treinoId) throws EntidadeNaoEncontradaException {
        Treino treino = buscarTreino(treinoId);
        dados.getTreinos().remove(treino);
        salvar();
    }

    public List<Atleta> listarAtletas() {
        return Collections.unmodifiableList(dados.getAtletas());
    }

    public List<Treinador> listarTreinadores() {
        return Collections.unmodifiableList(dados.getTreinadores());
    }

    public List<Exercicio> listarExercicios() {
        return Collections.unmodifiableList(dados.getExercicios());
    }

    public List<Treino> listarTreinos() {
        List<Treino> treinos = new ArrayList<Treino>(dados.getTreinos());
        Collections.sort(treinos, new Comparator<Treino>() {
            @Override
            public int compare(Treino t1, Treino t2) {
                return t2.getData().compareTo(t1.getData());
            }
        });
        return treinos;
    }

    public double calcularCargaTotalAtleta(int atletaId) {
        double total = 0.0;
        for (Treino treino : dados.getTreinos()) {
            if (treino.getAtleta().getId() == atletaId) {
                total += treino.calcularCargaTotal();
            }
        }
        return total;
    }

    public String gerarRelatorioResumo() {
        StringBuilder sb = new StringBuilder();
        sb.append("RELATORIO DE CARGAS\n\n");
        for (Atleta atleta : dados.getAtletas()) {
            sb.append(atleta.getNome()).append(" - objetivo: ").append(atleta.getObjetivo()).append("\n");
            sb.append("Carga acumulada: ").append(String.format("%.1f", calcularCargaTotalAtleta(atleta.getId()))).append("\n");
            sb.append("Treinador: ");
            sb.append(atleta.getTreinador() == null ? "sem treinador" : atleta.getTreinador().getNome());
            sb.append("\n\n");
        }
        sb.append("Ultimos treinos registrados:\n");
        for (Treino treino : listarTreinos()) {
            sb.append("#").append(treino.getId()).append(" - ");
            sb.append(treino.getData()).append(" - ");
            sb.append(treino.getAtleta().getNome()).append(" - ");
            sb.append(String.format("%.1f", treino.calcularCargaTotal())).append(" pontos - ");
            sb.append(treino.classificarCarga()).append("\n");
        }
        return sb.toString();
    }

    private Atleta buscarAtleta(int id) throws EntidadeNaoEncontradaException {
        for (Atleta atleta : dados.getAtletas()) {
            if (atleta.getId() == id) {
                return atleta;
            }
        }
        throw new EntidadeNaoEncontradaException("Atleta nao encontrado.");
    }

    private Treinador buscarTreinador(int id) throws EntidadeNaoEncontradaException {
        for (Treinador treinador : dados.getTreinadores()) {
            if (treinador.getId() == id) {
                return treinador;
            }
        }
        throw new EntidadeNaoEncontradaException("Treinador nao encontrado.");
    }

    private Exercicio buscarExercicio(int id) throws EntidadeNaoEncontradaException {
        for (Exercicio exercicio : dados.getExercicios()) {
            if (exercicio.getId() == id) {
                return exercicio;
            }
        }
        throw new EntidadeNaoEncontradaException("Exercicio nao encontrado.");
    }

    private Treino buscarTreino(int id) throws EntidadeNaoEncontradaException {
        for (Treino treino : dados.getTreinos()) {
            if (treino.getId() == id) {
                return treino;
            }
        }
        throw new EntidadeNaoEncontradaException("Treino nao encontrado.");
    }

    private void validarTexto(String valor, String campo) throws DadosInvalidosException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new DadosInvalidosException(campo + " e obrigatorio.");
        }
    }

    private void salvar() {
        bancoDados.salvar(dados);
    }

    private void popularExemplos() {
        try {
            Treinador treinador = cadastrarTreinador("Ana Lima", "ana@trackfit.com", "Treinamento funcional", "CREF-0001");
            Atleta carlos = cadastrarAtleta("Carlos Souza", "carlos@email.com", 82.5,
                    ObjetivoTreino.HIPERTROFIA, treinador.getId());
            Atleta marina = cadastrarAtleta("Marina Rocha", "marina@email.com", 64.0,
                    ObjetivoTreino.CONDICIONAMENTO, treinador.getId());
            Exercicio supino = cadastrarExercicioForca("Supino reto", "Peitoral");
            Exercicio agachamento = cadastrarExercicioForca("Agachamento livre", "Pernas");
            Exercicio corrida = cadastrarExercicioCardio("Corrida na esteira", "Corrida");

            popularTreinosExemplo(carlos, marina, supino, agachamento, corrida);
        } catch (DadosInvalidosException e) {
            throw new IllegalStateException(e);
        } catch (EntidadeNaoEncontradaException e) {
            throw new IllegalStateException(e);
        }
    }

    private void popularTreinosExemploSePossivel() {
        Atleta carlos = encontrarAtletaPorNome("Carlos Souza");
        Atleta marina = encontrarAtletaPorNome("Marina Rocha");
        Exercicio supino = encontrarExercicioPorNome("Supino reto");
        Exercicio agachamento = encontrarExercicioPorNome("Agachamento livre");
        Exercicio corrida = encontrarExercicioPorNome("Corrida na esteira");

        if (carlos == null || marina == null || supino == null || agachamento == null || corrida == null) {
            return;
        }

        try {
            popularTreinosExemplo(carlos, marina, supino, agachamento, corrida);
        } catch (DadosInvalidosException e) {
            throw new IllegalStateException(e);
        } catch (EntidadeNaoEncontradaException e) {
            throw new IllegalStateException(e);
        }
    }

    private void popularTreinosExemplo(Atleta carlos, Atleta marina, Exercicio supino,
            Exercicio agachamento, Exercicio corrida)
            throws DadosInvalidosException, EntidadeNaoEncontradaException {
        List<ItemTreino> treinoCarlos = new ArrayList<ItemTreino>();
        treinoCarlos.add(criarItemTreino(supino.getId(), 3, 10, 40, 0, 0, 5));
        treinoCarlos.add(criarItemTreino(corrida.getId(), 0, 0, 0, 30, 3, 5));
        registrarTreino(carlos.getId(), LocalDate.of(2026, 7, 14), "Treino de peito e triceps", treinoCarlos);

        List<ItemTreino> treinoMarina = new ArrayList<ItemTreino>();
        treinoMarina.add(criarItemTreino(corrida.getId(), 0, 0, 0, 50, 1, 5));
        registrarTreino(marina.getId(), LocalDate.of(2026, 7, 12), "Treino leve de condicionamento", treinoMarina);

        List<ItemTreino> treinoCarlosAnterior = new ArrayList<ItemTreino>();
        treinoCarlosAnterior.add(criarItemTreino(supino.getId(), 3, 10, 40, 0, 0, 5));
        treinoCarlosAnterior.add(criarItemTreino(agachamento.getId(), 2, 10, 30, 0, 0, 5));
        treinoCarlosAnterior.add(criarItemTreino(corrida.getId(), 0, 0, 0, 20, 0, 5));
        registrarTreino(carlos.getId(), LocalDate.of(2026, 7, 10), "Treino completo", treinoCarlosAnterior);
    }

    private Atleta encontrarAtletaPorNome(String nome) {
        for (Atleta atleta : dados.getAtletas()) {
            if (atleta.getNome().equalsIgnoreCase(nome)) {
                return atleta;
            }
        }
        return null;
    }

    private Exercicio encontrarExercicioPorNome(String nome) {
        for (Exercicio exercicio : dados.getExercicios()) {
            if (exercicio.getNome().equalsIgnoreCase(nome)) {
                return exercicio;
            }
        }
        return null;
    }
}
