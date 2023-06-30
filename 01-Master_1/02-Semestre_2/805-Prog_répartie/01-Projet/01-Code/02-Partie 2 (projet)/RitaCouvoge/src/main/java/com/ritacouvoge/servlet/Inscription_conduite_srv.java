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

public class Inscription_conduite_srv {

    class Inscription_conduite {
        public int Id_inscription_conduite;
        public int Id_utilisateur;
        public int Id_rdv;
        public boolean Lundi;
        public boolean Mardi;
        public boolean Mercredi;
        public boolean Jeudi;
        public boolean Vendredi;
        public boolean Samedi;
        public boolean Dimanche;

        Inscription_conduite(int Id_inscription_conduite, int Id_utilisateur, int Id_rdv, boolean Lundi , boolean Mardi,
        boolean Mercredi, boolean Jeudi,  boolean Vendredi, boolean Samedi, boolean Dimanche) {
            this.Id_inscription_conduite = Id_inscription_conduite;
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
    Map<Integer, Inscription_conduite> hashInscriptionConduite = new HashMap<>();

    public Inscription_conduite_srv(String chemin) {
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
        for (Inscription_conduite conduite : hashInscriptionConduite.values()) {
            if (conduite.Id_inscription_conduite > plus_grand_id) {
                plus_grand_id = conduite.Id_inscription_conduite;
            }
        }
        return plus_grand_id + 1;
    }

    public void remplir_hash() {
        NodeList inscriptionConduiteNodes = document.getElementsByTagName("Inscription_conduite");
        for (int i = 0; i < inscriptionConduiteNodes.getLength(); i++) {
            Element inscriptionConduiteElement = (Element) inscriptionConduiteNodes.item(i);
            int id_inscription_conduite = Integer.parseInt(inscriptionConduiteElement.getElementsByTagName("Id_inscription_conduite").item(0).getTextContent());
            int id_utilisateur = Integer.parseInt(inscriptionConduiteElement.getElementsByTagName("Id_utilisateur").item(0).getTextContent());
            int id_rdv = Integer.parseInt(inscriptionConduiteElement.getElementsByTagName("Id_rdv").item(0).getTextContent());
            boolean lundi = Boolean.parseBoolean(inscriptionConduiteElement.getElementsByTagName("Lundi").item(0).getTextContent());
            boolean mardi = Boolean.parseBoolean(inscriptionConduiteElement.getElementsByTagName("Mardi").item(0).getTextContent());
            boolean mercredi = Boolean.parseBoolean(inscriptionConduiteElement.getElementsByTagName("Mercredi").item(0).getTextContent());
            boolean jeudi = Boolean.parseBoolean(inscriptionConduiteElement.getElementsByTagName("Jeudi").item(0).getTextContent());
            boolean vendredi = Boolean.parseBoolean(inscriptionConduiteElement.getElementsByTagName("Vendredi").item(0).getTextContent());
            boolean samedi = Boolean.parseBoolean(inscriptionConduiteElement.getElementsByTagName("Samedi").item(0).getTextContent());
            boolean dimanche = Boolean.parseBoolean(inscriptionConduiteElement.getElementsByTagName("Dimanche").item(0).getTextContent());
    
            Inscription_conduite inscriptionConduite = new Inscription_conduite(id_inscription_conduite, id_utilisateur, id_rdv, lundi, mardi, mercredi, jeudi, vendredi, samedi, dimanche);
            hashInscriptionConduite.put(id_inscription_conduite, inscriptionConduite);
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

            Element inscriptionsElement = newDocument.createElement("Inscriptions_conduite");
            newDocument.appendChild(inscriptionsElement);

            Set<Integer> keys = hashInscriptionConduite.keySet();
            for (Integer key : keys) {
                Inscription_conduite inscription = hashInscriptionConduite.get(key);
                Element inscriptionElement = newDocument.createElement("Inscription_conduite");

                Element idInscriptionElement = newDocument.createElement("Id_inscription_conduite");
                idInscriptionElement.setTextContent(String.valueOf(inscription.Id_inscription_conduite));
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
    public Inscription_conduite get_inscription_conduite(int id_inscription_conduite) {
        return hashInscriptionConduite.get(id_inscription_conduite);
    }
    
    public int get_id_rdv(int id_inscription_conduite) {
        return hashInscriptionConduite.get(id_inscription_conduite).Id_rdv;
    }
    
    public boolean get_lundi(int id_inscription_conduite) {
        return hashInscriptionConduite.get(id_inscription_conduite).Lundi;
    }
    
    public boolean get_mardi(int id_inscription_conduite) {
        return hashInscriptionConduite.get(id_inscription_conduite).Mardi;
    }
    
    public boolean get_mercredi(int id_inscription_conduite) {
        return hashInscriptionConduite.get(id_inscription_conduite).Mercredi;
    }
    
    public boolean get_jeudi(int id_inscription_conduite) {
        return hashInscriptionConduite.get(id_inscription_conduite).Jeudi;
    }
    
    public boolean get_vendredi(int id_inscription_conduite) {
        return hashInscriptionConduite.get(id_inscription_conduite).Vendredi;
    }
    
    public boolean get_samedi(int id_inscription_conduite) {
        return hashInscriptionConduite.get(id_inscription_conduite).Samedi;
    }
    
    public boolean get_dimanche(int id_inscription_conduite) {
        return hashInscriptionConduite.get(id_inscription_conduite).Dimanche;
    }

    /**
     * Toutes les fonctions set (permettent de changer des champs depuis l'ID)
     */    
    public void set_inscription_conduite(int id_inscription_conduite, int id_utilisateur, int id_rdv, boolean lundi, boolean mardi,
        boolean mercredi, boolean jeudi, boolean vendredi, boolean samedi, boolean dimanche) {
        Inscription_conduite inscription_conduite = get_inscription_conduite(id_inscription_conduite);
        if (inscription_conduite != null) {
            inscription_conduite.Id_utilisateur = id_utilisateur;
            inscription_conduite.Id_rdv = id_rdv;
            inscription_conduite.Lundi = lundi;
            inscription_conduite.Mardi = mardi;
            inscription_conduite.Mercredi = mercredi;
            inscription_conduite.Jeudi = jeudi;
            inscription_conduite.Vendredi = vendredi;
            inscription_conduite.Samedi = samedi;
            inscription_conduite.Dimanche = dimanche;
        }
    }

    public void set_inscription_conduite_utilisateur(int id_inscription_conduite, int id_utilisateur) {
        Inscription_conduite inscription_conduite = get_inscription_conduite(id_inscription_conduite);
        if (inscription_conduite != null) {
            inscription_conduite.Id_utilisateur= id_utilisateur;
        }
    }

    public void set_inscription_conduite_rdv(int id_inscription_conduite, int id_rdv) {
        Inscription_conduite inscription_conduite = get_inscription_conduite(id_inscription_conduite);
        if (inscription_conduite != null) {
            inscription_conduite.Id_rdv= id_rdv;
        }
    }

    public void set_inscription_conduite_lundi(int id_inscription_conduite, boolean lundi) {
        Inscription_conduite inscription_conduite = get_inscription_conduite(id_inscription_conduite);
        if (inscription_conduite != null) {
            inscription_conduite.Lundi = lundi;
        }
    }
    
    public void set_inscription_conduite_mardi(int id_inscription_conduite, boolean mardi) {
        Inscription_conduite inscription_conduite = get_inscription_conduite(id_inscription_conduite);
        if (inscription_conduite != null) {
            inscription_conduite.Mardi = mardi;
        }
    }
    
    public void set_inscription_conduite_mercredi(int id_inscription_conduite, boolean mercredi) {
        Inscription_conduite inscription_conduite = get_inscription_conduite(id_inscription_conduite);
        if (inscription_conduite != null) {
            inscription_conduite.Mercredi = mercredi;
        }
    }
    
    public void set_inscription_conduite_jeudi(int id_inscription_conduite, boolean jeudi) {
        Inscription_conduite inscription_conduite = get_inscription_conduite(id_inscription_conduite);
        if (inscription_conduite != null) {
            inscription_conduite.Jeudi = jeudi;
        }
    }
    
    public void set_inscription_conduite_vendredi(int id_inscription_conduite, boolean vendredi) {
        Inscription_conduite inscription_conduite = get_inscription_conduite(id_inscription_conduite);
        if (inscription_conduite != null) {
            inscription_conduite.Vendredi = vendredi;
        }
    }
    
    public void set_inscription_conduite_samedi(int id_inscription_conduite, boolean samedi) {
        Inscription_conduite inscription_conduite = get_inscription_conduite(id_inscription_conduite);
        if (inscription_conduite != null) {
            inscription_conduite.Samedi = samedi;
        }
    }
    
    public void set_inscription_conduite_dimanche(int id_inscription_conduite, boolean dimanche) {
        Inscription_conduite inscription_conduite = get_inscription_conduite(id_inscription_conduite);
        if (inscription_conduite != null) {
            inscription_conduite.Dimanche = dimanche;
        }
    }
    
    /**
    * La fonction del (permet de supprimer une inscription de conduite existante)
    */
    public void del_inscription_conduite(int id_inscription_conduite){
        Inscription_conduite inscription_conduite = get_inscription_conduite(id_inscription_conduite);
        if (inscription_conduite != null) {
            hashInscriptionConduite.remove(id_inscription_conduite);
        }
    }

    /*
     * La fonction add permet de rajouter une inscription conduite si elle n'existe pas déjà
     */
    public void add_inscription_conduite(int id_inscription_conduite, int id_utilisateur, int id_rdv, boolean lundi, boolean mardi,
        boolean mercredi, boolean jeudi, boolean vendredi, boolean samedi, boolean dimanche) {
        Inscription_conduite inscription_conduite = get_inscription_conduite(id_inscription_conduite);
        if (inscription_conduite == null) {
            Inscription_conduite newInscription = new Inscription_conduite(id_inscription_conduite, id_utilisateur, id_rdv,
                    lundi, mardi, mercredi, jeudi, vendredi, samedi, dimanche);
            hashInscriptionConduite.put(id_inscription_conduite, newInscription);
        }
    }

    // @Override
    // protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //     resp.setContentType("text/html");

    //     PrintWriter out = resp.getWriter();
    //     del_inscription_conduite(69000);
    //     out.println("<html>");
    //     out.println("<head>");
    //     out.println("<title>Attribution</title>");
    //     out.println("</head>");
    //     out.println("<body>");
    //     set_inscription_conduite_dimanche(60000,true);        
    //     updateXML();
    //     out.println("<p>Id_rdv: " + get_dimanche(60000)+ "</p>");
    //     out.println("<p>Id_lien_lundi: " + get_id_rdv(60000) + "</p>");

        

    //     out.println("</body>");
    //     out.println("</html>");
    //     out.close();
    // }
}

   