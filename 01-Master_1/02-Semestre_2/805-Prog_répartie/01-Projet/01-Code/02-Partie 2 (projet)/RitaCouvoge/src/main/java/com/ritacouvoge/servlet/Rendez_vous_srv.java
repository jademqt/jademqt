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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class Rendez_vous_srv {

    class Rendez_vous {
        public int Id_rdv;
        public int Lieu_depart;
        public int Lieu_arrivee;
        public LocalTime Heure_depart;
        public LocalTime Heure_arrivee;
        public int Id_groupe;
    
        Rendez_vous(int Id_rdv, int Lieu_depart, int Lieu_arrivee, LocalTime heure_depart2, LocalTime heure_arrivee2, int Id_groupe ){
            this.Id_rdv = Id_rdv;
            this.Lieu_depart = Lieu_depart;
            this.Lieu_arrivee = Lieu_arrivee;
            this.Heure_depart = heure_depart2;
            this.Heure_arrivee = heure_arrivee2;
            this.Id_groupe = Id_groupe;
        }
    }
    
    Document document;
    Map<Integer,Rendez_vous> hashRendezVous = new HashMap<>();

    public Rendez_vous_srv(String chemin){        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.document = builder.parse(chemin);
            remplir_hash();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


public void remplir_hash() {
    NodeList rendezVousNodes = document.getElementsByTagName("Rendez_vous");
    for (int i = 0; i < rendezVousNodes.getLength(); i++) {
        Element rendezVousElement = (Element) rendezVousNodes.item(i);
        int Id_rdv = Integer.parseInt(rendezVousElement.getElementsByTagName("Id_rdv").item(0).getTextContent());
        int Lieu_depart = Integer.parseInt(rendezVousElement.getElementsByTagName("Lieu_depart").item(0).getTextContent());
        int Lieu_arrivee = Integer.parseInt(rendezVousElement.getElementsByTagName("Lieu_arrivee").item(0).getTextContent());
        LocalTime Heure_depart = parseTime(rendezVousElement.getElementsByTagName("Heure_depart").item(0).getTextContent());
        LocalTime Heure_arrivee = parseTime(rendezVousElement.getElementsByTagName("Heure_arrivee").item(0).getTextContent());
        int Id_groupe = Integer.parseInt(rendezVousElement.getElementsByTagName("Id_groupe").item(0).getTextContent());

        Rendez_vous rendezVous = new Rendez_vous(Id_rdv, Lieu_depart, Lieu_arrivee, Heure_depart, Heure_arrivee, Id_groupe);
        hashRendezVous.put(Id_rdv, rendezVous);
    }
}

    public LocalTime parseTime(String timeString) {
        return LocalTime.parse(timeString);
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
    
            Element RDVElement = newDocument.createElement("RDV");
            newDocument.appendChild(RDVElement);
    
            Set<Integer> keys = hashRendezVous.keySet();
            for (Integer key : keys) {
                Rendez_vous rendezVous = hashRendezVous.get(key);
                Element rendezVousElement = newDocument.createElement("Rendez_vous");
    
                Element idRdvElement = newDocument.createElement("Id_rdv");
                idRdvElement.setTextContent(String.valueOf(rendezVous.Id_rdv));
                rendezVousElement.appendChild(idRdvElement);
    
                Element lieuDepartElement = newDocument.createElement("Lieu_depart");
                lieuDepartElement.setTextContent(String.valueOf(rendezVous.Lieu_depart));
                rendezVousElement.appendChild(lieuDepartElement);
    
                Element lieuArriveeElement = newDocument.createElement("Lieu_arrivee");
                lieuArriveeElement.setTextContent(String.valueOf(rendezVous.Lieu_arrivee));
                rendezVousElement.appendChild(lieuArriveeElement);
    
                Element heureDepartElement = newDocument.createElement("Heure_depart");
                heureDepartElement.setTextContent(formatTime(rendezVous.Heure_depart));
                rendezVousElement.appendChild(heureDepartElement);

                Element heureArriveeElement = newDocument.createElement("Heure_arrivee");
                heureArriveeElement.setTextContent(formatTime(rendezVous.Heure_arrivee));
                rendezVousElement.appendChild(heureArriveeElement);
    
                Element idGroupeElement = newDocument.createElement("Id_groupe");
                idGroupeElement.setTextContent(String.valueOf(rendezVous.Id_groupe));
                rendezVousElement.appendChild(idGroupeElement);
    
                RDVElement.appendChild(rendezVousElement);
            }
    
            this.document = newDocument;
    
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(newDocument);
            StreamResult result = new StreamResult(new FileOutputStream(chemin));
    
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    
            transformer.setOutputProperty("omit-xml-declaration", "yes");  // Optionnel : pour omettre la déclaration XML
            transformer.transform(source, result);
    
            System.out.println("Le document XML a été mis à jour avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String formatTime(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }
    
    
    /**
     * Toutes les fonctions get (permettent de récupérer des champs depuis l'ID)
     */
    public Rendez_vous get_rendezVous(int Id_rdv) {
        return hashRendezVous.get(Id_rdv);
    }

    public int get_lieu_depart(int Id_rdv) {
        return hashRendezVous.get(Id_rdv).Lieu_depart;
    }

    public int get_lieu_arrivee(int Id_rdv) {
        return hashRendezVous.get(Id_rdv).Lieu_arrivee;
    }

    public LocalTime get_heure_depart(int Id_rdv) {
        return hashRendezVous.get(Id_rdv).Heure_depart;
    }

    public LocalTime get_heure_arrivee(int Id_rdv) {
        return hashRendezVous.get(Id_rdv).Heure_arrivee;
    }

    public int get_id_groupe(int Id_rdv) {
        return hashRendezVous.get(Id_rdv).Id_groupe;
    }

    /**
     * Toutes les fonctions set (permettent de changer des champs depuis l'ID)
     */
    public void set_rendezVous(int Id_rdv, Rendez_vous rendezVous) {
        if (hashRendezVous.containsKey(Id_rdv)) {
            hashRendezVous.put(Id_rdv, rendezVous);
        }
    }

    public void set_lieu_depart(int Id_rdv, int Lieu_depart) {
        Rendez_vous rendezVous = get_rendezVous(Id_rdv);
        if (rendezVous != null) {
            rendezVous.Lieu_depart = Lieu_depart;
        }
    }

    public void set_lieu_arrivee(int Id_rdv, int Lieu_arrivee) {
        Rendez_vous rendezVous = get_rendezVous(Id_rdv);
        if (rendezVous != null) {
            rendezVous.Lieu_arrivee = Lieu_arrivee;
        }
    }

    public void set_heure_depart(int Id_rdv, LocalTime Heure_depart) {
        Rendez_vous rendezVous = get_rendezVous(Id_rdv);
        if (rendezVous != null) {
            rendezVous.Heure_depart = Heure_depart;
        }
    }

    public void set_heure_arrivee(int Id_rdv, LocalTime Heure_arrivee) {
        Rendez_vous rendezVous = get_rendezVous(Id_rdv);
        if (rendezVous != null) {
            rendezVous.Heure_arrivee = Heure_arrivee;
        }
    }

    public void set_id_groupe(int Id_rdv, int Id_groupe) {
        Rendez_vous rendezVous = get_rendezVous(Id_rdv);
        if (rendezVous != null) {
            rendezVous.Id_groupe = Id_groupe;
        }
    }


    public void add_rendezVous(int Id_rdv, int Lieu_depart, int Lieu_arrivee, LocalTime Heure_depart, LocalTime Heure_arrivee, int Id_groupe) {
        if (!hashRendezVous.containsKey(Id_rdv)) {
            Rendez_vous rendezVous = new Rendez_vous(Id_rdv, Lieu_depart, Lieu_arrivee, Heure_depart, Heure_arrivee, Id_groupe);
            hashRendezVous.put(Id_rdv, rendezVous);
        }
    }
    
    public void del_rendezVous(int Id_rdv) {
        hashRendezVous.remove(Id_rdv);
    }
  
    
    // @Override
    // protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //     resp.setContentType("text/html");

    //     PrintWriter out = resp.getWriter();
    //     //add_rendezVous(33, 0, 0, get_heure_arrivee(55000), get_heure_arrivee(55000), 0);
    //     out.println("<html>");
    //     out.println("<head>");
    //     out.println("<title>Attribution</title>");
    //     out.println("</head>");
    //     out.println("<body>");
    //     //set_id_groupe(55000, 3330); 
    //     //del_rendezVous(33);
    //     //updateXML();
    //     out.println("<p>Id_rdv: " + get_id_groupe(55000)+ "</p>");
    //     out.println("<p>Id_lien_lundi: " + get_lieu_arrivee(55000) + "</p>");

        

    //     out.println("</body>");
    //     out.println("</html>");
    //     out.close();
    // }
    
}