package org.apollo.apollowebcrud.service;

import org.apollo.apollowebcrud.dao.StringSolarDAO;
import org.apollo.apollowebcrud.model.Inversor;
import org.apollo.apollowebcrud.model.StatusGeral;
import org.apollo.apollowebcrud.model.StringSolar;
import org.apollo.apollowebcrud.model.Usuario;

import java.util.List;

import static org.apollo.apollowebcrud.util.TextoUtils.excede;
import static org.apollo.apollowebcrud.util.TextoUtils.vazio;

public class StringSolarService {

    private final StringSolarDAO stringSolarDAO = new StringSolarDAO();
    private final InversorService inversorService = new InversorService();

    // Técnico e operador só enxergam o que pertence à própria filial
    public List<StringSolar> listarVisiveis(Usuario logado) {
        if (logado.getIdFilial() == null) {
            return List.of();
        }
        return stringSolarDAO.listarPorFilial(logado.getIdFilial());
    }

    // Strings do inversor, só se o inversor é da filial do usuário (lista vazia caso contrário)
    public List<StringSolar> listarDoInversor(int idInversor, Usuario logado) {
        if (inversorService.buscarVisivel(idInversor, logado) == null) {
            return List.of();
        }
        return stringSolarDAO.listarPorInversor(idInversor);
    }

    // Visualização: a string só aparece se o inversor dela é visível para o usuário (gerente: da empresa; demais: da filial)
    public StringSolar buscarVisivel(int idString, Usuario logado) {
        StringSolar string = stringSolarDAO.buscarPorId(idString);
        return string != null && inversorService.buscarVisivel(string.getIdInversor(), logado) != null ? string : null;
    }

    // Devolve a string só se o inversor dela é da filial do usuário (null caso contrário)
    public StringSolar buscarDaFilial(int idString, Usuario logado) {
        if (!logado.podeOperarEquipamentos()) {
            return null;
        }
        StringSolar string = stringSolarDAO.buscarPorId(idString);
        return string != null && inversorService.buscarDaFilial(string.getIdInversor(), logado) != null ? string : null;
    }

    // ---------- CADASTRO ----------

    public String cadastrarString(StringSolar nova, Usuario logado) {
        if (!logado.podeOperarEquipamentos()) {
            return "Você não tem permissão para cadastrar strings";
        }

        Inversor inversor = inversorService.buscarDaFilial(nova.getIdInversor(), logado);
        if (inversor == null) {
            return "O inversor selecionado não existe na sua filial";
        }

        String erro = validar(nova, inversor.getId(), 0);
        if (erro != null) {
            return erro;
        }

        nova.setCodigo(nova.getCodigo().trim());
        nova.setStatus(StatusGeral.ATIVO);

        return stringSolarDAO.inserir(nova) > 0 ? null : "Erro interno no servidor ao tentar salvar a string";
    }

    // ---------- EDIÇÃO (o inversor não muda; o status pode mudar aqui) ----------

    public String editarString(StringSolar dados, Usuario logado) {
        StringSolar atual = buscarDaFilial(dados.getId(), logado);
        if (atual == null) {
            return "String não encontrada ou sem permissão";
        }
        if (dados.getStatus() == null) {
            return "O status não foi informado";
        }

        String erro = validar(dados, atual.getIdInversor(), atual.getId());
        if (erro != null) {
            return erro;
        }

        atual.setCodigo(dados.getCodigo().trim());
        atual.setNumeroMppt(dados.getNumeroMppt());
        atual.setNumeroEntrada(dados.getNumeroEntrada());
        atual.setStatus(dados.getStatus());

        return stringSolarDAO.atualizar(atual) ? null : "Erro interno no servidor ao tentar salvar a string";
    }

    private String validar(StringSolar s, int idInversor, int idIgnorar) {
        if (vazio(s.getCodigo())) return "O código da string não foi informado";
        if (excede(s.getCodigo(), 50)) return "O código da string deve ter até 50 caracteres";
        if (s.getNumeroMppt() != null && s.getNumeroMppt() <= 0) return "O número MPPT deve ser maior que zero";
        if (s.getNumeroEntrada() != null && s.getNumeroEntrada() <= 0) return "O número de entrada deve ser maior que zero";

        StringSolar mesmoCodigo = stringSolarDAO.buscarPorCodigoNoInversor(idInversor, s.getCodigo().trim());
        if (mesmoCodigo != null && mesmoCodigo.getId() != idIgnorar) {
            return "Já existe uma string com este código neste inversor";
        }
        return null;
    }
}
