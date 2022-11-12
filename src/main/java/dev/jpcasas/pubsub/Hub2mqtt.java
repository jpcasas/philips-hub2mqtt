package dev.jpcasas.pubsub;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Outgoing;

import dev.jpcasas.clients.HubBridgeSubscriber;
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

    @Outgoing("hubevents")
    @Broadcast
    @ActivateRequestContext
    public Multi<String> generate() {

        Optional<Configuration> hubip = crepo.findById(SettingsEnum.HUB_IP.getKey());
        Optional<Configuration> appId = crepo.findById(SettingsEnum.APP_ID.getKey());

        log.info("HUB OUT");
        if (hubip.isPresent() && appId.isPresent()) {

            return Multi.createFrom().emitter(consumer -> {
                try {
                    log.info("creating emitter");
                    
                    subscriber.getEventSource(hubip.get().getConfigValue(), appId.get().getConfigValue()).register(ie -> {
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
        }else{
            return Multi.createFrom().items("NO HUB FOUND OR NOT HUB's BUTTON PUSHED");
        }
    }
}
