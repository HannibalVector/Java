<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Ankete</title>
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>
    <body>
        <h1>Ankete</h1>
        <p>Klikom na link odaberite željenu anketu!</p>
        <ol class="ankete">
        <c:forEach var="poll" items="${polls}">
            <li><a href = "glasanje?pollID=${poll.getID()}">${poll.getTitle()}</a></li>
        </c:forEach>
        </ol>
    </body>
</html>