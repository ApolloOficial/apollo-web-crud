<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="ap" tagdir="/WEB-INF/tags" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="manutencao"/>
<c:set var="podeEditar" value="${usuarioLogado.perfil != 'GERENTE'}"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apollo | manutenção</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${raiz}/docs/css/logoApollo.css">
    <link rel="stylesheet" href="${raiz}/docs/css/sidebar.css">
    <link rel="stylesheet" href="${raiz}/docs/css/apollo.css">
</head>
<body>
<div class="pagina">
    <%@ include file="includes/sidebar.jspf" %>

    <main class="conteudo">
        <c:set var="editando" value="${modo == 'editar'}"/>
        <ap:topo titulo="${editando ? 'Editar manutenção' : 'Cadastrar manutenção'}" subtitulo="Registre ou atualize a manutenção de um painel"/>


        <form class="cartao" action="${raiz}/manutencao" method="post">
            <input type="hidden" name="acao" value="${editando ? 'editar' : 'cadastrar'}">
            <input type="hidden" name="id" value="${item.id}">

            <h2 class="form-secao">Dados da manutenção</h2>
            <div class="form-grade">
                <label class="campo"><span>Painel *</span>
                    <c:if test="${editando}">
                        <input type="hidden" name="painel" value="${item.idPainel}">
                    </c:if>
                    <select name="${editando ? 'painelFixo' : 'painel'}" ${editando ? 'disabled' : 'required'}>
                        <option value="">Selecione o painel</option>
                        <c:forEach var="p" items="${listaPaineis}">
                            <option value="${p.id}" ${item.idPainel == p.id ? 'selected' : ''}><c:out value="${p.numeroSerie}"/></option>
                        </c:forEach>
                    </select></label>
                <label class="campo"><span>Tipo *</span>
                    <select name="tipoManutencao" required>
                        <option value="">Selecione o tipo</option>
                        <option value="PREVENTIVA" ${item.tipoManutencao == 'PREVENTIVA' ? 'selected' : ''}>Preventiva</option>
                        <option value="CORRETIVA" ${item.tipoManutencao == 'CORRETIVA' ? 'selected' : ''}>Corretiva</option>
                        <option value="PREDITIVA" ${item.tipoManutencao == 'PREDITIVA' ? 'selected' : ''}>Preditiva</option>
                    </select></label>
                <label class="campo"><span>Responsável</span>
                    <select name="responsavel">
                        <option value="">Sem responsável</option>
                        <c:forEach var="r" items="${listaResponsaveis}">
                            <option value="${r.id}" ${item.idUsuarioResponsavel == r.id ? 'selected' : ''}><c:out value="${r.nome}"/></option>
                        </c:forEach>
                    </select></label>
                <c:if test="${editando}">
                    <label class="campo"><span>Status *</span>
                        <select name="status" required>
                            <option value="ABERTA" ${item.status == 'ABERTA' ? 'selected' : ''}>Aberta</option>
                            <option value="EM_ANDAMENTO" ${item.status == 'EM_ANDAMENTO' ? 'selected' : ''}>Em andamento</option>
                            <option value="CONCLUIDA" ${item.status == 'CONCLUIDA' ? 'selected' : ''}>Concluída</option>
                            <option value="CANCELADA" ${item.status == 'CANCELADA' ? 'selected' : ''}>Cancelada</option>
                        </select></label>
                </c:if>
                <label class="campo"><span>Data de abertura</span>
                    <input type="date" name="dataAbertura" value="<c:out value='${item.dataAbertura}'/>"></label>
                <label class="campo"><span>Data prevista</span>
                    <input type="date" name="dataPrevista" value="<c:out value='${item.dataPrevista}'/>"></label>
                <label class="campo"><span>Data de conclusão</span>
                    <input type="date" name="dataConclusao" value="<c:out value='${item.dataConclusao}'/>"></label>
                <label class="campo largo"><span>Descrição</span>
                    <textarea name="descricao" maxlength="500"><c:out value="${item.descricao}"/></textarea></label>
            </div>

            <div class="form-rodape">
                <a class="btn btn-contorno" href="${raiz}/manutencao">Cancelar</a>
                <button class="btn" type="submit">Salvar</button>
            </div>
        </form>
        <c:set var="erroAviso" value="${erroManutencao}"/>
        <%@ include file="includes/aviso.jspf" %>
    </main>
</div>
<script src="${raiz}/docs/js/aviso.js"></script>
</body>
</html>
