package org.apollo.apollowebcrud.model;

public class TipoUsuario {

//    ATRIBUTOS

    private int id;
    private TipoUsuEnum nome;
    private String descricao;
    private String mensagemErro;

//    (o restante da classe continua exatamente igual)
//    CONSTRUTOR

    public TipoUsuario(int id, TipoUsuEnum nome, String descricao) {
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

    public TipoUsuEnum getNome() {
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

    public void setNome(TipoUsuEnum nome) {
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
    public String toString() {
        return "TipoUsuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}