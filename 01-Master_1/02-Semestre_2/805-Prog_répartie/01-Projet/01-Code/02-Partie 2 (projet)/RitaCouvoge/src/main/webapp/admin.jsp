<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.lang.String" %>
<%@ page import="com.ritacouvoge.servlet.Variables_globales" %>

<!DOCTYPE html>
<html>
<head>
    <title>Page d'administration</title>
</head>
<body>
    <%@ include file="menu.jsp" %>

    <h1>Page d'administration</h1>
    <div>
        <% Vector<Integer> groupes = (Vector<Integer>) request.getAttribute("groupes");
        if (groupes != null && groupes.size() > 0) { %>
            <h2>Voici la liste des groupes où vous êtes administrateur :</h2>
            <table>
                <tr>
                    <th>Nom du groupe</th>
                    <th>ID du groupe</th>
                    <th>Demande d'ajout au groupe</th>
                </tr>
                <% for (int groupe : groupes) {
                    if (groupe != 0) { 
                        String nom_groupe = Variables_globales.g_groupe.get_nom_groupe(groupe); 
                        int id_groupe = groupe; %>
                        <tr>
                            <td><%= nom_groupe %></td>
                            <td><%= id_groupe %></td>
                            <td>
                                <% Vector<Integer> demandes = Variables_globales.g_groupe.get_liste_demandes(groupe);
                                    if (demandes.size() > 1) {
                                        for (int utilisateur : demandes) {
                                            if (utilisateur != 0) {
                                                String prenom = Variables_globales.g_utilisateur.get_prenom_utilisateur(utilisateur);
                                                String nom = Variables_globales.g_utilisateur.get_nom_utilisateur(utilisateur);
                                    %>
                                    
                                    <%= prenom %>
                                    <%= nom %>
                                    <form action="admin" method="post">
                                        <fieldset>
                                            <legend>Voulez-vous accepter cette personne ?</legend>
                                            <input type="radio" name="accepter" value="accepter">Accepter<br>
                                            <input type="radio" name="refuser" value="refuser">Refuser<br>
                                            <input type="hidden" name="id_utilisateur" value="<%= utilisateur %>">
                                            <input type="hidden" name="id_groupe" value="<%= groupe %>">
                                            <br>
                                            <input type="submit" name="choix_utilisateur" value="Envoyer">
                                        </fieldset>        
                                    </form>
                                    </br>                                           
                                <% } } }%>
                                <form action="admin" method="post">
                                    <fieldset>
                                        <input type="hidden" name="id_groupe" value="<%= groupe %>">
                                        <input type="submit" name="gerer_repartition" value="Gérer la répartition des conduites">
                                    </fieldset>        
                                </form> 
                                                               
                            </td>
                        </tr>
                <% } } %>
            </table>
        <% } else { %>
            <p>Vous n'avez pas de groupe à administrer.</p>
        <% } %>
    </div>

</body>
</html>
