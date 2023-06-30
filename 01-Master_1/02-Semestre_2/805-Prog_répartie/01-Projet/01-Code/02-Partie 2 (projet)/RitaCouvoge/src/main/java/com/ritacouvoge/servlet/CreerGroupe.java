package com.ritacouvoge.servlet;

import java.io.IOException;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreerGroupe extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/creer_groupe.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int Id_groupe = Variables_globales.g_groupe.prochain_id_a_utiliser();
        String Nom_groupe = request.getParameter("nom_groupe");
        String Descriptif_groupe = request.getParameter("description_groupe");
        int Id_administrateur = Integer.parseInt(request.getSession().getAttribute("Id_utilisateur").toString());
        
        Vector<Integer> Liste_conducteurs = new Vector<>();
        Liste_conducteurs.add(0);
        Vector<Integer> Liste_passagers = new Vector<>();
        Liste_passagers.add(0);
        Vector<Integer> Tab_trajets = new Vector<>();
        Tab_trajets.add(0);
        Vector<Integer> Liste_demandes = new Vector<>();
        Liste_demandes.add(0);
        
        // On créé le nouvel utilisateur
        Variables_globales.g_groupe.add_groupe(Id_groupe, Nom_groupe, Descriptif_groupe, Id_administrateur, Liste_conducteurs, Liste_passagers, Tab_trajets, Liste_demandes);
        Variables_globales.g_utilisateur.add_id_groupe_a_admin(Id_administrateur, Id_groupe);
        Variables_globales.g_utilisateur.add_id_groupe_a_groupe(Id_administrateur, Id_groupe);

        Variables_globales.g_groupe.updateXML();
        Variables_globales.g_utilisateur.updateXML();

        // Rediriger l'utilisateur vers une autre page après le traitement

        response.sendRedirect(request.getContextPath() + "/groupe");
    }

}
