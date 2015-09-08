<%@ page contentType="text/css; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

body {
    background:
    <%
        String bgColor = (String) session.getAttribute("pickedBgCol");
        if (bgColor == null) {
            out.print("white");
        } else {
            out.print(bgColor);
        }
    %>;
}