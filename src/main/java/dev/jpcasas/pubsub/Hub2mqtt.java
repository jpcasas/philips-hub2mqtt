package dev.jpcasas.pubsub;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Outgoing;

import dev.jpcasas.clients.HubBridgeSubscriber;
import dev.jpcasas.configuration.HubInitialConfiguration;
import dev.jpcasas.model.Configuration;
import dev.jpcasas.model.SettingsEnum;
import dev.jpcasas.repositories.ConfigurationRepo;
import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import lombok.extern.java.Log;

@Log
public class Hub2mqtt {

    @Inject
    HubBridgeSubscriber subscriber;

    @Inject
    ConfigurationRepo crepo;

    @Inject
    HubInitialConfiguration initc;

    @Outgoing("hubevents")
    @Broadcast
    @ActivateRequestContext
    public Multi<String> generate() {
        log.info("HUB OUT");
       
        while (!initc.isCongifured()) {
            initc.registerApplication();

        }
        
        String hubip = initc.getHubIp();
        String appId = initc.getAppId();

        // Don't wan to verify if it's null -> should not
        return Multi.createFrom().emitter(consumer -> {
            try {
                log.info("creating emitter");
                subscriber.getEventSource(hubip, appId).register(ie -> {
                    log.info("Sending events");
                    String data = ie.readData();
                    log.info(data);
                    consumer.emit(data);

                }, error -> {
                    error.printStackTrace();
                }, () -> {
                    log.info("END");
                });
            } catch (KeyManagementException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

        });

    }
}
