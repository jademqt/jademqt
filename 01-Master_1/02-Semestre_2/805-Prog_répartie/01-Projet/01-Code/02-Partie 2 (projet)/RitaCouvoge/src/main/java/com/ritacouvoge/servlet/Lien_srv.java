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


public class Lien_srv {

    class Lien {
        public int Id_Lien;
        public int Id_conducteur;
        public Vector<Integer> Passagers_attribues;
    
        Lien(int Id_Lien, int Id_conducteur, Vector<Integer> Passagers_attribues ){
            this.Id_Lien = Id_Lien;
            this.Id_conducteur = Id_conducteur;
            this.Passagers_attribues = Passagers_attribues;
        }
    }
    
    Document document;
    Map<Integer,Lien> hashLien = new HashMap<>();

    public Lien_srv(String chemin){        
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
        NodeList lienNodes = document.getElementsByTagName("Lien");
        for (int i = 0; i < lienNodes.getLength(); i++) {
            Element lienElement = (Element) lienNodes.item(i);
            int id_lien = Integer.parseInt(lienElement.getElementsByTagName("Id_Lien").item(0).getTextContent());
            int id_conducteur = Integer.parseInt(lienElement.getElementsByTagName("Id_conducteur").item(0).getTextContent());
    
            NodeList passagersAttribuesNodes = lienElement.getElementsByTagName("Passagers_attribues");
            Element passagersAttribuesElement = (Element) passagersAttribuesNodes.item(0);
            NodeList idPassagerNodes = passagersAttribuesElement.getElementsByTagName("Id_passager");
    
            Vector<Integer> passagers_attribues = new Vector<>();
            for (int j = 0; j < idPassagerNodes.getLength(); j++) {
                Element idPassagerElement = (Element) idPassagerNodes.item(j);
                int id_passager = Integer.parseInt(idPassagerElement.getTextContent());
                passagers_attribues.add(id_passager);
            }
    
            Lien lien = new Lien(id_lien, id_conducteur, passagers_attribues);
            hashLien.put(id_lien, lien);
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
    
            Element liensElement = newDocument.createElement("Liens");
            newDocument.appendChild(liensElement);
    
            Set<Integer> keys = hashLien.keySet();
            for (Integer key : keys) {
                Lien lien = hashLien.get(key);
                Element lienElement = newDocument.createElement("Lien");
    
                Element idLienElement = newDocument.createElement("Id_Lien");
                idLienElement.setTextContent(String.valueOf(lien.Id_Lien));
                lienElement.appendChild(idLienElement);
    
                Element idConducteurElement = newDocument.createElement("Id_conducteur");
                idConducteurElement.setTextContent(String.valueOf(lien.Id_conducteur));
                lienElement.appendChild(idConducteurElement);
    
                Element passagersAttribuesElement = newDocument.createElement("Passagers_attribues");
                for (Integer idPassager : lien.Passagers_attribues) {
                    Element idPassagerElement = newDocument.createElement("Id_passager");
                    idPassagerElement.setTextContent(String.valueOf(idPassager));
                    passagersAttribuesElement.appendChild(idPassagerElement);
                }
                lienElement.appendChild(passagersAttribuesElement);
    
                liensElement.appendChild(lienElement);
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
    
    /**
     * Toutes les fonctions get (permettent de récupérer des champs depuis l'ID)
     */
    public Lien get_lien(int id_lien) {
        return hashLien.get(id_lien);
    }
    
    public int get_id_conducteur(int id_lien) {
        return hashLien.get(id_lien).Id_conducteur;
    }
    
    public Vector<Integer> get_passagers_attribues(int id_lien) {
        return hashLien.get(id_lien).Passagers_attribues;
    }

    /**
     * Toutes les fonctions set (permettent de changer des champs depuis l'ID)
     */
    public void set_lien(int id_lien, Lien lien) {
        if (hashLien.containsKey(id_lien)) {
            hashLien.put(id_lien, lien);
        }
    }
    
    public void set_id_conducteur(int id_lien, int id_conducteur) {
        Lien lien = get_lien(id_lien);
        if (lien != null) {
            lien.Id_conducteur = id_conducteur;
        }
    }
    
    public void set_passagers_attribues(int id_lien, Vector<Integer> passagers_attribues) {
        Lien lien = get_lien(id_lien);
        if (lien != null) {
            lien.Passagers_attribues = passagers_attribues;
        }
    }
    
    public void add_lien(int id_lien, int id_conducteur, Vector<Integer> passagers_attribues) {
        if (!hashLien.containsKey(id_lien)) {
            Lien lien = new Lien(id_lien, id_conducteur, passagers_attribues);
            hashLien.put(id_lien, lien);
        }
    }
    
    public void add_passager_attribue(int id_lien, int id_passager) {
        Lien lien = get_lien(id_lien);
        if (lien != null && !lien.Passagers_attribues.contains(id_passager)) {
            lien.Passagers_attribues.add(id_passager);
        }
    }
    
    
    public void del_passager_attribue(int id_lien, int id_passager) {
        Lien lien = get_lien(id_lien);
        if (lien != null) {
            lien.Passagers_attribues.remove(Integer.valueOf(id_passager));
        }
    }
    
    public void del_lien(int id_lien) {
        hashLien.remove(id_lien);
    }
    
    // @Override
    // protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //     resp.setContentType("text/html");

    //     PrintWriter out = resp.getWriter();
    //     add_passager_attribue(0, 20);
    //     out.println("<html>");
    //     out.println("<head>");
    //     out.println("<title>Attribution</title>");
    //     out.println("</head>");
    //     out.println("<body>");
    //     //set_id_conducteur(0,33);      
    //     del_lien(1);
    //     updateXML();
    //     out.println("<p>Id_rdv: " + get_passagers_attribues(0)+ "</p>");
    //     out.println("<p>Id_lien_lundi: " + get_id_conducteur(0) + "</p>");

        

    //     out.println("</body>");
    //     out.println("</html>");
    //     out.close();
    // }
    
}