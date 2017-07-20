package DropThatFile.engines.APIData;

import DropThatFile.GlobalVariables;
import DropThatFile.engines.XMLFields;
import DropThatFile.engines.XMLReader;
import com.sun.org.apache.xerces.internal.impl.dv.dtd.XML11DTDDVFactoryImpl;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nicol on 21/03/2017.
 *
 * Classe d'initialisation / de préparation de la connexion.
 */
public abstract class APIConnector {

    protected String baseURL = XMLReader.Instance().get(XMLFields.URL_API);

    public JSONObject readFromUrl(String URL, List<NameValuePair> content) throws Exception{
        JSONObject jsonObject = null;

        try {
            URL url = new URL(URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty( "charset", "utf-8");
            if(!URL.contains("configuration") && !URL.contains("accounts/login")){
                conn.setRequestProperty("Authorization", GlobalVariables.currentUser.getToken());
            }

            if(content != null){
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(content));
                writer.flush();
                writer.close();
                os.close();
            }

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

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return jsonObject;
        }
    }

    public List<NameValuePair> buildPOSTList(List<NameValuePair> listArguments, String contentName,String contentPOST){
        if (listArguments == null && contentPOST == null) return null;
        if (contentPOST == null || contentName == null) return listArguments;
        if (listArguments == null) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(contentName, contentPOST));
            return params;
        }
        listArguments.add(new BasicNameValuePair(contentName, contentPOST));
        return listArguments;
    }

    public java.util.Date getDateFrommString(String date) throws Exception{
        DateFormat format = new SimpleDateFormat("yyyy-M-dd", Locale.FRANCE);
        return format.parse(date.substring(0, 10));
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
