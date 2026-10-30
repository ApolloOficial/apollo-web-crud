package org.apollo.apollowebcrud.model;

// Números das métricas da tela de detalhes da filial.
// Inversores e strings contam só os ATIVOS; painéis contam ATIVO + INATIVO (e destaca quantos estão inativos);
// manutenções em aberto = ABERTA + EM_ANDAMENTO.
public class ResumoFilial {

    private int totalInversores;
    private double potenciaTotalKw;
    private int totalStrings;
    private int totalPaineis;
    private int paineisInativos;
    private int manutencoesEmAberto;
    private int manutencoesEmAndamento;

    public int getTotalInversores() {
        return totalInversores;
    }

    public void setTotalInversores(int totalInversores) {
        this.totalInversores = totalInversores;
    }

    public double getPotenciaTotalKw() {
        return potenciaTotalKw;
    }

    public void setPotenciaTotalKw(double potenciaTotalKw) {
        this.potenciaTotalKw = potenciaTotalKw;
    }

    public int getTotalStrings() {
        return totalStrings;
    }

    public void setTotalStrings(int totalStrings) {
        this.totalStrings = totalStrings;
    }

    public int getTotalPaineis() {
        return totalPaineis;
    }

    public void setTotalPaineis(int totalPaineis) {
        this.totalPaineis = totalPaineis;
    }

    public int getPaineisInativos() {
        return paineisInativos;
    }

    public void setPaineisInativos(int paineisInativos) {
        this.paineisInativos = paineisInativos;
    }

    public int getManutencoesEmAberto() {
        return manutencoesEmAberto;
    }

    public void setManutencoesEmAberto(int manutencoesEmAberto) {
        this.manutencoesEmAberto = manutencoesEmAberto;
    }

    public int getManutencoesEmAndamento() {
        return manutencoesEmAndamento;
    }

    public void setManutencoesEmAndamento(int manutencoesEmAndamento) {
        this.manutencoesEmAndamento = manutencoesEmAndamento;
    }
}
