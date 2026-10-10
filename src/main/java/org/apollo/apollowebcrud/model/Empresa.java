package org.apollo.apollowebcrud.model;

public class Empresa {

//     ATRIBUTOS

    private int id;
    private String nome;
    private String cnpj;
    private String descricao;
    private String emailContato;
    private int idEndereco;
    private StatusGeral status;
    private String urlFoto;
    private Integer idUsuarioCadastro;
    private String mensagemErro;

//    CONSTRUTOR

    public Empresa(int id, String nome, String cnpj, String descricao, String emailContato, int idEndereco, StatusGeral status, String urlFoto, Integer idUsuarioCadastro) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.descricao = descricao;
        this.emailContato = emailContato;
        this.idEndereco = idEndereco;
        this.status = StatusGeral.ATIVO;
        this.urlFoto = urlFoto;
        this.idUsuarioCadastro = idUsuarioCadastro;
    }

//    CONSTRUTOR VAZIO

    public Empresa() {
    }

//    GETTERS

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getEmailContato() {
        return emailContato;
    }

    public int getIdEndereco() {
        return idEndereco;
    }

    public StatusGeral getStatus() {
        return status;
    }

    public String getUrlFoto() {
        return urlFoto;
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

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setEmailContato(String emailContato) {
        this.emailContato = emailContato;
    }

    public void setIdEndereco(int idEndereco) {
        this.idEndereco = idEndereco;
    }

    public void setStatus(StatusGeral status) {
        this.status = status;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public void setIdUsuarioCadastro(Integer idUsuarioCadastro) {
        this.idUsuarioCadastro = idUsuarioCadastro;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

//    SAÍDA TOSTRING()

    @Override
    public String toString() {
        return "Empresa{" +
                "id=" + id +
                ", nome=" + nome +
                ", cnpj=" + cnpj +
                ", descricao=" + descricao +
                ", emailContato=" + emailContato +
                ", idEndereco=" + idEndereco +
                ", ativo=" + status +
                ", urlFoto=" + urlFoto +
                ", idUsuarioCadastro=" + idUsuarioCadastro +
                '}';
    }
}