<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Edit: ${post.title}</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
    </head>
    <body>
    <jsp:include page="header.jsp" />
    <div class="contents">
        <h1>Edit: ${post.title}</h1>

        <form action="../../edit-post" method="POST" id="editPost">

        <p>Post title</p>
        <input type="text" name="title" size="100" value="${post.title}"><br>

        <p>Post text</p>
        <textarea rows="10" cols="100" name="text" form="editPost">${post.text}</textarea><br>

        <input type="hidden" name="id" value="${post.id}">

        <input type="submit" value="Submit">

        </form>

</div>
    </body>
</html>