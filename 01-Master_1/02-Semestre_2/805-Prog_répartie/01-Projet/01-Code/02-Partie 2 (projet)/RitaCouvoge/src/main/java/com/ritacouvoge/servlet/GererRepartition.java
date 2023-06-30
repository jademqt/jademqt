package com.ritacouvoge.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GererRepartition extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String str_id_groupe = req.getParameter("id_groupe");
        int int_id_groupe = Integer.parseInt(str_id_groupe);
        String nom_groupe = Variables_globales.g_groupe.get_nom_groupe(int_id_groupe);
        req.setAttribute("nom_groupe", nom_groupe);
        req.getRequestDispatcher("/gerer_repartition.jsp").forward(req, resp);        
    }

}