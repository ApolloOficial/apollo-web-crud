package org.apollo.apollowebcrud.model;

public class TipoUsuario {

//    ATRIBUTOS E CONSTRUTORES

    private int id;
    private String tipo;
    private String descricao;

    public TipoUsuario(int id, String tipo, String descricao) {
        this.id = id;
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public TipoUsuario(){
    }

//    MÉTODOS GETTERS SETTERS


    public int getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // OVERRIDE DA SAÍDA TOSTRING()
    @Override
    public String toString() {
        return "ID=" + getId() + " TIPO=" + getTipo() + " DESCRICAO=" + getDescricao();
    }
}
