package nl.tudelft.jpacman.ui;

import nl.tudelft.jpacman.LevelInformation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MenuParser {
    final File outFile = new File("src/main/resources/Informations/Informations.xml");

    /**
     * get list of classical levels
     *
     * @return classical levels
     */
    public ArrayList<LevelInformation> listSimplesLevels() {
        ArrayList<LevelInformation> levels = new ArrayList<>();


        Element root = getDocument().getDocumentElement();
        NodeList levelList = root.getElementsByTagName("SimpleLevel");

        for (int i = 0; i < levelList.getLength(); i++) {
            Node level = levelList.item(i);
            LevelInformation newLevel = new LevelInformation(level.getTextContent(), Integer.parseInt(level.getAttributes().getNamedItem("Score").getTextContent()));
            levels.add(newLevel);
        }

        return levels;
    }

    /**
     * get list of campaigns.
     *
     * @return the list of campaigns
     */
    public ArrayList<String> listCampaign() {
        ArrayList<String> campaigns= new ArrayList<>();


        Element root = getDocument().getDocumentElement();
        NodeList campaignList = root.getElementsByTagName("Campaign");

        for (int i = 0; i < campaignList.getLength(); i++) {
            Node campaign = campaignList.item(i);
            campaigns.add(campaign.getAttributes().getNamedItem("name").getTextContent());
        }

        return campaigns;
    }

    /**
     * get list level of a specific campaigns.
     *
     * @param campaign specific campaigns.
     * @return list of campaign level.
     */
    public ArrayList<LevelInformation> listCampaignLevels(String campaign) {
        ArrayList<LevelInformation> levels = new ArrayList<>();

        Element root = getDocument().getDocumentElement();

        NodeList campaignList = root.getElementsByTagName("Campaign");

        for (int i = 0; i < campaignList.getLength(); i++) {
            Node node = campaignList.item(i);
            if (node.getAttributes().getNamedItem("name").getTextContent().equals(campaign)) {
                Node level = node.getFirstChild();
                while (level != null) {
                    if (level instanceof Element) {
                        LevelInformation newLevel = new LevelInformation(level.getTextContent(), Integer.parseInt(level.getAttributes().getNamedItem("Score").getTextContent()));
                        levels.add(newLevel);
                    }
                    level = level.getNextSibling();
                }
                return levels;
            }
        }

        return levels;
    }

    /**
     * get the document information.
     * @return document information.
     */
    public Document getDocument() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            return builder.parse(outFile);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * save the change make.
     * @param doc the document to change.
     */
    public void saveChange(Document doc) {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(outFile);
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save progression of classical level
     * @param levels classical levels
     */
    public void saveClassicalLevel(ArrayList<LevelInformation> levels){
        Document doc = getDocument();
        Element root = doc.getDocumentElement();
        NodeList levelList = root.getElementsByTagName("SimpleLevel");

        for (int i = 0; i < levelList.getLength(); i++) {
            Node level = levelList.item(i);
            level.getAttributes().getNamedItem("Score").setTextContent(Integer.toString(levels.get(i).getPoint()));
        }

        saveChange(doc);
    }

    /**
     * save campaign progression
     * @param levels actual levels
     * @param campaign campaign in progress
     */
    public void saveCampaignLevel(ArrayList<LevelInformation> levels, String campaign){
        Document doc = getDocument();
        Element root = doc.getDocumentElement();
        NodeList campaignList = root.getElementsByTagName("Campaign");

        for (int i = 0; i < campaignList.getLength(); i++) {
            Node node = campaignList.item(i);
            if (node.getAttributes().getNamedItem("name").getTextContent().equals(campaign)) {
                int j = 0;
                Node level = node.getFirstChild();
                while (level != null) {
                    if (level instanceof Element) {
                        level.getAttributes().getNamedItem("Score").setTextContent(Integer.toString(levels.get(j).getPoint()));
                        j ++;
                    }
                    level = level.getNextSibling();
                }
                break;
            }
        }

        saveChange(doc);
    }

    /**
     * reinitialize all levels score to 0.
     */
    public void reinitialize(){
        Document doc = getDocument();
        Element root = doc.getDocumentElement();
        NodeList campaignList = root.getElementsByTagName("Campaign");
        NodeList levelList = root.getElementsByTagName("SimpleLevel");

        for (int i = 0; i < levelList.getLength(); i++) {
            Node level = levelList.item(i);
            level.getAttributes().getNamedItem("Score").setTextContent(Integer.toString(0));
        }


        for (int i = 0; i < campaignList.getLength(); i++) {
            Node node = campaignList.item(i);
            NodeList levelListCamp = node.getChildNodes();
            for(int j = 0; j < levelListCamp.getLength(); j++){
                if (levelListCamp.item(j) instanceof Element) {
                    levelListCamp.item(j).getAttributes().getNamedItem("Score").setTextContent(Integer.toString(0));
                }
            }
        }

        saveChange(doc);
    }

}

