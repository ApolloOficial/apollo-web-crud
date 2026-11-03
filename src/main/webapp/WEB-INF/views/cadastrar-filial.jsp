<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="ap" tagdir="/WEB-INF/tags" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="filial"/>
<c:set var="editando" value="${modo == 'editar'}"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apollo | <c:out value="${editando ? 'Editar filial' : 'Cadastrar filial'}"/></title>
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
        <ap:topo titulo="${editando ? 'Editar filial' : 'Cadastrar filial'}" subtitulo="Preencha os dados da filial e o endereço onde ela funciona"/>


        <form class="cartao" action="${raiz}/filial" method="post">
            <input type="hidden" name="acao" value="${editando ? 'editar' : 'cadastrar'}">
            <input type="hidden" name="id" value="${item.id}">

            <h2 class="form-secao">Informações da filial</h2>
            <div class="form-grade">
                <label class="campo"><span>Nome da filial *</span>
                    <input type="text" name="nome" maxlength="120" placeholder="Ex.: Centro" required value="<c:out value='${item.nome}'/>"></label>
                <label class="campo"><span>Segmento *</span>
                    <select name="segmento" required>
                        <option value="">Selecione o segmento</option>
                        <c:forEach var="s" items="${listaSegmentos}">
                            <option value="${s.id}" ${item.idSegmento == s.id ? 'selected' : ''}><c:out value="${s.nome}"/></option>
                        </c:forEach>
                    </select></label>
                <label class="campo"><span>CNPJ *</span>
                    <input type="text" name="cnpj" data-mascara="cnpj" inputmode="numeric" placeholder="00.000.000/0000-00" required value="<c:out value='${item.cnpj}'/>"></label>
                <label class="campo"><span>E-mail de contato *</span>
                    <input type="email" name="email" maxlength="120" placeholder="filial@empresa.com" required value="<c:out value='${item.email}'/>"></label>
                <label class="campo"><span>Telefone</span>
                    <input type="text" name="telefone" data-mascara="telefone" inputmode="tel" placeholder="(11) 99999-9999" value="<c:out value='${item.telefone}'/>"></label>
            </div>

            <h2 class="form-secao">Endereço da filial</h2>
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
                <a class="btn btn-contorno" href="${raiz}/filial">Cancelar</a>
                <button class="btn" type="submit">Salvar</button>
            </div>
        </form>
        <c:set var="erroAviso" value="${erroFilial}"/>
        <%@ include file="includes/aviso.jspf" %>
    </main>
</div>
<script src="${raiz}/docs/js/mascaras.js"></script>
<script src="${raiz}/docs/js/aviso.js"></script>
</body>
</html>
