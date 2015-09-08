<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>${post.title}</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
    </head>
    <body>
    <jsp:include page="header.jsp" />
    <div class="contents">


    <p>${post.createdAt}</p>
    <h1>${post.title}</h1>
    <p>author: <b>${post.creator.nick}</b></p>

    <c:if test="${editingAllowed}">
                 <a href="edit?id=${post.id}">Edit post</a>
    </c:if>

     <div class="post">



     <div class="text">${post.text}</div>
     <c:if test="${post.lastModifiedAt!=null}">
     <p>last modified: ${post.lastModifiedAt}</p>
     </c:if>
     </div>


     <c:if test="${!post.comments.isEmpty()}">
    <h2>Comments</h2>
    <div id="comments">
           <c:forEach var="comment" items="${post.comments}">
           <div class="comment">
             <p><a href="mailto:${comment.author.email}">${comment.author.nick}</a> (<c:out value="${comment.postedOn}"/>)</p>
             <div class="commentBody"><c:out value="${comment.message}"/></div>
           </div>
           </c:forEach>
    </div>

     </c:if>

     <h2>Add comment</h2>
     <form action="../../add-comment" method="POST" id="newComment">
             <textarea rows="10" cols="40" name="message" form="newComment"></textarea><br>
             <input type="hidden" name="id" value="${post.id}">
             <input type="submit" value="Submit">
     </form>

    </div>
    </body>
</html>