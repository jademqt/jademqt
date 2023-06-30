<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
    <%@ page import="java.util.Vector" %>
        <%@ page import="com.ritacouvoge.servlet.Variables_globales" %>

            <!DOCTYPE html>
            <html>

            <head>
                <title>Ma page</title>
            </head>

            <body>
                <%@ include file="menu.jsp" %>

                    <h1>Groupe</h1>
                    <div>
                        <h2>Liste des groupes auxquels vous appartenez :</h2>
                        <% Vector<Integer> groupes = (Vector<Integer>) request.getAttribute("groupes");
                                if (groupes != null && groupes.size() > 0) {
                                %>
                                <table>
                                    <tr>
                                        <th>Nom du groupe</th>
                                        <th>Descriptif du groupe</th>
                                    </tr>
                                    <% for (int groupe : groupes) { if (groupe !=0) { String
                                        nom_groupe=Variables_globales.g_groupe.get_nom_groupe(groupe); String
                                        descriptif_groupe=Variables_globales.g_groupe.get_descriptif_groupe(groupe); %>
                                        <tr>
                                            <td>
                                                <%= nom_groupe %>
                                            </td>
                                            <td>
                                                <%= descriptif_groupe %>
                                            </td>
                                        </tr>
                                        <% } } %>
                                </table>
                                <% } else { %>
                                    <p>Vous n'appartenez actuellement à aucun groupe.</p>
                                    <% } %>
                    </div>

                    <div>
                        <h2>Liste des groupes où vous êtes administrateur.trice :</h2>
                        <% Vector<Integer> administrateur = (Vector<Integer>) request.getAttribute("admin");
                                if (administrateur != null && administrateur.size() > 0) {
                                %>
                                <table>
                                    <tr>
                                        <th>Nom de votre groupe</th>
                                        <th>Nombre de conducteur</th>
                                        <th>Nombre de passager</th>
                                    </tr>
                                    <% for (int ad : administrateur) { if (ad !=0) { String
                                        nom_groupe=Variables_globales.g_groupe.get_nom_groupe(ad); int
                                        nbr_conducteur=Variables_globales.g_groupe.get_liste_conducteurs(ad).size() - 1; int
                                        nbr_passager=Variables_globales.g_groupe.get_liste_passagers(ad).size() - 1; %>
                                        <tr>
                                            <td>
                                                <%= nom_groupe %>
                                            </td>
                                            <td>
                                                <%= nbr_conducteur %>
                                            </td>
                                            <td>
                                                <%= nbr_passager %>
                                            </td>
                                        </tr>
                                        <% } } %>
                                </table>
                                <% } else { %>
                                    <p>Vous n'êtes pas administrateur.</p>
                                    <% } %>
                    </div>

                    <div>
                        <form action="groupe" method="post">
                            <br>
                            <input type="submit" name="creer_groupe" value="Créer un groupe">
                            <input type="submit" name="rejoindre_groupe" value="Rejoindre un groupe">
                        </form>
                    </div>



            </body>

            </html>