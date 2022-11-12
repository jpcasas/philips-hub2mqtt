package dev.jpcasas.controllers;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dev.jpcasas.clients.PhilipsHubClient;
import dev.jpcasas.model.Configuration;
import dev.jpcasas.model.SettingsEnum;

import dev.jpcasas.repositories.ConfigurationRepo;

import io.quarkus.runtime.StartupEvent;
import lombok.extern.java.Log;

@Log
public class Startup {
    @Inject
    ConfigurationRepo crepo;

    @Inject
    PhilipsHubClient pclient;

    @ConfigProperty(name = "hue.time.to.push", defaultValue = "1000")
    long timeRetry;

    public void onStart(@Observes StartupEvent ev) {

        Optional<Configuration> hub = crepo.findById(SettingsEnum.HUB_IP.getKey());
        List<Configuration> info = null;
        if (!hub.isPresent()) {
            try {
                info = pclient.getInformation();
                crepo.saveAll(info);
            } catch (KeyManagementException e) {

                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {

                e.printStackTrace();
            }
        } else {
            info = Arrays.asList(hub.get());
        }
        Optional<Configuration> appkeyOptional = crepo.findById(SettingsEnum.APP_ID.getKey());
        if (!appkeyOptional.isPresent() && info != null) {
            int idx = info.indexOf(new Configuration(SettingsEnum.HUB_IP.getKey()));
            if (idx >= 0) {
                String ip = info.get(idx).getConfigValue();
                String appId = null;
                ;
                long times = timeRetry / 1000;
                try {
                    while (appId == null && times >= 0) {
                        log.info("PLEASE PUSH THE BUTTON OF THE PHILIPS HUB");
                        appId = pclient.createClient(String.format("https://%s/api", ip));
                        Thread.sleep(1000);
                        times--;

                    }
                    ;
                    if (times <= 0 && appId == null) {
                        log.info("EXITING APPLICATION - CAN't GET THE AUTHORIZATION TO CONNECT");
                        System.exit(0);
                    } else {
                        crepo.save(new Configuration(SettingsEnum.APP_ID.getKey(), appId));
                    }

                } catch (KeyManagementException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JsonProcessingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        }
        log.info("START UP APPLICATION OK");
    }
}
