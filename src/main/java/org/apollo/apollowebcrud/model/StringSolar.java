package org.apollo.apollowebcrud.model;

public class StringSolar {

//    ATRIBUTOS

    private int id;
    private int idInversor;
    private String codigo;
    private Integer numeroMppt;
    private Integer numeroEntrada;
    private StatusGeral status;
    private String mensagemErro;
//    CONSTRUTOR

    public StringSolar(int id, int idInversor, String codigo, Integer numeroMppt, Integer numeroEntrada, StatusGeral status) {
        this.id = id;
        this.idInversor = idInversor;
        this.codigo = codigo;
        this.numeroMppt = numeroMppt;
        this.numeroEntrada = numeroEntrada;
        this.status = StatusGeral.ATIVO;
    }

//    CONSTRUTOR VAZIO

    public StringSolar() {
    }

//    GETTERS

    public int getId() {
        return id;
    }

    public int getIdInversor() {
        return idInversor;
    }

    public String getCodigo() {
        return codigo;
    }

    public Integer getNumeroMppt() {
        return numeroMppt;
    }

    public Integer getNumeroEntrada() {
        return numeroEntrada;
    }

    public StatusGeral getStatus() {
        return status;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }

//    SETTERS

    public void setId(int id) {
        this.id = id;
    }

    public void setIdInversor(int idInversor) {
        this.idInversor = idInversor;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNumeroMppt(Integer numeroMppt) {
        this.numeroMppt = numeroMppt;
    }

    public void setNumeroEntrada(Integer numeroEntrada) {
        this.numeroEntrada = numeroEntrada;
    }

    public void setStatus(StatusGeral status) {
        this.status = status;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

//    SAÍDA TOSTRING()

    @Override
    public String toString() {
        return "String{" +
                "id=" + id +
                ", idInversor=" + idInversor +
                ", codigo=" + codigo +
                ", numeroMppt=" + numeroMppt +
                ", numeroEntrada=" + numeroEntrada +
                ", status=" + status +
                '}';
    }
}
