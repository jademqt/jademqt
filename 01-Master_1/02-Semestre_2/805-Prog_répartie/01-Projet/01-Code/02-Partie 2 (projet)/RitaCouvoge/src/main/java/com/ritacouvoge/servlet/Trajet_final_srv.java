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

public class Trajet_final_srv {

    // Classe interne représentant un trajet final
    class Trajet_final {
        public int Id_trajet_final;
        public Vector<Integer> Trajet_lundi;
        public Vector<Integer> Trajet_mardi;
        public Vector<Integer> Trajet_mercredi;
        public Vector<Integer> Trajet_jeudi;
        public Vector<Integer> Trajet_vendredi;
        public Vector<Integer> Trajet_samedi;
        public Vector<Integer> Trajet_dimanche;

        Trajet_final(int Id_trajet_final, Vector<Integer> Trajet_lundi, Vector<Integer> Trajet_mardi,
                     Vector<Integer> Trajet_mercredi, Vector<Integer> Trajet_jeudi, Vector<Integer> Trajet_vendredi,
                     Vector<Integer> Trajet_samedi, Vector<Integer> Trajet_dimanche) {
            this.Id_trajet_final = Id_trajet_final;
            this.Trajet_lundi = Trajet_lundi;
            this.Trajet_mardi = Trajet_mardi;
            this.Trajet_mercredi = Trajet_mercredi;
            this.Trajet_jeudi = Trajet_jeudi;
            this.Trajet_vendredi = Trajet_vendredi;
            this.Trajet_samedi = Trajet_samedi;
            this.Trajet_dimanche = Trajet_dimanche;
        }
    }

    Document document;
    Map<Integer, Trajet_final> hashTrajetFinal = new HashMap<>();

    public Trajet_final_srv(String chemin) {
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
        NodeList trajetFinalNodes = document.getElementsByTagName("Trajet_final");
        for (int i = 0; i < trajetFinalNodes.getLength(); i++) {
            Element trajetFinalElement = (Element) trajetFinalNodes.item(i);
            int Id_trajet_final = Integer.parseInt(trajetFinalElement.getElementsByTagName("Id_trajet_final").item(0).getTextContent());
    
            Vector<Integer> trajet_lundi = parseAttributions((Element) trajetFinalElement.getElementsByTagName("Trajet_lundi").item(0));
            Vector<Integer> trajet_mardi = parseAttributions((Element) trajetFinalElement.getElementsByTagName("Trajet_mardi").item(0));
            Vector<Integer> trajet_mercredi = parseAttributions((Element) trajetFinalElement.getElementsByTagName("Trajet_mercredi").item(0));
            Vector<Integer> trajet_jeudi = parseAttributions((Element) trajetFinalElement.getElementsByTagName("Trajet_jeudi").item(0));
            Vector<Integer> trajet_vendredi = parseAttributions((Element) trajetFinalElement.getElementsByTagName("Trajet_vendredi").item(0));
            Vector<Integer> trajet_samedi = parseAttributions((Element) trajetFinalElement.getElementsByTagName("Trajet_samedi").item(0));
            Vector<Integer> trajet_dimanche = parseAttributions((Element) trajetFinalElement.getElementsByTagName("Trajet_dimanche").item(0));

            // Créez une instance de la classe Trajet_final avec les données extraites
            Trajet_final_srv.Trajet_final trajetFinal = new Trajet_final_srv.Trajet_final(Id_trajet_final, trajet_lundi, trajet_mardi, trajet_mercredi, trajet_jeudi, trajet_vendredi, trajet_samedi, trajet_dimanche);
    
            // Ajoutez le trajet final au HashMap
            hashTrajetFinal.put(Id_trajet_final, trajetFinal);
        }
    }
    
    private Vector<Integer> parseAttributions(Element element) {
        NodeList idAttributionNodes = element.getElementsByTagName("Id_attribution");
        Vector<Integer> attributions = new Vector<>();
        for (int i = 0; i < idAttributionNodes.getLength(); i++) {
            Element idAttributionElement = (Element) idAttributionNodes.item(i);
            int id_attribution = Integer.parseInt(idAttributionElement.getTextContent());
            attributions.add(id_attribution);
        }
        return attributions;
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
    
            Element trajetFinauxElement = newDocument.createElement("Trajet_finaux");
            newDocument.appendChild(trajetFinauxElement);
    
            Set<Integer> keys = hashTrajetFinal.keySet();
            for (Integer key : keys) {
                Trajet_final trajetFinal = hashTrajetFinal.get(key);
                Element trajetFinalElement = newDocument.createElement("Trajet_final");
    
                Element idTrajetFinalElement = newDocument.createElement("Id_trajet_final");
                idTrajetFinalElement.setTextContent(String.valueOf(trajetFinal.Id_trajet_final));
                trajetFinalElement.appendChild(idTrajetFinalElement);
    
                // Trajet_lundi
                Element trajetLundiElement = newDocument.createElement("Trajet_lundi");
                for (Integer idAttribution : trajetFinal.Trajet_lundi) {
                    Element idAttributionElement = newDocument.createElement("Id_attribution");
                    idAttributionElement.setTextContent(String.valueOf(idAttribution));
                    trajetLundiElement.appendChild(idAttributionElement);
                }
                trajetFinalElement.appendChild(trajetLundiElement);
    
                // Trajet_mardi
                Element trajetMardiElement = newDocument.createElement("Trajet_mardi");
                for (Integer idAttribution : trajetFinal.Trajet_mardi) {
                    Element idAttributionElement = newDocument.createElement("Id_attribution");
                    idAttributionElement.setTextContent(String.valueOf(idAttribution));
                    trajetMardiElement.appendChild(idAttributionElement);
                }
                trajetFinalElement.appendChild(trajetMardiElement);
    
                // Trajet_mercredi
                Element trajetMercrediElement = newDocument.createElement("Trajet_mercredi");
                for (Integer idAttribution : trajetFinal.Trajet_mercredi) {
                    Element idAttributionElement = newDocument.createElement("Id_attribution");
                    idAttributionElement.setTextContent(String.valueOf(idAttribution));
                    trajetMercrediElement.appendChild(idAttributionElement);
                }
                trajetFinalElement.appendChild(trajetMercrediElement);
    
                // Trajet_jeudi
                Element trajetJeudiElement = newDocument.createElement("Trajet_jeudi");
                for (Integer idAttribution : trajetFinal.Trajet_jeudi) {
                    Element idAttributionElement = newDocument.createElement("Id_attribution");
                    idAttributionElement.setTextContent(String.valueOf(idAttribution));
                    trajetJeudiElement.appendChild(idAttributionElement);
                }
                trajetFinalElement.appendChild(trajetJeudiElement);
    
                // Trajet_vendredi
                Element trajetVendrediElement = newDocument.createElement("Trajet_vendredi");
                for (Integer idAttribution : trajetFinal.Trajet_vendredi) {
                    Element idAttributionElement = newDocument.createElement("Id_attribution");
                    idAttributionElement.setTextContent(String.valueOf(idAttribution));
                    trajetVendrediElement.appendChild(idAttributionElement);
                }
                trajetFinalElement.appendChild(trajetVendrediElement);
    
                // Trajet_samedi
                Element trajetSamediElement = newDocument.createElement("Trajet_samedi");
                for (Integer idAttribution : trajetFinal.Trajet_samedi) {
                    Element idAttributionElement = newDocument.createElement("Id_attribution");
                    idAttributionElement.setTextContent(String.valueOf(idAttribution));
                    trajetSamediElement.appendChild(idAttributionElement);
                }
                trajetFinalElement.appendChild(trajetSamediElement);
    
                // Trajet_dimanche
                Element trajetDimancheElement = newDocument.createElement("Trajet_dimanche");
                for (Integer idAttribution : trajetFinal.Trajet_dimanche) {
                    Element idAttributionElement = newDocument.createElement("Id_attribution");
                    idAttributionElement.setTextContent(String.valueOf(idAttribution));
                    trajetDimancheElement.appendChild(idAttributionElement);
                }
                trajetFinalElement.appendChild(trajetDimancheElement);
    
                trajetFinauxElement.appendChild(trajetFinalElement);
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
    public Trajet_final get_trajet_final(int id_trajet_final) {
        return hashTrajetFinal.get(id_trajet_final);
    }

    public Vector<Integer> get_trajet_lundi(int id_trajet_final) {
        return hashTrajetFinal.get(id_trajet_final).Trajet_lundi;
    }

    public Vector<Integer> get_trajet_mardi(int id_trajet_final) {
        return hashTrajetFinal.get(id_trajet_final).Trajet_mardi;
    }

    public Vector<Integer> get_trajet_mercredi(int id_trajet_final) {
        return hashTrajetFinal.get(id_trajet_final).Trajet_mercredi;
    }

    public Vector<Integer> get_trajet_jeudi(int id_trajet_final) {
        return hashTrajetFinal.get(id_trajet_final).Trajet_jeudi;
    }

    public Vector<Integer> get_trajet_vendredi(int id_trajet_final) {
        return hashTrajetFinal.get(id_trajet_final).Trajet_vendredi;
    }

    public Vector<Integer> get_trajet_samedi(int id_trajet_final) {
        return hashTrajetFinal.get(id_trajet_final).Trajet_samedi;
    }

    public Vector<Integer> get_trajet_dimanche(int id_trajet_final) {
        return hashTrajetFinal.get(id_trajet_final).Trajet_dimanche;
    }

    /**
     * Toutes les fonctions set (permettent de changer des champs depuis l'ID)
     */
    
     public void set_trajet_final(int id_trajet_final, Trajet_final trajet_final) {
        if (hashTrajetFinal.containsKey(id_trajet_final)) {
            hashTrajetFinal.put(id_trajet_final, trajet_final);
        }
    }

    public void set_trajet_lundi(int id_trajet_final, Vector<Integer> trajet_lundi) {
        Trajet_final trajet_final = get_trajet_final(id_trajet_final);
        if (trajet_final != null) {
            trajet_final.Trajet_lundi = trajet_lundi;
        }
    }

    public void set_trajet_mardi(int id_trajet_final, Vector<Integer> trajet_mardi) {
        Trajet_final trajet_final = get_trajet_final(id_trajet_final);
        if (trajet_final != null) {
            trajet_final.Trajet_mardi = trajet_mardi;
        }
    }

    public void set_trajet_mercredi(int id_trajet_final, Vector<Integer> trajet_mercredi) {
        Trajet_final trajet_final = get_trajet_final(id_trajet_final);
        if (trajet_final != null) {
            trajet_final.Trajet_mercredi = trajet_mercredi;
        }
    }

    public void set_trajet_jeudi(int id_trajet_final, Vector<Integer> trajet_jeudi) {
        Trajet_final trajet_final = get_trajet_final(id_trajet_final);
        if (trajet_final != null) {
            trajet_final.Trajet_jeudi = trajet_jeudi;
        }
    }

    public void set_trajet_vendredi(int id_trajet_final, Vector<Integer> trajet_vendredi) {
        Trajet_final trajet_final = get_trajet_final(id_trajet_final);
        if (trajet_final != null) {
            trajet_final.Trajet_vendredi = trajet_vendredi;
        }
    }

    public void set_trajet_samedi(int id_trajet_final, Vector<Integer> trajet_samedi) {
        Trajet_final trajet_final = get_trajet_final(id_trajet_final);
        if (trajet_final != null) {
            trajet_final.Trajet_samedi = trajet_samedi;
        }
    }

    public void set_trajet_dimanche(int id_trajet_final, Vector<Integer> trajet_dimanche) {
        Trajet_final trajet_final = get_trajet_final(id_trajet_final);
        if (trajet_final != null) {
            trajet_final.Trajet_dimanche = trajet_dimanche;
        }
    }

    public void add_trajet_final(int id_trajet_final, Vector<Integer> trajet_lundi, Vector<Integer> trajet_mardi,
                                 Vector<Integer> trajet_mercredi, Vector<Integer> trajet_jeudi, Vector<Integer> trajet_vendredi,
                                 Vector<Integer> trajet_samedi, Vector<Integer> trajet_dimanche) {
        if (!hashTrajetFinal.containsKey(id_trajet_final)) {
            Trajet_final trajet_final = new Trajet_final(id_trajet_final, trajet_lundi, trajet_mardi, trajet_mercredi,
                    trajet_jeudi, trajet_vendredi, trajet_samedi, trajet_dimanche);
            hashTrajetFinal.put(id_trajet_final, trajet_final);
        }
    }

        

    public void add_passager_attribue_lundi(int id_trajet_final, int id_passager) {
        Trajet_final trajet_final = get_trajet_final(id_trajet_final);
        if (trajet_final != null && !trajet_final.Trajet_lundi.contains(id_passager)) {
            trajet_final.Trajet_lundi.add(id_passager);
        }
    }

    public void add_passager_attribue_mardi(int id_trajet_final, int id_passager) {
        Trajet_final trajet_final = get_trajet_final(id_trajet_final);
        if (trajet_final != null && !trajet_final.Trajet_mardi.contains(id_passager)) {
            trajet_final.Trajet_mardi.add(id_passager);
        }
    }

    public void add_passager_attribue_mercredi(int id_trajet_final, int id_passager) {
        Trajet_final trajet_final = get_trajet_final(id_trajet_final);
        if (trajet_final != null && !trajet_final.Trajet_mercredi.contains(id_passager)) {
            trajet_final.Trajet_mercredi.add(id_passager);
        }
    }

    public void add_passager_attribue_jeudi(int id_trajet_final, int id_passager) {
        Trajet_final trajet_final = get_trajet_final(id_trajet_final);
        if (trajet_final != null && !trajet_final.Trajet_jeudi.contains(id_passager)) {
            trajet_final.Trajet_jeudi.add(id_passager);
        }
    }

    public void add_passager_attribue_vendredi(int id_trajet_final, int id_passager) {
        Trajet_final trajet_final = get_trajet_final(id_trajet_final);
        if (trajet_final != null && !trajet_final.Trajet_vendredi.contains(id_passager)) {
            trajet_final.Trajet_vendredi.add(id_passager);
        }
    }

    public void add_passager_attribue_samedi(int id_trajet_final, int id_passager) {
        Trajet_final trajet_final = get_trajet_final(id_trajet_final);
        if (trajet_final != null && !trajet_final.Trajet_samedi.contains(id_passager)) {
            trajet_final.Trajet_samedi.add(id_passager);
        }
    }

    public void add_passager_attribue_dimanche(int id_trajet_final, int id_passager) {
        Trajet_final trajet_final = get_trajet_final(id_trajet_final);
        if (trajet_final != null && !trajet_final.Trajet_dimanche.contains(id_passager)) {
            trajet_final.Trajet_dimanche.add(id_passager);
        }
    }

    public void del_trajet_final(int id_trajet_final) {
        hashTrajetFinal.remove(id_trajet_final);
    }


    public void del_passager_attribue_lundi(int id_trajet_final, int id_passager) {
        Trajet_final trajet_final = get_trajet_final(id_trajet_final);
        if (trajet_final != null) {
            trajet_final.Trajet_lundi.remove(Integer.valueOf(id_passager));
        }
    }

    public void del_passager_attribue_mardi(int id_trajet_final, int id_passager) {
        Trajet_final trajet_final = get_trajet_final(id_trajet_final);
        if (trajet_final != null) {
            trajet_final.Trajet_mardi.remove(Integer.valueOf(id_passager));
        }
    }

    public void del_passager_attribue_mercredi(int id_trajet_final, int id_passager) {
        Trajet_final trajet_final = get_trajet_final(id_trajet_final);
        if (trajet_final != null) {
            trajet_final.Trajet_mercredi.remove(Integer.valueOf(id_passager));
        }
    }

    public void del_passager_attribue_jeudi(int id_trajet_final, int id_passager) {
        Trajet_final trajet_final = get_trajet_final(id_trajet_final);
        if (trajet_final != null) {
            trajet_final.Trajet_jeudi.remove(Integer.valueOf(id_passager));
        }
    }

    public void del_passager_attribue_vendredi(int id_trajet_final, int id_passager) {
        Trajet_final trajet_final = get_trajet_final(id_trajet_final);
        if (trajet_final != null) {
            trajet_final.Trajet_vendredi.remove(Integer.valueOf(id_passager));
        }
    }

    public void del_passager_attribue_samedi(int id_trajet_final, int id_passager) {
        Trajet_final trajet_final = get_trajet_final(id_trajet_final);
        if (trajet_final != null) {
            trajet_final.Trajet_samedi.remove(Integer.valueOf(id_passager));
        }
    }

    public void del_passager_attribue_dimanche(int id_trajet_final, int id_passager) {
        Trajet_final trajet_final = get_trajet_final(id_trajet_final);
        if (trajet_final != null) {
            trajet_final.Trajet_dimanche.remove(Integer.valueOf(id_passager));
        }
    }

    // @Override
    // protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //     resp.setContentType("text/html");

    //     PrintWriter out = resp.getWriter();
        
    //     out.println("<html>");
    //     out.println("<head>");
    //     out.println("<title>Attribution</title>");
    //     out.println("</head>");
    //     out.println("<body>");
    //     Vector<Integer> test = get_trajet_lundi(44000);
    //     set_trajet_dimanche(44000, test);   
    //     updateXML(); 
    //     out.println("<p>Id_rdv: " + get_trajet_lundi(44000)+ "</p>");
    //     out.println("<p>Id_lien_lundi: " + get_trajet_dimanche(44000) + "</p>");

        

    //     out.println("</body>");
    //     out.println("</html>");
    //     out.close();
    // }

}