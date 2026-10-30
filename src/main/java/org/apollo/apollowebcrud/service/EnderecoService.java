package org.apollo.apollowebcrud.service;

import org.apollo.apollowebcrud.dao.EnderecoDAO;
import org.apollo.apollowebcrud.model.Endereco;
import org.apollo.apollowebcrud.util.CepUtils;

// Regras de endereço usadas no cadastro de empresa e de filial
public class EnderecoService {

    private final EnderecoDAO enderecoDAO = new EnderecoDAO();

    public Endereco buscarPorId(int idEndereco) {
        return idEndereco <= 0 ? null : enderecoDAO.buscarPorId(idEndereco);
    }

    // Devolve null se está tudo certo, ou a mensagem de erro
    public String validar(Endereco end) {
        if (!CepUtils.isCepValido(end.getCep())) {
            return "O CEP não é válido";
        }
        if (vazio(end.getLogradouro())) return "O logradouro não foi informado";
        if (vazio(end.getNumero())) return "O número do endereço não foi informado";
        if (vazio(end.getBairro())) return "O bairro não foi informado";
        if (vazio(end.getCidade())) return "A cidade não foi informada";
        if (end.getEstado() == null || !end.getEstado().trim().matches("[A-Za-z]{2}")) {
            return "O estado deve ter 2 letras (ex.: SP)";
        }
        return null;
    }

    // Deixa no formato do banco: CEP só com dígitos, estado em maiúsculas, complemento vazio vira null
    public void normalizar(Endereco end) {
        end.setCep(end.getCep().replaceAll("\\D", ""));
        end.setLogradouro(end.getLogradouro().trim());
        end.setNumero(end.getNumero().trim());
        end.setComplemento(vazio(end.getComplemento()) ? null : end.getComplemento().trim());
        end.setBairro(end.getBairro().trim());
        end.setCidade(end.getCidade().trim());
        end.setEstado(end.getEstado().trim().toUpperCase());
    }

    private boolean vazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
