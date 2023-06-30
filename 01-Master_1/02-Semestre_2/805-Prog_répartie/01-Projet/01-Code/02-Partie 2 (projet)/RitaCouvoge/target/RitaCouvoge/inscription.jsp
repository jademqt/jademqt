<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ page import="java.util.Vector" %>
<%@ page import="com.ritacouvoge.servlet.Variables_globales" %>

<!DOCTYPE html>
<html>

<head>
    <title>Inscription </title>
</head>

<body>
    <%@ include file="menu.jsp" %>

        <p>Semaine affichée : ${semaineAffichee}</p>
        <p>Groupe affiché : ${groupeAffichee}</p>
    </br>
        <% Vector<Integer> joursSemaine = (Vector<Integer>) request.getAttribute("numerosJoursSemaine");%>

        <table>
            <tr>
                <th>Lundi <%= joursSemaine.get(0) %>
                </th>
                <th>Mardi <%= joursSemaine.get(1) %>
                </th>
                <th>Mercredi <%= joursSemaine.get(2) %>
                </th>
                <th>Jeudi <%= joursSemaine.get(3) %>
                </th>
                <th>Vendredi <%= joursSemaine.get(4) %>
                </th>
                <th>Samedi <%= joursSemaine.get(5) %>
                </th>
                <th>Dimanche <%= joursSemaine.get(6) %>
                </th>
            </tr>
            <tr>
                <td>to do</td>
                <td>to do</td>
                <td>to do</td>
                <td>to do</td>
                <td>to do</td>
                <td>to do</td>
                <td>to do</td>
            </tr>
        </table>
        </Integer>
    </br>

        <form method="POST" action="inscription">
            <label>Choix de la semaine à afficher :</label>
            <select name="semaine_affichage">
                <% Vector<Integer> vector_semaines = (Vector<Integer>)
                        request.getAttribute("vector_semaines");
                        for (Integer semaine : vector_semaines) {
                        %>
                        <option value="<%= semaine %>">
                            <%= semaine %>
                        </option>
                        <% } %>
            </select>
            </br>
            <label>Afficher les rendez-vous existants du groupe :</label>
            <select name="groupe_affichage">
                <% Vector<Integer> liste_groupe = (Vector<Integer>)
                        request.getAttribute("liste_groupe");
                        for (Integer groupe : liste_groupe) {
                        String nom_groupe = Variables_globales.g_groupe.get_nom_groupe(groupe);
                        %>
                        <option value="<%= groupe %>">
                            <%= nom_groupe %>
                        </option>
                        <% } %>
            </select>
            </br>
            <input type="submit" name="affichage" value="Afficher">
        </form>
    </br>
        <h2>Voici la liste des rendez-vous disponibles pour le groupe et la semaine sélectionnés :</h2>

        <ol>
        <%
        Vector<Integer> liste_lieu_depart = (Vector<Integer>) request.getAttribute("liste_lieu_depart");
        Vector<Integer> liste_lieu_arrivee = (Vector<Integer>) request.getAttribute("liste_lieu_arrivee");

        int size = liste_lieu_depart.size();
        for (int i = 0; i < size; i++) {
            if (liste_lieu_depart.get(i) != 0) {
                String lieu_depart = Variables_globales.g_lieu.get_description(liste_lieu_depart.get(i));
                String lieu_arrivee = Variables_globales.g_lieu.get_description(liste_lieu_arrivee.get(i));
        %>
            <li>
                Départ : <%= lieu_depart %> - Arrivée : <%= lieu_arrivee %>
                <form action="inscription" method="post">
                    <input type="hidden" name="index" value="<%= i %>" />
                    <input type="submit" name="inscription" value="Ca m'interesse" />
                </form>
            </li>
            </br>
        <% 
            }
        }
        %>
        </ol>
    </br>
        <h2> Créer un autre rendez-vous</h2>
</body>

</html>