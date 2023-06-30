package com.ritacouvoge.servlet;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Vector;

public class Inscription extends HttpServlet {
    public int semaineEnCours;
    public int semaineAffichee ;
    public String groupeAffichee;
    public int id_groupeAffichee;
    public String id_fichier_rdv;
    public Vector<Integer> liste_lieu_depart = new Vector<>();
    public Vector<Integer> liste_lieu_arrivee = new Vector<>();
    public Vector<Integer> liste_id_rdv = new Vector<>();
    boolean bool_def = true;

    public Vector<Integer> jour_semaine_a_afficher(){
        int annee_ = LocalDate.now().getYear(); // Année actuelle

        // Créer un objet LocalDate pour le premier jour de la semaine spécifiée
        LocalDate premierJourSemaine = LocalDate.ofYearDay(annee_, semaineAffichee * 7).with(DayOfWeek.MONDAY);

        // Créer un vecteur pour stocker les numéros des jours de la semaine
        Vector<Integer> numerosJoursSemaine = new Vector<>();

        // Récupérer les numéros des jours de la semaine
        for (int i = 0; i < 7; i++) {
            int numeroJour = premierJourSemaine.plusDays(i).getDayOfMonth();
            numerosJoursSemaine.add(numeroJour);
        }
        return numerosJoursSemaine;
    }
    
    public void id_lieu_dep(String clef){    
        if (Variables_globales.g_rdv.containsKey(clef)) {  
            for (Integer id_rdv : Variables_globales.g_rdv.get(clef).hashRendezVous.keySet()) {
                Integer lieu_dep = Variables_globales.g_rdv.get(clef).get_lieu_depart(id_rdv);
                if(!liste_lieu_depart.contains(lieu_dep)){
                    liste_lieu_depart.add(lieu_dep);
                    liste_id_rdv.add(id_rdv);
                }
            }   
        }     
    }

    public void id_lieu_arrive(String clef){  
        if (Variables_globales.g_rdv.containsKey(clef)) {     
            for (Integer id_rdv : Variables_globales.g_rdv.get(clef).hashRendezVous.keySet()) {
                Integer lieu_arrive = Variables_globales.g_rdv.get(clef).get_lieu_arrivee(id_rdv);
                if(!liste_lieu_arrivee.contains(lieu_arrive)){
                    liste_lieu_arrivee.add(lieu_arrive);
                }
            }  
        }      
    }


    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //On récupère le numéro de semaine pour créer un vecteur qui comprend le numéro des 5 prochaines semaines  
        Calendar calendar = Calendar.getInstance();
        this.semaineEnCours = calendar.get(Calendar.WEEK_OF_YEAR);
        int annee = LocalDate.now().getYear();
        Vector<Integer> prochainesSemaines = new Vector<>();
        prochainesSemaines.add(semaineEnCours);
        for (int i = 1; i <= 5; i++) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
            int semaineSuivante = calendar.get(Calendar.WEEK_OF_YEAR);
            prochainesSemaines.add(semaineSuivante);
        }    
        //par défaut la semaine affichée est celle en cours
        if(bool_def){
            semaineAffichee = semaineEnCours;
        }        
      
        //rendez-vous existants
        int id_utilisateur = Integer.parseInt(req.getSession().getAttribute("Id_utilisateur").toString());
        Vector<Integer> groupes = Variables_globales.g_utilisateur.get_groupe(id_utilisateur);
        
        
        //on vérifie si les rendez-vous sont déjà dans variables globales sinon on les rajoute
        Vector<String> liste_fichier_rdv = new Vector<>();
        Vector<String> liste_chemin = new Vector<>();
        Vector<Integer> liste_groupe = new Vector<>();
        for (Integer groupe : groupes) {
            if(groupe != 0){
                String concatene = annee + String.format("%02d", semaineAffichee) + groupe;
                String chemin =  "DATA/" + annee + "/" + String.format("%02d", semaineAffichee) + "/" + groupe + "/Rendez_vous.xml";
                liste_fichier_rdv.add(concatene);
                liste_chemin.add(chemin);
                liste_groupe.add(groupe);
            }         

        }  
        if(liste_groupe.size() != 0){
            if(bool_def){
                groupeAffichee = Variables_globales.g_groupe.get_nom_groupe(liste_groupe.get(0));
                id_groupeAffichee = liste_groupe.get(0);
            }
                        
            //on vérifie que les rdv des groupe de l'utilisateur sont présents dans VGlobales (sinon on les rajoute)
            for (int i = 0; i < liste_fichier_rdv.size(); i++) {
                String fichier_rdv= liste_fichier_rdv.get(i);
                String chemin_ = liste_chemin.get(i);
                
                if (!Variables_globales.g_rdv.containsKey(fichier_rdv)) {
                    Rendez_vous_srv rendezVous = new Rendez_vous_srv(chemin_);
                    Variables_globales.g_rdv.put(fichier_rdv, rendezVous);
                }       
            }
            
            String clef= "2023" + String.valueOf(semaineAffichee) + String.valueOf(id_groupeAffichee);
            id_fichier_rdv =clef; 
            id_lieu_dep(clef);
            id_lieu_arrive(clef);
    }
        
        req.setAttribute("test", liste_id_rdv);
        req.setAttribute("liste_lieu_depart", liste_lieu_depart);
        req.setAttribute("liste_lieu_arrivee", liste_lieu_arrivee);
        req.setAttribute("numerosJoursSemaine", jour_semaine_a_afficher());
        req.setAttribute("vector_semaines", prochainesSemaines);
        req.setAttribute("semaineAffichee", semaineAffichee);
        req.setAttribute("groupeAffichee", groupeAffichee);
        req.setAttribute("liste_groupe", liste_groupe);
        req.getRequestDispatcher("/inscription.jsp").forward(req, resp);        
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String affichage = request.getParameter("affichage");
        String indexParam = request.getParameter("index");
                
        if (indexParam != null) {
            int id_rdv = liste_id_rdv.get(Integer.parseInt(indexParam));
            response.sendRedirect(request.getContextPath() + "/rdv_inscription?IndexRDV=" + id_rdv + "&Id_fichier_rdv=" + id_fichier_rdv);
        }
        if(affichage != null){
            bool_def = false;
            String semaine_affichage = request.getParameter("semaine_affichage");
            String groupe_affichage = request.getParameter("groupe_affichage");
            this.semaineAffichee = Integer.parseInt(semaine_affichage);
            this.groupeAffichee = groupe_affichage;
            this.id_groupeAffichee = Variables_globales.g_groupe.trouverIdGroupeParNom(groupe_affichage);
            response.sendRedirect(request.getContextPath() + "/inscription");
        }
    }

}