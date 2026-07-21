package br.com.trackfit.model;

public class ExercicioForca extends Exercicio {
    private static final long serialVersionUID = 1L;

    public ExercicioForca(int id, String nome, String grupoMuscular) {
        super(id, nome, grupoMuscular);
    }

    @Override
    public double calcularCarga(ItemTreino item) {
        return item.getSeries() * item.getRepeticoes() * item.getCargaKg();
    }

    @Override
    public String descreverFormula() {
        return "series x repeticoes x carga em kg";
    }

    @Override
    public String getTipo() {
        return "Forca";
    }
}
