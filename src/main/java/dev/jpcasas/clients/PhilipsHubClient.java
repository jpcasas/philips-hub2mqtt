package dev.jpcasas.clients;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.jpcasas.model.Configuration;
import dev.jpcasas.model.SettingsEnum;
import dev.jpcasas.model.hub.CreateClientRequest;
import dev.jpcasas.model.hub.CreateClientResponse;
import dev.jpcasas.model.hub.HubInformation;


@Singleton
public class PhilipsHubClient {
    
    ObjectMapper mapper = new ObjectMapper();

    @ConfigProperty(name = "hue.hub-discover.url")
    String discoverUrl;

    public List<Configuration> getInformation() throws KeyManagementException, NoSuchAlgorithmException{
        Client c = ClientTools.getClient();
        Response res = c.target(discoverUrl).request(MediaType.APPLICATION_JSON).get();
        if(res.getStatus() == 200){
            
            return res.readEntity(HubInformation[].class)[0].toConfiguration();
            
        }
        return null;
    }
    public String createClient(String url) throws KeyManagementException, NoSuchAlgorithmException, JsonMappingException, JsonProcessingException{

        Client c = ClientTools.getClient();
        Response res = c.target(url).request(MediaType.APPLICATION_JSON).post(Entity.entity(new CreateClientRequest(SettingsEnum.APP_NAME.getKey()), MediaType.APPLICATION_JSON));
        if(res.getStatus() == 200){
            String response = res.readEntity(String.class);
            if(response.contains("link button not pressed")){
                return null;
            }else{
                CreateClientResponse ccr = mapper.readValue(response, CreateClientResponse[].class)[0];
                return ccr.getSuccess().getUsername();
            }
            
        }

        return null;
    }

   
  
 

}
