package org.apollo.apollowebcrud.model;

public class Usuario {

//    ATRIBUTO E CONSTRUTORES

    private int id;
    private String nome;
    private int idTipoUsuario;
    private int idEmpresa;
    private String email;
    private String telefone;
    private String cpf;
    private String senha;

    public Usuario(int id, String nome, int idTipoUsuario, int idEmpresa, String email, String telefone, String cpf, String senha) {
        this.id = id;
        this.nome = nome;
        this.idTipoUsuario = idTipoUsuario;
        this.idEmpresa = idEmpresa;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.senha = senha;
    }

    public Usuario() {

    }

//    MÉTODOS GETTERS SETTERS


    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getIdTipoUsuario() {
        return idTipoUsuario;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setIdTipoUsuario(int idTipoUsuario) {
        this.idTipoUsuario = idTipoUsuario;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    // OVERRIDE DA SAÍDA TOSTRING()
    @Override
    public String toString() {
        return "ID=" + getId() +
                " NOME=" + getNome() + " ID_TIPO_USUARIO=" + getIdTipoUsuario() + " ID_EMPRESA=" + getIdEmpresa() + " EMAIL=" + getEmail() + " TELEFONE=" + getTelefone() + " CPF=" + getCpf() + " SENHA=" + getSenha();
    }
}