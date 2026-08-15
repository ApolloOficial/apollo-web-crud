package org.apollo.apollowebcrud.model;

public class Filial {

    // ATRIBUTOS E CONSTRUTORES
    private int id;
    private int idEmpresa;
    private String localizacao;
    private String mensagemErro;

    public Filial() {
    }

    public Filial(int id, int idEmpresa, String localizacao) {
        this.id = id;
        this.idEmpresa = idEmpresa;
        this.localizacao = localizacao;
    }

    // MÉTODOS GETTERS E SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
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
        return "ID=" + getId() +" ID_EMPRESA=" + getIdEmpresa() + " LOCALIZACAO=" + getLocalizacao();
    }
}