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
            urlAPI = getTextValue(doc, XMLFields.URL_API);
            if (urlAPI != null) {
                if (!urlAPI.isEmpty())
                    elements.put(XMLFields.URL_API, urlAPI);
            }
            //endregion
            //region <lasttoken>
            String lastToken;
            lastToken = getTextValue(doc, XMLFields.LAST_TOKEN);
            if (lastToken != null) {
                if (!lastToken.isEmpty())
                    elements.put(XMLFields.LAST_TOKEN, urlAPI);
            }
            //endregion
            //region <networkconfiguration> => <user>
            String netUser;
            netUser = getTextValue(doc, XMLFields.SFTP_USER);
            if (netUser != null) {
                if (!netUser.isEmpty())
                    elements.put(XMLFields.SFTP_USER, netUser);
            }
            //endregion
            //region <networkconfiguration> => <password>
            String netPassword;
            netPassword = getTextValue(doc, XMLFields.SFTP_PASSSWD);
            if (netPassword != null) {
                if (!netPassword.isEmpty())
                    elements.put(XMLFields.SFTP_PASSSWD, netPassword);
            }
            //endregion
            //region <networkconfiguration> => <port>
            String netPort;
            netPort = getTextValue(doc, XMLFields.SFTP_PORT);
            if (netPort != null) {
                if (!netPort.isEmpty())
                    elements.put(XMLFields.SFTP_PORT, netPort);
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

    //region Méthode : get
    public String get(String field){
        return this.elements.get(field);
    }
    //endregion

    //region Méthode : set
    public void set(String field, String value){
        this.elements.put(field, value);
    }
    //endregion

    //endregion
}
