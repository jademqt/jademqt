package com.ritacouvoge.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Connexion extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/connexion.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pseudo = request.getParameter("pseudo");
        String password = request.getParameter("password");
        String creer_compte = request.getParameter("creer_compte");
        String formulaire = request.getParameter("formulaire");

        int id_utilisateur = 0;
        if (formulaire != null) {
            // Vérifier les informations de connexion et effectuer les actions appropriées
            boolean pseudoEtMdpOk = false;
            for (Utilisateur_srv.Utilisateur utilisateur : Variables_globales.g_utilisateur.hashUtilisateur.values()) {
                if (utilisateur.Pseudonyme.equals(pseudo)) {
                    if (utilisateur.Mot_de_Passe.equals(password)) {
                        pseudoEtMdpOk = true;
                        id_utilisateur = utilisateur.Id_utilisateur;
                        break;
                    }
                }
            }

            if (pseudoEtMdpOk) {
                HttpSession session = request.getSession();
                session.setAttribute("Id_utilisateur", id_utilisateur);
                response.sendRedirect(request.getContextPath() + "/home");
            } else {
                request.setAttribute("hasError", true);
                request.getRequestDispatcher("/connexion.jsp").forward(request, response);
            }
        } else if (creer_compte != null) {
            response.sendRedirect(request.getContextPath() + "/creer_compte");
        }

    }

}
