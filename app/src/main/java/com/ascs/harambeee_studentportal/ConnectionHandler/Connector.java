package com.ascs.harambeee_studentportal.ConnectionHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Connector {

    public static HttpURLConnection connection;


    public static Object connect (String url){

        try {
            URL json_url = new URL(url);
            connection = (HttpURLConnection) json_url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(300000);
            connection.setReadTimeout(300000);
            connection.setDoInput(true);


            return connection;

        } catch (Exception e) {
            return "error url :"+e.getMessage();
        }
    }
}
