package com.ritacouvoge.servlet;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RdvInscription extends HttpServlet {

    String message ="";
    public String Id_fichier;
    public int IndexRDV;
    public String str_id_semaine;
    public String str_id_groupe;
    public String role;
    String ins_passager_chemin ;
    String ins_conducteur_chemin ;

    public Vector<Boolean> bool_semaine_depuis_form(String[] jours){
        String jour_select="";
        Vector<Boolean> bool_semaine = new Vector<>(Collections.nCopies(7, false));

        //on transforme en vector pour que ce soit plus pratique
        Vector<String> vectorJours = new Vector<>();
        for (String jour : jours) {
            vectorJours.add(jour);
            jour_select = jour_select + jour + ", ";
        }

        //on complète le vector bool_semaine
        if(vectorJours.contains("lundi")){bool_semaine.setElementAt(true, 0);}
        if(vectorJours.contains("mardi")){bool_semaine.setElementAt(true, 1);}
        if(vectorJours.contains("mercredi")){bool_semaine.setElementAt(true, 2);}
        if(vectorJours.contains("jeudi")){bool_semaine.setElementAt(true, 3);}
        if(vectorJours.contains("vendredi")){bool_semaine.setElementAt(true, 4);}
        if(vectorJours.contains("samedi")){bool_semaine.setElementAt(true, 5);}
        if(vectorJours.contains("dimanche")){bool_semaine.setElementAt(true, 6);}     
        
        message="Le " + jour_select + " vous êtes bien inscrit.e en tant que "+ role ;
        return bool_semaine;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String str_IndexRDV = req.getParameter("IndexRDV");
        Id_fichier = req.getParameter("Id_fichier_rdv");
        IndexRDV = Integer.parseInt(str_IndexRDV);
        str_id_semaine = Id_fichier.substring(4, 6);
        str_id_groupe = Id_fichier.substring(6);
        
    
        //information du rendeze-vous sélectionné
        int id_lieu_depart = Variables_globales.g_rdv.get(Id_fichier).get_lieu_depart(IndexRDV);
        String lieu_depart_descri = Variables_globales.g_lieu.get_description(id_lieu_depart);
        String lieu_depart_adresse = Variables_globales.g_lieu.get_adresse(id_lieu_depart);
        String lieu_depart_ville = Variables_globales.g_lieu.get_ville(id_lieu_depart);
        int lieu_depart_code = Variables_globales.g_lieu.get_code_postal(id_lieu_depart);
        LocalTime heure_dep = Variables_globales.g_rdv.get(Id_fichier).get_heure_depart(IndexRDV);

        int id_lieu_arrivee = Variables_globales.g_rdv.get(Id_fichier).get_lieu_arrivee(IndexRDV);
        String lieu_arrivee_descri = Variables_globales.g_lieu.get_description(id_lieu_arrivee);
        String lieu_arrivee_adresse = Variables_globales.g_lieu.get_adresse(id_lieu_arrivee);
        String lieu_arrivee_ville = Variables_globales.g_lieu.get_ville(id_lieu_arrivee);
        int lieu_arrivee_code = Variables_globales.g_lieu.get_code_postal(id_lieu_arrivee);
        LocalTime heure_arr = Variables_globales.g_rdv.get(Id_fichier).get_heure_arrivee(IndexRDV);
       
        //on rajoute dans VGlobales les entrées correspondantes au fichier conducteur et passager
        //(s'ils n'y sont pas déjà)
        if(!Variables_globales.g_conduite.containsKey(Id_fichier)){
            ins_conducteur_chemin = "DATA/2023/" + str_id_semaine +"/" + str_id_groupe + "/Inscription_conduite.xml";
            Inscription_conduite_srv ins_conducteur = new Inscription_conduite_srv(ins_conducteur_chemin);
            Variables_globales.g_conduite.put(Id_fichier, ins_conducteur);
        }
        if(!Variables_globales.g_passager.containsKey(Id_fichier)){
            ins_passager_chemin = "DATA/2023/" + str_id_semaine +"/" + str_id_groupe + "/Inscription_passager.xml";
            Inscription_passager_srv ins_passager = new Inscription_passager_srv(ins_passager_chemin);
            Variables_globales.g_passager.put(Id_fichier, ins_passager);
        }
        
        req.setAttribute("lieu_depart_descri", lieu_depart_descri);
        req.setAttribute("lieu_depart_adresse", lieu_depart_adresse);
        req.setAttribute("lieu_depart_ville", lieu_depart_ville);
        req.setAttribute("lieu_depart_code", lieu_depart_code);
        req.setAttribute("heure_dep", heure_dep);

        req.setAttribute("lieu_arrivee_descri", lieu_arrivee_descri);
        req.setAttribute("lieu_arrivee_adresse", lieu_arrivee_adresse);
        req.setAttribute("lieu_arrivee_ville", lieu_arrivee_ville);
        req.setAttribute("lieu_arrivee_code", lieu_arrivee_code);
        req.setAttribute("heure_arr", heure_arr);

        req.setAttribute("test", message);
        req.getRequestDispatcher("/rdv_inscription.jsp").forward(req, resp);        
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //On récupère les paramètres envoyés
        role = request.getParameter("role"); 
        String[] jours = request.getParameterValues("jours");
        int nbr_jour_select = jours.length;

        //on vérifie qu'ils ne soient pas vide         
        if ((role != null) && (nbr_jour_select > 0)){
            
            HttpSession session = request.getSession();
            Integer id_utilisateur = (Integer) session.getAttribute("Id_utilisateur");
            Vector<Boolean> choix_semaine = bool_semaine_depuis_form(jours);
            //on crée une nouvelle inscription en fonction des choix cochés
            if(role.equals("conducteur")){
                
                int id_conduite = Variables_globales.g_conduite.get(Id_fichier).prochain_id_a_utiliser();
                Variables_globales.g_conduite.get(Id_fichier).add_inscription_conduite(id_conduite, id_utilisateur, IndexRDV, choix_semaine.get(0), choix_semaine.get(1), choix_semaine.get(2), choix_semaine.get(3), choix_semaine.get(4), choix_semaine.get(5), choix_semaine.get(6));
                Variables_globales.g_conduite.get(Id_fichier).updateXML(ins_conducteur_chemin);
            }
            else{
                int id_passager = Variables_globales.g_passager.get(Id_fichier).prochain_id_a_utiliser();
                Variables_globales.g_passager.get(Id_fichier).add_inscription_passager(id_passager, id_utilisateur, IndexRDV, choix_semaine.get(0), choix_semaine.get(1), choix_semaine.get(2), choix_semaine.get(3), choix_semaine.get(4), choix_semaine.get(5), choix_semaine.get(6));
                Variables_globales.g_passager.get(Id_fichier).updateXML(ins_passager_chemin);
            }            
            
            response.sendRedirect(request.getContextPath() + "/rdv_inscription?IndexRDV=" + IndexRDV + "&Id_fichier_rdv=" + Id_fichier);
        }
    }

}