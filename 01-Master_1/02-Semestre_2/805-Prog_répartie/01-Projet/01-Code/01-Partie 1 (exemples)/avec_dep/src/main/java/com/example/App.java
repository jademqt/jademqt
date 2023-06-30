package com.example;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class App {
    public static void main(String[] args) {
        try {
            // Chargement du fichier XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("/home/adm_jma/Documents/805/Partie_1/avec_dep/src/main/java/com/example/chevaux.xml");

            // Récupération de la liste des nœuds cheval
            NodeList chevalNodes = document.getElementsByTagName("cheval");

            // Parcours des nœuds cheval
            for (int i = 0; i < chevalNodes.getLength(); i++) {
                Element chevalElement = (Element) chevalNodes.item(i);

                // Récupération des informations du cheval
                String nom = getElementText(chevalElement, "nom");
                String dateNaissanceStr = getElementText(chevalElement, "date_naissance");

                // Vérification si le jour de naissance est pair
                boolean isJourPair = StringUtils.isNumeric(StringUtils.substring(dateNaissanceStr, 8, 10))
                        && Integer.parseInt(StringUtils.substring(dateNaissanceStr, 8, 10)) % 2 == 0;

                // Affichage du nom du cheval si le jour de naissance est pair
                if (isJourPair) {
                    System.out.println("Nom du cheval né un jour pair : " + nom);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour récupérer le texte d'un élément dans le document XML
    private static String getElementText(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        Element tagElement = (Element) nodeList.item(0);
        return tagElement.getTextContent();
    }
}
