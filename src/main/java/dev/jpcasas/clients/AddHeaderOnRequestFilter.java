package dev.jpcasas.clients;

import java.io.IOException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

public class AddHeaderOnRequestFilter implements ClientRequestFilter {

    
    public static final String FILTER_HEADER_KEY = "hue-application-key";

    String appId;

    public AddHeaderOnRequestFilter(String appId) {
        this.appId = appId;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
            requestContext.getHeaders().add(FILTER_HEADER_KEY, appId);
        
        
    }
}