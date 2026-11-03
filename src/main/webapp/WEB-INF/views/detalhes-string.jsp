<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="ap" tagdir="/WEB-INF/tags" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="filial"/>
<c:set var="podeEditar" value="${usuarioLogado.perfil != 'GERENTE'}"/>
<c:set var="somaW" value="0"/>
<c:set var="totalAtivos" value="0"/>
<c:forEach var="p" items="${listaPaineis}">
    <c:if test="${not empty p.potenciaW}"><c:set var="somaW" value="${somaW + p.potenciaW}"/></c:if>
    <c:if test="${p.status == 'ATIVO'}"><c:set var="totalAtivos" value="${totalAtivos + 1}"/></c:if>
</c:forEach>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apollo | Detalhes da string</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${raiz}/docs/css/logoApollo.css">
    <link rel="stylesheet" href="${raiz}/docs/css/detalhes-string.css">
    <link rel="stylesheet" href="${raiz}/docs/css/sidebar.css">
    <link rel="stylesheet" href="${raiz}/docs/css/detalhes-melhoria.css">
</head>
<body>
<div class="pagina">
    <%@ include file="includes/sidebar.jspf" %>

    <main class="conteudo" id="tela-string">

        <header class="topo topo-detalhes">
            <div>
                <h1 class="titulo">Detalhes da String</h1>
                <nav class="breadcrumb" aria-label="Você está em">
                    <a href="${raiz}/filial">Filial</a><span class="sep">&gt;</span>
                    <a href="${raiz}/filial?detalhes=${inversor.idFilial}"><c:out value="${filial.nome}" default="Filial"/></a><span class="sep">&gt;</span>
                    <a href="${raiz}/inversor?detalhes=${item.idInversor}"><c:out value="${inversor.codigo}" default="Inversor"/></a><span class="sep">&gt;</span>
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
                <div class="avatar">STR</div>
                <div>
                    <div class="linha-nome">
                        <h2><c:out value="${item.codigo}"/></h2>
                        <ap:badge valor="${item.status}"/>
                    </div>
                    <p class="subtitulo">Inversor <c:out value="${inversor.codigo}" default="—"/> · MPPT <c:out value="${item.numeroMppt}" default="—"/> · Entrada <c:out value="${item.numeroEntrada}" default="—"/></p>
                </div>
            </div>
            <c:if test="${podeEditar}">
                <div class="acoes">
                    <a class="btn btn-editar" href="${raiz}/string?editar=${item.id}">Editar</a>
                </div>
            </c:if>
        </section>

        <section class="grade-info">
            <div class="card-info">
                <span class="rotulo">Inversor</span>
                <span class="valor"><c:out value="${inversor.codigo}" default="—"/></span>
            </div>
            <div class="card-info">
                <span class="rotulo">Código</span>
                <span class="valor"><c:out value="${item.codigo}"/></span>
            </div>
            <div class="card-info">
                <span class="rotulo">Nº de MPPT</span>
                <span class="valor"><c:out value="${item.numeroMppt}" default="—"/></span>
            </div>
            <div class="card-info">
                <span class="rotulo">Número de entrada</span>
                <span class="valor"><c:out value="${item.numeroEntrada}" default="—"/></span>
            </div>
            <div class="card-info card-status">
                <span class="rotulo">Status</span>
                <ap:badge valor="${item.status}"/>
            </div>
        </section>

        <section class="secao-paineis">
            <div class="paineis-topo">
                <h3>Painéis da String</h3>
                <c:if test="${podeEditar}">
                    <a class="btn btn-cadastrar" href="${raiz}/painel?novo=1">Adicionar painel</a>
                </c:if>
            </div>

            <div class="metricas">
                <div class="metrica">
                    <div><p>Painéis totais</p><strong>${listaPaineis.size()}</strong></div>
                    <svg viewBox="0 0 24 24"><path d="M4 4h16l2 12H2zM12 4v12M7 4l-2 12M17 4l2 12M3 10h18M10 20h4"/></svg>
                </div>
                <div class="metrica">
                    <div><p>Potência total</p><strong><fmt:formatNumber value="${somaW / 1000}" minFractionDigits="1" maxFractionDigits="2"/> kWp</strong></div>
                    <svg viewBox="0 0 24 24"><path d="M13 2 4 14h7l-1 8 9-12h-7z"/></svg>
                </div>
                <div class="metrica">
                    <div><p>Ativos</p><strong>${totalAtivos}/${listaPaineis.size()}</strong></div>
                    <svg viewBox="0 0 24 24"><circle cx="12" cy="12" r="9"/><path d="m8 12 3 3 5-6"/></svg>
                </div>
            </div>

            <div class="filtros">
                <label class="busca">
                    <span class="lupa"></span>
                    <input type="search" id="busca" placeholder="Busque pelo modelo ou série do painel" aria-label="Buscar painel">
                </label>
                <select class="filtro-status" id="filtro-status" aria-label="Filtrar por status">
                    <option value="">Todos os status</option>
                    <option value="ATIVO">Ativo</option>
                    <option value="INATIVO">Inativo</option>
                </select>
            </div>

            <div class="tabela-paineis">
                <table>
                    <thead>
                    <tr><th>Nº de série</th><th>Marca</th><th>Modelo</th><th>Potência (W)</th><th>Data de instalação</th><th>Status</th><th>Ações</th></tr>
                    </thead>
                    <tbody>
                    <c:forEach var="p" items="${listaPaineis}">
                        <tr data-linha data-status="${p.status}">
                            <td><c:out value="${p.numeroSerie}"/></td>
                            <td><c:out value="${p.marca}"/></td>
                            <td><c:out value="${p.modelo}"/></td>
                            <td><c:out value="${p.potenciaW}" default="—"/></td>
                            <td><ap:data valor="${p.dataInstalacao}"/></td>
                            <td><ap:badge valor="${p.status}"/></td>
                            <td>
                                <c:if test="${podeEditar}">
                                    <a class="mais" href="${raiz}/painel?editar=${p.id}" aria-label="Editar painel ${p.numeroSerie}" style="text-decoration:none">Editar</a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty listaPaineis}">
                        <tr><td colspan="7">Nenhum painel cadastrado nesta string.</td></tr>
                    </c:if>
                    <tr id="sem-resultado" hidden><td colspan="7">Nenhum painel encontrado para o filtro informado.</td></tr>
                    </tbody>
                </table>
            </div>
        </section>

        <div class="secao-topo">
            <h3 class="titulo-secao">Manutenções</h3>
            <c:if test="${podeEditar}">
                <a class="btn btn-cadastrar" href="${raiz}/manutencao?novo=1">Cadastrar manutenção</a>
            </c:if>
        </div>
        <section class="lista-manut">
            <c:forEach var="m" items="${listaManutencoes}">
                <article class="card-manut">
                    <div class="linha"><span>Placa</span><span class="selos">
                        <ap:badge valor="${m.status}"/>
                        <ap:badge valor="${m.tipoManutencao}"/>
                    </span></div>
                    <p class="codigo"><c:out value="${seriesPaineis[m.idPainel]}" default="—"/></p>
                    <p class="id">OS <c:out value="${m.id}"/> · <c:out value="${m.descricao}" default="Sem descrição"/></p>
                    <div class="rodape">
                        <span>Aberta em <ap:data valor="${m.dataAbertura}"/></span>
                        <div class="botoes">
                            <c:if test="${podeEditar && m.status != 'CONCLUIDA' && m.status != 'CANCELADA'}">
                                <a class="btn btn-atualizar" href="${raiz}/manutencao?editar=${m.id}">Atualizar</a>
                            </c:if>
                        </div>
                    </div>
                </article>
            </c:forEach>
            <c:if test="${empty listaManutencoes}">
                <p class="vazio" style="padding:24px 0;color:#7a7a7a;font-size:14px">Nenhuma manutenção para os painéis desta string.</p>
            </c:if>
        </section>

        <div class="rodape-detalhes">
            <a href="${raiz}/inversor?detalhes=${item.idInversor}" class="link-voltar">Voltar para o inversor</a>
        </div>
    </main>
</div>
<script src="${raiz}/docs/js/filtro-tabela.js"></script>
</body>
</html>
