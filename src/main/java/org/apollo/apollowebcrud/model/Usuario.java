package org.apollo.apollowebcrud.model;

public class Usuario {

//    ATRIBUTO E CONSTRUTORES

    private int id;
    private String nome;
    private int idTipoUsuario;
    private int idEmpresa;
    private int idFilial;
    private String email;
    private String telefone;
    private String cpf;
    private String senha;
    private String mensagemErro;

    public Usuario(int id, String nome, int idTipoUsuario, int idEmpresa, int idFilial, String email, String telefone, String cpf, String senha) {
        this.id = id;
        this.nome = nome;
        this.idTipoUsuario = idTipoUsuario;
        this.idEmpresa = idEmpresa;
        this.idFilial = idFilial;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.senha = senha;
    }

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

    public int getIdFilial() {
        return idFilial;
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

    public String getMensagemErro() {
        return mensagemErro;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", idTipoUsuario=" + idTipoUsuario +
                ", idEmpresa=" + idEmpresa +
                ", idFilial=" + idFilial +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", cpf='" + cpf + '\'' +
                ", senha='" + senha + '\'' +
                '}';
    }
}