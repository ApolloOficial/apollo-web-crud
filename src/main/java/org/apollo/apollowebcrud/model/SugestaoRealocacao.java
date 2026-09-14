package org.apollo.apollowebcrud.model;

public class SugestaoRealocacao {

    // ATRIBUTOS E CONSTRUTORES

    private int id;
    private int idLote;
    private double preco;
    private String justificativa;
    private String destino;
    private String mensagemErro;

    public SugestaoRealocacao() {
    }

    public SugestaoRealocacao(int id, int idLote, double preco, String justificativa, String destino) {
        this.id = id;
        this.idLote = idLote;
        this.preco = preco;
        this.justificativa = justificativa;
        this.destino = destino;
    }

    // MÉTODOS GETTERS E SETTERS

    public int getId() {
        return id;
    }

    public int getIdLote() {
        return idLote;
    }

    public double getPreco() {
        return preco;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public String getDestino() {
        return destino;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdLote(int idLote) {
        this.idLote = idLote;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

    //OVERRIDE DA SAÍDA TOSTRING()

    @Override
    public String toString() {
        return "SugestaoRealocacao{" +
                "idLote=" + idLote +
                ", preco=" + preco +
                ", justificativa='" + justificativa + '\'' +
                ", destino='" + destino + '\'' +
                ", id=" + id +
                '}';
    }
}