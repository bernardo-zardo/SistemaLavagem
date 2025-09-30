package com.bernardo.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author Bernardo Zardo Mergen
 */
public class GeocodingUtil {

	  public static double[] buscarLatLong(String endereco) {
	        try {
	            String url = "https://nominatim.openstreetmap.org/search?q=" 
	                    + URLEncoder.encode(endereco, "UTF-8") 
	                    + "&format=json&limit=1";

	            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
	            conn.setRequestMethod("GET");

	            conn.setRequestProperty("User-Agent", "SeuSistemaLavagem/1.0");

	            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	            StringBuilder sb = new StringBuilder();
	            String line;
	            while ((line = br.readLine()) != null) {
	                sb.append(line);
	            }
	            br.close();

	            JSONArray arr = new JSONArray(sb.toString());
	            if (arr.length() > 0) {
	                JSONObject obj = arr.getJSONObject(0);
	                double lat = obj.getDouble("lat");
	                double lon = obj.getDouble("lon");
	                return new double[]{lat, lon};
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null; 
	    }
}
