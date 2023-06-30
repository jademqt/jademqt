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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Inscription_passager_srv {

    class Inscription_passager {
        public int Id_inscription_passager;
        public int Id_utilisateur;
        public int Id_rdv;
        public boolean Lundi;
        public boolean Mardi;
        public boolean Mercredi;
        public boolean Jeudi;
        public boolean Vendredi;
        public boolean Samedi;
        public boolean Dimanche;

        Inscription_passager(int Id_inscription_passager, int Id_utilisateur, int Id_rdv, boolean Lundi , boolean Mardi,
        boolean Mercredi, boolean Jeudi,  boolean Vendredi, boolean Samedi, boolean Dimanche) {
            this.Id_inscription_passager = Id_inscription_passager;
            this.Id_rdv = Id_rdv;
            this.Id_utilisateur = Id_utilisateur;
            this.Lundi = Lundi;
            this.Mardi  = Mardi;
            this.Mercredi = Mercredi;
            this.Jeudi = Jeudi ;
            this.Vendredi = Vendredi;
            this.Samedi = Samedi;
            this.Dimanche = Dimanche;
        }
    }

    Document document;
    Map<Integer, Inscription_passager> hashInscriptionPassager = new HashMap<>();

    public Inscription_passager_srv(String chemin) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.document = builder.parse(chemin);
            remplir_hash();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int prochain_id_a_utiliser() {
        int plus_grand_id = 0;
        for (Inscription_passager passager : hashInscriptionPassager.values()) {
            if (passager.Id_inscription_passager > plus_grand_id) {
                plus_grand_id = passager.Id_inscription_passager;
            }
        }
        return plus_grand_id + 1;
    }

    public void remplir_hash() {
        NodeList inscriptionPassagerNodes = document.getElementsByTagName("Inscription_passager");
        for (int i = 0; i < inscriptionPassagerNodes.getLength(); i++) {
            Element inscriptionPassagerElement = (Element) inscriptionPassagerNodes.item(i);
            int id_inscription_passager = Integer.parseInt(inscriptionPassagerElement.getElementsByTagName("Id_inscription_passager").item(0).getTextContent());
            int id_utilisateur = Integer.parseInt(inscriptionPassagerElement.getElementsByTagName("Id_utilisateur").item(0).getTextContent());
            int id_rdv = Integer.parseInt(inscriptionPassagerElement.getElementsByTagName("Id_rdv").item(0).getTextContent());
            boolean lundi = Boolean.parseBoolean(inscriptionPassagerElement.getElementsByTagName("Lundi").item(0).getTextContent());
            boolean mardi = Boolean.parseBoolean(inscriptionPassagerElement.getElementsByTagName("Mardi").item(0).getTextContent());
            boolean mercredi = Boolean.parseBoolean(inscriptionPassagerElement.getElementsByTagName("Mercredi").item(0).getTextContent());
            boolean jeudi = Boolean.parseBoolean(inscriptionPassagerElement.getElementsByTagName("Jeudi").item(0).getTextContent());
            boolean vendredi = Boolean.parseBoolean(inscriptionPassagerElement.getElementsByTagName("Vendredi").item(0).getTextContent());
            boolean samedi = Boolean.parseBoolean(inscriptionPassagerElement.getElementsByTagName("Samedi").item(0).getTextContent());
            boolean dimanche = Boolean.parseBoolean(inscriptionPassagerElement.getElementsByTagName("Dimanche").item(0).getTextContent());
    
            Inscription_passager inscriptionPassager = new Inscription_passager(id_inscription_passager, id_utilisateur, id_rdv, lundi, mardi, mercredi, jeudi, vendredi, samedi, dimanche);
            hashInscriptionPassager.put(id_inscription_passager, inscriptionPassager);
        }
    }

    public void updateXML(String chemin) {
        try {
            String chemin_copie = Fichier_srv.createCopyPath(chemin);
            Fichier_srv.copier_fichier(chemin, chemin_copie);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Fichier_srv.clear_fichier(chemin);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            updateDocumentFromHash(chemin);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateDocumentFromHash(String chemin) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document newDocument = builder.newDocument();

            Element inscriptionsElement = newDocument.createElement("Inscriptions_passager");
            newDocument.appendChild(inscriptionsElement);

            Set<Integer> keys = hashInscriptionPassager.keySet();
            for (Integer key : keys) {
                Inscription_passager inscription = hashInscriptionPassager.get(key);
                Element inscriptionElement = newDocument.createElement("Inscription_passager");

                Element idInscriptionElement = newDocument.createElement("Id_inscription_passager");
                idInscriptionElement.setTextContent(String.valueOf(inscription.Id_inscription_passager));
                inscriptionElement.appendChild(idInscriptionElement);

                Element idUtilisateurElement = newDocument.createElement("Id_utilisateur");
                idUtilisateurElement.setTextContent(String.valueOf(inscription.Id_utilisateur));
                inscriptionElement.appendChild(idUtilisateurElement);

                Element idRdvElement = newDocument.createElement("Id_rdv");
                idRdvElement.setTextContent(String.valueOf(inscription.Id_rdv));
                inscriptionElement.appendChild(idRdvElement);

                Element lundiElement = newDocument.createElement("Lundi");
                lundiElement.setTextContent(String.valueOf(inscription.Lundi));
                inscriptionElement.appendChild(lundiElement);

                Element mardiElement = newDocument.createElement("Mardi");
                mardiElement.setTextContent(String.valueOf(inscription.Mardi));
                inscriptionElement.appendChild(mardiElement);

                Element mercrediElement = newDocument.createElement("Mercredi");
                mercrediElement.setTextContent(String.valueOf(inscription.Mercredi));
                inscriptionElement.appendChild(mercrediElement);

                Element jeudiElement = newDocument.createElement("Jeudi");
                jeudiElement.setTextContent(String.valueOf(inscription.Jeudi));
                inscriptionElement.appendChild(jeudiElement);

                Element vendrediElement = newDocument.createElement("Vendredi");
                vendrediElement.setTextContent(String.valueOf(inscription.Vendredi));
                inscriptionElement.appendChild(vendrediElement);

                Element samediElement = newDocument.createElement("Samedi");
                samediElement.setTextContent(String.valueOf(inscription.Samedi));
                inscriptionElement.appendChild(samediElement);

                Element dimancheElement = newDocument.createElement("Dimanche");
                dimancheElement.setTextContent(String.valueOf(inscription.Dimanche));
                inscriptionElement.appendChild(dimancheElement);

                inscriptionsElement.appendChild(inscriptionElement);
            }

            this.document = newDocument;

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(newDocument);
            StreamResult result = new StreamResult(new FileOutputStream(chemin));

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
    public Inscription_passager get_inscription_passager(int id_inscription_passager) {
        return hashInscriptionPassager.get(id_inscription_passager);
    }
    
    public int get_id_rdv(int id_inscription_passager) {
        return hashInscriptionPassager.get(id_inscription_passager).Id_rdv;
    }
    
    public boolean get_lundi(int id_inscription_passager) {
        return hashInscriptionPassager.get(id_inscription_passager).Lundi;
    }
    
    public boolean get_mardi(int id_inscription_passager) {
        return hashInscriptionPassager.get(id_inscription_passager).Mardi;
    }
    
    public boolean get_mercredi(int id_inscription_passager) {
        return hashInscriptionPassager.get(id_inscription_passager).Mercredi;
    }
    
    public boolean get_jeudi(int id_inscription_passager) {
        return hashInscriptionPassager.get(id_inscription_passager).Jeudi;
    }
    
    public boolean get_vendredi(int id_inscription_passager) {
        return hashInscriptionPassager.get(id_inscription_passager).Vendredi;
    }
    
    public boolean get_samedi(int id_inscription_passager) {
        return hashInscriptionPassager.get(id_inscription_passager).Samedi;
    }
    
    public boolean get_dimanche(int id_inscription_passager) {
        return hashInscriptionPassager.get(id_inscription_passager).Dimanche;
    }

    /**
     * Toutes les fonctions set (permettent de changer des champs depuis l'ID)
     */    
    public void set_inscription_passager(int id_inscription_passager, int id_utilisateur, int id_rdv, boolean lundi, boolean mardi,
        boolean mercredi, boolean jeudi, boolean vendredi, boolean samedi, boolean dimanche) {
        Inscription_passager inscription_passager = get_inscription_passager(id_inscription_passager);
        if (inscription_passager != null) {
            inscription_passager.Id_utilisateur = id_utilisateur;
            inscription_passager.Id_rdv = id_rdv;
            inscription_passager.Lundi = lundi;
            inscription_passager.Mardi = mardi;
            inscription_passager.Mercredi = mercredi;
            inscription_passager.Jeudi = jeudi;
            inscription_passager.Vendredi = vendredi;
            inscription_passager.Samedi = samedi;
            inscription_passager.Dimanche = dimanche;
        }
    }

    public void set_inscription_passager_utilisateur(int id_inscription_passager, int id_utilisateur) {
        Inscription_passager inscription_passager = get_inscription_passager(id_inscription_passager);
        if (inscription_passager != null) {
            inscription_passager.Id_utilisateur= id_utilisateur;
        }
    }

    public void set_inscription_passager_rdv(int id_inscription_passager, int id_rdv) {
        Inscription_passager inscription_passager = get_inscription_passager(id_inscription_passager);
        if (inscription_passager != null) {
            inscription_passager.Id_rdv= id_rdv;
        }
    }

    public void set_inscription_passager_lundi(int id_inscription_passager, boolean lundi) {
        Inscription_passager inscription_passager = get_inscription_passager(id_inscription_passager);
        if (inscription_passager != null) {
            inscription_passager.Lundi = lundi;
        }
    }
    
    public void set_inscription_passager_mardi(int id_inscription_passager, boolean mardi) {
        Inscription_passager inscription_passager = get_inscription_passager(id_inscription_passager);
        if (inscription_passager != null) {
            inscription_passager.Mardi = mardi;
        }
    }
    
    public void set_inscription_passager_mercredi(int id_inscription_passager, boolean mercredi) {
        Inscription_passager inscription_passager = get_inscription_passager(id_inscription_passager);
        if (inscription_passager != null) {
            inscription_passager.Mercredi = mercredi;
        }
    }
    
    public void set_inscription_passager_jeudi(int id_inscription_passager, boolean jeudi) {
        Inscription_passager inscription_passager = get_inscription_passager(id_inscription_passager);
        if (inscription_passager != null) {
            inscription_passager.Jeudi = jeudi;
        }
    }
    
    public void set_inscription_passager_vendredi(int id_inscription_passager, boolean vendredi) {
        Inscription_passager inscription_passager = get_inscription_passager(id_inscription_passager);
        if (inscription_passager != null) {
            inscription_passager.Vendredi = vendredi;
        }
    }
    
    public void set_inscription_passager_samedi(int id_inscription_passager, boolean samedi) {
        Inscription_passager inscription_passager = get_inscription_passager(id_inscription_passager);
        if (inscription_passager != null) {
            inscription_passager.Samedi = samedi;
        }
    }
    
    public void set_inscription_passager_dimanche(int id_inscription_passager, boolean dimanche) {
        Inscription_passager inscription_passager = get_inscription_passager(id_inscription_passager);
        if (inscription_passager != null) {
            inscription_passager.Dimanche = dimanche;
        }
    }
    
    /**
    * La fonction del (permet de supprimer une inscription de passager existante)
    */
    public void del_inscription_passager(int id_inscription_passager){
        Inscription_passager inscription_passager = get_inscription_passager(id_inscription_passager);
        if (inscription_passager != null) {
            hashInscriptionPassager.remove(id_inscription_passager);
        }
    }

    /*
     * La fonction add permet de rajouter une inscription passager si elle n'existe pas déjà
     */
    public void add_inscription_passager(int id_inscription_passager, int id_utilisateur, int id_rdv, boolean lundi, boolean mardi,
        boolean mercredi, boolean jeudi, boolean vendredi, boolean samedi, boolean dimanche) {
        Inscription_passager inscription_passager = get_inscription_passager(id_inscription_passager);
        if (inscription_passager == null) {
            Inscription_passager newInscription = new Inscription_passager(id_inscription_passager, id_utilisateur, id_rdv,
                    lundi, mardi, mercredi, jeudi, vendredi, samedi, dimanche);
            hashInscriptionPassager.put(id_inscription_passager, newInscription);
        }
    }

    // @Override
    // protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //     resp.setContentType("text/html");

    //     PrintWriter out = resp.getWriter();
    //     //add_inscription_passager(99, 99, 88, false, false, false, false, false, false, false);
    //     out.println("<html>");
    //     out.println("<head>");
    //     out.println("<title>Attribution</title>");
    //     out.println("</head>");
    //     out.println("<body>");
    //     del_inscription_passager(99);        
    //     updateXML();
    //     out.println("<p>Id_rdv: " + get_dimanche(70001)+ "</p>");
    //     out.println("<p>Id_lien_lundi: " + get_id_rdv(70001) + "</p>");

        

    //     out.println("</body>");
    //     out.println("</html>");
    //     out.close();
    // }
}

   