<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <title>Rejoindre un groupe</title>
    </head>

    <body>
        <%@ include file="menu.jsp" %>
        
        <h1>Rejoindre un groupe</h1>
        <p>Pour une raison de sécurité la liste des groupes n'est pas accessible. 
            Vous pouvez rejoindre un groupe qu'en connaissance de son ID.
            Contactez l'administrateur du groupe pour récupérer cet ID.
        </p>
        <form action="rejoindre_groupe" method="post">
            <label for="id_groupe">Nom du groupe :</label>
            <input type="number" id="id_groupe" name="id_groupe" value="11000" required>
            <br>
            <input type="submit" value="Envoyer">            
        </form>

        <p><%= request.getAttribute("reponse_donnee") %></p>

    </body>

    </html>