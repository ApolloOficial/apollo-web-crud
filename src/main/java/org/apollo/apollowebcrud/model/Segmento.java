package org.apollo.apollowebcrud.model;

public class Segmento {

//    ATRIBUTOS

    private int id;
    private String nome;
    private String descricao;
    private String mensagemErro;

//    CONSTRUTOR

    public Segmento(int id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

//    CONSTRUTOR VAZIO

    public Segmento() {
    }

//    GETTERS


    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }

//    SETTERS


    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

//    SAÍDA TOSTRING()

    @Override
    public java.lang.String toString() {
        return "Segmento{" +
                "id=" + id +
                ", nome=" + nome +
                ", descricao=" + descricao +
                '}';
    }
}
