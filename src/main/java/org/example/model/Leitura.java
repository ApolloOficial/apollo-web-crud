package org.example.model;

import java.time.LocalDate;

public class Leitura {
    //ATRIBUTOS E CONSTRUTOR

    private int id;
    private LocalDate data;
    private int id_leitor;
    private int id_lote;

    public Leitura(int id, LocalDate data, int id_leitor, int id_lote) {
        this.id = id;
        this.data = data;
        this.id_leitor = id_leitor;
        this.id_lote = id_lote;
    }
    //MÉTODOS GETTERS E SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public int getId_leitor() {
        return id_leitor;
    }

    public void setId_leitor(int id_leitor) {
        this.id_leitor = id_leitor;
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
        return "ID="+getId()+" DATA="+getData()+" ID LEITOR="+getId_leitor()+" ID LOTE="+getId_lote();
    }
}
