<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String attemptedFirstName = (String)request.getAttribute("attemptedFirstName");
    String attemptedLastName = (String)request.getAttribute("attemptedLastName");
    String attemptedEmail = (String)request.getAttribute("attemptedEmail");
    String attemptedNick = (String)request.getAttribute("attemptedNick");
%>

<html>
    <head>
        <title>Register</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
    </head>
    <body>
    <jsp:include page="header.jsp" />
    <div class="contents">
        <h1>Register</h1>
        <p>Please fill in all fields.</p>

        <form action="complete-registration" method="POST">

        <p>First name<br><input type="text" name="firstName" value="${attemptedFirstName}"></p>
        <p>Last name<br><input type="text" name="lastName" value="${attemptedLastName}"><p>
        <p>e-mail<br><input type="text" name="email" value="${attemptedEmail}"><p>
        <p>Nickname<br><input type="text" name="nick" value="${attemptedNick}"><p>
        <p>Password<br><input type="password" name="password"><p>
        <p><input type="submit" value="Submit"></p>
        </form>
    </div>
    </body>
</html>