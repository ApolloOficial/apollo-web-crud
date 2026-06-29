package org.example.model;

public class Lote {
    //ATRIBUTOS E CONSTRUTOR

    private int id;
    private int quantia_paineis;
    private int eficiencia_geral;
    private String observacao;
    private int id_cadastro;
    private String localizacao;

    public Lote(){
    }

    public Lote(int id, int quantia_paineis, int eficiencia_geral, String observacao, int id_cadastro, String localizacao) {
        this.id = id;
        this.quantia_paineis = quantia_paineis;
        this.eficiencia_geral = eficiencia_geral;
        this.observacao = observacao;
        this.id_cadastro = id_cadastro;
        this.localizacao = localizacao;
    }
    //MÉTODOS GETTERS E SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantia_paineis() {
        return quantia_paineis;
    }

    public void setQuantia_paineis(int quantia_paineis) {
        this.quantia_paineis = quantia_paineis;
    }

    public int getEficiencia_geral() {
        return eficiencia_geral;
    }

    public void setEficiencia_geral(int eficiencia_geral) {
        this.eficiencia_geral = eficiencia_geral;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public int getId_cadastro() {
        return id_cadastro;
    }

    public void setId_cadastro(int id_cadastro) {
        this.id_cadastro = id_cadastro;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }
    //OVERRIDE DA SAÍDA TOSTRING()

    @Override
    public String toString(){
        return "ID="+getId()+" QUANTIA P="+getQuantia_paineis()+" EFICIÊNCIA GERAL="+getEficiencia_geral()+" OBSERVAÇÃO="+getObservacao()+" ID CADASTRO="+getId_cadastro()+" LOCALIZAÇÃO="+getLocalizacao();
    }
}
