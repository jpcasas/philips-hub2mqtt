package dev.jpcasas.model;


import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@Entity
@EqualsAndHashCode
public class Configuration {

    @Id
    String config;
    @EqualsAndHashCode.Exclude
    String configValue;

    public Configuration(String config, String configValue){
        this.config = config;
        this.configValue = configValue;
    }
    public Configuration(){
        super();
    }
    public Configuration(String config){
        this.config = config;
    }
    
}
