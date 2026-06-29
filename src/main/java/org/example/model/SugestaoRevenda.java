package org.example.model;

public class SugestaoRevenda {
    //ATRIBUTOS E CONSTRUTOR

    private int id;
    private int id_painel;
    private double preco;

    public SugestaoRevenda() {
    }

    public SugestaoRevenda(int id, int id_painel, double preco) {
        this.id = id;
        this.id_painel = id_painel;
        this.preco = preco;
    }
    //MÉTODOS GETTERS E SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_painel() {
        return id_painel;
    }

    public void setId_painel(int id_painel) {
        this.id_painel = id_painel;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
    //OVERRIDE DA SAÍDA TOSTRING()

    @Override
    public String toString(){
        return "ID="+getId()+" ID PAINEL="+getId_painel()+" PREÇO="+getPreco();
    }
}
