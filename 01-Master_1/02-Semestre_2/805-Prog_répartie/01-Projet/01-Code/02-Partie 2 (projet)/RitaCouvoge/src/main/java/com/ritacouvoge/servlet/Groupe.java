package com.ritacouvoge.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Groupe extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Integer id_utilisateur = (Integer) session.getAttribute("Id_utilisateur");
        
        if (id_utilisateur != null) {
            req.setAttribute("groupes", Variables_globales.g_utilisateur.get_groupe(id_utilisateur) );
            req.setAttribute("admin", Variables_globales.g_utilisateur.get_administrateur(id_utilisateur) );
            req.getRequestDispatcher("/groupe.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/connexion.jsp").forward(req, resp);
        }

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String creer_groupe = request.getParameter("creer_groupe");
        String rejoindre_groupe = request.getParameter("rejoindre_groupe");
        
        if (creer_groupe != null) {
            response.sendRedirect(request.getContextPath() + "/creer_groupe");
        }
        if (rejoindre_groupe != null) {
            response.sendRedirect(request.getContextPath() + "/rejoindre_groupe");
        }

    }
}
