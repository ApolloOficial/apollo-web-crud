package org.apollo.apollowebcrud.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Painel {

//    ATRIBUTOS

    private int id;
    private int idString;
    private String numeroSerie;
    private String marca;
    private String modelo;
    private double potenciaW;
    private String notaFiscal;
    private StatusGeral status;
    private LocalDate dataInstalacao;
    private String mensagemErro;

//    CONSTRUTOR

    public Painel(int id, int idString, String numeroSerie, String marca, String modelo, double potenciaW, String notaFiscal, String status, LocalDate dataInstalacao) {
        this.id = id;
        this.idString = idString;
        this.numeroSerie = numeroSerie;
        this.marca = marca;
        this.modelo = modelo;
        this.potenciaW = potenciaW;
        this.notaFiscal = notaFiscal;
        this.status = StatusGeral.ATIVO;
        this.dataInstalacao = dataInstalacao;
    }

//    CONSTRUTOR VAZIO

    public Painel() {
    }

//    GETTERS

    public int getId() {
        return id;
    }

    public int getIdString() {
        return idString;
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

    public double getPotenciaW() {
        return potenciaW;
    }

    public String getNotaFiscal() {
        return notaFiscal;
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

    public void setIdString(int idString) {
        this.idString = idString;
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

    public void setPotenciaW(double potenciaW) {
        this.potenciaW = potenciaW;
    }

    public void setNotaFiscal(String notaFiscal) {
        this.notaFiscal = notaFiscal;
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
    public java.lang.String toString() {
        return "Painel{" +
                "id=" + id +
                ", idString=" + idString +
                ", numeroSerie=" + numeroSerie +
                ", marca=" + marca +
                ", modelo=" + modelo +
                ", potenciaW=" + potenciaW +
                ", notaFiscal=" + notaFiscal +
                ", status=" + status +
                ", dataInstalacao=" + dataInstalacao +
                '}';
    }
}