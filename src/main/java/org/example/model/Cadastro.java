package org.example.model;

import java.time.LocalDate;

public class Cadastro {
    //ATRIBUTOS E CONSTRUTOR

    private int id;
    private LocalDate data;
    private String info_foto;
    private int id_cadastrante;

    public Cadastro() {
        this.id = 0;
        this.data = null;
        this.info_foto = null;
        this.id_cadastrante = 0;
    }

    public Cadastro(int id, LocalDate data, String info_foto, int id_cadastrante) {
        this.id = id;
        this.data = data;
        this.info_foto = info_foto;
        this.id_cadastrante = id_cadastrante;
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

    public String getInfo_foto() {
        return info_foto;
    }

    public void setInfo_foto(String info_foto) {
        this.info_foto = info_foto;
    }

    public int getId_cadastrante() {
        return id_cadastrante;
    }

    public void setId_cadastrante(int id_cadastrante) {
        this.id_cadastrante = id_cadastrante;
    }
    //OVERRIDE DA SAÍDA TOSTRING()

    @Override
    public String toString(){
        return "ID="+getId()+" DATA="+getData()+" FOTO INFO.="+getInfo_foto()+" ID CADASTRANTE="+getId_cadastrante();
    }
}
