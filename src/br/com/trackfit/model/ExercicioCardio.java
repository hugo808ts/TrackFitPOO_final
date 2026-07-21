package br.com.trackfit.model;

public class ExercicioCardio extends Exercicio {
    private static final long serialVersionUID = 1L;

    public ExercicioCardio(int id, String nome, String modalidade) {
        super(id, nome, modalidade);
    }

    @Override
    public double calcularCarga(ItemTreino item) {
        return (item.getDuracaoMinutos() * item.getIntensidade()) + (item.getDistanciaKm() * 10.0);
    }

    @Override
    public String descreverFormula() {
        return "(duracao em minutos x intensidade) + (distancia em km x 10)";
    }

    @Override
    public String getTipo() {
        return "Cardio";
    }
}
