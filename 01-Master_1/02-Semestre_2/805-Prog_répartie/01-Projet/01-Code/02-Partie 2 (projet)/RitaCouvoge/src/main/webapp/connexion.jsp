<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Connexion</title>
</head>
<body>
    <h1>Connexion</h1>
    <% if ((request.getAttribute("hasError") != null) && ((boolean) request.getAttribute("hasError") )){ %>
        <script> alert('Votre pseudo n a pas été trouvé ou votre mot de passe n est pas valide.'); </script>
    <% } %>
    <form action="connexion" method="post">
        <label for="username">Votre pseudo :</label>
        <input type="text" id="username" name="pseudo" required>
        <br>
        <label for="password">Mot de passe :</label>
        <input type="password" id="password" name="password" required>
        <br>
        <input type="submit" name="formulaire" value="Se connecter">
    </form>
    
    <form action="connexion" method="post">
        <br>
        <input type="submit" name="creer_compte" value="Créer un compte">
    </form>

</body>
</html>
