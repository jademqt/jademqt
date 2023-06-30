<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% String nom_groupe = (String) request.getAttribute("nom_groupe"); %>
<!DOCTYPE html>
<html>
<head>
    <title>Gestion du groupe <%= nom_groupe %> </title>
</head>
<body>
    <%@ include file="menu.jsp" %>

    <h1>Gestion du groupe <%= nom_groupe %></h1>
    
</body>
</html>