<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="ap" tagdir="/WEB-INF/tags" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="filial"/>
<c:set var="ehGerente" value="${usuarioLogado.perfil == 'GERENTE'}"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apollo | Detalhes da filial</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${raiz}/docs/css/logoApollo.css">
    <link rel="stylesheet" href="${raiz}/docs/css/detalhes-filial.css">
    <link rel="stylesheet" href="${raiz}/docs/css/sidebar.css">
    <link rel="stylesheet" href="${raiz}/docs/css/detalhes-melhoria.css">
</head>
<body>
<div class="pagina">
    <%@ include file="includes/sidebar.jspf" %>

    <main class="conteudo" id="tela-filial">

        <header class="topo topo-detalhes">
            <div>
                <h1 class="titulo">Detalhes da Filial</h1>
                <nav class="breadcrumb" aria-label="Você está em">
                    <a href="${raiz}/filial">Filial</a><span class="sep">&gt;</span>
                    <span class="atual"><c:out value="${item.nome}"/></span>
                </nav>
            </div>
            <div class="usuario">
                <a href="${raiz}/perfil" style="display:flex;align-items:center;gap:10px;text-decoration:none;color:inherit">
                    <div class="avatar-usuario" aria-hidden="true"></div>
                    <span><c:out value="${usuarioLogado.nome}"/></span>
                </a>
            </div>
        </header>

        <c:if test="${not empty erroFilial}">
            <p class="pop-erro" role="alert" style="margin-bottom:16px"><c:out value="${erroFilial}"/></p>
        </c:if>

        <section class="card-inversor">
            <div class="inversor-info">
                <div class="avatar"><c:out value="${fn:toUpperCase(fn:substring(item.nome, 0, 2))}"/></div>
                <div>
                    <div class="linha-nome">
                        <h2><c:out value="${item.nome}"/></h2>
                        <c:choose>
                            <c:when test="${item.status == 'ATIVO'}"><span class="badge badge-ativo">Ativo</span></c:when>
                            <c:otherwise><span class="badge badge-inativo">Inativo</span></c:otherwise>
                        </c:choose>
                    </div>
                    <p class="subtitulo"><c:out value="${empresa.nome}"/> · Segmento: <c:out value="${segmento.nome}" default="—"/></p>
                </div>
            </div>

            <c:if test="${ehGerente}">
                <div class="acoes">
                    <a class="btn btn-editar" href="${raiz}/filial?editar=${item.id}">Editar filial</a>
                    <form action="${raiz}/filial" method="post" style="display:inline" data-confirmar="Confirma esta alteração de status?">
                        <input type="hidden" name="id" value="${item.id}">
                        <c:choose>
                            <c:when test="${item.status == 'ATIVO'}">
                                <input type="hidden" name="acao" value="inativar">
                                <button type="submit" class="btn btn-inativar">Inativar filial</button>
                            </c:when>
                            <c:otherwise>
                                <input type="hidden" name="acao" value="reativar">
                                <button type="submit" class="btn btn-editar">Reativar filial</button>
                            </c:otherwise>
                        </c:choose>
                    </form>
                </div>
            </c:if>
        </section>

        <section class="metricas-filial">
            <div class="metrica-card">
                <span class="rotulo">Inversores</span>
                <strong>${resumo.totalInversores}</strong>
                <span class="badge badge-ativo badge-mini"><fmt:formatNumber value="${resumo.potenciaTotalKw}" minFractionDigits="0" maxFractionDigits="2"/> kW</span>
            </div>
            <div class="metrica-card">
                <span class="rotulo">Strings</span>
                <strong>${resumo.totalStrings}</strong>
            </div>
            <div class="metrica-card">
                <span class="rotulo">Painéis</span>
                <strong>${resumo.totalPaineis}</strong>
                <span class="badge badge-amarelo badge-mini">${resumo.paineisInativos} inativo(s)</span>
            </div>
            <div class="metrica-card">
                <span class="rotulo">Manutenções em aberto</span>
                <strong>${resumo.manutencoesEmAberto}</strong>
                <span class="badge badge-amarelo badge-mini">${resumo.manutencoesEmAndamento} em andamento</span>
            </div>
        </section>

        <section class="info-trio">
            <article class="painel-info">
                <h3>Dados da filial</h3>
                <ul class="lista-dados">
                    <li><span class="chave">Nome</span><span class="valor-dado"><c:out value="${item.nome}"/></span></li>
                    <li><span class="chave">CNPJ</span><span class="valor-dado"><ap:cnpj valor="${item.cnpj}"/></span></li>
                    <li><span class="chave">E-mail</span><span class="valor-dado"><c:out value="${item.email}"/></span></li>
                    <li><span class="chave">Telefone</span><span class="valor-dado"><ap:telefone valor="${item.telefone}"/></span></li>
                    <li><span class="chave">Segmento</span><span class="valor-dado"><c:out value="${segmento.nome}" default="—"/></span></li>
                    <li><span class="chave">Empresa</span><span class="valor-dado"><c:out value="${empresa.nome}" default="—"/></span></li>
                </ul>
            </article>

            <article class="painel-info">
                <h3>Endereço</h3>
                <ul class="lista-dados">
                    <li><span class="chave">Logradouro</span><span class="valor-dado"><c:out value="${endereco.logradouro}"/>, <c:out value="${endereco.numero}"/></span></li>
                    <li><span class="chave">Complemento</span><span class="valor-dado"><c:out value="${endereco.complemento}" default="—"/></span></li>
                    <li><span class="chave">Bairro</span><span class="valor-dado"><c:out value="${endereco.bairro}"/></span></li>
                    <li><span class="chave">Cidade / UF</span><span class="valor-dado"><c:out value="${endereco.cidade}"/> / <c:out value="${endereco.estado}"/></span></li>
                    <li><span class="chave">CEP</span><span class="valor-dado"><c:out value="${endereco.cep}"/></span></li>
                </ul>
            </article>

            <article class="painel-info">
                <div class="painel-topo">
                    <h3>Equipe da filial</h3>
                    <c:if test="${ehGerente || usuarioLogado.perfil == 'TECNICO'}">
                        <a class="btn btn-cadastrar mini" href="${raiz}/cadastro">Cadastrar usuário</a>
                    </c:if>
                </div>
                <c:choose>
                    <c:when test="${empty equipe}">
                        <p class="vazio" style="padding:18px 0;color:#7a7a7a;font-size:13px">Nenhum membro nesta filial.</p>
                    </c:when>
                    <c:otherwise>
                        <ul class="lista-equipe">
                            <c:forEach var="m" items="${equipe}">
                                <li>
                                    <div class="avatar-mini"><c:out value="${m.iniciais}"/></div>
                                    <div>
                                        <span class="membro-nome"><c:out value="${m.nome}"/>
                                            <c:choose>
                                                <c:when test="${m.perfil == 'TECNICO'}"><span class="cargo cargo-azul">Técnico</span></c:when>
                                                <c:otherwise><span class="cargo cargo-amarelo">Operador</span></c:otherwise>
                                            </c:choose>
                                        </span>
                                        <span class="membro-email"><c:out value="${m.email}"/></span>
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>
                    </c:otherwise>
                </c:choose>
                <c:if test="${ehGerente || usuarioLogado.perfil == 'TECNICO'}">
                    <div class="equipe-rodape">
                        <a href="${raiz}/membros" class="btn btn-mais">Ver mais</a>
                    </div>
                </c:if>
            </article>
        </section>

        <section class="card-monitor">
            <div>
                <h3>Monitoramento do inversor</h3>
                <p>Potência, energia gerada, temperatura e status em tempo real aparecerão aqui após a integração com a API do inversor.</p>
            </div>
            <span class="badge badge-amarelo">Aguardando integração com a API</span>
        </section>

        <div class="secao-topo">
            <h3 class="titulo-secao">Inversores</h3>
            <c:if test="${!ehGerente && usuarioLogado.idFilial == item.id}">
                <a class="btn btn-cadastrar" href="${raiz}/inversor?novo=1">Cadastrar inversor</a>
            </c:if>
        </div>
        <section class="grade-strings" id="listaInversores">
            <c:forEach var="inv" items="${listaInversores}">
                <article class="card-string">
                    <div>
                        <span class="tipo">Inversor</span>
                        <span class="codigo"><c:out value="${inv.codigo}"/></span>
                        <span class="mppt"><c:out value="${inv.marca}"/> <c:out value="${inv.modelo}"/> · <c:out value="${inv.potenciaKw}" default="—"/> kW</span>
                        <span class="paineis">Nº de série: <c:out value="${inv.numeroSerie}"/></span>
                        <span class="data">Instalado em <ap:data valor="${inv.dataInstalacao}"/></span>
                    </div>
                    <div class="lado-direito">
                        <c:choose>
                            <c:when test="${inv.status == 'ATIVO'}"><span class="badge badge-ativo">Ativo</span></c:when>
                            <c:otherwise><span class="badge badge-inativo">Inativo</span></c:otherwise>
                        </c:choose>
                        <a href="${raiz}/inversor?detalhes=${inv.id}" class="btn btn-detalhes">Ver detalhes</a>
                    </div>
                </article>
            </c:forEach>
            <c:if test="${empty listaInversores}">
                <p class="vazio" style="padding:24px 0;color:#7a7a7a;font-size:14px">Nenhum inversor cadastrado nesta filial.</p>
            </c:if>
        </section>
        <c:set var="erroAviso" value=""/>
        <%@ include file="includes/aviso.jspf" %>
    </main>
</div>
<script src="${raiz}/docs/js/aviso.js"></script>
</body>
</html>
