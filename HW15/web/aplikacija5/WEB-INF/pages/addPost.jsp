<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>New post</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
    </head>
    <body>
    <jsp:include page="header.jsp" />
    <div class="contents">
        <h1>New post</h1>

        <form action="../../add-post" method="POST" id="newPost">

        <p>Post title</p>
        <input type="text" name="title" size="100"><br>

        <p>Post text</p>
        <textarea rows="10" cols="100" name="text" form="newPost"></textarea><br>

        <input type="hidden" name="nick" value="${author.nick}">

        <input type="submit" value="Submit">

        </form>

</div>
    </body>
</html>