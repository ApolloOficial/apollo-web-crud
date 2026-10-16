package org.apollo.apollowebcrud.model;

import java.io.ObjectInputFilter;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Contrato {

//    ATRIBUTOS

    private int id;
    private int idEmpresa;
    private LocalDate dataInicio;
    private BigDecimal valorMensal;
    private StatusGeral status;
    private Integer idUsuarioCadastro;
    private String mensagemErro;

//    CONSTRUTOR

    public Contrato(int id, int idEmpresa, LocalDate dataInicio, BigDecimal valorMensal, StatusGeral status, Integer idUsuarioCadastro) {
        this.id = id;
        this.idEmpresa = idEmpresa;
        this.dataInicio = dataInicio;
        this.valorMensal = valorMensal;
        this.status = StatusGeral.ATIVO;
        this.idUsuarioCadastro = idUsuarioCadastro;
    }

//    CONSTRUTOR VAZIO

    public Contrato() {
    }

//    GETTERS

    public int getId() {
        return id;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public BigDecimal getValorMensal() {
        return valorMensal;
    }

    public StatusGeral getStatus() {
        return status;
    }

    public Integer getIdUsuarioCadastro() {
        return idUsuarioCadastro;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }

//    SETTERS

    public void setId(int id) {
        this.id = id;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public void setValorMensal(BigDecimal valorMensal) {
        this.valorMensal = valorMensal;
    }

    public void setStatus(StatusGeral status) {
        this.status = status;
    }

    public void setIdUsuarioCadastro(Integer idUsuarioCadastro) {
        this.idUsuarioCadastro = idUsuarioCadastro;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

//    SAÍDA TOSTRING()

    @Override
    public String toString() {
        return "Contrato{" +
                "id=" + id +
                ", idEmpresa=" + idEmpresa +
                ", dataInicio=" + dataInicio +
                ", valorMensal=" + valorMensal +
                ", status=" + status +
                ", idUsuarioCadastro=" + idUsuarioCadastro +
                '}';
    }
}