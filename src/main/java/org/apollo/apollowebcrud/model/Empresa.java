package org.apollo.apollowebcrud.model;

public class Empresa {

    // ATRIBUTOS E CONSTRUTORES

    private int id;
    private String nome;
    private String mensagemErro;

    public Empresa(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Empresa() {
    }

    // MÉTODOS GETTERS E SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
        return "ID=" + getId() + " NOME=" + getNome();
    }
}