<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ attribute name="valor" required="false" type="java.lang.Object" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="n" value="${valor}"/>
<c:choose>
    <c:when test="${empty n}">—</c:when>
    <c:when test="${fn:length(n) == 11}">(${fn:substring(n,0,2)}) ${fn:substring(n,2,7)}-${fn:substring(n,7,11)}</c:when>
    <c:when test="${fn:length(n) == 10}">(${fn:substring(n,0,2)}) ${fn:substring(n,2,6)}-${fn:substring(n,6,10)}</c:when>
    <c:otherwise><c:out value="${n}"/></c:otherwise>
</c:choose>
