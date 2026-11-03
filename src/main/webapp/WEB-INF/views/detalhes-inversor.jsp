<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="ap" tagdir="/WEB-INF/tags" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="filial"/>
<c:set var="podeEditar" value="${usuarioLogado.perfil != 'GERENTE'}"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apollo | Detalhes do inversor</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${raiz}/docs/css/logoApollo.css">
    <link rel="stylesheet" href="${raiz}/docs/css/detalhes-inversor.css">
    <link rel="stylesheet" href="${raiz}/docs/css/sidebar.css">
    <link rel="stylesheet" href="${raiz}/docs/css/detalhes-melhoria.css">
</head>
<body>
<div class="pagina">
    <%@ include file="includes/sidebar.jspf" %>

    <main class="conteudo" id="tela-inversor">

        <header class="topo topo-detalhes">
            <div>
                <h1 class="titulo">Detalhes do inversor</h1>
                <nav class="breadcrumb" aria-label="Você está em">
                    <a href="${raiz}/filial">Filial</a><span class="sep">&gt;</span>
                    <a href="${raiz}/filial?detalhes=${item.idFilial}"><c:out value="${filial.nome}" default="Filial"/></a><span class="sep">&gt;</span>
                    <span class="atual"><c:out value="${item.codigo}"/></span>
                </nav>
            </div>
            <div class="usuario">
                <a href="${raiz}/perfil" style="display:flex;align-items:center;gap:10px;text-decoration:none;color:inherit">
                    <div class="avatar-usuario" aria-hidden="true"></div>
                    <span><c:out value="${usuarioLogado.nome}"/></span>
                </a>
            </div>
        </header>

        <section class="card-inversor">
            <div class="inversor-info">
                <div class="avatar">INV</div>
                <div>
                    <div class="linha-nome">
                        <h2><c:out value="${item.codigo}"/></h2>
                        <ap:badge valor="${item.status}"/>
                    </div>
                    <p class="subtitulo"><c:out value="${item.marca}"/> <c:out value="${item.modelo}"/> · <c:out value="${item.potenciaKw}" default="—"/> kW</p>
                </div>
            </div>
            <c:if test="${podeEditar}">
                <div class="acoes">
                    <a class="btn btn-editar" href="${raiz}/inversor?editar=${item.id}">Editar</a>
                </div>
            </c:if>
        </section>

        <section class="grade-info">
            <div class="card-info">
                <span class="rotulo">Filial</span>
                <span class="valor"><c:out value="${filial.nome}" default="—"/></span>
            </div>
            <div class="card-info">
                <span class="rotulo">Código</span>
                <span class="valor"><c:out value="${item.codigo}"/></span>
            </div>
            <div class="card-info">
                <span class="rotulo">N.º de série</span>
                <span class="valor"><c:out value="${item.numeroSerie}"/></span>
            </div>
            <div class="card-info">
                <span class="rotulo">Marca</span>
                <span class="valor"><c:out value="${item.marca}"/></span>
            </div>
            <div class="card-info">
                <span class="rotulo">Modelo</span>
                <span class="valor"><c:out value="${item.modelo}"/></span>
            </div>
            <div class="card-info">
                <span class="rotulo">Potência (kW)</span>
                <span class="valor"><c:out value="${item.potenciaKw}" default="—"/></span>
            </div>
            <div class="card-info card-status">
                <span class="rotulo">Status</span>
                <ap:badge valor="${item.status}"/>
            </div>
            <div class="card-info">
                <span class="rotulo">Data de instalação</span>
                <span class="valor"><ap:data valor="${item.dataInstalacao}"/></span>
            </div>
        </section>

        <div class="secao-topo">
            <h3 class="titulo-secao">Strings</h3>
            <c:if test="${podeEditar}">
                <a class="btn btn-cadastrar" href="${raiz}/string?novo=1">Cadastrar String</a>
            </c:if>
        </div>
        <section class="grade-strings" id="listaStrings">
            <c:forEach var="s" items="${listaStrings}">
                <article class="card-string">
                    <div>
                        <span class="tipo">String</span>
                        <span class="codigo"><c:out value="${s.codigo}"/></span>
                        <span class="mppt">MPPT <c:out value="${s.numeroMppt}" default="—"/> · Entrada <c:out value="${s.numeroEntrada}" default="—"/></span>
                    </div>
                    <div class="lado-direito">
                        <ap:badge valor="${s.status}"/>
                        <a href="${raiz}/string?detalhes=${s.id}" class="btn btn-detalhes">Ver detalhes</a>
                    </div>
                </article>
            </c:forEach>
            <c:if test="${empty listaStrings}">
                <p class="vazio" style="padding:24px 0;color:#7a7a7a;font-size:14px">Nenhuma string cadastrada neste inversor.</p>
            </c:if>
        </section>

        <div class="rodape-detalhes">
            <a href="${raiz}/filial?detalhes=${item.idFilial}" class="link-voltar">Voltar para filial</a>
        </div>
    </main>
</div>
</body>
</html>
