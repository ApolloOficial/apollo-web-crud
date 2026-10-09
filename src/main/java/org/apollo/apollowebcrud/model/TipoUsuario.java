package org.apollo.apollowebcrud.model;

public class TipoUsuario {

//    ENUM

    public enum Tipo {
        ADMINISTRADOR,
        GERENTE,
        OPERADOR
    }

//    ATRIBUTOS

    private int id;
    private Tipo nome;
    private String descricao;
    private String mensagemErro;

//    (o restante da classe continua exatamente igual)
//    CONSTRUTOR

    public TipoUsuario(int id, Tipo nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }


//    CONSTRUTOR VAZIO

    public TipoUsuario() {
    }

    //    GETTERS

    public int getId() {
        return id;
    }

    public Tipo getNome() {
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

    public void setNome(Tipo nome) {
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
        return "TipoUsuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}