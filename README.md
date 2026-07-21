# TrackFit POO - Sistema de Rastreamento de Treinos e Cargas

Projeto final de Programacao Orientada a Objetos em Java com interface Swing.

## Tema

O TrackFit permite cadastrar treinadores, atletas, exercicios de forca/cardio e treinos realizados. Cada treino calcula uma carga total a partir dos itens registrados, permitindo acompanhar volume, intensidade e evolucao do atleta.

## Como executar

1. Instale um JDK 8 ou superior.
2. Abra o Prompt de Comando ou PowerShell dentro desta pasta.
3. Execute:

```bat
compile.bat
run.bat
```

O sistema salva os dados em `data/trackfit.ser`.

## Como executar no VS Code

1. Abra a pasta `TrackFitPOO` no VS Code.
2. Instale a extensao `Extension Pack for Java`, se ainda nao tiver.
3. Confira no terminal:

```bat
java -version
javac -version
```

4. Se os dois comandos responderem, abra `src/br/com/trackfit/app/Main.java`.
5. Clique em `Run Java` ou use a configuracao `Run TrackFitPOO`.

Se a tela abrir sem os dados iguais ao print, rode `reset-data.bat` e depois execute o sistema de novo. Esse script apaga `data/trackfit.ser`, que guarda os dados antigos do sistema.

## Conceitos de POO aplicados

- Heranca: `Pessoa` e especializada por `Atleta` e `Treinador`; `Exercicio` e especializado por `ExercicioForca` e `ExercicioCardio`.
- Classe abstrata: `Pessoa` e `Exercicio`.
- Interface: `CargaCalculavel`, implementada por exercicios para calcular carga.
- Polimorfismo: o treino chama `exercicio.calcularCarga(item)` sem saber se o exercicio e de forca ou cardio.
- Composicao: `Treino` contem uma lista de `ItemTreino`; os itens fazem sentido dentro do treino.
- Agregacao: `Atleta` pode estar vinculado a um `Treinador`, mas ambos existem separadamente.
- Persistencia: `BancoDados` salva e carrega `DadosSistema` com `ObjectOutputStream` e `ObjectInputStream`.
- Excecoes customizadas: `DadosInvalidosException` e `EntidadeNaoEncontradaException`.
- Interface grafica: `MainFrame` usa `javax.swing`.

## Estrutura

```text
TrackFitPOO/
  compile.bat
  run.bat
  README.md
  data/
  docs/
    diagrama-classes.mmd
    relatorio-trackfit.pdf
    screenshots/
  src/
    br/com/trackfit/app/Main.java
    br/com/trackfit/model/
    br/com/trackfit/service/
    br/com/trackfit/persistence/
    br/com/trackfit/exception/
    br/com/trackfit/ui/
```

## Observacao

O relatorio em PDF esta em `docs/relatorio-trackfit.pdf`. Os nomes dos integrantes e o link do GitHub devem ser preenchidos pelo grupo antes da entrega final.
