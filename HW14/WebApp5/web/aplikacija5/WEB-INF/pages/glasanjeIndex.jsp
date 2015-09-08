<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>${selectedPoll.getTitle()}</title>
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>
    <body>
        <h1>${selectedPoll.getTitle()}</h1>
        <p>${selectedPoll.getMessage()}</p>

        <form action="glasanje-glasaj" method="POST">
        <ul class="opcije">
        <c:forEach var="option" items="${selectedPoll.getPollOptions()}" varStatus = "status">
        <li><input type="radio" name="optionIndex" value="${status.index}" checked>${option.getName()}</li>
        </c:forEach>
        </ul>
        <input type="submit" value="Glasaj">
        <input type="hidden" name="pollID" value="${selectedPoll.getID()}">
        </form>
    </body>
</html>