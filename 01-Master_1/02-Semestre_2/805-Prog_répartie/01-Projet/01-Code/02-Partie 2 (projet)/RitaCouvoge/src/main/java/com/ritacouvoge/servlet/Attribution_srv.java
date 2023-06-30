package com.ritacouvoge.servlet;
/* 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
*/
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

public class Attribution_srv  {

    class Attribution {
        public int Id_rdv;
        public int Id_attribution;
        public int Id_lien_lundi;
        public int Id_lien_mardi;
        public int Id_lien_mercredi;
        public int Id_lien_jeudi;
        public int Id_lien_vendredi;
        public int Id_lien_samedi;
        public int Id_lien_dimanche;

        Attribution(int Id_rdv, int Id_attribution, int id_lien_lundi, int id_lien_mardi, int id_lien_mercredi,
                int id_lien_jeudi, int id_lien_vendredi, int id_lien_samedi, int id_lien_dimanche) {
            this.Id_rdv = Id_rdv;
            this.Id_attribution = Id_attribution;
            this.Id_lien_lundi = id_lien_lundi;
            this.Id_lien_mardi = id_lien_mardi;
            this.Id_lien_mercredi = id_lien_mercredi;
            this.Id_lien_jeudi = id_lien_jeudi;
            this.Id_lien_vendredi = id_lien_vendredi;
            this.Id_lien_samedi = id_lien_samedi;
            this.Id_lien_dimanche = id_lien_dimanche;
        }
    }

    Document document;
    Map<Integer, Attribution> hashAttribution = new HashMap<>();

    public Attribution_srv() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.document = builder.parse("DATA/2023/00/11000/Attribution.xml");
            remplir_hash();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remplir_hash() {
        NodeList attributionNodes = document.getElementsByTagName("Attribution");
        for (int i = 0; i < attributionNodes.getLength(); i++) {
            Element attributionElement = (Element) attributionNodes.item(i);
            int id_rdv = Integer.parseInt(attributionElement.getElementsByTagName("Id_rdv").item(0).getTextContent());
            int id_attribution = Integer.parseInt(
                    attributionElement.getElementsByTagName("Id_attribution").item(0).getTextContent());
            int id_lien_lundi = Integer
                    .parseInt(attributionElement.getElementsByTagName("Id_lien_lundi").item(0).getTextContent());
            int id_lien_mardi = Integer
                    .parseInt(attributionElement.getElementsByTagName("Id_lien_mardi").item(0).getTextContent());
            int id_lien_mercredi = Integer
                    .parseInt(attributionElement.getElementsByTagName("Id_lien_mercredi").item(0).getTextContent());
            int id_lien_jeudi = Integer
                    .parseInt(attributionElement.getElementsByTagName("Id_lien_jeudi").item(0).getTextContent());
            int id_lien_vendredi = Integer
                    .parseInt(attributionElement.getElementsByTagName("Id_lien_vendredi").item(0).getTextContent());
            int id_lien_samedi = Integer
                    .parseInt(attributionElement.getElementsByTagName("Id_lien_samedi").item(0).getTextContent());
            int id_lien_dimanche = Integer
                    .parseInt(attributionElement.getElementsByTagName("Id_lien_dimanche").item(0).getTextContent());

            Attribution attribution = new Attribution(id_rdv, id_attribution, id_lien_lundi, id_lien_mardi,
                    id_lien_mercredi, id_lien_jeudi, id_lien_vendredi, id_lien_samedi, id_lien_dimanche);
            hashAttribution.put(id_attribution, attribution);
        }
    }

    public void updateXML() {
        try {
            Fichier_srv.copier_fichier("DATA/2023/00/11000/Attribution.xml",
                    "DATA/2023/00/11000/AttributionCopie.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Fichier_srv.clear_fichier("DATA/2023/00/11000/Attribution.xml");
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

            Element attributionsElement = newDocument.createElement("Attributions");
            newDocument.appendChild(attributionsElement);

            Set<Integer> keys = hashAttribution.keySet();
            for (Integer key : keys) {
                Attribution attribution = hashAttribution.get(key);
                Element attributionElement = newDocument.createElement("Attribution");

                Element idRdvElement = newDocument.createElement("Id_rdv");
                idRdvElement.setTextContent(String.valueOf(attribution.Id_rdv));
                attributionElement.appendChild(idRdvElement);

                Element idAttributionElement = newDocument.createElement("Id_attribution");
                idAttributionElement.setTextContent(String.valueOf(attribution.Id_attribution));
                attributionElement.appendChild(idAttributionElement);

                Element idLienLundiElement = newDocument.createElement("Id_lien_lundi");
                idLienLundiElement.setTextContent(String.valueOf(attribution.Id_lien_lundi));
                attributionElement.appendChild(idLienLundiElement);

                Element idLienMardiElement = newDocument.createElement("Id_lien_mardi");
                idLienMardiElement.setTextContent(String.valueOf(attribution.Id_lien_mardi));
                attributionElement.appendChild(idLienMardiElement);

                Element idLienMercrediElement = newDocument.createElement("Id_lien_mercredi");
                idLienMercrediElement.setTextContent(String.valueOf(attribution.Id_lien_mercredi));
                attributionElement.appendChild(idLienMercrediElement);

                Element idLienJeudiElement = newDocument.createElement("Id_lien_jeudi");
                idLienJeudiElement.setTextContent(String.valueOf(attribution.Id_lien_jeudi));
                attributionElement.appendChild(idLienJeudiElement);

                Element idLienVendrediElement = newDocument.createElement("Id_lien_vendredi");
                idLienVendrediElement.setTextContent(String.valueOf(attribution.Id_lien_vendredi));
                attributionElement.appendChild(idLienVendrediElement);

                Element idLienSamediElement = newDocument.createElement("Id_lien_samedi");
                idLienSamediElement.setTextContent(String.valueOf(attribution.Id_lien_samedi));
                attributionElement.appendChild(idLienSamediElement);

                Element idLienDimancheElement = newDocument.createElement("Id_lien_dimanche");
                idLienDimancheElement.setTextContent(String.valueOf(attribution.Id_lien_dimanche));
                attributionElement.appendChild(idLienDimancheElement);

                attributionsElement.appendChild(attributionElement);
            }

            this.document = newDocument;

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(newDocument);
            StreamResult result = new StreamResult(
                    new FileOutputStream("DATA/2023/00/11000/Attribution.xml"));

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
    public Attribution get_attribution(int id_attribution) {
        return hashAttribution.get(id_attribution);
    }

    public int get_id_rdv(int id_attribution) {
        return hashAttribution.get(id_attribution).Id_rdv;
    }

    public int get_id_lien_lundi(int id_attribution) {
        return hashAttribution.get(id_attribution).Id_lien_lundi;
    }

    public int get_id_lien_mardi(int id_attribution) {
        return hashAttribution.get(id_attribution).Id_lien_mardi;
    }

    public int get_id_lien_mercredi(int id_attribution) {
        return hashAttribution.get(id_attribution).Id_lien_mercredi;
    }

    public int get_id_lien_jeudi(int id_attribution) {
        return hashAttribution.get(id_attribution).Id_lien_jeudi;
    }

    public int get_id_lien_vendredi(int id_attribution) {
        return hashAttribution.get(id_attribution).Id_lien_vendredi;
    }

    public int get_id_lien_samedi(int id_attribution) {
        return hashAttribution.get(id_attribution).Id_lien_samedi;
    }

    public int get_id_lien_dimanche(int id_attribution) {
        return hashAttribution.get(id_attribution).Id_lien_dimanche;
    }

    /**
     * Toutes les fonctions set (permettent de changer des champs depuis l'ID)
     */
    public void set_attribution(int id_attribution, int id_rdv, int id_lien_lundi, int id_lien_mardi,
            int id_lien_mercredi, int id_lien_jeudi, int id_lien_vendredi, int id_lien_samedi,
            int id_lien_dimanche) {
        Attribution attribution = get_attribution(id_attribution);
        if (attribution != null) {
            attribution.Id_rdv = id_rdv;
            attribution.Id_lien_lundi = id_lien_lundi;
            attribution.Id_lien_mardi = id_lien_mardi;
            attribution.Id_lien_mercredi = id_lien_mercredi;
            attribution.Id_lien_jeudi = id_lien_jeudi;
            attribution.Id_lien_vendredi = id_lien_vendredi;
            attribution.Id_lien_samedi = id_lien_samedi;
            attribution.Id_lien_dimanche = id_lien_dimanche;
        }
    }

    public void set_attribution_rdv(int id_attribution, int id_rdv) {
        Attribution attribution = get_attribution(id_attribution);
        if (attribution != null) {
            attribution.Id_rdv = id_rdv;
        }
    }

    public void set_attribution_Lundi(int id_attribution, int id_lien_lundi) {
        Attribution attribution = get_attribution(id_attribution);
        if (attribution != null) {
            attribution.Id_lien_lundi= id_lien_lundi;
        }
    }
    public void set_attribution_Mardi(int id_attribution, int id_lien_mardi) {
        Attribution attribution = get_attribution(id_attribution);
        if (attribution != null) {
            attribution.Id_lien_mardi = id_lien_mardi;
        }
    }

    public void set_attribution_Mercredi(int id_attribution, int id_lien_mercredi) {
        Attribution attribution = get_attribution(id_attribution);
        if (attribution != null) {
            attribution.Id_lien_mercredi = id_lien_mercredi;
        }
    }

    public void set_attribution_Jeudi(int id_attribution, int id_lien_jeudi) {
        Attribution attribution = get_attribution(id_attribution);
        if (attribution != null) {
            attribution.Id_lien_jeudi = id_lien_jeudi;
        }
    }

    public void set_attribution_Vendredi(int id_attribution, int id_lien_vendredi) {
        Attribution attribution = get_attribution(id_attribution);
        if (attribution != null) {
            attribution.Id_lien_vendredi = id_lien_vendredi;
        }
    }

    public void set_attribution_Samedi(int id_attribution, int id_lien_samedi) {
        Attribution attribution = get_attribution(id_attribution);
        if (attribution != null) {
            attribution.Id_lien_samedi = id_lien_samedi;
        }
    }

    public void set_attribution_Dimanche(int id_attribution, int id_lien_dimanche) {
        Attribution attribution = get_attribution(id_attribution);
        if (attribution != null) {
            attribution.Id_lien_dimanche = id_lien_dimanche;
        }
    }
    /*
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");

        PrintWriter out = resp.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Attribution</title>");
        out.println("</head>");
        out.println("<body>");
        //set_attribution_Mercredi(33000, 0);
        //updateXML();
        out.println("<p>Id_rdv: " + get_id_rdv(33000) + "</p>");
        out.println("<p>Id_lien_lundi: " + get_id_lien_lundi(33000) + "</p>");

        

        out.println("</body>");
        out.println("</html>");
        out.close();
    }
     */
    
}