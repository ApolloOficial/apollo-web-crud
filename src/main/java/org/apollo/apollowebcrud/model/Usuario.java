package org.apollo.apollowebcrud.model;

public class Usuario {

//    ATRIBUTO

    private int id;
    private String nome;
    private int idTipoUsuario;
    private Integer idEmpresa;
    private Integer idFilial;
    private String email;
    private String telefone;
    private String cpf;
    private String senhaHash;
    private String status;
    private String urlFoto;
    private Integer idUsuarioCadastro;
    private TipoUsuEnum perfil;
    private String mensagemErro;

//    CONSTRUTOR

    public Usuario(int id, String nome, int idTipoUsuario, Integer idEmpresa, Integer idFilial, String email, String telefone, String cpf, String senhaHash, String status, String urlFoto) {
        this.id = id;
        this.nome = nome;
        this.idTipoUsuario = idTipoUsuario;
        this.idEmpresa = idEmpresa;
        this.idFilial = idFilial;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.senhaHash = senhaHash;
        this.status = status;
        this.urlFoto = urlFoto;
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

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public Integer getIdFilial() {
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

    public String getStatus() {
        return status;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    // Técnico e operador (têm filial) são quem opera inversores, strings, painéis e manutenções
    public boolean podeOperarEquipamentos() {
        return idFilial != null && (perfil == TipoUsuEnum.TECNICO || perfil == TipoUsuEnum.OPERADOR);
    }

    // Primeira letra do primeiro e do último nome, para o avatar das telas (ex.: "Ana Souza" → "AS")
    public String getIniciais() {
        if (nome == null || nome.isBlank()) {
            return "";
        }
        String[] partes = nome.trim().split("\\s+");
        String iniciais = partes.length == 1
                ? partes[0].substring(0, 1)
                : partes[0].substring(0, 1) + partes[partes.length - 1].substring(0, 1);
        return iniciais.toUpperCase();
    }

    public TipoUsuEnum getPerfil() {
        return perfil;
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

    public void setIdTipoUsuario(int idTipoUsuario) {
        this.idTipoUsuario = idTipoUsuario;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public void setIdFilial(Integer idFilial) {
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

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public void setPerfil(TipoUsuEnum perfil) {
        this.perfil = perfil;
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
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", idTipoUsuario=" + idTipoUsuario +
                ", idEmpresa=" + idEmpresa +
                ", idFilial=" + idFilial +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", cpf='" + cpf + '\'' +
                ", ativo='" + status + '\'' +
                ", urlFoto='" + urlFoto + '\'' +
                ", idUsuarioCadastro=" + idUsuarioCadastro +
                '}';
    }
}