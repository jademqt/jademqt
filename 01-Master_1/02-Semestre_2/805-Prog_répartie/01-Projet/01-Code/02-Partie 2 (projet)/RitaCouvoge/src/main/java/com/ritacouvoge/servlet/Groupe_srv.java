package com.ritacouvoge.servlet;

// import javax.servlet.ServletException;
// import javax.servlet.http.HttpServlet;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import java.io.PrintWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Groupe_srv {

    class Groupe {
        public int Id_groupe;
        public String Nom_groupe;
        public String Descriptif_groupe;
        public int Id_administrateur;
        public Vector<Integer> Liste_conducteurs;
        public Vector<Integer> Liste_passagers;
        public Vector<Integer> Tab_trajets;
        public Vector<Integer> Liste_demandes;
        
        Groupe( int Id_groupe, String Nom_groupe, String Descriptif_groupe, int Id_administrateur,
        Vector<Integer> Liste_conducteurs, Vector<Integer> Liste_passagers, Vector<Integer> Tab_trajets, Vector<Integer> Liste_demandes){
            this.Id_groupe = Id_groupe;
            this.Nom_groupe = Nom_groupe;
            this.Descriptif_groupe = Descriptif_groupe;
            this.Id_administrateur = Id_administrateur;
            this.Liste_conducteurs = Liste_conducteurs;
            this.Liste_passagers = Liste_passagers;
            this.Tab_trajets = Tab_trajets;
            this.Liste_demandes = Liste_demandes;
        }
    }
    
    Document document;
    Map<Integer,Groupe> hashGroupe = new HashMap<>(); 
  
    public Groupe_srv(){        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.document = builder.parse("DATA/Groupe.xml");
            remplir_hash();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int trouverIdGroupeParNom(String nomGroupe) {
        for (Groupe groupe : hashGroupe.values()) {
            if (groupe.Nom_groupe.equals(nomGroupe)) {
                return groupe.Id_groupe;
            }
        }
        return -1; // Retourne -1 si le groupe n'est pas trouvé
    }

    public int prochain_id_a_utiliser(){
        int plus_grand_id = 0;
        
        for ( Groupe groupe : hashGroupe.values() ) {
            if (groupe.Id_groupe > plus_grand_id){
                plus_grand_id = groupe.Id_groupe;
            }
        }    
        return (plus_grand_id + 1);
    }

    public boolean utilisateur_dans_groupe(int Id_groupe, int Id_utilisateur) {
        if (hashGroupe.containsKey(Id_groupe)) {
            Groupe groupe = hashGroupe.get(Id_groupe);
            Vector<Integer> conducteurs = groupe.Liste_conducteurs;
            Vector<Integer> passagers = groupe.Liste_passagers;
    
            if (conducteurs.contains(Id_utilisateur) || passagers.contains(Id_utilisateur)) {
                // L'utilisateur est présent parmi les conducteurs ou les passagers du groupe
                return true;
            }
        }        
        // L'utilisateur n'est pas présent dans le groupe
        return false;
    }
    


    public void remplir_hash() {
        NodeList groupeNodes = document.getElementsByTagName("Groupe");
        for (int i = 0; i < groupeNodes.getLength(); i++) {
            Element groupeElement = (Element) groupeNodes.item(i);
            int id_groupe = Integer.parseInt(groupeElement.getElementsByTagName("Id_groupe").item(0).getTextContent());
            String nom_groupe = groupeElement.getElementsByTagName("Nom_groupe").item(0).getTextContent();
            String descriptif_groupe = groupeElement.getElementsByTagName("Descriptif_groupe").item(0).getTextContent();
            int id_administrateur = Integer.parseInt(groupeElement.getElementsByTagName("Id_administrateur").item(0).getTextContent());
    
            Vector<Integer> liste_conducteurs = new Vector<>();
            NodeList conducteurNodes = groupeElement.getElementsByTagName("Liste_conducteurs");
            for (int j = 0; j < conducteurNodes.getLength(); j++) {
                Element conducteurElement = (Element) conducteurNodes.item(j);
                NodeList idUtilisateurNodes = conducteurElement.getElementsByTagName("Id_utilisateur");
                for (int k = 0; k < idUtilisateurNodes.getLength(); k++) {
                    int id_utilisateur = Integer.parseInt(idUtilisateurNodes.item(k).getTextContent());
                    liste_conducteurs.add(id_utilisateur);
                }
            }
    
            Vector<Integer> liste_passagers = new Vector<>();
            NodeList passagerNodes = groupeElement.getElementsByTagName("Liste_passagers");
            for (int j = 0; j < passagerNodes.getLength(); j++) {
                Element passagerElement = (Element) passagerNodes.item(j);
                NodeList idUtilisateurNodes = passagerElement.getElementsByTagName("Id_utilisateur");
                for (int k = 0; k < idUtilisateurNodes.getLength(); k++) {
                    int id_utilisateur = Integer.parseInt(idUtilisateurNodes.item(k).getTextContent());
                    liste_passagers.add(id_utilisateur);
                }
            }
    
            Vector<Integer> tab_trajets = new Vector<>();
            NodeList trajetNodes = groupeElement.getElementsByTagName("Tab_trajets");
            for (int j = 0; j < trajetNodes.getLength(); j++) {
                Element trajetElement = (Element) trajetNodes.item(j);
                NodeList idTrajetNodes = trajetElement.getElementsByTagName("Id_trajet_final");
                for (int k = 0; k < idTrajetNodes.getLength(); k++) {
                    int id_trajet_final = Integer.parseInt(idTrajetNodes.item(k).getTextContent());
                    tab_trajets.add(id_trajet_final);
                }
            }
    
            Vector<Integer> liste_demandes = new Vector<>();
            NodeList demandeNodes = groupeElement.getElementsByTagName("Liste_demandes");
            for (int j = 0; j < demandeNodes.getLength(); j++) {
                Element demandeElement = (Element) demandeNodes.item(j);
                NodeList idUtilisateurNodes = demandeElement.getElementsByTagName("Id_utilisateur");
                for (int k = 0; k < idUtilisateurNodes.getLength(); k++) {
                    int id_utilisateur = Integer.parseInt(idUtilisateurNodes.item(k).getTextContent());
                    liste_demandes.add(id_utilisateur);
                }
            }
    
            Groupe groupe = new Groupe(id_groupe, nom_groupe, descriptif_groupe, id_administrateur,
                    liste_conducteurs, liste_passagers, tab_trajets, liste_demandes);
            hashGroupe.put(id_groupe, groupe);
        }
    }
    
    

    public void updateXML() {
        try {
            Fichier_srv.copier_fichier("DATA/Groupe.xml", "DATA/GroupeCopie.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
       
        try {
            Fichier_srv.clear_fichier("DATA/Groupe.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }    
        
        try {
            updateDocumentFromHash();
             
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }


    public void updateDocumentFromHash() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document newDocument = builder.newDocument();
    
            Element groupesElement = newDocument.createElement("Groupes");
            newDocument.appendChild(groupesElement);
    
            Set<Integer> keys = hashGroupe.keySet();
            for (Integer key : keys) {
                Groupe groupe = hashGroupe.get(key);
                Element groupeElement = newDocument.createElement("Groupe");
    
                Element idGroupeElement = newDocument.createElement("Id_groupe");
                idGroupeElement.setTextContent(String.valueOf(groupe.Id_groupe));
                groupeElement.appendChild(idGroupeElement);
    
                Element nomGroupeElement = newDocument.createElement("Nom_groupe");
                nomGroupeElement.setTextContent(groupe.Nom_groupe);
                groupeElement.appendChild(nomGroupeElement);
    
                Element descriptifGroupeElement = newDocument.createElement("Descriptif_groupe");
                descriptifGroupeElement.setTextContent(groupe.Descriptif_groupe);
                groupeElement.appendChild(descriptifGroupeElement);
    
                Element idAdministrateurElement = newDocument.createElement("Id_administrateur");
                idAdministrateurElement.setTextContent(String.valueOf(groupe.Id_administrateur));
                groupeElement.appendChild(idAdministrateurElement);
    
                Element listeConducteursElement = newDocument.createElement("Liste_conducteurs");
                for (Integer id_conducteur : groupe.Liste_conducteurs) {
                    Element idUtilisateurElement = newDocument.createElement("Id_utilisateur");
                    idUtilisateurElement.setTextContent(String.valueOf(id_conducteur));
                    listeConducteursElement.appendChild(idUtilisateurElement);
                }
                groupeElement.appendChild(listeConducteursElement);
    
                Element listePassagersElement = newDocument.createElement("Liste_passagers");
                for (Integer id_passager : groupe.Liste_passagers) {
                    Element idUtilisateurElement = newDocument.createElement("Id_utilisateur");
                    idUtilisateurElement.setTextContent(String.valueOf(id_passager));
                    listePassagersElement.appendChild(idUtilisateurElement);
                }
                groupeElement.appendChild(listePassagersElement);
    
                Element tabTrajetsElement = newDocument.createElement("Tab_trajets");
                for (Integer id_trajet : groupe.Tab_trajets) {
                    Element idTrajetElement = newDocument.createElement("Id_trajet_final");
                    idTrajetElement.setTextContent(String.valueOf(id_trajet));
                    tabTrajetsElement.appendChild(idTrajetElement);
                }
                groupeElement.appendChild(tabTrajetsElement);
    
                Element listeDemandesElement = newDocument.createElement("Liste_demandes");
                for (Integer id_demande : groupe.Liste_demandes) {
                    Element idUtilisateurElement = newDocument.createElement("Id_utilisateur");
                    idUtilisateurElement.setTextContent(String.valueOf(id_demande));
                    listeDemandesElement.appendChild(idUtilisateurElement);
                }
                groupeElement.appendChild(listeDemandesElement);
    
                groupesElement.appendChild(groupeElement);
            }
    
            this.document = newDocument;
    
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(newDocument);
            StreamResult result = new StreamResult(new FileOutputStream("DATA/Groupe.xml"));
    
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("omit-xml-declaration", "yes");  // Optionnel : pour omettre la déclaration XML
            transformer.transform(source, result);
    
            System.out.println("Le document XML a été mis à jour avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        
    /**
     * Toutes les fonctions get (permettent de récupérer des champs depuis l'ID)
     */
    public Groupe get_groupe(int id_groupe){
        return hashGroupe.get(id_groupe);
    }

    public String get_nom_groupe(int id_groupe){
        return hashGroupe.get(id_groupe).Nom_groupe;
    }

    public String get_descriptif_groupe(int id_groupe){
        return hashGroupe.get(id_groupe).Descriptif_groupe;
    }

    public int get_id_administrateur(int id_groupe){
        return hashGroupe.get(id_groupe).Id_administrateur;
    }

    public Vector<Integer> get_liste_conducteurs(int id_groupe){
        return hashGroupe.get(id_groupe).Liste_conducteurs;
    }

    public Vector<Integer> get_liste_passagers(int id_groupe){
        return hashGroupe.get(id_groupe).Liste_passagers;
    }

    public Vector<Integer> get_tab_trajets(int id_groupe){
        return hashGroupe.get(id_groupe).Tab_trajets;
    }

    public Vector<Integer> get_liste_demandes(int id_groupe){
        return hashGroupe.get(id_groupe).Liste_demandes;
    }

    /**
     * Toutes les fonctions set (permettent de changer des champs depuis l'ID)
     */
    public void set_groupe(int id_groupe, String nom_groupe, String descriptif_groupe, int id_administrateur,
                        Vector<Integer> liste_conducteurs, Vector<Integer> liste_passagers, Vector<Integer> tab_trajets,
                        Vector<Integer> liste_demandes){
        Groupe groupe = get_groupe(id_groupe);
        if (groupe != null) {
            groupe.Nom_groupe = nom_groupe;
            groupe.Descriptif_groupe = descriptif_groupe;
            groupe.Id_administrateur = id_administrateur;
            groupe.Liste_conducteurs = liste_conducteurs;
            groupe.Liste_passagers = liste_passagers;
            groupe.Tab_trajets = tab_trajets;
            groupe.Liste_demandes = liste_demandes;
        }
    }

    public void set_nom_groupe(int id_groupe, String nom_groupe){
        Groupe groupe = get_groupe(id_groupe);
        if (groupe != null) {
            groupe.Nom_groupe = nom_groupe;
        }
    }

    public void set_descriptif_groupe(int id_groupe, String descriptif_groupe){
        Groupe groupe = get_groupe(id_groupe);
        if (groupe != null) {
            groupe.Descriptif_groupe = descriptif_groupe;
        }
    }

    public void set_id_administrateur(int id_groupe, int id_administrateur){
        Groupe groupe = get_groupe(id_groupe);
        if (groupe != null) {
            groupe.Id_administrateur = id_administrateur;
        }
    }

    public void set_liste_conducteurs(int id_groupe, Vector<Integer> liste_conducteurs){
        Groupe groupe = get_groupe(id_groupe);
        if (groupe != null) {
            groupe.Liste_conducteurs = liste_conducteurs;
        }
    }

    public void set_liste_passagers(int id_groupe, Vector<Integer> liste_passagers){
        Groupe groupe = get_groupe(id_groupe);
        if (groupe != null) {
            groupe.Liste_passagers = liste_passagers;
        }
    }

    public void set_tab_trajets(int id_groupe, Vector<Integer> tab_trajets){
        Groupe groupe = get_groupe(id_groupe);
        if (groupe != null) {
            groupe.Tab_trajets = tab_trajets;
        }
    }
    
    public void set_liste_demandes(int id_groupe, Vector<Integer> liste_demandes){
        Groupe groupe = get_groupe(id_groupe);
        if (groupe != null) {
            groupe.Liste_demandes = liste_demandes;
        }
    }

    /**
     * La fonction add (permet de créer un nouveau groupe si l'ID n'est pas déjà utilisé)
     * @throws IOException
     */
    public void add_groupe(int id_groupe, String nom_groupe, String descriptif_groupe, int id_administrateur,
    Vector<Integer> liste_conducteurs, Vector<Integer> liste_passagers, Vector<Integer> tab_trajets,
    Vector<Integer> liste_demandes) throws IOException{
        Groupe groupe = get_groupe(id_groupe);
        if (groupe == null) {
            Groupe groupe_ = new Groupe(id_groupe, nom_groupe, descriptif_groupe, id_administrateur, liste_conducteurs,
            liste_passagers, tab_trajets, liste_demandes);
            hashGroupe.put(id_groupe, groupe_);

            //On créé le dossier du groupe dans les semaines
            Fichier_srv.creer_dossier_groupe(Integer.toString(id_groupe));
        }
    }

    public void add_id_util_a_conducteurs(int id_groupe, int id_utilisateur){
        Groupe groupe = get_groupe(id_groupe);
        if (groupe != null) {
            groupe.Liste_conducteurs.add(id_utilisateur);
        }
    }

    public void add_id_util_a_passagers(int id_groupe, int id_utilisateur){
        Groupe groupe = get_groupe(id_groupe);
        if (groupe != null) {
            groupe.Liste_passagers.add(id_utilisateur);
        }
    }

    public void add_trajet_final_a_trajets(int id_groupe, int id_trajet_final){
        Groupe groupe = get_groupe(id_groupe);
        if (groupe != null) {
            groupe.Liste_passagers.add(id_trajet_final);
        }
    }

    public void add_id_util_a_demandes(int id_groupe, int id_utilisateur){
        Groupe groupe = get_groupe(id_groupe);
        if (groupe != null) {
            groupe.Liste_passagers.add(id_utilisateur);
        }
    }

    /**
    * La fonction del (permet de supprimer un groupe existant)
    */
    public void del_groupe(int id_groupe){
        Groupe groupe = get_groupe(id_groupe);
        if (groupe != null) {
            hashGroupe.remove(id_groupe);
        }
    }

    public void del_id_util_a_conducteurs(int id_groupe, int id_utilisateur) {
        Groupe groupe = get_groupe(id_groupe);
        if (groupe != null) {
            groupe.Liste_conducteurs.remove(Integer.valueOf(id_utilisateur));
        }
    }

    public void del_id_util_a_passagers(int id_groupe, int id_utilisateur) {
        Groupe groupe = get_groupe(id_groupe);
        if (groupe != null) {
            groupe.Liste_passagers.remove(Integer.valueOf(id_utilisateur));
        }
    }

    public void del_trajet_final_a_trajets(int id_groupe, int id_trajet_final) {
        Groupe groupe = get_groupe(id_groupe);
        if (groupe != null) {
            groupe.Tab_trajets.remove(Integer.valueOf(id_trajet_final));
        }
    }
 
    public void del_id_util_a_demandes(int id_groupe, int id_utilisateur) {
        Groupe groupe = get_groupe(id_groupe);
        if (groupe != null) {
            groupe.Liste_demandes.remove(Integer.valueOf(id_utilisateur));
        }
    }

    // @Override
    // protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //     // Vérifier si le lieu avec l'ID 90001 existe dans le dictionnaire
    //     if (hashGroupe.containsKey(11000)) {
    //         int test = get_id_administrateur(11000);
    //         del_id_util_a_conducteurs(11000, 22003);
    //         del_id_util_a_passagers(11000, 22123);
    //         set_id_administrateur(11000, 22000);
    //         int test2 = get_id_administrateur(11000);


    //         del_groupe(11003);
    //         updateXML();
            
    //         // Générer la réponse HTML avec les informations du lieu
    //         response.setContentType("text/html");
    //         PrintWriter out = response.getWriter();
            
    //         out.println("<html>");
    //         out.println("<head>");
    //         out.println("<title>Information du lieu</title>");
    //         out.println("</head>");
    //         out.println("<body>");

    //         out.println("<h1>Information du lieu</h1>");
    //         out.println("<p>Avant: " + test + "</p>");
    //         out.println("<p>Après: " + test2 + "</p>");
            
            
    //         out.println("</body>");
    //         out.println("</html>");
            
    //         out.close();
    //     } else {
    //         // Si le lieu n'existe pas, renvoyer une réponse d'erreur
    //         response.sendError(HttpServletResponse.SC_NOT_FOUND, "Lieu non trouvé");
    //     }
    // }
    
}
