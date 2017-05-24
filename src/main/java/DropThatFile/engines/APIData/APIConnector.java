package DropThatFile.engines.APIData;

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
public class APIConnector {

    public JSONObject readFromUrl(String URL){
        JSONObject jsonObject = null;

        try {
            URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String jsonString = "";
            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                jsonString += output;
            }

            jsonObject = new JSONObject(jsonString);

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return jsonObject;
        }
    }
}
