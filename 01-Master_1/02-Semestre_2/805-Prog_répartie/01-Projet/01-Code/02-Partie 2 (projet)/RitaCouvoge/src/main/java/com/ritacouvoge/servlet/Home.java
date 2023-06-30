package com.ritacouvoge.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Home extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Integer id_utilisateur = (Integer) session.getAttribute("Id_utilisateur");


        if (id_utilisateur != null) {
            req.setAttribute("pseudo", Variables_globales.g_utilisateur.get_pseudonyme(id_utilisateur) );
            req.setAttribute("nbr_fois_passager", Variables_globales.g_utilisateur.get_nbr_de_fois_passager(id_utilisateur) );
            req.setAttribute("nbr_fois_conducteur", Variables_globales.g_utilisateur.get_nbr_conduite(id_utilisateur) );
            req.setAttribute("prenom", Variables_globales.g_utilisateur.get_prenom_utilisateur(id_utilisateur) );
            req.setAttribute("nom", Variables_globales.g_utilisateur.get_nom_utilisateur(id_utilisateur) );
            req.setAttribute("date_de_naissance", Variables_globales.g_utilisateur.get_date_naissance(id_utilisateur) );
            req.setAttribute("groupes", Variables_globales.g_utilisateur.get_groupe(id_utilisateur) );
            req.setAttribute("admin", Variables_globales.g_utilisateur.get_administrateur(id_utilisateur) );
            req.setAttribute("bool_conducteur", Variables_globales.g_utilisateur.get_est_conducteur(id_utilisateur) );
            req.setAttribute("bool_passager", Variables_globales.g_utilisateur.get_est_passager(id_utilisateur) );
            
            
            
            
            req.getRequestDispatcher("/home.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/connexion.jsp").forward(req, resp);
        }

    }
}
