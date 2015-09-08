<%@ page import="java.time.Duration" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
<title>Application Info</title>
<link rel="stylesheet" type="text/css" href="CSS.jsp" />
</head>
<body>

<h1>Application Info</h1>
<p>Application running time is:
<%
    long difference = System.currentTimeMillis() - (long)application.getAttribute("startTime");
    Duration duration = Duration.ofMillis(difference);
    String runningTime = String.format("%d days %d hours %d minutes %d seconds and %d milliseconds.",
            duration.toDays(), duration.toHours()%24, duration.toMinutes()%60,
            duration.getSeconds()%60, duration.toMillis()%1000);
    out.print(runningTime);
%>
</p>

</body>
</html>