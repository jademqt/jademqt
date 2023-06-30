package com.ritacouvoge.servlet;

import java.util.HashMap;
import java.util.Map;

public class Variables_globales {
    public static final Attribution_srv g_attribution;
    public static final Groupe_srv g_groupe;
    public static final Utilisateur_srv g_utilisateur;
    public static final Voiture_srv g_voiture;
    public static final Lieu_srv g_lieu;
    public static final Map<String, Rendez_vous_srv> g_rdv;
    public static final Map<String, Trajet_final_srv> g_trajet_final;
    public static final Map<String, Inscription_conduite_srv> g_conduite;
    public static final Map<String, Inscription_passager_srv> g_passager;
    public static final Map<String, Lien_srv> g_lien;
    
    
    static {
        g_attribution = new Attribution_srv();
        g_groupe = new Groupe_srv();
        g_utilisateur = new Utilisateur_srv();
        g_voiture = new Voiture_srv();
        g_lieu = new Lieu_srv();
        g_rdv =  new HashMap<>();
        g_trajet_final = new HashMap<>();
        g_conduite = new HashMap<>();
        g_passager = new HashMap<>();
        g_lien = new HashMap<>();
    }
}
