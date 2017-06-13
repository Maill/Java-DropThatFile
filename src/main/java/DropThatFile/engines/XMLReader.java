package DropThatFile.engines;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Nicol on 21/03/2017.
 *
 * Classe pour lecture du fichier de configuration.
 */
public class XMLReader {

    //region Attributs
    //Singleton
    private static XMLReader instance;

    private Logger log = LogManagement.getInstanceLogger(this);

    private HashMap<String, String> elements = new HashMap<>();
    //endregion

    //region Constructeur privé
    private XMLReader(){
        readConfigXML();
    }
    //endregion

    //region Méthode statique : Instance
    public static synchronized XMLReader Instance(){
        if(instance != null){
            return instance;
        } else {
            instance = new XMLReader();
            return instance;
        }
    }
    //endregion

    //region Méthode : readConfigXML
    /**
     * Lit et garde les attributs XML en mémoire.
     */
    private void readConfigXML() {
        Document dom;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            //region Eléments du parser XML
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse("config.xml");
            Element doc = dom.getDocumentElement();
            //endregion

            //region <urlapi>
            String urlAPI;
            urlAPI = getTextValue(doc, "urlapi");
            if (urlAPI != null) {
                if (!urlAPI.isEmpty())
                    elements.put("urlAPI", urlAPI);
            }
            //endregion
            //region <lasttoken>
            String lastToken;
            lastToken = getTextValue(doc, "lasttoken");
            if (lastToken != null) {
                if (!lastToken.isEmpty())
                    elements.put("lastToken", urlAPI);
            }
            //endregion
            //region <networkconfiguration> => <user>
            String netUser;
            netUser = getTextValue(doc, "user");
            if (netUser != null) {
                if (!netUser.isEmpty())
                    elements.put("netUser", netUser);
            }
            //endregion
            //region <networkconfiguration> => <password>
            String netPassword;
            netPassword = getTextValue(doc, "password");
            if (netPassword != null) {
                if (!netPassword.isEmpty())
                    elements.put("netPassword", netPassword);
            }
            //endregion
            //region <networkconfiguration> => <port>
            String netPort;
            netPort = getTextValue(doc, "port");
            if (netPort != null) {
                if (!netPort.isEmpty())
                    elements.put("netPort", netPort);
            }
            //endregion

        } catch (ParserConfigurationException pce) {
            System.out.println(pce.getMessage());
        }  catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        } catch (org.xml.sax.SAXException e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region Méthode privée : getTexteValue
    /**
     * [Méthode privée] Récupère la donnée tu tag XML.
     * @param doc Pointeur du document XML.
     * @param tag Nom de la balise XML.
     */
    private String getTextValue(Element doc, String tag) {
        String value = null;
        NodeList nl;
        nl = doc.getElementsByTagName(tag);
        if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) {
            value = nl.item(0).getFirstChild().getNodeValue();
        }
        return value;
    }
    //endregion
}
