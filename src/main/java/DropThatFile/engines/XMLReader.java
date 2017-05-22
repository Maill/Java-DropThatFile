package DropThatFile.engines;

import jdk.internal.org.xml.sax.SAXException;
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

    //Singleton
    private static XMLReader instance;

    private Logger log = LogManagement.getInstanceLogger(this);

    private HashMap<String, String> elements = new HashMap<>();

    //Constructeur privé
    private XMLReader(){
        readConfigXML();
    }

    //synchronised ==> 1 seul thread à la fois
    public static synchronized XMLReader Instance(){
        if(instance != null){
            return instance;
        } else {
            instance = new XMLReader();
            return instance;
        }
    }

    public void readConfigXML() {
        Document dom;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            //region Eléments du parser XML
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse("config.xml");
            Element doc = dom.getDocumentElement();
            //endregion

            //region <urlapi>
            String urlAPI = null;
            urlAPI = getTextValue(urlAPI, doc, "urlapi");
            if (urlAPI != null) {
                if (!urlAPI.isEmpty())
                    elements.put("urlAPI", urlAPI);
            }
            //endregion
            //region <lasttoken>
            String lastToken = null;
            lastToken = getTextValue(lastToken, doc, "lasttoken");
            if (lastToken != null) {
                if (!lastToken.isEmpty())
                    elements.put("lastToken", urlAPI);
            }
            //endregion
            //region <networkconfiguration> => <user>
            String netUser = null;
            netUser = getTextValue(netUser, doc, "user");
            if (netUser != null) {
                if (!netUser.isEmpty())
                    elements.put("netUser", netUser);
            }
            //endregion
            //region <networkconfiguration> => <password>
            String netPassword = null;
            netPassword = getTextValue(netPassword, doc, "password");
            if (netPassword != null) {
                if (!netPassword.isEmpty())
                    elements.put("netPassword", netPassword);
            }
            //endregion
            //region <networkconfiguration> => <port>
            String netPort = null;
            netPassword = getTextValue(netPassword, doc, "port");
            if (netPassword != null) {
                if (!netPassword.isEmpty())
                    elements.put("netPort", netPassword);
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

    private String getTextValue(String def, Element doc, String tag) {
        String value = def;
        NodeList nl;
        nl = doc.getElementsByTagName(tag);
        if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) {
            value = nl.item(0).getFirstChild().getNodeValue();
        }
        return value;
    }
}
