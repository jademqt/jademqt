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

public class Voiture_srv {
    
    class Voiture {
        public int Id_voiture;
        public int Nbr_max_place;
        

        Voiture( int Id_voiture, int Nbr_max_place){
            this.Id_voiture = Id_voiture ;
            this.Nbr_max_place = Nbr_max_place;
        }
    }
    
    Document document;
    Map<Integer,Voiture> hashVoiture = new HashMap<>();
    
  

    public Voiture_srv(){        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.document = builder.parse("DATA/Voiture.xml");
            remplir_hash();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int prochain_id_a_utiliser(){
        int plus_grand_id = 0;
        for (Voiture voiture : hashVoiture.values()) {
            if (voiture.Id_voiture > plus_grand_id){
                plus_grand_id = voiture.Id_voiture;
            }
        }    
        return plus_grand_id + 1;
    }

    public void remplir_hash() {
        NodeList voitureNodes = document.getElementsByTagName("Voiture");
        for (int i = 0; i < voitureNodes.getLength(); i++) {
            Element voitureElement = (Element) voitureNodes.item(i);
            int id_voiture = Integer.parseInt(voitureElement.getElementsByTagName("Id_voiture").item(0).getTextContent());
            int nbr_max_place = Integer.parseInt(voitureElement.getElementsByTagName("Nbr_max_place").item(0).getTextContent());
    
            Voiture voiture = new Voiture(id_voiture, nbr_max_place);
            hashVoiture.put(id_voiture, voiture);
        }
    }
    

    public void updateXML() {
        try {
            Fichier_srv.copier_fichier("DATA/Voiture.xml", "DATA/VoitureCopie.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
       
        try {
            Fichier_srv.clear_fichier("DATA/Voiture.xml");
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
    
            Element voituresElement = newDocument.createElement("Voitures");
            newDocument.appendChild(voituresElement);
    
            Set<Integer> keys = hashVoiture.keySet();
            for (Integer key : keys) {
                Voiture voiture = hashVoiture.get(key);
                Element voitureElement = newDocument.createElement("Voiture");
    
                Element idVoitureElement = newDocument.createElement("Id_voiture");
                idVoitureElement.setTextContent(String.valueOf(voiture.Id_voiture));
                voitureElement.appendChild(idVoitureElement);
    
                Element nbrMaxPlaceElement = newDocument.createElement("Nbr_max_place");
                nbrMaxPlaceElement.setTextContent(String.valueOf(voiture.Nbr_max_place));
                voitureElement.appendChild(nbrMaxPlaceElement);
    
                voituresElement.appendChild(voitureElement);
            }
    
            this.document = newDocument;
    
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(newDocument);
            StreamResult result = new StreamResult(new FileOutputStream("DATA/Voiture.xml"));
    
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
    public Voiture get_voiture(int id_voiture){
        return hashVoiture.get(id_voiture);
    }

    public int get_nbr_max_place(int id_voiture){
        return hashVoiture.get(id_voiture).Nbr_max_place;
    }

    /**
     * Toutes les fonctions set (permettent de changer des champs depuis l'ID)
     */
    public void set_voiture(int id_voiture, int nbr_max_place){
        Voiture voiture = get_voiture(id_voiture);
        if(voiture != null){
            voiture.Nbr_max_place = nbr_max_place;
        }
    }

    public void set_nbr_max_place(int id_voiture, int nbr_max_place){
        Voiture voiture = get_voiture(id_voiture);
        if(voiture != null){
            voiture.Nbr_max_place = nbr_max_place;
        }
    }

    /**
     * La fonction add (permet de créer une nouvelle voiture si l'ID n'est pas déjà utilisé)
     */
    public void add_voiture(int id_voiture, int nbr_max_place){
        Voiture voiture = get_voiture(id_voiture);
        if(voiture == null){
            Voiture newVoiture = new Voiture(id_voiture, nbr_max_place);
            hashVoiture.put(id_voiture, newVoiture);
        }
    }

    /**
     * La fonction del (permet de supprimer une voiture existante)
     */
    public void del_voiture(int id_voiture){
        Voiture voiture = get_voiture(id_voiture);
        if(voiture != null){
            hashVoiture.remove(id_voiture);
        }
    }



    // @Override
    // protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //     // Vérifier si le lieu avec l'ID 90001 existe dans le dictionnaire
    //     if (hashVoiture.containsKey(22000)) {
    //         int test = get_nbr_max_place(22000);
    //         del_voiture(22001);
        
    //         int test2 = get_nbr_max_place(22000);
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

    
