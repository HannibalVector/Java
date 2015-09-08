<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Glasanje za omiljeni bend</title>
        <link rel="stylesheet" type="text/css" href="CSS.jsp" />
        <style type="text/css">
            table.rez td {text-align: center;}
        </style>
    </head>
    <body>
        <h1>Rezultati glasanja</h1>
        <p>Ovo su rezultati glasanja.</p>
        <table border="1" cellspacing="0" class="rez">
            <thead><tr><th>Bend</th><th>Broj glasova</th></tr></thead>
            <tbody>
                <c:forEach var="artist" items="${artists}" varStatus = "status">
                <tr><td>${artist.getName()}</td><td>${artist.getVotes()}</td></tr>
                </c:forEach>
            </tbody>
        </table>

        <h2>Grafički prikaz rezultata</h2>
        <img alt="Pie-chart" src="glasanje-grafika" />

        <h2>Rezultati u XLS formatu</h2>
        <p>Rezultati u XLS formatu dostupni su <a href="glasanje-xls">ovdje</a>.</p>

        <h2>Razno</h2>
        <p>Primjeri pjesama pobjedničkih bendova:</p>
        <ul>
        <c:forEach var="artist" items="${artists}" varStatus = "status">
            <c:if test="${artist.getVotes() == artists.get(0).getVotes()}">
            <li><a href = "${artist.getLinkToSong()}"}>${artist.getName()}</a></li>
            </c:if>
        </c:forEach>
        </ul>
    </body>
</html>