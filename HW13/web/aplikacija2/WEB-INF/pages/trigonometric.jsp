<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Table of trigonometric functions</title>
        <link rel="stylesheet" type="text/css" href="CSS.jsp" />
    </head>
    <body>
        <h1>Trigonometric functions</h1>
        <p>Here are the values of sines and cosines for requested angles.</p>
        <p>Table consists of ${results.size()} entries.</p>
        <table border = "1" cellSpacing = "0">
            <thead>
                <tr><th>Entry no.</th><th>x</th><th>sin(x)</th><th>cos(x)</th></tr>
            </thead>
            <tbody>
                <c:forEach var="entry" items="${results}" varStatus = "status">
                <tr>
                    <td>${status.index + 1}.</td>
                    <td>${entry.getAngle()}&deg;</td>
                    <td>${entry.getSine()}</td>
                    <td>${entry.getCosine()}</td>
                </tr>
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>