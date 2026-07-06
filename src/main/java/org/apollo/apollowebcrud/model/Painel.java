package org.apollo.apollowebcrud.model;

public class Painel {
    //ATRIBUTOS E CONSTRUTOR

    private int id_painel;
    private String material;
    private String modelo;
    private String marca;
    private int eficiencia;
    private int vida_util;
    private String localizacao;
    private int id_lote;

    public Painel(){
    }

    public Painel(int id, String material, String modelo, String marca, int eficiencia, int vida_util, String localizacao, int id_lote) {
        this.id_painel = id;
        this.material = material;
        this.modelo = modelo;
        this.marca = marca;
        this.eficiencia = eficiencia;
        this.vida_util = vida_util;
        this.localizacao = localizacao;
        this.id_lote = id_lote;
    }
    //MÉTODOS GETTERS E SETTERS

    public int getId_painel() {
        return id_painel;
    }

    public void setId_painel(int id_painel) {
        this.id_painel = id_painel;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getEficiencia() {
        return eficiencia;
    }

    public void setEficiencia(int eficiencia) {
        this.eficiencia = eficiencia;
    }

    public int getVida_util() {
        return vida_util;
    }

    public void setVida_util(int vida_util) {
        this.vida_util = vida_util;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public int getId_lote() {
        return id_lote;
    }

    public void setId_lote(int id_lote) {
        this.id_lote = id_lote;
    }
    //OVERRIDE DA SAÍDA TOSTRING()

    @Override
    public String toString(){
        return "ID="+ getId_painel()+" MATERIAL="+getMaterial()+" MODELO="+getModelo()+" MARCA="+getMarca()+" EFICIÊNCIA="+getEficiencia()+" VIDA ÚTIL="+getVida_util()+" LOCALIZAÇÃO="+ getLocalizacao()+" ID LOTE="+getId_lote();
    }
}
