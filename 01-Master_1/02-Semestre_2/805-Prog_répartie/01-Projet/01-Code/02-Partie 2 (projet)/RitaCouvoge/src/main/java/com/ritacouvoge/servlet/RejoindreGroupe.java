package com.ritacouvoge.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RejoindreGroupe extends HttpServlet {
    

    String reponse_donnee = " ";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("reponse_donnee", reponse_donnee);
        req.getRequestDispatcher("/rejoindre_groupe.jsp").forward(req, resp);

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int Id_groupe = Integer.parseInt(request.getParameter("id_groupe"));
        int Id_utilisateur = Integer.parseInt(request.getSession().getAttribute("Id_utilisateur").toString());

        //on vérifie si le groupe existe
        if(Variables_globales.g_groupe.hashGroupe.containsKey(Id_groupe)){
            //on vérifie si on est déjà dans la liste des demandeurs
            if(Variables_globales.g_groupe.get_liste_demandes(Id_groupe).contains(Id_utilisateur)){
                reponse_donnee = "Votre demande est déjà en cours.";
            }
            //on vérifie si on est admin de ce groupe
            else if(Variables_globales.g_groupe.get_id_administrateur(Id_groupe) == Id_utilisateur){
                reponse_donnee = "Vous êtes déjà administrateur.trice de ce groupe.";
            }
            //on vérifie si on fait déjà parti du groupe
            else if(Variables_globales.g_groupe.utilisateur_dans_groupe(Id_groupe, Id_utilisateur)){
                reponse_donnee = "Vous faites déjà parti de ce groupe.";
            }            
            //on peut envoyer la demande
            else{
                Variables_globales.g_groupe.add_id_util_a_demandes(Id_groupe, Id_utilisateur);
                reponse_donnee = "La demande a été envoyé à l'administrateur.";
                //Variables_globales.g_groupe.updateXML();
            }            
        }
        else{
            reponse_donnee = "Le groupe que vous souhaitez rejoindre n'existe pas.";
        }
      
        response.sendRedirect(request.getContextPath() + "/rejoindre_groupe");
    }
}