<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <title>Créer votre groupe</title>
    </head>

    <body>

        <%@ include file="menu.jsp" %>
        
        <h1>Créer votre groupe</h1>

        <form action="creer_groupe" method="post">
            <label for="nom_groupe">Nom de votre groupe :</label>
            <input type="text" id="nom_groupe" name="nom_groupe" required>
            <br>
            <label for="description_groupe">Description de votre groupe :</label>
            <input type="text" id="description_groupe" name="description_groupe" required>
            <br>
            <input type="submit" value="Envoyer">
        </form>

    </body>

    </html>