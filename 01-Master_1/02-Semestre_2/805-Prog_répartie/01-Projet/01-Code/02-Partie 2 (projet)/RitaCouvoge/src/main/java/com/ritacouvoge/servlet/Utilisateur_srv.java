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

public class Utilisateur_srv {

    class Utilisateur {
        public int Id_utilisateur;
        public String Pseudonyme;
        public String Mot_de_Passe;
        public String Nom_utilisateur;
        public String Prenom_utilisateur;
        public String Date_naissance;
        public boolean Conducteur;
        public boolean Detient_permis;
        public int Id_voiture;
        public boolean Passager;
        public Vector<Integer> Administrateur;
        public Vector<Integer> Groupe;
        public int Nbr_de_conduite;
        public int Nbr_de_fois_passager;

        Utilisateur(int Id_utilisateur, String Pseudonyme, String Mot_de_Passe, String Nom_utilisateur,
                String Prenom_utilisateur, String Date_naissance, boolean Conducteur, boolean Detient_permis,
                int Id_voiture,
                boolean Passager, Vector<Integer> Administrateur, Vector<Integer> Groupe, int Nbr_de_conduite,
                int Nbr_de_fois_passager) {
            this.Id_utilisateur = Id_utilisateur;
            this.Pseudonyme = Pseudonyme;
            this.Mot_de_Passe = Mot_de_Passe;
            this.Nom_utilisateur = Nom_utilisateur;
            this.Prenom_utilisateur = Prenom_utilisateur;
            this.Date_naissance = Date_naissance;
            this.Conducteur = Conducteur;
            this.Detient_permis = Detient_permis;
            this.Id_voiture = Id_voiture;
            this.Passager = Passager;
            this.Groupe = Groupe;
            this.Administrateur = Administrateur;
            this.Nbr_de_conduite = Nbr_de_conduite;
            this.Nbr_de_fois_passager = Nbr_de_fois_passager;
        }
    }

    Document document;
    Map<Integer, Utilisateur> hashUtilisateur = new HashMap<>();
    int dernier_id;

    public Utilisateur_srv() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.document = builder.parse("DATA/Utilisateur.xml");
            remplir_hash();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int prochain_id_a_utiliser() {
        int plus_grand_id = 0;
        for (Utilisateur utilisateur : hashUtilisateur.values()) {
            if (utilisateur.Id_utilisateur > plus_grand_id) {
                plus_grand_id = utilisateur.Id_utilisateur;
            }
        }
        return plus_grand_id + 1;
    }

    public void remplir_hash() {
        NodeList utilisateurNodes = document.getElementsByTagName("Utilisateur");
        for (int i = 0; i < utilisateurNodes.getLength(); i++) {
            Element utilisateurElement = (Element) utilisateurNodes.item(i);
            int id_utilisateur = Integer
                    .parseInt(utilisateurElement.getElementsByTagName("Id_utilisateur").item(0).getTextContent());
            String pseudonyme = utilisateurElement.getElementsByTagName("Pseudonyme").item(0).getTextContent();
            String mot_de_passe = utilisateurElement.getElementsByTagName("Mot_de_Passe").item(0).getTextContent();
            String nom_utilisateur = utilisateurElement.getElementsByTagName("Nom_utilisateur").item(0)
                    .getTextContent();
            String prenom_utilisateur = utilisateurElement.getElementsByTagName("Prenom_utilisateur").item(0)
                    .getTextContent();
            String date_naissance = utilisateurElement.getElementsByTagName("Date_naissance").item(0).getTextContent();
            boolean conducteur = Boolean
                    .parseBoolean(utilisateurElement.getElementsByTagName("Conducteur").item(0).getTextContent());
            boolean detient_permis = Boolean
                    .parseBoolean(utilisateurElement.getElementsByTagName("Detient_permis").item(0).getTextContent());
            int id_voiture = Integer
                    .parseInt(utilisateurElement.getElementsByTagName("Id_voiture").item(0).getTextContent());
            boolean passager = Boolean
                    .parseBoolean(utilisateurElement.getElementsByTagName("Passager").item(0).getTextContent());

            NodeList administrateurNodes = utilisateurElement.getElementsByTagName("Administrateur");
            Vector<Integer> administrateur = new Vector<>();
            for (int j = 0; j < administrateurNodes.getLength(); j++) {
                Element administrateurElement = (Element) administrateurNodes.item(j);
                NodeList idGroupeNodes = administrateurElement.getElementsByTagName("Id_groupe");
                for (int k = 0; k < idGroupeNodes.getLength(); k++) {
                    Element idGroupeElement = (Element) idGroupeNodes.item(k);
                    int id_groupe = Integer.parseInt(idGroupeElement.getTextContent());
                    administrateur.add(id_groupe);
                }
            }

            NodeList groupeNodes = utilisateurElement.getElementsByTagName("Groupe");
            Vector<Integer> groupe = new Vector<>();
            for (int j = 0; j < groupeNodes.getLength(); j++) {
                Element groupeElement = (Element) groupeNodes.item(j);
                NodeList idGroupeNodes = groupeElement.getElementsByTagName("Id_groupe");
                for (int k = 0; k < idGroupeNodes.getLength(); k++) {
                    Element idGroupeElement = (Element) idGroupeNodes.item(k);
                    int id_groupe = Integer.parseInt(idGroupeElement.getTextContent());
                    groupe.add(id_groupe);
                }
            }

            int nbr_de_conduite = Integer
                    .parseInt(utilisateurElement.getElementsByTagName("Nbr_de_conduite").item(0).getTextContent());
            int nbr_de_fois_passager = Integer
                    .parseInt(utilisateurElement.getElementsByTagName("Nbr_de_fois_passager").item(0).getTextContent());

            Utilisateur utilisateur = new Utilisateur(id_utilisateur, pseudonyme, mot_de_passe, nom_utilisateur,
                    prenom_utilisateur, date_naissance, conducteur, detient_permis, id_voiture,
                    passager, administrateur, groupe, nbr_de_conduite, nbr_de_fois_passager);
            hashUtilisateur.put(id_utilisateur, utilisateur);
        }
    }

    public void updateXML() {
        try {
            Fichier_srv.copier_fichier("DATA/Utilisateur.xml", "DATA/UtilisateurCopie.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Fichier_srv.clear_fichier("DATA/Utilisateur.xml");
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

            Element utilisateursElement = newDocument.createElement("Utilisateurs");
            newDocument.appendChild(utilisateursElement);

            Set<Integer> keys = hashUtilisateur.keySet();
            for (Integer key : keys) {
                Utilisateur utilisateur = hashUtilisateur.get(key);
                Element utilisateurElement = newDocument.createElement("Utilisateur");

                Element idUtilisateurElement = newDocument.createElement("Id_utilisateur");
                idUtilisateurElement.setTextContent(String.valueOf(utilisateur.Id_utilisateur));
                utilisateurElement.appendChild(idUtilisateurElement);

                Element pseudonymeElement = newDocument.createElement("Pseudonyme");
                pseudonymeElement.setTextContent(utilisateur.Pseudonyme);
                utilisateurElement.appendChild(pseudonymeElement);

                Element motDePasseElement = newDocument.createElement("Mot_de_Passe");
                motDePasseElement.setTextContent(utilisateur.Mot_de_Passe);
                utilisateurElement.appendChild(motDePasseElement);

                Element nomUtilisateurElement = newDocument.createElement("Nom_utilisateur");
                nomUtilisateurElement.setTextContent(utilisateur.Nom_utilisateur);
                utilisateurElement.appendChild(nomUtilisateurElement);

                Element prenomUtilisateurElement = newDocument.createElement("Prenom_utilisateur");
                prenomUtilisateurElement.setTextContent(utilisateur.Prenom_utilisateur);
                utilisateurElement.appendChild(prenomUtilisateurElement);

                Element dateNaissanceElement = newDocument.createElement("Date_naissance");
                dateNaissanceElement.setTextContent(utilisateur.Date_naissance);
                utilisateurElement.appendChild(dateNaissanceElement);

                Element conducteurElement = newDocument.createElement("Conducteur");
                conducteurElement.setTextContent(String.valueOf(utilisateur.Conducteur));
                utilisateurElement.appendChild(conducteurElement);

                Element detientPermisElement = newDocument.createElement("Detient_permis");
                detientPermisElement.setTextContent(String.valueOf(utilisateur.Detient_permis));
                utilisateurElement.appendChild(detientPermisElement);

                Element idVoitureElement = newDocument.createElement("Id_voiture");
                idVoitureElement.setTextContent(String.valueOf(utilisateur.Id_voiture));
                utilisateurElement.appendChild(idVoitureElement);

                Element passagerElement = newDocument.createElement("Passager");
                passagerElement.setTextContent(String.valueOf(utilisateur.Passager));
                utilisateurElement.appendChild(passagerElement);

                Element administrateurElement = newDocument.createElement("Administrateur");
                for (Integer adminId : utilisateur.Administrateur) {
                    Element adminIdElement = newDocument.createElement("Id_groupe");
                    adminIdElement.setTextContent(String.valueOf(adminId));
                    administrateurElement.appendChild(adminIdElement);
                }
                utilisateurElement.appendChild(administrateurElement);

                Element groupeElement = newDocument.createElement("Groupe");
                for (Integer groupeId : utilisateur.Groupe) {
                    Element groupeIdElement = newDocument.createElement("Id_groupe");
                    groupeIdElement.setTextContent(String.valueOf(groupeId));
                    groupeElement.appendChild(groupeIdElement);
                }
                utilisateurElement.appendChild(groupeElement);

                Element nbrConduiteElement = newDocument.createElement("Nbr_de_conduite");
                nbrConduiteElement.setTextContent(String.valueOf(utilisateur.Nbr_de_conduite));
                utilisateurElement.appendChild(nbrConduiteElement);

                Element nbrFoisPassagerElement = newDocument.createElement("Nbr_de_fois_passager");
                nbrFoisPassagerElement.setTextContent(String.valueOf(utilisateur.Nbr_de_fois_passager));
                utilisateurElement.appendChild(nbrFoisPassagerElement);

                utilisateursElement.appendChild(utilisateurElement);
            }

            this.document = newDocument;

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(newDocument);
            StreamResult result = new StreamResult(new FileOutputStream("DATA/Utilisateur.xml"));

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            transformer.setOutputProperty("omit-xml-declaration", "yes"); // Optionnel : pour omettre la déclaration XML
            transformer.transform(source, result);

            System.out.println("Le document XML a été mis à jour avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Toutes les fonctions get (permettent de récupérer des champs depuis l'ID)
     */
    public Utilisateur get_utilisateur(int id_utilisateur) {
        return hashUtilisateur.get(id_utilisateur);
    }

    public String get_pseudonyme(int id_utilisateur) {
        return hashUtilisateur.get(id_utilisateur).Pseudonyme;
    }

    public String get_mdp(int id_utilisateur) {
        return hashUtilisateur.get(id_utilisateur).Mot_de_Passe;
    }

    public String get_nom_utilisateur(int id_utilisateur) {
        return hashUtilisateur.get(id_utilisateur).Nom_utilisateur;
    }

    public String get_prenom_utilisateur(int id_utilisateur) {
        return hashUtilisateur.get(id_utilisateur).Prenom_utilisateur;
    }

    public String get_date_naissance(int id_utilisateur) {
        return hashUtilisateur.get(id_utilisateur).Date_naissance;
    }

    public boolean get_est_conducteur(int id_utilisateur) {
        return hashUtilisateur.get(id_utilisateur).Conducteur;
    }

    public boolean get_detient_permis(int id_utilisateur) {
        return hashUtilisateur.get(id_utilisateur).Detient_permis;
    }

    public int get_id_voiture(int id_utilisateur) {
        return hashUtilisateur.get(id_utilisateur).Id_voiture;
    }

    public boolean get_est_passager(int id_utilisateur) {
        return hashUtilisateur.get(id_utilisateur).Passager;
    }

    public Vector<Integer> get_administrateur(int id_utilisateur) {
        return hashUtilisateur.get(id_utilisateur).Administrateur;
    }

    public Vector<Integer> get_groupe(int id_utilisateur) {
        return hashUtilisateur.get(id_utilisateur).Groupe;
    }

    public int get_nbr_conduite(int id_utilisateur) {
        return hashUtilisateur.get(id_utilisateur).Nbr_de_conduite;
    }

    public int get_nbr_de_fois_passager(int id_utilisateur) {
        return hashUtilisateur.get(id_utilisateur).Nbr_de_fois_passager;
    }

    /**
     * Toutes les fonctions set (permettent de changer des champs depuis l'ID)
     */
    public void set_utilisateur(int id_utilisateur, Utilisateur utilisateur) {
        if (hashUtilisateur.containsKey(id_utilisateur)) {
            hashUtilisateur.put(id_utilisateur, utilisateur);
        }
    }

    public void set_pseudonyme(int id_utilisateur, String pseudonyme) {
        Utilisateur utilisateur = get_utilisateur(id_utilisateur);
        if (utilisateur != null) {
            utilisateur.Pseudonyme = pseudonyme;
        }
    }

    public void set_mdp(int id_utilisateur, String mot_de_passe) {
        Utilisateur utilisateur = get_utilisateur(id_utilisateur);
        if (utilisateur != null) {
            utilisateur.Mot_de_Passe = mot_de_passe;
        }
    }

    public void set_nom_utilisateur(int id_utilisateur, String nom_utilisateur) {
        Utilisateur utilisateur = get_utilisateur(id_utilisateur);
        if (utilisateur != null) {
            utilisateur.Nom_utilisateur = nom_utilisateur;
        }
    }

    public void set_prenom_utilisateur(int id_utilisateur, String prenom_utilisateur) {
        Utilisateur utilisateur = get_utilisateur(id_utilisateur);
        if (utilisateur != null) {
            utilisateur.Prenom_utilisateur = prenom_utilisateur;
        }
    }

    public void set_date_naissance(int id_utilisateur, String date_naissance) {
        Utilisateur utilisateur = get_utilisateur(id_utilisateur);
        if (utilisateur != null) {
            utilisateur.Date_naissance = date_naissance;
        }
    }

    public void set_est_conducteur(int id_utilisateur, boolean conducteur) {
        Utilisateur utilisateur = get_utilisateur(id_utilisateur);
        if (utilisateur != null) {
            utilisateur.Conducteur = conducteur;
        }
    }

    public void set_detient_permis(int id_utilisateur, boolean detient_permis) {
        Utilisateur utilisateur = get_utilisateur(id_utilisateur);
        if (utilisateur != null) {
            utilisateur.Detient_permis = detient_permis;
        }
    }

    public void set_id_voiture(int id_utilisateur, int id_voiture) {
        Utilisateur utilisateur = get_utilisateur(id_utilisateur);
        if (utilisateur != null) {
            utilisateur.Id_voiture = id_voiture;
        }
    }

    public void set_est_passager(int id_utilisateur, boolean passager) {
        Utilisateur utilisateur = get_utilisateur(id_utilisateur);
        if (utilisateur != null) {
            utilisateur.Passager = passager;
        }
    }

    public void set_administrateur(int id_utilisateur, Vector<Integer> administrateur) {
        Utilisateur utilisateur = get_utilisateur(id_utilisateur);
        if (utilisateur != null) {
            utilisateur.Administrateur = administrateur;
        }
    }

    public void set_groupe(int id_utilisateur, Vector<Integer> groupe) {
        Utilisateur utilisateur = get_utilisateur(id_utilisateur);
        if (utilisateur != null) {
            utilisateur.Groupe = groupe;
        }
    }

    public void set_nbr_conduite(int id_utilisateur, int nbr_conduite) {
        Utilisateur utilisateur = get_utilisateur(id_utilisateur);
        if (utilisateur != null) {
            utilisateur.Nbr_de_conduite = nbr_conduite;
        }
    }

    public void set_nbr_de_fois_passager(int id_utilisateur, int nbr_de_fois_passager) {
        Utilisateur utilisateur = get_utilisateur(id_utilisateur);
        if (utilisateur != null) {
            utilisateur.Nbr_de_fois_passager = nbr_de_fois_passager;
        }
    }

    /**
     * La fonction add (permet de créer un nouvel utilisateur si l'ID n'est pas déjà
     * utilisé)
     */
    public void add_utilisateur(int id_utilisateur, String pseudonyme, String mot_de_passe, String nom_utilisateur,
            String prenom_utilisateur, String date_naissance, boolean conducteur, boolean detient_permis,
            int id_voiture, boolean passager, Vector<Integer> administrateur, Vector<Integer> groupe,
            int nbr_de_conduite, int nbr_de_fois_passager) {
        Utilisateur utilisateur = get_utilisateur(id_utilisateur);
        if (utilisateur == null) {
            Utilisateur newUtilisateur = new Utilisateur(id_utilisateur, pseudonyme, mot_de_passe, nom_utilisateur,
                    prenom_utilisateur, date_naissance, conducteur, detient_permis, id_voiture, passager,
                    administrateur,
                    groupe, nbr_de_conduite, nbr_de_fois_passager);
            hashUtilisateur.put(id_utilisateur, newUtilisateur);
        }
    }

    public void add_id_groupe_a_groupe(int id_utilisateur, int id_groupe) {
        Utilisateur utilisateur = get_utilisateur(id_utilisateur);
        if (utilisateur != null) {
            utilisateur.Groupe.add(id_groupe);
        }
    }

    public void add_id_groupe_a_admin(int id_utilisateur, int id_groupe) {
        Utilisateur utilisateur = get_utilisateur(id_utilisateur);
        if (utilisateur != null) {
            utilisateur.Administrateur.add(id_groupe);
        }
    }

    /**
     * La fonction del (permet de supprimer un utilisateur existant)
     */
    public void del_utilisateur(int id_utilisateur) {
        Utilisateur utilisateur = get_utilisateur(id_utilisateur);
        if (utilisateur != null) {
            hashUtilisateur.remove(id_utilisateur);
        }
    }

    public void del_id_groupe_a_groupe(int id_utilisateur, int id_groupe) {
        Utilisateur utilisateur = get_utilisateur(id_utilisateur);
        if (utilisateur != null) {
            utilisateur.Groupe.remove(Integer.valueOf(id_groupe));
        }
    }

    // @Override
    // protected void doGet(HttpServletRequest request, HttpServletResponse
    // response) throws ServletException, IOException {

    // if (hashUtilisateur.containsKey(88000)) {
    // Utilisateur utilisateur = get_utilisateur(88000);
    // String pseudonyme = get_pseudonyme(88000);
    // String nom = get_nom_utilisateur(88000);
    // String prenom = get_prenom_utilisateur(88000);
    // /*
    // // Modifier certaines valeurs de l'utilisateur
    // set_pseudonyme(88000, "CaptainRita");
    // set_nom_utilisateur(88000, "Covoge");
    // set_prenom_utilisateur(88000, "Rita");
    // */

    // updateXML();

    // // Générer la réponse HTML avec les informations de l'utilisateur
    // response.setContentType("text/html");
    // PrintWriter out = response.getWriter();

    // out.println("<html>");
    // out.println("<head>");
    // out.println("<title>Information de l'utilisateur</title>");
    // out.println("</head>");
    // out.println("<body>");

    // out.println("<h1>Information de l'utilisateur</h1>");
    // out.println("<p>ID: " + utilisateur.Id_utilisateur + "</p>");
    // out.println("<p>Pseudonyme (Avant modification): " + pseudonyme + "</p>");
    // out.println("<p>Nom (Avant modification): " + nom + "</p>");
    // out.println("<p>Prénom (Avant modification): " + prenom + "</p>");
    // out.println("<p>Pseudonyme (Après modification): " + get_pseudonyme(88000) +
    // "</p>");
    // out.println("<p>Nom (Après modification): " + get_nom_utilisateur(88000) +
    // "</p>");
    // out.println("<p>Prénom (Après modification): " +
    // get_prenom_utilisateur(88000) + "</p>");

    // out.println("</body>");
    // out.println("</html>");

    // out.close();
    // } else {
    // // Si l'utilisateur n'existe pas, renvoyer une réponse d'erreur
    // response.sendError(HttpServletResponse.SC_NOT_FOUND, "Utilisateur non
    // trouvé");
    // }
    // }

}
