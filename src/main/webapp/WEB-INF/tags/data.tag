<%@ tag body-content="empty" pageEncoding="UTF-8" %>
<%@ attribute name="valor" required="false" type="java.lang.Object" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:choose>
    <c:when test="${empty valor}">—</c:when>
    <c:otherwise>
        <fmt:parseDate value="${valor}" pattern="yyyy-MM-dd" var="dataLida"/>
        <fmt:formatDate value="${dataLida}" pattern="dd/MM/yyyy"/>
    </c:otherwise>
</c:choose>
