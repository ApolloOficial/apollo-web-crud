package org.apollo.apollowebcrud.model;

public class SugestaoRealocacao {

    // ATRIBUTOS E CONSTRUTORES

    private int id;
    private int idPainel;
    private double preco;
    private String justificativa;
    private String destino;
    private String mensagemErro;

    public SugestaoRealocacao() {
    }

    public SugestaoRealocacao(int id, int idPainel, double preco, String justificativa, String destino) {
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

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
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

    public String getMensagemErro() {
        return mensagemErro;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

    // OVERRIDE DA SAÍDA TOSTRING()

    @Override
    public String toString() {
        return "ID=" + getId() + " ID_PAINEL=" + getIdPainel() + " PRECO=" + getPreco() + " JUSTIFICATIVA=" + getJustificativa() + " DESTINO=" + getDestino();
    }
}