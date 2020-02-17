package br.com.everdata.instagram.utils;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.brunocvcunha.instagram4j.InstagramConstants;
import org.brunocvcunha.instagram4j.requests.InstagramRequest;

/**
 * Simple Get Request
 * 
 * @author Eduardo Falcao
 *
 */
public class SimpleGetRequest extends InstagramRequest<String> {

	private final String BASE_URL = "https://www.instagram.com/p/";
	
    private String urlSuffix;
    
    public SimpleGetRequest(String urlSuffix) {
		this.urlSuffix = urlSuffix;
	}
    
    @Override
    public String getMethod() {
        return "GET";
    }
    
    @Override
    public String execute() throws ClientProtocolException, IOException {
        HttpGet get = new HttpGet(BASE_URL + getUrl());
        get.addHeader("Connection", "close");
        get.addHeader("Accept", "*/*");
        get.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        get.addHeader("Cookie2", "$Version=1");
        get.addHeader("Accept-Language", "en-US");
        get.addHeader("User-Agent", InstagramConstants.USER_AGENT);
        
        HttpResponse response = api.getClient().execute(get);
        api.setLastResponse(response);
        
        int resultCode = response.getStatusLine().getStatusCode();
        String content = EntityUtils.toString(response.getEntity());
        
        get.releaseConnection();

        return parseResult(resultCode, content);
    }

    @Override
    public String getUrl() {
        return urlSuffix;
    }

    @Override
    public String parseResult(int statusCode, String content) {
        return content;
    }

    @Override
    public boolean requiresLogin() {
        return false;
    }

}
