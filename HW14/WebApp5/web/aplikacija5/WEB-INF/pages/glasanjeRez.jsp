<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>${selectedPoll.getTitle()}</title>
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>
    <body>
        <h1>${selectedPoll.getTitle()}</h1>
        <h2>Rezultati glasanja</h2>
        <p>Ovo su rezultati glasanja.</p>
        <table>
            <thead><tr><th>Opcija</th><th>Broj glasova</th></tr></thead>
            <tbody>
                <c:forEach var="option" items="${results}">
                <tr><td>${option.getName()}</td><td>${option.getVotes()}</td></tr>
                </c:forEach>
            </tbody>
        </table>

        <h2>Grafički prikaz rezultata</h2>
        <img alt="Pie-chart" src="glasanje-grafika?pollID=${selectedPoll.getID()}" />

        <h2>Rezultati u XLS formatu</h2>
        <p>Rezultati u XLS formatu dostupni su <a href="glasanje-xls?pollID=${selectedPoll.getID()}">ovdje</a>.</p>

        <h2>Razno</h2>
        <p>Više podataka o pobjedničkoj opciji:</p>
        <ul class="results">
        <c:forEach var="option" items="${results}">
            <c:if test="${option.getVotes() == results.get(0).getVotes()}">
            <li><a href = "${option.getLink()}"}>${option.getName()}</a></li>
            </c:if>
        </c:forEach>
        </ul>
    </body>
</html>