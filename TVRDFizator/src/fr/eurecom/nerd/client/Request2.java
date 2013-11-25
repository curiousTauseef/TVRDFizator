//   nerd4java - A java library which provides a programmable interface to NERD
//               http://nerd.eurecom.fr
//
//   Copyright 2012
//
//   Authors:
//      Giuseppe Rizzo <giuse.rizzo@gmail.com>
//
// This program is free software; you can redistribute it and/or modify it
// under the terms of the GNU General Public License published by
// the Free Software Foundation, either version 3 of the License, or (at 
// your option) any later version. See the file Documentation/GPL3 in the
// original distribution for details. There is ABSOLUTELY NO warranty.


package fr.eurecom.nerd.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

public class Request2 {

	
	//private static int timeout = 600000;  //milliseconds, 10 min timeout
    protected enum RequestType {
      GET,
      POST
    }    
    
    protected static synchronized String request(
                                        String uri,
                                        RequestType method, 
                                        MultivaluedMap<String, String> queryParams
                                    )
    {
    	HttpClient client = new DefaultHttpClient(); 

        
//        System.out.println("TIMEOUTS= "+client.getProperties().get(ClientConfig.PROPERTY_READ_TIMEOUT)+"    "+client.getProperties().get(ClientConfig.PROPERTY_READ_TIMEOUT));
//        client.setReadTimeout(timeout);
//        client.setConnectTimeout(timeout);
//
//        System.out.println("TIMEOUTS= "+client.getProperties().get(ClientConfig.PROPERTY_READ_TIMEOUT)+"    "+client.getProperties().get(ClientConfig.PROPERTY_READ_TIMEOUT));

        //WebResource webResource = client.resource(uri);
        
        String json = null;
        
        

    	List<NameValuePair> nameValuePairs = new LinkedList<NameValuePair>();
    	Iterator<String> paramsIterator = queryParams.keySet().iterator();

        
        
        switch (method) {
        case GET:


        	while  (paramsIterator.hasNext()){
        		String param = paramsIterator.next();
        		List<String> listValues = queryParams.get(param);
        		String value= "";
        		for (int v = 0; v < listValues.size(); v ++){
        			value = value + listValues.get(v);
        			if (v+1 != listValues.size()) value = value + ",";
        		}
        		BasicNameValuePair pair = new BasicNameValuePair(param, value);
        		nameValuePairs.add(pair);
        	}
        	
    		String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");

        	uri = uri +paramString;
        	HttpGet get = new HttpGet(uri);


        	try {
        		HttpResponse response = client.execute(get);

        		json = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

        	} catch (IOException e) {
        		e.printStackTrace();
        	}

            
            
/*            json = webResource.
                        queryParams(queryParams).
                        accept(MediaType.APPLICATION_JSON_TYPE).
                        get(String.class);*/
            break;
            
        case POST:
        	
        	
            HttpPost post = new HttpPost(uri);
            try {

            	
            	
              while  (paramsIterator.hasNext()){
            	  String param = paramsIterator.next();
            	  List<String> listValues = queryParams.get(param);
            	  String value= "";
            	  for (int v = 0; v < listValues.size(); v ++){
            		  value = value + listValues.get(v);
            		  if (v+1 != listValues.size()) value = value + ",";
            	  }
            	  BasicNameValuePair pair = new BasicNameValuePair(param, value);
                  nameValuePairs.add(pair);
              }
                  
              post.setEntity(new UrlEncodedFormEntity(nameValuePairs));    
              HttpResponse response = client.execute(post);
       
              json = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

            } catch (IOException e) {
              e.printStackTrace();
            }
            
            
            
            
/*            
            json = webResource.
                        accept(MediaType.APPLICATION_JSON_TYPE). 
                        post(String.class, queryParams);
            break;*/

        default:
            break;
        }
        
        return json;
    }
}
