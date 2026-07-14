package org.apollo.apollowebcrud.model;

public class Usuario {
    //ATRIBUTOS E CONSTRUTOR

    private int id_usuario;
    private String nome;
    private String email;
    private String cpf;
    private String senha;
    private String tipo;
    private String mensagemErro;

    public Usuario(){
    }

    public Usuario(int id_usuario, String nome, String email, String cpf, String senha, String tipo) {
        this.id_usuario = id_usuario;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.senha = senha;
        this.tipo = tipo;
    }
    //MÉTODOS GETTERS E SETTERS

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

    //OVERRIDE DA SAÍDA TOSTRING()
    @Override
    public String toString(){
        return "ID="+getId_usuario()+" NOME="+getNome()+" EMAIL="+getEmail()+" CPF="+getCpf()+" Senha="+getSenha();
    }
}
