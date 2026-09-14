package org.apollo.apollowebcrud.model;

public class Painel {
    //ATRIBUTOS E CONSTRUTOR

    private int id;
    private String numeroSerie;
    private String material;
    private String modelo;
    private String marca;
    private String notaFiscal;
    private double eficiencia;
    private int vidaUtil;
    private int idLote;
    private int tempoUso;
    private String mensagemErro;

    public Painel(int id, String numeroSerie, String material, String modelo, String marca, String notaFiscal, double eficiencia, int vidaUtil, int idLote, int tempoUso) {
        this.id = id;
        this.numeroSerie = numeroSerie;
        this.material = material;
        this.modelo = modelo;
        this.marca = marca;
        this.notaFiscal = notaFiscal;
        this.eficiencia = eficiencia;
        this.vidaUtil = vidaUtil;
        this.idLote = idLote;
        this.tempoUso = tempoUso;
    }

    public Painel() {
    }

//    MÉTODOS GETTERS SETTERS

    public int getId() {
        return id;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public String getMaterial() {
        return material;
    }

    public String getModelo() {
        return modelo;
    }

    public String getMarca() {
        return marca;
    }

    public String getNotaFiscal() {
        return notaFiscal;
    }

    public double getEficiencia() {
        return eficiencia;
    }

    public int getVidaUtil() {
        return vidaUtil;
    }

    public int getIdLote() {
        return idLote;
    }

    public int getTempoUso() {
        return tempoUso;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setNotaFiscal(String notaFiscal) {
        this.notaFiscal = notaFiscal;
    }

    public void setEficiencia(double eficiencia) {
        this.eficiencia = eficiencia;
    }

    public void setVidaUtil(int vidaUtil) {
        this.vidaUtil = vidaUtil;
    }

    public void setIdLote(int idLote) {
        this.idLote = idLote;
    }

    public void setTempoUso(int tempoUso) {
        this.tempoUso = tempoUso;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

    //OVERRIDE DA SAÍDA TOSTRING()


    @Override
    public String toString() {
        return "Painel{" +
                "id=" + id +
                ", numeroSerie='" + numeroSerie + '\'' +
                ", material='" + material + '\'' +
                ", modelo='" + modelo + '\'' +
                ", marca='" + marca + '\'' +
                ", notaFiscal='" + notaFiscal + '\'' +
                ", eficiencia=" + eficiencia +
                ", vidaUtil=" + vidaUtil +
                ", idLote=" + idLote +
                ", tempoUso=" + tempoUso +
                '}';
    }
}