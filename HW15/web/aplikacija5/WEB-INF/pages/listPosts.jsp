<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Posts by ${author.nick}</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
    </head>
    <body>
    <jsp:include page="header.jsp" />
    <div class="contents">

    <h1>Posts by ${author.nick}</h1>

     <div class="posts">

         <c:choose>
             <c:when test="${posts==null}">
               No posts!
             </c:when>
             <c:otherwise>
               <ul>
               <c:forEach var="post" items="${posts}">
                 <li><a href="${author.nick}/${post.id}">${post.title}</a></li>
               </c:forEach>
               </ul>
             </c:otherwise>
           </c:choose>
     </div>

     <c:if test="${editingAllowed}">
        <a href="${author.nick}/new">New post</a>
     </c:if>

</div>
    </body>
</html>