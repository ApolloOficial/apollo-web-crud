package org.apollo.apollowebcrud.model;

public class Manutencao {

//    ATRIBUTOS E CONSTRUTOR

    private int id;
    private int idPainel;
    private String tipo;
    private String descricao;
    private String prazo;
    private String mensagemErro;

    public Manutencao(int id, int idPainel, String tipo, String descricao, String prazo) {
        this.id = id;
        this.idPainel = idPainel;
        this.tipo = tipo;
        this.descricao = descricao;
        this.prazo = prazo;
    }

    public Manutencao() {
    }

//    MÉTODOS GETTERS SETTERS

    public int getId() {
        return id;
    }

    public int getIdPainel() {
        return idPainel;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getPrazo() {
        return prazo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdPainel(int idPainel) {
        this.idPainel = idPainel;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setPrazo(String prazo) {
        this.prazo = prazo;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

    //OVERRIDE DA SAÍDA TOSTRING()

    @Override
    public String toString() {
        return "ID=" + getId() + " ID_PAINEL=" + getIdPainel() + " TIPO=" + getTipo() + " DESCRICAO=" + getDescricao() + " PRAZO=" + getPrazo();
    }
}