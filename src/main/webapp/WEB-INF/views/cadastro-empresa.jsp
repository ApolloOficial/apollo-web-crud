<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="ap" tagdir="/WEB-INF/tags" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="empresa"/>
<c:set var="editando" value="${modo == 'editar'}"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apollo | <c:out value="${editando ? 'Editar empresa' : 'Cadastrar empresa'}"/></title>
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
        <ap:topo titulo="${editando ? 'Editar empresa' : 'Cadastrar empresa'}" subtitulo="${editando ? 'Atualize os dados da sua empresa' : 'Cadastre a sua empresa para liberar o restante do sistema'}"/>


        <form class="cartao" action="${raiz}/empresa" method="post">
            <input type="hidden" name="acao" value="${editando ? 'editar' : 'cadastrar'}">

            <h2 class="form-secao">Dados da empresa</h2>
            <div class="form-grade">
                <label class="campo"><span>Nome da empresa *</span>
                    <input type="text" name="nome" maxlength="120" required value="<c:out value='${item.nome}'/>"></label>
                <label class="campo"><span>CNPJ *</span>
                    <input type="text" name="cnpj" data-mascara="cnpj" inputmode="numeric" placeholder="00.000.000/0000-00" required ${editando ? 'readonly' : ''} value="<c:out value='${item.cnpj}'/>"></label>
                <label class="campo"><span>E-mail de contato *</span>
                    <input type="email" name="emailContato" maxlength="120" placeholder="contato@empresa.com" required value="<c:out value='${item.emailContato}'/>"></label>
                <label class="campo"><span>Link da foto</span>
                    <input type="text" name="urlFoto" maxlength="255" value="<c:out value='${item.urlFoto}'/>"></label>
                <label class="campo largo"><span>Descrição</span>
                    <input type="text" name="descricao" maxlength="255" value="<c:out value='${item.descricao}'/>"></label>
            </div>

            <h2 class="form-secao">Endereço</h2>
            <div class="form-grade">
                <label class="campo"><span>CEP *</span>
                    <input type="text" name="cep" data-mascara="cep" inputmode="numeric" placeholder="00000-000" required value="<c:out value='${endereco.cep}'/>"></label>
                <label class="campo"><span>Logradouro *</span>
                    <input type="text" name="logradouro" maxlength="120" required value="<c:out value='${endereco.logradouro}'/>"></label>
                <label class="campo"><span>Número *</span>
                    <input type="text" name="numero" maxlength="10" required value="<c:out value='${endereco.numero}'/>"></label>
                <label class="campo"><span>Complemento</span>
                    <input type="text" name="complemento" maxlength="60" placeholder="Ex.: Sala 101 (opcional)" value="<c:out value='${endereco.complemento}'/>"></label>
                <label class="campo"><span>Bairro *</span>
                    <input type="text" name="bairro" maxlength="80" required value="<c:out value='${endereco.bairro}'/>"></label>
                <label class="campo"><span>Cidade *</span>
                    <input type="text" name="cidade" maxlength="80" required value="<c:out value='${endereco.cidade}'/>"></label>
                <label class="campo"><span>Estado (UF) *</span>
                    <input type="text" name="estado" maxlength="2" pattern="[A-Za-z]{2}" placeholder="SP" required value="<c:out value='${endereco.estado}'/>"></label>
            </div>

            <div class="form-rodape">
                <c:if test="${editando}">
                    <a class="btn btn-contorno" href="${raiz}/empresa">Cancelar</a>
                </c:if>
                <button class="btn" type="submit"><c:out value="${editando ? 'Salvar' : 'Cadastrar'}"/></button>
            </div>
        </form>
        <c:set var="erroAviso" value="${erroEmpresa}"/>
        <%@ include file="includes/aviso.jspf" %>
    </main>
</div>
<script src="${raiz}/docs/js/mascaras.js"></script>
<script src="${raiz}/docs/js/aviso.js"></script>
</body>
</html>
