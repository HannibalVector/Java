<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="header">
<a class="home" href="${pageContext.request.contextPath}">home</a>
  <c:choose>
    <c:when test="<%= session.getAttribute(\"current.user.id\")==null %>">
    You are currently not logged in.
    </c:when>
    <c:otherwise>
    Logged in as <%= session.getAttribute("current.user.firstName")%> <%= session.getAttribute("current.user.lastName")%>.
    (<a href="${pageContext.request.contextPath}/servleti/logout">logout</a>)
    </c:otherwise>
  </c:choose>
</div>