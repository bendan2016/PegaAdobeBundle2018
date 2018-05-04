package com.adobe.restservice.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.adobe.restservice.Distance;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
 
//This is a component so it can provide or consume services
@Component
   
    
@Service

public class DistanceImp implements Distance {

	@Override
    public String getDistance() {
        // TODO Auto-generated method stub
         
        try
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();
              
            HttpGet getRequest = new HttpGet("https://maps.googleapis.com/maps/api/distancematrix/json?key=AIzaSyAP-hUtIh8NaYxhXNRC3Jum_83JEC8oTQM&origins=Vancouver%20BC&destinations=San%20Francisco&sensor=false");
            getRequest.addHeader("accept", "application/json");
 
            HttpResponse response = httpClient.execute(getRequest);
 
            if (response.getStatusLine().getStatusCode() != 200) {
                            throw new RuntimeException("Failed : HTTP error code : "
                                    + response.getStatusLine().getStatusCode());
                        }
 
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
 
            String output;
             String myJSON="" ;
                while ((output = br.readLine()) != null) {
                    //System.out.println(output);
                    myJSON = myJSON + output;
                }
 
              
            httpClient.getConnectionManager().shutdown();
            return myJSON ;
        }
         
        catch (Exception e)
        {
            e.printStackTrace() ; 
        }
         
        return null;
    }
 

}
