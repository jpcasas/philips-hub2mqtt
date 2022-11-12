package dev.jpcasas.model;

public enum SettingsEnum {
    HUB_IP("hub-ip"),
    APP_ID("hub-app"),
    HUB_PORT("hub-port"), 
    APP_NAME("HUB2MQTT");

    private final String key;
    
    SettingsEnum(String name) {
        this.key = name;
    }
    public String getKey(){
        return key;
    }
}
