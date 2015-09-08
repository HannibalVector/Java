<%@ page import="java.util.Random" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%!
String[] colors = new String[]{"orange", "blue", "yellow", "black", "magenta", "#123456"};
Random rand = new Random();
%>

<html>
<head>
<title>Some funny story</title>
<link rel="stylesheet" type="text/css" href="../CSS.jsp" />
<style>
body {
    width: 400px;
    margin: auto;
}
p {
    color: <%= colors[rand.nextInt(colors.length)] %>;
}
</style>
</head>
<body>

<p>On the day of my big job interview I woke up late. Frantically I threw on a suit. &#8220;OH NO!&#8221; I thought. &#8220;MY TIE! My Dad was out of town and wasn&#8217;t there to help me, and for the life of me I did not know how to tie a tie!</p>
<p>I grabbed a tie and ran out the door. &#8220;Excuse me sir,&#8221; I said to the crossing guard, &#8220;I have an important job interview, can you please help me make this tie?!&#8221;</p>
<p>&#8220;Sure&#8221; said the guard, &#8220;just lie down on this bench.&#8221; Well if someone was going to help me I wasn&#8217;t going to ask any questions. After he finished and the tie looked good I just had to ask why I had to lie down.</p>
<p>&#8220;Well in my previous job I learned how to tie tie&#8217;s on other people when they were lying down. He replied.</p>
<p>What was your previous job? I asked incredulously.</p>
<p>&#8220;I ran a morg.&#8221; Was the reply.</p>

</body>
</html>