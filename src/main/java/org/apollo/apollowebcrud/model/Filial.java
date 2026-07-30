package org.apollo.apollowebcrud.model;

public class Filial {

    // ATRIBUTOS E CONSTRUTORES
    private int idFilial;
    private int idEmpresa;
    private String localizacao;

    public Filial() {
    }

    public Filial(int idFilial, int idEmpresa, String localizacao) {
        this.idFilial = idFilial;
        this.idEmpresa = idEmpresa;
        this.localizacao = localizacao;
    }

    // MÉTODOS GETTERS E SETTERS
    public int getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
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

    // OVERRIDE DA SAÍDA TOSTRING()
    @Override
    public String toString() {
        return "ID_FILIAL=" + getIdFilial() + " ID_EMPRESA=" + getIdEmpresa() + " LOCALIZACAO=" + getLocalizacao();
    }
}