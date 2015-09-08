<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Glasanje za omiljeni bend</title>
        <link rel="stylesheet" type="text/css" href="CSS.jsp" />
    </head>
    <body>
        <h1>Glasanje za omiljeni bend</h1>
        <p>Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!</p>
        <ol>
        <c:forEach var="artist" items="${artists}" varStatus = "status">
            <li><a href = "glasanje-glasaj?id=${status.index + 1}">${artist.getName()}</a></li>
        </c:forEach>
        </ol>
    </body>
</html>