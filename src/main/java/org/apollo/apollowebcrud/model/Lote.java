package org.apollo.apollowebcrud.model;

public class Lote {
    //ATRIBUTOS E CONSTRUTOR

    private int id;
    private int quantidadePaineis;
    private int eficienciaGeral;
    private String observacao;
    private int idFilial;

    public Lote() {
    }

    public Lote(int id, int quantidadePaineis, int eficienciaGeral, String observacao, int idFilial) {
        this.id = id;
        this.quantidadePaineis = quantidadePaineis;
        this.eficienciaGeral = eficienciaGeral;
        this.observacao = observacao;
        this.idFilial = idFilial;
    }
    //MÉTODOS GETTERS E SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantidadePaineis() {
        return quantidadePaineis;
    }

    public void setQuantidadePaineis(int quantidadePaineis) {
        this.quantidadePaineis = quantidadePaineis;
    }

    public int getEficienciaGeral() {
        return eficienciaGeral;
    }

    public void setEficienciaGeral(int eficienciaGeral) {
        this.eficienciaGeral = eficienciaGeral;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public int getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
    }
    //OVERRIDE DA SAÍDA TOSTRING()

    @Override
    public String toString() {
        return "ID=" + getId() + " QUANTIA P=" + getQuantidadePaineis() + " EFICIÊNCIA GERAL=" + getEficienciaGeral() + " OBSERVAÇÃO=" + getObservacao() + " ID CADASTRO=" + getIdFilial() + " LOCALIZAÇÃO=" + getLocalizacao();
    }
}
