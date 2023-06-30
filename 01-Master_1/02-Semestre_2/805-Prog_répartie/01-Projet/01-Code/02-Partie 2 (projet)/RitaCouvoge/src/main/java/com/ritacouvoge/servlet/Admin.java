package com.ritacouvoge.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Admin extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id_utilisateur = Integer.parseInt(req.getSession().getAttribute("Id_utilisateur").toString());
        req.setAttribute("groupes", Variables_globales.g_utilisateur.get_groupe(id_utilisateur));
        req.getRequestDispatcher("/admin.jsp").forward(req, resp);        
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String choix_utilisateur = request.getParameter("choix_utilisateur");
        String gerer_repartition = request.getParameter("gerer_repartition");
        
        if(gerer_repartition != null){
            String id_groupe = request.getParameter("id_groupe");
            response.sendRedirect(request.getContextPath() + "/gerer_repartition?id_groupe=" + id_groupe);
        }

        if(choix_utilisateur != null){
            boolean accepter = request.getParameter("accepter") != null;
            boolean refuser = request.getParameter("refuser") != null;
            String id_utilisateur = request.getParameter("id_utilisateur");
            String id_groupe = request.getParameter("id_groupe");
            int int_id_utilisateur = Integer.parseInt(id_utilisateur);
            int int_id_groupe = Integer.parseInt(id_groupe);
            
            
            if (accepter) {
                //on vérifie si l'utilisateur est passager/conducteur
                boolean est_conducteur = Variables_globales.g_utilisateur.get_est_conducteur(int_id_utilisateur);
                boolean est_passager = Variables_globales.g_utilisateur.get_est_passager(int_id_utilisateur);

                if(est_conducteur){
                    Variables_globales.g_groupe.add_id_util_a_conducteurs(int_id_groupe, int_id_utilisateur);
                }
                if(est_passager){
                    Variables_globales.g_groupe.add_id_util_a_passagers(int_id_groupe, int_id_utilisateur);
                }
                Variables_globales.g_groupe.del_id_util_a_demandes(int_id_groupe, int_id_utilisateur);
                Variables_globales.g_utilisateur.add_id_groupe_a_groupe(int_id_utilisateur, int_id_groupe);
                Variables_globales.g_utilisateur.updateXML();
            } 
            if( refuser) {
                Variables_globales.g_groupe.del_id_util_a_demandes(int_id_groupe, int_id_utilisateur);
            }        
    
            Variables_globales.g_groupe.updateXML();
            response.sendRedirect(request.getContextPath() + "/admin");

        }  
    }      
}