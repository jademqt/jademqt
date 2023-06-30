<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>Inscription à un rendez-vous </title>
</head>
<body>
    <%@ include file="menu.jsp" %>
    <p> ${test}</p>
    <h1>Incription</h1>
    <h2>Information du rendez-vous sélectionné :</h2>
    <h3>Départ :</h3>
    <p>Ville : ${lieu_depart_ville}, ${lieu_depart_code}</p>
    <p>Adresse : ${lieu_depart_adresse}</p>
    <p>Description : ${lieu_depart_descri}</p>
    <p>Heure : ${heure_dep}</p>

    <h3>Arrivée :</h3>
    <p>Ville : ${lieu_arrivee_ville}, ${lieu_arrivee_code}</p>
    <p>Adresse : ${lieu_arrivee_adresse}</p>
    <p>Description : ${lieu_arrivee_descri}</p>
    <p>Heure : ${heure_arr}</p>
    
    <h2>Inscription</h2>
    <form action="rdv_inscription" method="post">
        <label for="role">Souhaitez-vous vous inscrire en tant que :</label>
        <select name="role" id="role">
          <option value="conducteur">Conducteur</option>
          <option value="passager">Passager</option>
        </select>
        </br>
        <label for="jours">Quels jours ?</label>
        <br>
        <input type="checkbox" name="jours" value="lundi" id="lundi">
        <label for="lundi">Lundi</label>
      
        <input type="checkbox" name="jours" value="mardi" id="mardi">
        <label for="mardi">Mardi</label>
      
        <input type="checkbox" name="jours" value="mercredi" id="mercredi">
        <label for="mercredi">Mercredi</label>
      
        <input type="checkbox" name="jours" value="jeudi" id="jeudi">
        <label for="jeudi">Jeudi</label>
      
        <input type="checkbox" name="jours" value="vendredi" id="vendredi">
        <label for="vendredi">Vendredi</label>
      
        <input type="checkbox" name="jours" value="samedi" id="samedi">
        <label for="samedi">Samedi</label>
      
        <input type="checkbox" name="jours" value="dimanche" id="dimanche">
        <label for="dimanche">Dimanche</label>
      
        <br><br>
        <input type="submit" value="Valider">
      </form>
      
</body>
</html>