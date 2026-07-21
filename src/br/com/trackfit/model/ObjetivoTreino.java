package br.com.trackfit.model;

public enum ObjetivoTreino {
    HIPERTROFIA("Hipertrofia"),
    FORCA("Forca"),
    EMAGRECIMENTO("Emagrecimento"),
    CONDICIONAMENTO("Condicionamento"),
    REABILITACAO("Reabilitacao");

    private final String descricao;

    ObjetivoTreino(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
