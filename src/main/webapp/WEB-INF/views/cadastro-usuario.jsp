<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="ap" tagdir="/WEB-INF/tags" %>
<c:set var="raiz" value="${pageContext.request.contextPath}"/>
<c:set var="ativo" value="membros"/>
<c:set var="editando" value="${modo == 'editar'}"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apollo | <c:out value="${editando ? 'Editar membro' : 'Cadastrar membro'}"/></title>
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
        <ap:topo titulo="${editando ? 'Editar membro' : 'Cadastrar membro'}" subtitulo="${editando ? 'Atualize os dados do membro' : (usuarioLogado.perfil == 'GERENTE' ? 'O gerente cadastra técnicos' : 'O técnico cadastra operadores')}"/>

        <p class="alerta alerta-erro" id="aviso-senha" role="alert" hidden></p>

        <form class="cartao" id="form-usuario" action="${raiz}/${editando ? 'membros' : 'cadastro'}" method="post">
            <c:if test="${editando}">
                <input type="hidden" name="acao" value="editar">
                <input type="hidden" name="id" value="${item.id}">
            </c:if>

            <h2 class="form-secao">Dados pessoais</h2>
            <div class="form-grade">
                <label class="campo"><span>Nome completo *</span>
                    <input type="text" name="nome" maxlength="120" placeholder="Ex.: Mason Mount" required value="<c:out value='${item.nome}'/>"></label>
                <label class="campo"><span>Telefone</span>
                    <input type="text" name="telefone" data-mascara="telefone" inputmode="tel" placeholder="(11) 99999-9999" value="<c:out value='${item.telefone}'/>"></label>
                <label class="campo"><span>E-mail *</span>
                    <input type="email" name="email" maxlength="120" placeholder="nome@empresa.com" required value="<c:out value='${item.email}'/>"></label>
                <label class="campo"><span>CPF *</span>
                    <input type="text" name="cpf" data-mascara="cpf" inputmode="numeric" placeholder="000.000.000-00" required value="<c:out value='${item.cpf}'/>"></label>
                <label class="campo largo"><span>Link da foto</span>
                    <input type="text" name="urlFoto" maxlength="255" value="<c:out value='${item.urlFoto}'/>"></label>
            </div>

            <h2 class="form-secao">Acesso</h2>
            <div class="form-grade">
                <label class="campo"><span>Senha ${editando ? '' : '*'}</span>
                    <input type="password" id="senha" name="senha" autocomplete="new-password" placeholder="${editando ? 'Deixe em branco para manter a atual' : 'Digite a senha'}" ${editando ? '' : 'required'}></label>
                <label class="campo"><span>Confirmar senha ${editando ? '' : '*'}</span>
                    <input type="password" id="confirmar-senha" autocomplete="new-password" placeholder="Confirme a senha" ${editando ? '' : 'required'}></label>
            </div>

            <c:if test="${!editando}">
                <h2 class="form-secao">Filial</h2>
                <div class="form-grade">
                    <label class="campo"><span>Filial *</span>
                        <select name="filial" required>
                            <option value="">Selecione a filial</option>
                            <c:forEach var="f" items="${filiaisDisponiveis}">
                                <option value="${f.id}" ${item.idFilial == f.id ? 'selected' : ''}><c:out value="${f.nome}"/></option>
                            </c:forEach>
                        </select></label>
                    <label class="campo"><span>Perfil</span>
                        <input type="text" readonly value="${usuarioLogado.perfil == 'GERENTE' ? 'Técnico' : 'Operador'}"></label>
                </div>
            </c:if>

            <div class="form-rodape">
                <a class="btn btn-contorno" href="${raiz}/membros">Cancelar</a>
                <button class="btn" type="submit"><c:out value="${editando ? 'Salvar' : 'Cadastrar'}"/></button>
            </div>
        </form>
        <c:set var="erroAviso" value="${not empty ErroNoCadastro ? ErroNoCadastro : erroMembros}"/>
        <%@ include file="includes/aviso.jspf" %>
    </main>
</div>
<script src="${raiz}/docs/js/mascaras.js"></script>
<script src="${raiz}/docs/js/confirmar-senha.js"></script>
<script src="${raiz}/docs/js/aviso.js"></script>
</body>
</html>
