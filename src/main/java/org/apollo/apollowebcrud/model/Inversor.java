package org.apollo.apollowebcrud.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Inversor {

//    ATRIBUTOS

    private int id;
    private int idFilial;
    private String codigo;
    private String numeroSerie;
    private String marca;
    private String modelo;
    private double potenciaKw;
    private StatusGeral status;
    private LocalDate dataInstalacao;
    private String mensagemErro;

//    CONSTRUTOR

    public Inversor(int id, int idFilial, String codigo, String numeroSerie, String marca, String modelo, double potenciaKw, StatusGeral status, LocalDate dataInstalacao) {
        this.id = id;
        this.idFilial = idFilial;
        this.codigo = codigo;
        this.numeroSerie = numeroSerie;
        this.marca = marca;
        this.modelo = modelo;
        this.potenciaKw = potenciaKw;
        this.status = StatusGeral.ATIVO;
        this.dataInstalacao = dataInstalacao;
    }

//    CONSTRUTOR VAZIO

    public Inversor() {
    }

//    GETTERS

    public int getId() {
        return id;
    }

    public int getIdFilial() {
        return idFilial;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public double getPotenciaKw() {
        return potenciaKw;
    }

    public StatusGeral getStatus() {
        return status;
    }

    public LocalDate getDataInstalacao() {
        return dataInstalacao;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }

//    SETTERS

    public void setId(int id) {
        this.id = id;
    }

    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setPotenciaKw(double potenciaKw) {
        this.potenciaKw = potenciaKw;
    }

    public void setStatus(StatusGeral status) {
        this.status = status;
    }

    public void setDataInstalacao(LocalDate dataInstalacao) {
        this.dataInstalacao = dataInstalacao;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

//    SAÍDA TOSTRING()

    @Override
    public String toString() {
        return "Inversor{" +
                "id=" + id +
                ", idFilial=" + idFilial +
                ", codigo=" + codigo +
                ", numeroSerie=" + numeroSerie +
                ", marca=" + marca +
                ", modelo=" + modelo +
                ", potenciaKw=" + potenciaKw +
                ", status=" + status +
                ", dataInstalacao=" + dataInstalacao +
                '}';
    }
}
