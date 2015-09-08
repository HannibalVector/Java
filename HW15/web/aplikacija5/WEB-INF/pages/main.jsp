<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% String attemptedNick = (String)request.getAttribute("attemptedNick"); %>

<html>
    <head>
        <title>Main page</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
    </head>
    <body>
    <jsp:include page="header.jsp" />
    <div class="contents">

    <h1>Main page</h1>

  <c:choose>
    <c:when test="<%= session.getAttribute(\"current.user.id\")==null %>">
      <h2>Login</h2>
          <form action="main" method="POST" class="login">
          <p align="center">
              <input type="text" name="nick" value="${attemptedNick}" placeholder="Nickname">
              <input type="password" name="password" placeholder="Password">
              <input type="submit" name="action" value="Login">
              <input type="submit" name="action" value="Register">
          </p>
          </form>
    </c:when>
    <c:otherwise>
    <p>Hello, <%= session.getAttribute("current.user.firstName")%> <%= session.getAttribute("current.user.lastName")%>!</p>
    </c:otherwise>
  </c:choose>

    <h2>Contributing authors</h2>
    <c:choose>
        <c:when test="${authors==null}">
            Nema autora!
        </c:when>
        <c:otherwise>
            <ul>
                <c:forEach var="author" items="${authors}">
                    <li><a href="author/${author.nick}">${author.nick}</a></li>
                </c:forEach>
            </ul>
        </c:otherwise>
        </c:choose>
     </div>
    </body>
</html>