package DropThatFile.engines.APIData;

import DropThatFile.GlobalVariables;
import DropThatFile.engines.XMLFields;
import DropThatFile.engines.XMLReader;
import com.sun.org.apache.xerces.internal.impl.dv.dtd.XML11DTDDVFactoryImpl;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by Nicol on 21/03/2017.
 *
 * Classe d'initialisation / de préparation de la connexion.
 */
public abstract class APIConnector {

    protected String baseURL = XMLReader.Instance().get(XMLFields.URL_API);

    public JSONObject readFromUrl(String URL){
        JSONObject jsonObject = null;

        try {
            URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", GlobalVariables.currentUser.getToken());

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            StringBuilder jsonString = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                jsonString.append(output);
            }

            jsonObject = new JSONObject(jsonString.toString());

            conn.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return jsonObject;
        }
    }

    public static void login(){

    }
}
