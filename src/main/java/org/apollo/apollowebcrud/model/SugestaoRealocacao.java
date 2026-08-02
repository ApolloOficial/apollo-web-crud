package org.apollo.apollowebcrud.model;

public class SugestaoRealocacao {

    // ATRIBUTOS E CONSTRUTORES

    private int id;
    private int idPainel;
    private int preco;
    private String justificativa;
    private String destino;

    public SugestaoRealocacao() {
    }

    public SugestaoRealocacao(int id, int idPainel, int preco, String justificativa, String destino) {
        this.id = id;
        this.idPainel = idPainel;
        this.preco = preco;
        this.justificativa = justificativa;
        this.destino = destino;
    }

    // MÉTODOS GETTERS E SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPainel() {
        return idPainel;
    }

    public void setIdPainel(int idPainel) {
        this.idPainel = idPainel;
    }

    public int getPreco() {
        return preco;
    }

    public void setPreco(int preco) {
        this.preco = preco;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    // OVERRIDE DA SAÍDA TOSTRING()

    @Override
    public String toString() {
        return "ID=" + getId() + " ID_PAINEL=" + getIdPainel() + " PRECO=" + getPreco() + " JUSTIFICATIVA=" + getJustificativa() + " DESTINO=" + getDestino();
    }
}