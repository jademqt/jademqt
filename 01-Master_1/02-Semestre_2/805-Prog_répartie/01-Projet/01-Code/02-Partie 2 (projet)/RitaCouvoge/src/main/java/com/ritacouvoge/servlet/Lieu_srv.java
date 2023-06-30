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

public class Lieu_srv {
    
    class Lieu {
        public int Id_lieu;
        public int Code_postal;
        public String Adresse;
        public String Ville;
        public String Description;

        Lieu( int Id_lieu, int Code_postal, String Adresse, String Ville, String Description){
            this.Id_lieu = Id_lieu;
            this.Code_postal = Code_postal;
            this.Adresse = Adresse;
            this.Ville = Ville;
            this.Description = Description;
        }
    }
    
    Document document;
    Map<Integer,Lieu> hashLieu = new HashMap<>();
    
  

    public Lieu_srv(){        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.document = builder.parse("DATA/Lieu.xml");
            remplir_hash();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void remplir_hash(){
        NodeList lieuNodes = document.getElementsByTagName("Lieu");
        for (int i = 0; i < lieuNodes.getLength(); i++) {
            Element lieuElement = (Element) lieuNodes.item(i);
            int id_lieu = Integer.parseInt(lieuElement.getElementsByTagName("Id_lieu").item(0).getTextContent());
            int code_postal = Integer.parseInt(lieuElement.getElementsByTagName("Code_postal").item(0).getTextContent());
            String adresse = lieuElement.getElementsByTagName("Adresse").item(0).getTextContent();
            String ville = lieuElement.getElementsByTagName("Ville").item(0).getTextContent();
            String description = lieuElement.getElementsByTagName("Description").item(0).getTextContent();
            
            Lieu lieu = new Lieu(id_lieu, code_postal, adresse, ville, description);
            hashLieu.put(id_lieu, lieu);
        }
    }

    public void updateXML() {
        try {
            Fichier_srv.copier_fichier("DATA/Lieu.xml", "DATA/LieuCopie.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
       
        try {
            Fichier_srv.clear_fichier("DATA/Lieu.xml");
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
    
            Element lieuxElement = newDocument.createElement("Lieux");
            newDocument.appendChild(lieuxElement);
    
            Set<Integer> keys = hashLieu.keySet();
            for (Integer key : keys) {
                Lieu lieu = hashLieu.get(key);
                Element lieuElement = newDocument.createElement("Lieu");
    
                Element idElement = newDocument.createElement("Id_lieu");
                idElement.setTextContent(String.valueOf(lieu.Id_lieu));
                lieuElement.appendChild(idElement);
    
                Element codePostalElement = newDocument.createElement("Code_postal");
                codePostalElement.setTextContent(String.valueOf(lieu.Code_postal));
                lieuElement.appendChild(codePostalElement);
    
                Element adresseElement = newDocument.createElement("Adresse");
                adresseElement.setTextContent(lieu.Adresse);
                lieuElement.appendChild(adresseElement);
    
                Element villeElement = newDocument.createElement("Ville");
                villeElement.setTextContent(lieu.Ville);
                lieuElement.appendChild(villeElement);
    
                Element descriptionElement = newDocument.createElement("Description");
                descriptionElement.setTextContent(lieu.Description);
                lieuElement.appendChild(descriptionElement);
    
                lieuxElement.appendChild(lieuElement);
            }
    
            this.document = newDocument;
    
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(newDocument);
            StreamResult result = new StreamResult(new FileOutputStream("DATA/Lieu.xml"));
            
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
    public Lieu get_lieu(int id_lieu){
        return hashLieu.get(id_lieu);
    }

    public int get_code_postal(int id_lieu){
        return hashLieu.get(id_lieu).Code_postal;
    }

    public String get_adresse(int id_lieu){
        return hashLieu.get(id_lieu).Adresse;
    }

    public String get_ville(int id_lieu){
        return hashLieu.get(id_lieu).Ville;
    }

    public String get_description(int id_lieu){
        return hashLieu.get(id_lieu).Description;
    }

    /**
     * Toutes les fonctions set (permettent de changer des champs depuis l'ID)
     */
    public void set_lieu(int id_lieu, int code_postal, String adresse, String ville, String description){
        Lieu lieu = get_lieu(id_lieu);
        if( lieu != null){
            lieu.Code_postal = code_postal;
            lieu.Adresse = adresse;
            lieu.Description = description;
            lieu.Ville = ville;
        }
    }

    public void set_code_postal(int id_lieu, int code_postal ){
        Lieu lieu = get_lieu(id_lieu);
        if( lieu != null){
            lieu.Code_postal = code_postal;
        }
    }

    public void set_adresse(int id_lieu, String adresse){
        Lieu lieu = get_lieu(id_lieu);
        if( lieu != null){
            lieu.Adresse = adresse;
        }
    }

    public void set_ville(int id_lieu, String ville){
        Lieu lieu = get_lieu(id_lieu);
        if( lieu != null){
            lieu.Ville= ville;
        }
    }

    public void set_description(int id_lieu, String description){
        Lieu lieu = get_lieu(id_lieu);
        if( lieu != null){
            lieu.Description= description;
        }
    }

    /**
     * La fonction add (permet de créer un nouveau lieu si l'ID n'est pas déjà utilisé)
     */
    public void add_lieu(int id_lieu, int code_postal, String adresse, String ville, String description){
        Lieu lieu = get_lieu(id_lieu);
        if( lieu == null){
            Lieu lieu_ = new Lieu(id_lieu, code_postal, adresse, ville, description);
            hashLieu.put(id_lieu, lieu_);
        }
    }

    /**
     * La fonction del (permet de supprimer un lieu existant)
     */
    public void del_lieu(int id_lieu){
        Lieu lieu = get_lieu(id_lieu);
        if( lieu != null){
            hashLieu.remove(id_lieu);
        }
    }


    // @Override
    // protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //     // Vérifier si le lieu avec l'ID 90001 existe dans le dictionnaire
    //     if (hashLieu.containsKey(90001)) {
    //         String test = get_description(90001);
    //         del_lieu(90003);
    //         String test2 = get_description(90002);
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

    
