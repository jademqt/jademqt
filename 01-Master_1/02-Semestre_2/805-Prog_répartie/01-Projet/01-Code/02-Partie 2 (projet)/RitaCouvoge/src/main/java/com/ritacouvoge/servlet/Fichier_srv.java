package com.ritacouvoge.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Vector;


public class Fichier_srv {
    
    public static void copier_fichier(String source, String destination) throws IOException {
        File f_source = new File(source);
        File f_destination = new File(destination);
        Files.copy(f_source.toPath(), f_destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void clear_fichier(String chemin_fichier) throws IOException {
        FileOutputStream fos = new FileOutputStream(chemin_fichier);
        fos.write("".getBytes());
        fos.close();
    }

    public static Vector<String> recup_liste_dossier(){
        String dossierParent = "DATA/2023";
        File dossierParentFile = new File(dossierParent);
        
        // Récupérer la liste des fichiers dans le dossier parent
        File[] fichiers = dossierParentFile.listFiles();

        // Liste pour stocker les noms des répertoires
        Vector<String> listeRepertoires = new Vector<>();

        // Parcourir les fichiers dans le dossier parent
        for (File fichier : fichiers) {
            // Vérifier si le fichier est un répertoire
            if (fichier.isDirectory()) {
                listeRepertoires.add(fichier.getName());
            }
        }
        return listeRepertoires;
    }

    

    public static void creer_dossier_groupe(String id_groupe) throws IOException {
        Vector<String> repertoireListe = recup_liste_dossier();
        String dossierParent = "DATA/2023";

        for (String repertoire : repertoireListe) {
            String chemin = dossierParent + "/" + repertoire + "/" + id_groupe;
            File dossier = new File(chemin);

            if (!dossier.exists()) {
                dossier.mkdirs();
                copier_fichier("DATA/INIT/INIT_Attribution.xml", chemin + "/Attribution.xml"); 
                copier_fichier("DATA/INIT/INIT_Inscription_conduite.xml", chemin + "/Inscription_conduite.xml");               
                copier_fichier("DATA/INIT/INIT_Inscription_passager.xml", chemin + "/Inscription_passager.xml");               
                copier_fichier("DATA/INIT/INIT_Lien.xml", chemin + "/Lien.xml");      
                copier_fichier("DATA/INIT/INIT_Rendez_vous.xml", chemin + "/Rendez_vous.xml");  
                copier_fichier("DATA/INIT/INIT_Trajet_final.xml", chemin + "/Trajet_final.xml");               
                
            } 
            
        }
    }

    public static String createCopyPath(String originalPath) {
        int dotIndex = originalPath.lastIndexOf('.');
        String extension = originalPath.substring(dotIndex); // Obtenir l'extension (".xml")
        
        String pathWithoutExtension = originalPath.substring(0, dotIndex); // Obtenir le chemin sans l'extension
        
        String copyPath = pathWithoutExtension + "Copie" + extension; // Ajouter "Copie" au chemin sans l'extension
        
        return copyPath;
    }
}
