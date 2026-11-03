<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="ap" tagdir="/WEB-INF/tags" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="inversor"/>
<c:set var="podeEditar" value="${usuarioLogado.perfil != 'GERENTE'}"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apollo | inversor</title>
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
        <ap:topo titulo="${editando ? 'Editar inversor' : 'Cadastrar inversor'}" subtitulo="Informe os dados do inversor"/>


        <form class="cartao" action="${raiz}/inversor" method="post">
            <input type="hidden" name="acao" value="${editando ? 'editar' : 'cadastrar'}">
            <input type="hidden" name="id" value="${item.id}">

            <h2 class="form-secao">Dados do inversor</h2>
            <div class="form-grade">
                <label class="campo"><span>Código do inversor *</span>
                    <input type="text" name="codigo" maxlength="40" required value="<c:out value='${item.codigo}'/>"></label>
                <label class="campo"><span>Número de série *</span>
                    <input type="text" name="numeroSerie" maxlength="60" required value="<c:out value='${item.numeroSerie}'/>"></label>
                <label class="campo"><span>Marca *</span>
                    <input type="text" name="marca" maxlength="60" required value="<c:out value='${item.marca}'/>"></label>
                <label class="campo"><span>Modelo *</span>
                    <input type="text" name="modelo" maxlength="60" required value="<c:out value='${item.modelo}'/>"></label>
                <label class="campo"><span>Potência (kW)</span>
                    <input type="number" name="potenciaKw" min="0" step="0.01" value="<c:out value='${item.potenciaKw}'/>"></label>
                <label class="campo"><span>Data de instalação</span>
                    <input type="date" name="dataInstalacao" value="<c:out value='${item.dataInstalacao}'/>"></label>
                <c:if test="${editando}">
                    <label class="campo"><span>Status *</span>
                        <select name="status" required>
                            <option value="ATIVO" ${item.status == 'ATIVO' ? 'selected' : ''}>Ativo</option>
                            <option value="INATIVO" ${item.status == 'INATIVO' ? 'selected' : ''}>Inativo</option>
                        </select></label>
                </c:if>
            </div>

            <div class="form-rodape">
                <a class="btn btn-contorno" href="${raiz}/inversor">Cancelar</a>
                <button class="btn" type="submit">Salvar</button>
            </div>
        </form>
        <c:set var="erroAviso" value="${erroInversor}"/>
        <%@ include file="includes/aviso.jspf" %>
    </main>
</div>
<script src="${raiz}/docs/js/aviso.js"></script>
</body>
</html>
