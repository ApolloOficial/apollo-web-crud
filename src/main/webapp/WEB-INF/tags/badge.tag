<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ attribute name="valor" required="false" type="java.lang.Object" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="chave"><c:out value="${valor}"/></c:set>
<c:choose>
    <c:when test="${chave == 'ATIVO'}"><span class="badge badge-ativo">Ativo</span></c:when>
    <c:when test="${chave == 'INATIVO'}"><span class="badge badge-inativo">Inativo</span></c:when>
    <c:when test="${chave == 'CANCELADO'}"><span class="badge badge-cancelado">Cancelado</span></c:when>
    <c:when test="${chave == 'ABERTA'}"><span class="badge badge-aberta">Aberta</span></c:when>
    <c:when test="${chave == 'EM_ANDAMENTO'}"><span class="badge badge-em-andamento badge-andamento">Em andamento</span></c:when>
    <c:when test="${chave == 'CONCLUIDA'}"><span class="badge badge-concluida">Concluída</span></c:when>
    <c:when test="${chave == 'CANCELADA'}"><span class="badge badge-cancelada">Cancelada</span></c:when>
    <c:when test="${chave == 'PREVENTIVA'}"><span class="badge badge-preventiva">Preventiva</span></c:when>
    <c:when test="${chave == 'CORRETIVA'}"><span class="badge badge-corretiva">Corretiva</span></c:when>
    <c:when test="${chave == 'PREDITIVA'}"><span class="badge badge-preditiva">Preditiva</span></c:when>
    <c:otherwise><span class="badge"><c:out value="${chave}" default="—"/></span></c:otherwise>
</c:choose>
