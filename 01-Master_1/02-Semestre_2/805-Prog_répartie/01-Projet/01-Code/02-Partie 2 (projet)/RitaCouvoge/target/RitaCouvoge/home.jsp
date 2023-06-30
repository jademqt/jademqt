<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
    <%@ page import="java.util.Vector" %>

        <!DOCTYPE html>
        <html>

        <head>
            <title>Ma page</title>
        </head>

        <body>
            
            <%@ include file="menu.jsp" %>
            
            <h1>Bienvenue ${pseudo}</h1>
            <div>
                <h2>Statistiques</h2>
                <p>Vous avez été passager.e ${nbr_fois_passager} fois.</p>
                <p>Vous avez conduis ${nbr_fois_conducteur} fois.</p>
            </div>
            <div>
                <h2>Mes informations</h2>
                <p>Prénom : ${prenom}</p>
                <p>Nom : ${nom}</p>
                <p>Date de naissance : ${date_de_naissance}</p>
                <% 
                boolean _bool_passager = (boolean) request.getAttribute("bool_passager");
                if (_bool_passager) { 
                %>
                    <p>Vous êtes inscrit.e comme passager.e.</p>
                <% } %>
                <% 
                boolean _bool_conducteur = (boolean) request.getAttribute("bool_conducteur");
                if (_bool_conducteur) { 
                %>
                    <p>Vous êtes inscrit.e comme driver.</p>
                <% } %>

            </div>
            <div>
                <h2>Mes groupes </h2>
                <ul>
                    <% 
                    Vector<Integer> groupes = (Vector<Integer>) request.getAttribute("groupes");
                    if (groupes != null && groupes.size() > 1) {
                        for (int groupe : groupes) {
                            if (groupe != 0) {
                    %>
                            <li><%= groupe %></li>
                    <% 
                            }
                        }
                    } else {
                    %>
                        <p>Vous n'appartenez actuellement à aucun groupe.</p>
                    <% } %>
                </ul>
                

            </div>
            <div>
                <h2>Vous êtes administrateur.trice ...</h2>
                <ul>
                    <% 
                    Vector<Integer> administrateur = (Vector<Integer>) request.getAttribute("admin");
                    if (administrateur != null && administrateur.size() > 1) {
                        for (int ad : administrateur) {
                            if (ad != 0) {
                    %>
                            <li><%= ad %></li>
                    <% 
                            }
                        }
                    } else {
                    %>
                        <p>Vous n'administrez pour le moment aucun groupe.</p>
                    <% } %>
                </ul>
            </div>

        </body>

        </html>