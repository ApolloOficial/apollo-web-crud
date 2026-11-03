<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="ap" tagdir="/WEB-INF/tags" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="string"/>
<c:set var="podeEditar" value="${usuarioLogado.perfil != 'GERENTE'}"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apollo | string</title>
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
        <ap:topo titulo="${editando ? 'Editar string' : 'Cadastrar string'}" subtitulo="Informe os dados da string"/>


        <form class="cartao" action="${raiz}/string" method="post">
            <input type="hidden" name="acao" value="${editando ? 'editar' : 'cadastrar'}">
            <input type="hidden" name="id" value="${item.id}">

            <h2 class="form-secao">Dados da string</h2>
            <div class="form-grade">
                <label class="campo"><span>Inversor *</span>
                    <c:if test="${editando}">
                        <input type="hidden" name="inversor" value="${item.idInversor}">
                    </c:if>
                    <select name="${editando ? 'inversorFixo' : 'inversor'}" ${editando ? 'disabled' : 'required'}>
                        <option value="">Selecione o inversor</option>
                        <c:forEach var="i" items="${listaInversores}">
                            <option value="${i.id}" ${item.idInversor == i.id ? 'selected' : ''}><c:out value="${i.codigo}"/></option>
                        </c:forEach>
                    </select></label>
                <label class="campo"><span>Código da string *</span>
                    <input type="text" name="codigo" maxlength="40" required value="<c:out value='${item.codigo}'/>"></label>
                <label class="campo"><span>Nº de MPPT</span>
                    <input type="number" name="numeroMppt" min="1" step="1" value="<c:out value='${item.numeroMppt}'/>"></label>
                <label class="campo"><span>Nº de entrada</span>
                    <input type="number" name="numeroEntrada" min="1" step="1" value="<c:out value='${item.numeroEntrada}'/>"></label>
                <c:if test="${editando}">
                    <label class="campo"><span>Status *</span>
                        <select name="status" required>
                            <option value="ATIVO" ${item.status == 'ATIVO' ? 'selected' : ''}>Ativo</option>
                            <option value="INATIVO" ${item.status == 'INATIVO' ? 'selected' : ''}>Inativo</option>
                        </select></label>
                </c:if>
            </div>

            <div class="form-rodape">
                <a class="btn btn-contorno" href="${raiz}/string">Cancelar</a>
                <button class="btn" type="submit">Salvar</button>
            </div>
        </form>
        <c:set var="erroAviso" value="${erroString}"/>
        <%@ include file="includes/aviso.jspf" %>
    </main>
</div>
<script src="${raiz}/docs/js/aviso.js"></script>
</body>
</html>
