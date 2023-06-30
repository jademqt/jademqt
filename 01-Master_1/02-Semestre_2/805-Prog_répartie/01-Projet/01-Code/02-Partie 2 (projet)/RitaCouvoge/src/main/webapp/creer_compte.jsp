<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <title>Page d'accueil</title>
    </head>

    <body>
        <h1>Création de votre compte</h1>

        <form action="creer_compte" method="post">
            <label for="pseudo">Pseudonyme :</label>
            <input type="text" id="pseudo" name="pseudo" required>
            <br>
            <label for="password">Mot de passe :</label>
            <input type="password" id="password" name="password" required>
            <br>
            <label for="nom">Nom :</label>
            <input type="text" id="nom" name="nom" required>
            <br>
            <label for="prenom">Prénom :</label>
            <input type="text" id="prenom" name="prenom" required>
            <br>
            <label for="dateNaissance">Date de naissance :</label>
            <input type="text" id="dateNaissance" name="dateNaissance" placeholder="jj-mm-aaaa" required>
            <br>
            <label for="detientPermis">Possèdez-vous le permis de conduire ? </label>
            <input type="checkbox" id="detientPermis" name="detientPermis">
            <br>
            <label for="conducteur">Souhaitez-vous être parfois être le conducteur ?</label>
            <input type="checkbox" id="conducteur" name="conducteur">
            <br>
            <label for="nombrePlacesVoiture">Nombre de places disponnible dans votre voiture (sans la place conducteur):</label>
            <input type="number" id="nombrePlacesVoiture" name="nombrePlacesVoiture">
            <br>
            <label for="passager">Souhaitez-vous être parfois être un passager ?</label>
            <input type="checkbox" id="passager" name="passager">
            <br>
            <input type="submit" value="Envoyer">
        </form>

    </body>

    </html>