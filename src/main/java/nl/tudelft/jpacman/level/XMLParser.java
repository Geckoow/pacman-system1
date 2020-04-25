package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.PacmanConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Scanner;

public class XMLParser {
    public static char[][] ParseMap(String file) {
        try {
            File outFile = new File("src/main/resources/"+file);

            Hashtable<String, Integer> tile = new Hashtable<>();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(outFile);

            Element root = doc.getDocumentElement();
            Node layer = root.getElementsByTagName("layer").item(0);
            int height = Integer.parseInt(root.getAttributes().getNamedItem("height").getTextContent());
            int width = Integer.parseInt(root.getAttributes().getNamedItem("width").getTextContent());
            NodeList tileset = root.getElementsByTagName("tile");
            for(int n =0; n < tileset.getLength() ; n++){
                int id = Integer.parseInt(tileset.item(n).getAttributes().getNamedItem("id").getTextContent());
                String type = tileset.item(n).getAttributes().getNamedItem("type").getTextContent();
                tile.put(type,id);
            }
            Scanner scan = new Scanner(layer.getTextContent());
            scan.useDelimiter(",|\n");



            char[][] board = new char[width][height];
            int i = 0;
            int j = 0;
            String out;
            while(scan.hasNext()){
                out = scan.next();
                if(!(out.contains(" ")||out.length()==0)) {
                    if (i == width) {
                        i = 0;
                        j = j + 1;
                    }
                    board[i][j] = getString(Integer.parseInt(out),tile);
                    i += 1;
                }
            }

            return board;
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static char getString(int c, Hashtable<String, Integer> tile){
        c--;
        if(c == tile.get("Ground")) {
            return ' ';
        }else if(c == tile.get("Wall")) {
            return '#';
        }else if(c == tile.get("Pellet")) {
            return '.';
        }else if(c == tile.get("Ghost")) {
            return 'G';
        }else if(c == tile.get("Player")) {
            return 'P';
        }else if(c == tile.get("PowerPill")) {
            return 'o';
        }else if(c == tile.get("Fruit")) {
            return 'f';
        }else {
            throw new PacmanConfigurationException("Invalid character "+ c);
        }
    }
}
