package br.com.trackfit.ui;

import br.com.trackfit.exception.DadosInvalidosException;
import br.com.trackfit.exception.EntidadeNaoEncontradaException;
import br.com.trackfit.model.Atleta;
import br.com.trackfit.model.Exercicio;
import br.com.trackfit.model.ItemTreino;
import br.com.trackfit.model.ObjetivoTreino;
import br.com.trackfit.model.Treinador;
import br.com.trackfit.model.Treino;
import br.com.trackfit.service.AcademiaService;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private static final Color AZUL = new Color(33, 98, 142);
    private static final Color AZUL_ESCURO = new Color(24, 76, 115);
    private static final Color VERDE = new Color(47, 132, 88);
    private static final Color FUNDO = new Color(244, 247, 251);
    private static final Color CARTAO = Color.WHITE;
    private static final Color BORDA = new Color(194, 208, 225);
    private static final Color CAMPO = new Color(248, 251, 255);
    private static final Color TEXTO = new Color(20, 29, 45);
    private static final Color TEXTO_MUTED = new Color(73, 91, 115);
    private static final Font FONTE_BASE = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONTE_HEADER = new Font("Segoe UI", Font.BOLD, 24);

    private final AcademiaService service;
    private final Map<String, RoundedButton> botoesAbas = new LinkedHashMap<String, RoundedButton>();

    private CardLayout cardLayout;
    private JPanel cardsPanel;

    private DefaultTableModel atletasModel;
    private DefaultTableModel treinadoresModel;
    private DefaultTableModel exerciciosModel;
    private DefaultTableModel treinosModel;

    private JComboBox<Treinador> atletaTreinadorCombo;
    private JComboBox<Atleta> treinoAtletaCombo;
    private JComboBox<Exercicio> treinoExercicioCombo;
    private JTextArea relatorioArea;
    private JList<String> itensList;
    private DefaultListModel<String> itensListModel;
    private final List<ItemTreino> itensTemporarios = new ArrayList<ItemTreino>();
    private boolean itensExemploCriados;

    public MainFrame(AcademiaService service) {
        this.service = service;
        setTitle("TrackFit POO - Rastreamento de Treinos e Cargas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1280, 720));
        setPreferredSize(new Dimension(1280, 720));
        setContentPane(criarConteudo());
        atualizarTudo();
        selecionarAba("Treinos");
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel criarConteudo() {
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(FUNDO);
        raiz.add(criarCabecalho(), BorderLayout.NORTH);

        JPanel corpo = new JPanel(new BorderLayout(0, 18));
        corpo.setBackground(FUNDO);
        corpo.setBorder(new EmptyBorder(14, 24, 24, 31));
        corpo.add(criarAbas(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        cardsPanel.setOpaque(false);
        cardsPanel.add(criarPainelAtletas(), "Atletas");
        cardsPanel.add(criarPainelTreinadores(), "Treinadores");
        cardsPanel.add(criarPainelExercicios(), "Exercicios");
        cardsPanel.add(criarPainelTreinos(), "Treinos");
        cardsPanel.add(criarPainelRelatorios(), "Relatorios");

        corpo.add(cardsPanel, BorderLayout.CENTER);
        raiz.add(corpo, BorderLayout.CENTER);
        return raiz;
    }

    private JLabel criarCabecalho() {
        JLabel label = new JLabel("TrackFit POO - Rastreamento de Treinos e Cargas");
        label.setOpaque(true);
        label.setBackground(AZUL);
        label.setForeground(Color.WHITE);
        label.setFont(FONTE_HEADER);
        label.setBorder(new EmptyBorder(0, 24, 0, 24));
        label.setPreferredSize(new Dimension(0, 55));
        return label;
    }

    private JPanel criarAbas() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        painel.setOpaque(false);
        adicionarAba(painel, "Atletas");
        adicionarAba(painel, "Treinadores");
        adicionarAba(painel, "Exercicios");
        adicionarAba(painel, "Treinos");
        adicionarAba(painel, "Relatorios");
        return painel;
    }

    private void adicionarAba(JPanel painel, final String nome) {
        final RoundedButton botao = new RoundedButton(nome, 8);
        botao.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        botao.setHorizontalAlignment(SwingConstants.LEFT);
        botao.setPreferredSize(new Dimension("Treinadores".equals(nome) ? 150 : 119, 43));
        botao.setMargin(new Insets(0, 18, 0, 18));
        botao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selecionarAba(nome);
            }
        });
        botoesAbas.put(nome, botao);
        painel.add(botao);
    }

    private void selecionarAba(String nome) {
        if (cardLayout != null && cardsPanel != null) {
            cardLayout.show(cardsPanel, nome);
        }
        for (Map.Entry<String, RoundedButton> entry : botoesAbas.entrySet()) {
            boolean ativa = entry.getKey().equals(nome);
            RoundedButton botao = entry.getValue();
            botao.setFont(new Font("Segoe UI", ativa ? Font.BOLD : Font.PLAIN, 18));
            botao.setColors(ativa ? Color.WHITE : new Color(232, 238, 246),
                    ativa ? AZUL_ESCURO : TEXTO_MUTED, BORDA);
        }
    }

    private JPanel criarPainelAtletas() {
        JPanel painel = criarPagina();

        final JTextField nomeField = criarCampoTexto(18);
        final JTextField emailField = criarCampoTexto(18);
        final JTextField pesoField = criarCampoTexto(8);
        final JComboBox<ObjetivoTreino> objetivoCombo = new JComboBox<ObjetivoTreino>(ObjetivoTreino.values());
        estilizarCombo(objetivoCombo);
        atletaTreinadorCombo = new JComboBox<Treinador>();
        estilizarCombo(atletaTreinadorCombo);
        configurarRenderTreinador(atletaTreinadorCombo);

        JPanel cardFormulario = criarCartao("Novo atleta");
        JPanel formulario = criarFormulario();
        adicionarLinha(formulario, 0, "Nome", nomeField);
        adicionarLinha(formulario, 1, "Email", emailField);
        adicionarLinha(formulario, 2, "Peso (kg)", pesoField);
        adicionarLinha(formulario, 3, "Objetivo", objetivoCombo);
        adicionarLinha(formulario, 4, "Treinador", atletaTreinadorCombo);

        JButton salvar = criarBotaoPrimario("Cadastrar atleta");
        salvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Treinador treinador = (Treinador) atletaTreinadorCombo.getSelectedItem();
                    Integer treinadorId = treinador == null ? null : treinador.getId();
                    service.cadastrarAtleta(nomeField.getText(), emailField.getText(),
                            parseDouble(pesoField.getText()), (ObjetivoTreino) objetivoCombo.getSelectedItem(), treinadorId);
                    limparCampos(nomeField, emailField, pesoField);
                    atualizarTudo();
                    informar("Atleta cadastrado com sucesso.");
                } catch (Exception ex) {
                    mostrarErro(ex);
                }
            }
        });

        cardFormulario.add(formulario, BorderLayout.CENTER);
        cardFormulario.add(criarBarraBotoes(salvar), BorderLayout.SOUTH);

        atletasModel = criarModelo("ID", "Nome", "Email", "Peso", "Objetivo", "Treinador");
        JTable tabela = criarTabela(atletasModel);

        painel.add(cardFormulario, BorderLayout.NORTH);
        painel.add(criarCartaoTabela("Atletas cadastrados", tabela), BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelTreinadores() {
        JPanel painel = criarPagina();

        final JTextField nomeField = criarCampoTexto(18);
        final JTextField emailField = criarCampoTexto(18);
        final JTextField especialidadeField = criarCampoTexto(18);
        final JTextField crefField = criarCampoTexto(12);

        JPanel cardFormulario = criarCartao("Novo treinador");
        JPanel formulario = criarFormulario();
        adicionarLinha(formulario, 0, "Nome", nomeField);
        adicionarLinha(formulario, 1, "Email", emailField);
        adicionarLinha(formulario, 2, "Especialidade", especialidadeField);
        adicionarLinha(formulario, 3, "CREF", crefField);

        JButton salvar = criarBotaoPrimario("Cadastrar treinador");
        salvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    service.cadastrarTreinador(nomeField.getText(), emailField.getText(),
                            especialidadeField.getText(), crefField.getText());
                    limparCampos(nomeField, emailField, especialidadeField, crefField);
                    atualizarTudo();
                    informar("Treinador cadastrado com sucesso.");
                } catch (DadosInvalidosException ex) {
                    mostrarErro(ex);
                }
            }
        });

        cardFormulario.add(formulario, BorderLayout.CENTER);
        cardFormulario.add(criarBarraBotoes(salvar), BorderLayout.SOUTH);

        treinadoresModel = criarModelo("ID", "Nome", "Email", "Especialidade", "CREF");
        JTable tabela = criarTabela(treinadoresModel);

        painel.add(cardFormulario, BorderLayout.NORTH);
        painel.add(criarCartaoTabela("Treinadores cadastrados", tabela), BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelExercicios() {
        JPanel painel = criarPagina();

        final JComboBox<String> tipoCombo = new JComboBox<String>(new String[] {"Forca", "Cardio"});
        estilizarCombo(tipoCombo);
        final JTextField nomeField = criarCampoTexto(18);
        final JTextField grupoField = criarCampoTexto(18);

        JPanel cardFormulario = criarCartao("Novo exercicio");
        JPanel formulario = criarFormulario();
        adicionarLinha(formulario, 0, "Tipo", tipoCombo);
        adicionarLinha(formulario, 1, "Nome", nomeField);
        adicionarLinha(formulario, 2, "Grupo/modalidade", grupoField);

        JButton salvar = criarBotaoPrimario("Cadastrar exercicio");
        salvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String tipo = (String) tipoCombo.getSelectedItem();
                    if ("Cardio".equals(tipo)) {
                        service.cadastrarExercicioCardio(nomeField.getText(), grupoField.getText());
                    } else {
                        service.cadastrarExercicioForca(nomeField.getText(), grupoField.getText());
                    }
                    limparCampos(nomeField, grupoField);
                    atualizarTudo();
                    informar("Exercicio cadastrado com sucesso.");
                } catch (DadosInvalidosException ex) {
                    mostrarErro(ex);
                }
            }
        });

        cardFormulario.add(formulario, BorderLayout.CENTER);
        cardFormulario.add(criarBarraBotoes(salvar), BorderLayout.SOUTH);

        exerciciosModel = criarModelo("ID", "Nome", "Tipo", "Grupo/modalidade", "Formula de carga");
        JTable tabela = criarTabela(exerciciosModel);

        painel.add(cardFormulario, BorderLayout.NORTH);
        painel.add(criarCartaoTabela("Exercicios cadastrados", tabela), BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelTreinos() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setOpaque(false);

        treinoAtletaCombo = new JComboBox<Atleta>();
        treinoExercicioCombo = new JComboBox<Exercicio>();
        estilizarCombo(treinoAtletaCombo);
        estilizarCombo(treinoExercicioCombo);
        configurarRenderAtleta(treinoAtletaCombo);
        configurarRenderExercicio(treinoExercicioCombo);

        final JTextField dataField = criarCampoTexto("2026-07-14", 10);
        final JTextField seriesField = criarCampoTexto("3", 5);
        final JTextField repeticoesField = criarCampoTexto("10", 5);
        final JTextField cargaField = criarCampoTexto("40", 5);
        final JTextField duracaoField = criarCampoTexto("0", 5);
        final JTextField distanciaField = criarCampoTexto("0", 5);
        final JTextField intensidadeField = criarCampoTexto("5", 5);
        final JTextField observacoesField = criarCampoTexto("Treino de peito e triceps", 22);

        JPanel cardFormulario = criarCartao("Registro de treino");
        JPanel formulario = criarFormulario();
        adicionarLinha(formulario, 0, "Atleta", treinoAtletaCombo);
        adicionarLinha(formulario, 1, "Data (AAAA-MM-DD)", dataField);
        adicionarLinha(formulario, 2, "Exercicio", treinoExercicioCombo);
        adicionarLinha(formulario, 3, "Series", seriesField);
        adicionarLinha(formulario, 4, "Repeticoes", repeticoesField);
        adicionarLinha(formulario, 5, "Carga kg", cargaField);
        adicionarLinha(formulario, 6, "Duracao min", duracaoField);
        adicionarLinha(formulario, 7, "Distancia km", distanciaField);
        adicionarLinha(formulario, 8, "Intensidade 0-10", intensidadeField);
        adicionarLinha(formulario, 9, "Observacoes", observacoesField);

        itensListModel = new DefaultListModel<String>();
        itensList = new JList<String>(itensListModel);
        itensList.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        itensList.setBackground(CARTAO);
        itensList.setFixedCellHeight(42);
        itensList.setCellRenderer(new CaixaListaRenderer());
        JScrollPane itensScroll = new JScrollPane(itensList);
        itensScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        itensScroll.getViewport().setBackground(CARTAO);

        JButton adicionarItem = criarBotaoSucesso("Adicionar item");
        adicionarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Exercicio exercicio = (Exercicio) treinoExercicioCombo.getSelectedItem();
                    if (exercicio == null) {
                        throw new DadosInvalidosException("Cadastre ou selecione um exercicio.");
                    }
                    ItemTreino item = service.criarItemTreino(exercicio.getId(),
                            parseInt(seriesField.getText()),
                            parseInt(repeticoesField.getText()),
                            parseDouble(cargaField.getText()),
                            parseDouble(duracaoField.getText()),
                            parseDouble(distanciaField.getText()),
                            parseInt(intensidadeField.getText()));
                    itensTemporarios.add(item);
                    itensListModel.addElement(item.resumo());
                } catch (Exception ex) {
                    mostrarErro(ex);
                }
            }
        });

        JButton registrarTreino = criarBotaoPrimario("Registrar treino");
        registrarTreino.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Atleta atleta = (Atleta) treinoAtletaCombo.getSelectedItem();
                    if (atleta == null) {
                        throw new DadosInvalidosException("Cadastre ou selecione um atleta.");
                    }
                    service.registrarTreino(atleta.getId(), LocalDate.parse(dataField.getText().trim()),
                            observacoesField.getText(), new ArrayList<ItemTreino>(itensTemporarios));
                    itensTemporarios.clear();
                    itensListModel.clear();
                    observacoesField.setText("");
                    atualizarTudo();
                    informar("Treino registrado com sucesso.");
                } catch (Exception ex) {
                    mostrarErro(ex);
                }
            }
        });

        cardFormulario.add(formulario, BorderLayout.CENTER);
        cardFormulario.add(criarBarraBotoes(adicionarItem, registrarTreino), BorderLayout.SOUTH);

        JPanel cardItens = criarCartao("Itens adicionados ao treino");
        cardItens.add(itensScroll, BorderLayout.CENTER);

        treinosModel = criarModelo("ID", "Data", "Atleta", "Itens", "Carga", "Nivel");
        final JTable tabela = criarTabela(treinosModel);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton removerTreino = criarBotaoPrimario("Excluir treino selecionado");
        removerTreino.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int linha = tabela.getSelectedRow();
                if (linha < 0) {
                    informar("Selecione um treino para excluir.");
                    return;
                }
                int treinoId = (Integer) treinosModel.getValueAt(linha, 0);
                int resposta = JOptionPane.showConfirmDialog(MainFrame.this,
                        "Deseja excluir o treino selecionado?",
                        "Confirmar exclusao", JOptionPane.YES_NO_OPTION);
                if (resposta == JOptionPane.YES_OPTION) {
                    try {
                        service.removerTreino(treinoId);
                        atualizarTudo();
                        informar("Treino excluido com sucesso.");
                    } catch (EntidadeNaoEncontradaException ex) {
                        mostrarErro(ex);
                    }
                }
            }
        });

        JPanel cardTabela = criarCartao("Treinos cadastrados");
        cardTabela.add(new JScrollPane(tabela), BorderLayout.CENTER);
        cardTabela.add(criarBarraBotoes(removerTreino), BorderLayout.SOUTH);

        JPanel colunaDireita = new JPanel(new GridLayout(2, 1, 0, 24));
        colunaDireita.setOpaque(false);
        colunaDireita.add(cardItens);
        colunaDireita.add(cardTabela);

        GridBagConstraints esquerda = new GridBagConstraints();
        esquerda.gridx = 0;
        esquerda.gridy = 0;
        esquerda.weightx = 0.45;
        esquerda.weighty = 1.0;
        esquerda.fill = GridBagConstraints.BOTH;
        esquerda.insets = new Insets(0, 0, 0, 29);
        painel.add(cardFormulario, esquerda);

        GridBagConstraints direita = new GridBagConstraints();
        direita.gridx = 1;
        direita.gridy = 0;
        direita.weightx = 0.55;
        direita.weighty = 1.0;
        direita.fill = GridBagConstraints.BOTH;
        painel.add(colunaDireita, direita);
        return painel;
    }

    private JPanel criarPainelRelatorios() {
        JPanel painel = criarPagina();
        JPanel card = criarCartao("Relatorio de cargas");

        relatorioArea = new JTextArea();
        relatorioArea.setEditable(false);
        relatorioArea.setLineWrap(true);
        relatorioArea.setWrapStyleWord(true);
        relatorioArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        relatorioArea.setForeground(TEXTO);
        relatorioArea.setBackground(CAMPO);
        relatorioArea.setBorder(new EmptyBorder(12, 12, 12, 12));

        JButton atualizar = criarBotaoPrimario("Atualizar relatorio");
        atualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarRelatorio();
            }
        });

        card.add(new JScrollPane(relatorioArea), BorderLayout.CENTER);
        card.add(criarBarraBotoes(atualizar), BorderLayout.SOUTH);
        painel.add(card, BorderLayout.CENTER);
        return painel;
    }

    private void atualizarTudo() {
        atualizarTabelas();
        atualizarCombos();
        preencherItensExemplo();
        atualizarRelatorio();
    }

    private void atualizarTabelas() {
        if (atletasModel != null) {
            atletasModel.setRowCount(0);
            for (Atleta atleta : service.listarAtletas()) {
                atletasModel.addRow(new Object[] {
                        atleta.getId(),
                        atleta.getNome(),
                        atleta.getEmail(),
                        formatar(atleta.getPesoKg()),
                        atleta.getObjetivo(),
                        atleta.getTreinador() == null ? "Sem treinador" : atleta.getTreinador().getNome()
                });
            }
        }

        if (treinadoresModel != null) {
            treinadoresModel.setRowCount(0);
            for (Treinador treinador : service.listarTreinadores()) {
                treinadoresModel.addRow(new Object[] {
                        treinador.getId(),
                        treinador.getNome(),
                        treinador.getEmail(),
                        treinador.getEspecialidade(),
                        treinador.getCref()
                });
            }
        }

        if (exerciciosModel != null) {
            exerciciosModel.setRowCount(0);
            for (Exercicio exercicio : service.listarExercicios()) {
                exerciciosModel.addRow(new Object[] {
                        exercicio.getId(),
                        exercicio.getNome(),
                        exercicio.getTipo(),
                        exercicio.getGrupo(),
                        exercicio.descreverFormula()
                });
            }
        }

        if (treinosModel != null) {
            treinosModel.setRowCount(0);
            for (Treino treino : service.listarTreinos()) {
                treinosModel.addRow(new Object[] {
                        treino.getId(),
                        treino.getData(),
                        treino.getAtleta().getNome(),
                        treino.getItens().size(),
                        formatar(treino.calcularCargaTotal()),
                        treino.classificarCarga()
                });
            }
        }
    }

    private void atualizarCombos() {
        if (atletaTreinadorCombo != null) {
            DefaultComboBoxModel<Treinador> model = new DefaultComboBoxModel<Treinador>();
            model.addElement(null);
            for (Treinador treinador : service.listarTreinadores()) {
                model.addElement(treinador);
            }
            atletaTreinadorCombo.setModel(model);
        }

        if (treinoAtletaCombo != null) {
            DefaultComboBoxModel<Atleta> model = new DefaultComboBoxModel<Atleta>();
            for (Atleta atleta : service.listarAtletas()) {
                model.addElement(atleta);
            }
            treinoAtletaCombo.setModel(model);
        }

        if (treinoExercicioCombo != null) {
            DefaultComboBoxModel<Exercicio> model = new DefaultComboBoxModel<Exercicio>();
            for (Exercicio exercicio : service.listarExercicios()) {
                model.addElement(exercicio);
            }
            treinoExercicioCombo.setModel(model);
        }
    }

    private void preencherItensExemplo() {
        if (itensExemploCriados || itensListModel == null || !itensTemporarios.isEmpty()) {
            return;
        }

        Exercicio supino = encontrarExercicioPorNome("Supino reto");
        Exercicio corrida = encontrarExercicioPorNome("Corrida na esteira");
        if (supino == null || corrida == null) {
            itensExemploCriados = true;
            return;
        }

        try {
            ItemTreino item1 = service.criarItemTreino(supino.getId(), 3, 10, 40, 0, 0, 5);
            ItemTreino item2 = service.criarItemTreino(corrida.getId(), 0, 0, 0, 30, 3, 5);
            itensTemporarios.add(item1);
            itensTemporarios.add(item2);
            itensListModel.addElement(item1.resumo());
            itensListModel.addElement(item2.resumo());
        } catch (Exception ex) {
            // Os itens de exemplo sao apenas para a tela inicial ficar igual ao mockup.
        }
        itensExemploCriados = true;
    }

    private Exercicio encontrarExercicioPorNome(String nome) {
        for (Exercicio exercicio : service.listarExercicios()) {
            if (exercicio.getNome().equalsIgnoreCase(nome)) {
                return exercicio;
            }
        }
        return null;
    }

    private void atualizarRelatorio() {
        if (relatorioArea != null) {
            relatorioArea.setText(service.gerarRelatorioResumo());
            relatorioArea.setCaretPosition(0);
        }
    }

    private JPanel criarPagina() {
        JPanel painel = new JPanel(new BorderLayout(0, 18));
        painel.setOpaque(false);
        return painel;
    }

    private JPanel criarCartao(String titulo) {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(CARTAO);
        panel.setBorder(new CompoundBorder(new LineBorder(BORDA, 1, true), new EmptyBorder(18, 20, 12, 20)));

        JLabel label = new JLabel(titulo);
        label.setFont(FONTE_TITULO);
        label.setForeground(TEXTO);
        panel.add(label, BorderLayout.NORTH);
        return panel;
    }

    private JPanel criarCartaoTabela(String titulo, JTable tabela) {
        JPanel card = criarCartao(titulo);
        card.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return card;
    }

    private JPanel criarFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        return panel;
    }

    private void adicionarLinha(JPanel panel, int linha, String rotulo, Component campo) {
        JLabel labelComponent = new JLabel(rotulo);
        labelComponent.setFont(FONTE_BASE);
        labelComponent.setForeground(TEXTO_MUTED);
        labelComponent.setPreferredSize(new Dimension(160, 34));

        GridBagConstraints label = new GridBagConstraints();
        label.gridx = 0;
        label.gridy = linha;
        label.anchor = GridBagConstraints.LINE_START;
        label.insets = new Insets(4, 0, 4, 16);
        panel.add(labelComponent, label);

        GridBagConstraints input = new GridBagConstraints();
        input.gridx = 1;
        input.gridy = linha;
        input.weightx = 1.0;
        input.fill = GridBagConstraints.HORIZONTAL;
        input.insets = new Insets(4, 0, 4, 0);
        panel.add(campo, input);
    }

    private JTextField criarCampoTexto(int colunas) {
        return criarCampoTexto("", colunas);
    }

    private JTextField criarCampoTexto(String valor, int colunas) {
        JTextField campo = new JTextField(valor, colunas);
        campo.setFont(FONTE_BASE);
        campo.setForeground(TEXTO);
        campo.setBackground(CAMPO);
        campo.setBorder(new CompoundBorder(new LineBorder(BORDA, 1, true), new EmptyBorder(6, 10, 6, 10)));
        campo.setPreferredSize(new Dimension(300, 34));
        return campo;
    }

    private void estilizarCombo(JComboBox<?> combo) {
        combo.setFont(FONTE_BASE);
        combo.setForeground(TEXTO);
        combo.setBackground(CAMPO);
        combo.setBorder(new LineBorder(BORDA, 1, true));
        combo.setPreferredSize(new Dimension(300, 34));
    }

    private JTable criarTabela(DefaultTableModel model) {
        JTable tabela = new JTable(model);
        tabela.setFont(FONTE_BASE);
        tabela.setForeground(TEXTO);
        tabela.setBackground(CARTAO);
        tabela.setRowHeight(46);
        tabela.setShowVerticalLines(false);
        tabela.setGridColor(new Color(220, 227, 236));
        tabela.setIntercellSpacing(new Dimension(0, 1));
        tabela.setFillsViewportHeight(true);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setForeground(AZUL_ESCURO);
        header.setBackground(CARTAO);
        header.setBorder(new LineBorder(new Color(204, 214, 226), 0));
        header.setPreferredSize(new Dimension(0, 38));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBorder(new EmptyBorder(0, 0, 0, 0));
        renderer.setFont(FONTE_BASE);
        renderer.setForeground(TEXTO);
        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        return tabela;
    }

    private JPanel criarBarraBotoes(JButton... botoes) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panel.setOpaque(false);
        for (JButton botao : botoes) {
            panel.add(botao);
        }
        return panel;
    }

    private JButton criarBotaoPrimario(String texto) {
        RoundedButton botao = new RoundedButton(texto, 6);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botao.setColors(AZUL, Color.WHITE, AZUL);
        botao.setMargin(new Insets(8, 18, 8, 18));
        return botao;
    }

    private JButton criarBotaoSucesso(String texto) {
        RoundedButton botao = new RoundedButton(texto, 6);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botao.setColors(VERDE, Color.WHITE, VERDE);
        botao.setMargin(new Insets(8, 18, 8, 18));
        return botao;
    }

    private DefaultTableModel criarModelo(String... colunas) {
        return new DefaultTableModel(colunas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void configurarRenderTreinador(JComboBox<Treinador> combo) {
        combo.setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setFont(FONTE_BASE);
                if (value == null) {
                    setText("Sem treinador");
                } else {
                    setText(((Treinador) value).getNome());
                }
                return this;
            }
        });
    }

    private void configurarRenderAtleta(JComboBox<Atleta> combo) {
        combo.setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setFont(FONTE_BASE);
                setText(value == null ? "" : ((Atleta) value).getNome());
                return this;
            }
        });
    }

    private void configurarRenderExercicio(JComboBox<Exercicio> combo) {
        combo.setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setFont(FONTE_BASE);
                if (value == null) {
                    setText("");
                } else {
                    Exercicio exercicio = (Exercicio) value;
                    setText(exercicio.getNome() + " (" + exercicio.getTipo() + ")");
                }
                return this;
            }
        });
    }

    private int parseInt(String valor) throws DadosInvalidosException {
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            throw new DadosInvalidosException("Informe um numero inteiro valido.");
        }
    }

    private double parseDouble(String valor) throws DadosInvalidosException {
        try {
            return Double.parseDouble(valor.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new DadosInvalidosException("Informe um numero valido.");
        }
    }

    private String formatar(double valor) {
        return String.format("%.1f", valor);
    }

    private void limparCampos(JTextField... campos) {
        for (JTextField campo : campos) {
            campo.setText("");
        }
    }

    private void informar(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "TrackFit", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarErro(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private static class CaixaListaRenderer extends DefaultListCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            label.setForeground(TEXTO);
            label.setBackground(isSelected ? new Color(226, 238, 250) : CAMPO);
            label.setBorder(new CompoundBorder(new LineBorder(BORDA, 1, true), new EmptyBorder(6, 14, 6, 14)));
            return label;
        }
    }

    private static class RoundedButton extends JButton {
        private static final long serialVersionUID = 1L;

        private Color backgroundColor = Color.WHITE;
        private Color borderColor = BORDA;
        private final int arc;

        RoundedButton(String text, int arc) {
            super(text);
            this.arc = arc;
            setFocusPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setBorder(new EmptyBorder(8, 18, 8, 18));
            setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        }

        void setColors(Color backgroundColor, Color textColor, Color borderColor) {
            this.backgroundColor = backgroundColor;
            this.borderColor = borderColor;
            setForeground(textColor);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color cor = backgroundColor;
            if (getModel().isPressed()) {
                cor = backgroundColor.darker();
            }
            g2.setColor(cor);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
            g2.dispose();
        }
    }
}
