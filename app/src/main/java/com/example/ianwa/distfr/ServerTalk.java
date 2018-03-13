package com.example.ianwa.distfr;
import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

//import javax.net.ssl.HttpsURLConnection;

/**
 * Created by ianwa on 2/27/2018.
 * class to communicate with server
 */

class ServerTalk {

    static String comm(HashMap<String, String> params){
        String result = "";

        try {
            String site = "https://zeblin.bid/openface?";
            String urlParameters = "";
            for(Map.Entry<String, String> e: params.entrySet()){
                urlParameters += e.getKey() + "=" + e.getValue() + "&";
            }
            urlParameters = urlParameters.substring(0 , urlParameters.length()-1);
            //System.out.println(urlParameters);

            URL fr = new URL(site+urlParameters);
            URLConnection frc = fr.openConnection();
            BufferedReader in = new BufferedReader (new InputStreamReader(frc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                result += inputLine + "#@#";
            in.close();
        }
        catch (Exception e){
            result = "Shit fucked up bro";
            e.printStackTrace();
        }
        return result;
    }
}

