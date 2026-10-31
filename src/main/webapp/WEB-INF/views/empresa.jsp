<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="ap" tagdir="/WEB-INF/tags" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="empresa"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apollo | Empresa</title>
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
        <ap:topo titulo="Empresa" subtitulo="Visualize as informações da empresa e suas filiais cadastradas"/>


        <c:forEach var="e" items="${listaEmpresas}">
            <section class="cartao">
                <div class="cabecalho-card">
                    <div class="identidade">
                        <div class="logo-card"><c:out value="${fn:toUpperCase(fn:substring(e.nome, 0, 2))}"/></div>
                        <div>
                            <h2><c:out value="${e.nome}"/></h2>
                            <ap:badge valor="${e.status}"/>
                        </div>
                    </div>
                    <c:if test="${usuarioLogado.perfil == 'GERENTE'}">
                        <a class="btn btn-contorno" href="${raiz}/empresa?editar=1">Editar empresa</a>
                    </c:if>
                </div>
                <div class="dados" style="margin-top:18px">
                    <div><span>CNPJ</span><strong><ap:cnpj valor="${e.cnpj}"/></strong></div>
                    <div><span>E-mail de contato</span><strong><c:out value="${e.emailContato}" default="—"/></strong></div>
                    <div><span>Descrição</span><strong><c:out value="${e.descricao}" default="—"/></strong></div>
                    <div><span>Endereço</span>
                        <strong>
                            <c:choose>
                                <c:when test="${not empty endereco}">
                                    <c:out value="${endereco.logradouro}"/>, <c:out value="${endereco.numero}"/>
                                    <c:if test="${not empty endereco.complemento}"> - <c:out value="${endereco.complemento}"/></c:if><br>
                                    <c:out value="${endereco.bairro}"/> - <c:out value="${endereco.cidade}"/>/<c:out value="${endereco.estado}"/><br>
                                    CEP <c:out value="${endereco.cep}"/>
                                </c:when>
                                <c:otherwise>—</c:otherwise>
                            </c:choose>
                        </strong>
                    </div>
                </div>
            </section>
        </c:forEach>

        <c:if test="${empty listaEmpresas}">
            <section class="cartao"><p class="vazio">Nenhuma empresa vinculada ao seu usuário.</p></section>
        </c:if>

        <div class="barra">
            <h2 style="font-size:18px;font-weight:600">Filiais cadastradas</h2>
            <c:if test="${usuarioLogado.perfil == 'GERENTE'}">
                <a class="btn" href="${raiz}/filial?novo=1">Adicionar nova filial</a>
            </c:if>
        </div>

        <c:choose>
            <c:when test="${empty listaFiliais}">
                <section class="cartao"><p class="vazio">Nenhuma filial cadastrada.</p></section>
            </c:when>
            <c:otherwise>
                <section class="grade-filiais">
                    <c:forEach var="f" items="${listaFiliais}">
                        <article class="card-filial">
                            <h3><c:out value="${f.nome}"/></h3>
                            <p>CNPJ <ap:cnpj valor="${f.cnpj}"/></p>
                            <div class="acoes" style="display:flex;gap:8px;align-items:center;justify-content:space-between">
                                <ap:badge valor="${f.status}"/>
                                <a class="btn btn-mini btn-contorno" href="${raiz}/filial?detalhes=${f.id}">Ver detalhes</a>
                            </div>
                        </article>
                    </c:forEach>
                </section>
            </c:otherwise>
        </c:choose>
        <c:set var="erroAviso" value=""/>
        <%@ include file="includes/aviso.jspf" %>
    </main>
</div>
<script src="${raiz}/docs/js/aviso.js"></script>
</body>
</html>
