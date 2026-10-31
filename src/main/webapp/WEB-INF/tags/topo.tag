<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ attribute name="titulo" required="true" %>
<%@ attribute name="subtitulo" required="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="raizTopo" value="${pageContext.request.contextPath}"/>
<header class="topo">
    <div>
        <h1 class="titulo"><c:out value="${titulo}"/></h1>
        <jsp:doBody/>
        <c:if test="${not empty subtitulo}"><p class="subtitulo-pagina"><c:out value="${subtitulo}"/></p></c:if>
    </div>
    <a class="usuario" href="${raizTopo}/perfil">
        <span class="avatar-usuario" aria-hidden="true"><c:out value="${usuarioLogado.iniciais}"/></span>
        <span><c:out value="${usuarioLogado.nome}"/></span>
    </a>
</header>
