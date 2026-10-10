package org.apollo.apollowebcrud.model;

public class Filial {

    // ATRIBUTOS

    private int id;
    private int idEmpresa;
    private int idSegmento;
    private String nome;
    private String cnpj;
    private String email;
    private String telefone;
    private int idEndereco;
    private StatusGeral status;
    private Integer idUsuarioCadastro;
    private String mensagemErro;

//    CONSTRUTOR

    public Filial(int id, int idEmpresa, int idSegmento, String nome, String cnpj, String email, String telefone, int idEndereco, StatusGeral status, Integer idUsuarioCadastro) {
        this.id = id;
        this.idEmpresa = idEmpresa;
        this.idSegmento = idSegmento;
        this.nome = nome;
        this.cnpj = cnpj;
        this.email = email;
        this.telefone = telefone;
        this.idEndereco = idEndereco;
        this.status = StatusGeral.ATIVO;
        this.idUsuarioCadastro = idUsuarioCadastro;
    }

//    CONSTRUTOR VAZIO

    public Filial() {
    }

//  GETTERS

    public int getId() {
        return id;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public int getIdSegmento() {
        return idSegmento;
    }

    public String getNome() {
        return nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public int getIdEndereco() {
        return idEndereco;
    }

    public StatusGeral getStatus() {
        return status;
    }

    public Integer getIdUsuarioCadastro() {
        return idUsuarioCadastro;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }

//    SETTERS

    public void setId(int id) {
        this.id = id;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public void setIdSegmento(int idSegmento) {
        this.idSegmento = idSegmento;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setIdEndereco(int idEndereco) {
        this.idEndereco = idEndereco;
    }

    public void setStatus(StatusGeral status) {
        this.status = status;
    }

    public void setIdUsuarioCadastro(Integer idUsuarioCadastro) {
        this.idUsuarioCadastro = idUsuarioCadastro;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

//    SAÍDA TOSRTING()

    @Override
    public java.lang.String toString() {
        return "Filial{" +
                "id=" + id +
                ", idEmpresa=" + idEmpresa +
                ", idSegmento=" + idSegmento +
                ", nome=" + nome +
                ", cnpj=" + cnpj +
                ", email=" + email +
                ", telefone=" + telefone +
                ", idEndereco=" + idEndereco +
                ", status=" + status +
                ", idUsuarioCadastro=" + idUsuarioCadastro +
                '}';
    }
}