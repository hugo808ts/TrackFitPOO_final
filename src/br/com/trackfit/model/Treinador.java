package br.com.trackfit.model;

public class Treinador extends Pessoa {
    private static final long serialVersionUID = 1L;

    private String especialidade;
    private String cref;

    public Treinador(int id, String nome, String email, String especialidade, String cref) {
        super(id, nome, email);
        this.especialidade = especialidade;
        this.cref = cref;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getCref() {
        return cref;
    }

    public void setCref(String cref) {
        this.cref = cref;
    }
}
