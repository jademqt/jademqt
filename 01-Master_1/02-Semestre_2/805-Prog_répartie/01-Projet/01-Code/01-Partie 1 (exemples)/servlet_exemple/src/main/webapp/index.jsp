<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Météo du jour</title>
</head>
<body>
    <h1>Voici la météo du jour :</h1>
    <p><%= request.getAttribute("weatherData") %></p>
</body>
</html>
