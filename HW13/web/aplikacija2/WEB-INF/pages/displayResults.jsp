<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Trigonometric functions</title>
        <link rel="stylesheet" type="text/css" href="CSS.jsp" />
    </head>
    <body>
        <h1>List of values of sines and cosines for requested angles.</h1>
        <p>List consists of ${results.size()} entries.</p>
        <table border = "1" cellSpacing = "0">
            <thead>
                <tr><th>Entry no.</th><th>x</th><th>sin(x)</th><th>cos(x)</th></tr>
            </thead>
            <tbody>
                <c:forEach var="record" items="${results}" varStatus = "status">
                <tr><td>${status.index + 1}.</td><td>${record.angle}&deg;</td><td>${record.getSine()}</td><td>${record.getCosine()}</td></tr>
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>