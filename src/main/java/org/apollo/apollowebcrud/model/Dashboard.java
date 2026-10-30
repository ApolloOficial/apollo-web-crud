package org.apollo.apollowebcrud.model;

// Números dos 4 cards do dashboard.
// Gerente: membros da empresa, filiais, inversores e potência total (todas as filiais da empresa).
// Técnico/operador: membros da filial, manutenções, inversores e potência total (da filial).
// Os campos que não se aplicam ao perfil ficam em 0.
public class Dashboard {

    private int totalMembros;
    private int totalFiliais;
    private int totalManutencoes;
    private int totalInversores;
    private double potenciaTotalKw;

    public int getTotalMembros() {
        return totalMembros;
    }

    public void setTotalMembros(int totalMembros) {
        this.totalMembros = totalMembros;
    }

    public int getTotalFiliais() {
        return totalFiliais;
    }

    public void setTotalFiliais(int totalFiliais) {
        this.totalFiliais = totalFiliais;
    }

    public int getTotalManutencoes() {
        return totalManutencoes;
    }

    public void setTotalManutencoes(int totalManutencoes) {
        this.totalManutencoes = totalManutencoes;
    }

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
}
