package org.apollo.apollowebcrud.model;

public class Empresa {

    // ATRIBUTOS E CONSTRUTORES
    private int idEmpresa;
    private String nome;

    public Empresa() {
    }

    public Empresa(int idEmpresa, String nome) {
        this.idEmpresa = idEmpresa;
        this.nome = nome;
    }

    // MÉTODOS GETTERS E SETTERS
    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // OVERRIDE DA SAÍDA TOSTRING()

    @Override
    public String toString() {
        return "ID=" + getIdEmpresa() + " NOME=" + getNome();
    }
}