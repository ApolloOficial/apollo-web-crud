package org.apollo.apollowebcrud.filter;

import org.apollo.apollowebcrud.model.TipoUsuEnum;
import org.apollo.apollowebcrud.model.Usuario;

import java.util.Map;
import java.util.Set;

// Regra única de "quem pode abrir qual tela". O AuthFilter só pergunta; para mudar permissão, edite aqui.
public final class Autorizacao {

    private static final Set<String> COMUNS = Set.of("/dashboard", "/logout", "/empresa");

    // Telas de equipamento: técnico e operador cadastram/editam; o gerente só visualiza
    private static final Set<String> ROTAS_EQUIPAMENTO = Set.of("/inversor", "/string", "/painel", "/manutencao");

    private static final Map<TipoUsuEnum, Set<String>> ROTAS_POR_PERFIL = Map.of(
            TipoUsuEnum.GERENTE, Set.of("/contrato", "/filial", "/membros", "/cadastro", "/inversor", "/string", "/painel", "/manutencao"),
            TipoUsuEnum.TECNICO, Set.of("/filial", "/membros", "/cadastro", "/inversor", "/string", "/painel", "/manutencao"),
            TipoUsuEnum.OPERADOR, Set.of("/filial", "/inversor", "/string", "/painel", "/manutencao")
    );

    // O gerente que ainda não tem empresa só pode usar a tela de empresa (e sair)
    private static final Set<String> ROTAS_GERENTE_SEM_EMPRESA = Set.of("/empresa", "/logout");

    private Autorizacao() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada.");
    }

    public static boolean permite(Usuario usuario, String caminho) {
        if (usuario.getPerfil() == null) {
            return false;
        }
        if (usuario.getPerfil() == TipoUsuEnum.GERENTE && usuario.getIdEmpresa() == null) {
            return casa(ROTAS_GERENTE_SEM_EMPRESA, caminho);
        }
        return casa(COMUNS, caminho) || casa(ROTAS_POR_PERFIL.get(usuario.getPerfil()), caminho);
    }

    // Cadastrar, editar e inativar equipamentos: só técnico e operador. O gerente apenas visualiza.
    // O AuthFilter chama isto quando a requisição é de escrita (POST, ?novo ou ?editar).
    public static boolean permiteEscrita(Usuario usuario, String caminho) {
        return !(usuario.getPerfil() == TipoUsuEnum.GERENTE && casa(ROTAS_EQUIPAMENTO, caminho));
    }

    // "/filial" libera "/filial" e "/filial/qualquer-coisa", mas não "/filialxyz"
    private static boolean casa(Set<String> rotas, String caminho) {
        return rotas.stream().anyMatch(r -> caminho.equals(r) || caminho.startsWith(r + "/"));
    }
}
