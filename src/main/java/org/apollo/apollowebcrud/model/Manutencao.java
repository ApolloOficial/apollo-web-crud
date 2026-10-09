package org.apollo.apollowebcrud.model;

import java.time.LocalDate;

public class Manutencao {



//    ATRIBUTOS

    private int id;
    private int idPainel;
    private TipoManutencao tipoManutencao;
    private String descricao;
    private StatusManutencao status;
    private LocalDate dataAbertura = LocalDate.now();
    private LocalDate dataPrevista;
    private LocalDate dataConclusao;
    private Integer idUsuarioResponsavel;
    private String mensagemErro;

//    CONSTRUTOR

    public Manutencao(int id, int idPainel, TipoManutencao tipoManutencao, String descricao, StatusManutencao status, LocalDate dataAbertura, LocalDate dataPrevista, LocalDate dataConclusao, Integer idUsuarioResponsavel) {
        this.id = id;
        this.idPainel = idPainel;
        this.tipoManutencao = tipoManutencao;
        this.descricao = descricao;
        this.status = StatusManutencao.ABERTA;
        this.dataAbertura = dataAbertura;
        this.dataPrevista = dataPrevista;
        this.dataConclusao = dataConclusao;
        this.idUsuarioResponsavel = idUsuarioResponsavel;
    }

//    CONSTRUTOR VAZIO

    public Manutencao() {
    }

//    GETTERS

    public int getId() {
        return id;
    }

    public int getIdPainel() {
        return idPainel;
    }

    public TipoManutencao getTipoManutencao() {
        return tipoManutencao;
    }

    public String getDescricao() {
        return descricao;
    }

    public StatusManutencao getStatus() {
        return status;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public LocalDate getDataPrevista() {
        return dataPrevista;
    }

    public LocalDate getDataConclusao() {
        return dataConclusao;
    }

    public Integer getIdUsuarioResponsavel() {
        return idUsuarioResponsavel;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }


//    SETTERS

    public void setId(int id) {
        this.id = id;
    }

    public void setIdPainel(int idPainel) {
        this.idPainel = idPainel;
    }

    public void setTipoManutencao(TipoManutencao tipoManutencao) {
        this.tipoManutencao = tipoManutencao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setStatus(StatusManutencao status) {
        this.status = status;
    }

    public void setDataAbertura(LocalDate dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public void setDataPrevista(LocalDate dataPrevista) {
        this.dataPrevista = dataPrevista;
    }

    public void setDataConclusao(LocalDate dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public void setIdUsuarioResponsavel(Integer idUsuarioResponsavel) {
        this.idUsuarioResponsavel = idUsuarioResponsavel;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }


//    SAÍDA TOSTRING()

    @Override
    public java.lang.String toString() {
        return "Manutencao{" +
                "id=" + id +
                ", idPainel=" + idPainel +
                ", tipoManutencao=" + tipoManutencao +
                ", descricao=" + descricao +
                ", status=" + status +
                ", dataAbertura=" + dataAbertura +
                ", dataPrevista=" + dataPrevista +
                ", dataConclusao=" + dataConclusao +
                ", idUsuarioResponsavel=" + idUsuarioResponsavel +
                '}';
    }
}