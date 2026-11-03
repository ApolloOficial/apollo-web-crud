package org.apollo.apollowebcrud.model;

/**
 * Números dos quatro cards do dashboard. O gerente vê membros da empresa, filiais, inversores e potência total.
 * Técnico e operador veem membros da filial, manutenções, inversores e potência total. Os campos que não se
 * aplicam ao perfil ficam em zero.
 */
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
