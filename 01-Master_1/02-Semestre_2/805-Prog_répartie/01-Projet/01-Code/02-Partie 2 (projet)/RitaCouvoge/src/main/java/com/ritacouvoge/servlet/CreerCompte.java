package com.ritacouvoge.servlet;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CreerCompte extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/creer_compte.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pseudo = request.getParameter("pseudo");
        String password = request.getParameter("password");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String dateNaissance = request.getParameter("dateNaissance");
        boolean detientPermis = request.getParameter("detientPermis") != null;
        boolean conducteur = request.getParameter("conducteur") != null;
        int nombrePlacesVoiture = Integer.parseInt(request.getParameter("nombrePlacesVoiture"));
        boolean passager = request.getParameter("passager") != null;

        // traitement des champs supplémentaires
        // On récupère un nouvel ID
        int id_utilisateur = Variables_globales.g_utilisateur.prochain_id_a_utiliser();

        // Si la personne n'a pas le permis on passe automatiquement le champ conducteur
        // à faux
        if (!detientPermis) {
            conducteur = false;
        }

        // On créé une voiture avec le nombre de place si conducteur est vrai
        int id_voiture = 0;
        if (conducteur) {
            id_voiture = Variables_globales.g_voiture.prochain_id_a_utiliser();
            Variables_globales.g_voiture.add_voiture(id_voiture, nombrePlacesVoiture);
        }

        // création des vector vides car au départ l'utilisateur n'est pas admin/
        // n'appartient pas à un groupe
        Vector<Integer> admin = new Vector<>();
        admin.add(0);
        Vector<Integer> groupe = new Vector<>();
        groupe.add(0);

        // On créé le nouvel utilisateur
        Variables_globales.g_utilisateur.add_utilisateur(id_utilisateur, pseudo, password, nom, prenom, dateNaissance,
                conducteur, detientPermis, id_voiture, passager, admin, groupe, 0, 0);

        Variables_globales.g_utilisateur.updateXML();

        // Rediriger l'utilisateur vers une autre page après le traitement
        HttpSession session = request.getSession();
        session.setAttribute("Id_utilisateur", id_utilisateur);
        response.sendRedirect(request.getContextPath() + "/home");
    }

}
