package org.apollo.apollowebcrud.model;

import java.time.LocalDate;

public class Metas {

    private int id;
    private int idFilial;
    private LocalDate prazo;
    private String descricao;
    private String mensagemErro;

    public Metas(int id, int idFilial, LocalDate prazo, String descricao) {
        this.id = id;
        this.idFilial = idFilial;
        this.prazo = prazo;
        this.descricao = descricao;
    }

    public Metas() {
}

    public void setId(int id) {
        this.id = id;
    }

    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
    }

    public void setPrazo(LocalDate prazo) {
        this.prazo = prazo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

    public int getId() {
        return id;
    }

    public int getIdFilial() {
        return idFilial;
    }

    public LocalDate getPrazo() {
        return prazo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }

    @Override
    public String toString() {
        return "Metas{" +
                "descricao='" + descricao + '\'' +
                ", prazo=" + prazo +
                ", idFilial=" + idFilial +
                ", id=" + id +
                '}';
    }
}
