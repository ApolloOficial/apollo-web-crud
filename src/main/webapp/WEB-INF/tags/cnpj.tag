<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ attribute name="valor" required="false" type="java.lang.Object" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="n" value="${valor}"/>
<c:choose>
    <c:when test="${empty n}">—</c:when>
    <c:when test="${fn:length(n) == 14}">${fn:substring(n,0,2)}.${fn:substring(n,2,5)}.${fn:substring(n,5,8)}/${fn:substring(n,8,12)}-${fn:substring(n,12,14)}</c:when>
    <c:otherwise><c:out value="${n}"/></c:otherwise>
</c:choose>
