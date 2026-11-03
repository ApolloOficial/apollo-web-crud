<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ attribute name="valor" required="false" type="java.lang.Object" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="n" value="${valor}"/>
<c:choose>
    <c:when test="${empty n}">—</c:when>
    <c:when test="${fn:length(n) == 11}">${fn:substring(n,0,3)}.${fn:substring(n,3,6)}.${fn:substring(n,6,9)}-${fn:substring(n,9,11)}</c:when>
    <c:otherwise><c:out value="${n}"/></c:otherwise>
</c:choose>
