package dev.jpcasas.model.hub;

import java.util.Arrays;
import java.util.List;

import dev.jpcasas.model.Configuration;
import dev.jpcasas.model.SettingsEnum;
import lombok.Data;

@Data
public class HubInformation {

    String id;
    String internalipaddress;
    Integer port;

    public List<Configuration> toConfiguration() {
        return Arrays.asList(new Configuration(SettingsEnum.HUB_IP.getKey(), internalipaddress),
                new Configuration(SettingsEnum.HUB_PORT.getKey(), port.toString()));
    }

   

}
