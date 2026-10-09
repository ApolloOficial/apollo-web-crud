package org.apollo.apollowebcrud.model;

public class Usuario {

//    ATRIBUTO

    private int id;
    private String nome;
    private int idTipoUsuario;
    private int idEmpresa;
    private int idFilial;
    private String email;
    private String telefone;
    private String cpf;
    private String senhaHash;
    private String ativo;
    private String mensagemErro;

//    CONSTRUTOR

    public Usuario(int id, String nome, int idTipoUsuario, int idEmpresa, int idFilial, String email, String telefone, String cpf, String senhaHash, String ativo) {
        this.id = id;
        this.nome = nome;
        this.idTipoUsuario = idTipoUsuario;
        this.idEmpresa = idEmpresa;
        this.idFilial = idFilial;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.senhaHash = senhaHash;
        this.ativo = ativo;
    }

//    CONSTRUTOR VAZIO

    public Usuario() {
    }

//    GETTERS

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

    public String getSenhaHash() {
        return senhaHash;
    }

    public String getAtivo() {
        return ativo;
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

    public void setIdTipoUsuario(int idTipoUsuario) {
        this.idTipoUsuario = idTipoUsuario;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
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

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

//    SAÍDA TOSTRING()

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome=" + nome +
                ", idTipoUsuario=" + idTipoUsuario +
                ", idEmpresa=" + idEmpresa +
                ", idFilial=" + idFilial +
                ", email=" + email +
                ", telefone=" + telefone +
                ", cpf=" + cpf +
                ", senhaHash=" + senhaHash +
                ", ativo=" + ativo +
                '}';
    }
}