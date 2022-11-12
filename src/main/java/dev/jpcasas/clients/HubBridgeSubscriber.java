package dev.jpcasas.clients;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.SseEventSource;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import dev.jpcasas.repositories.ConfigurationRepo;
import io.quarkus.runtime.ShutdownEvent;
import lombok.extern.java.Log;

@Log
@Singleton
public class HubBridgeSubscriber {

    @ConfigProperty(name = "hue.api.events.path")
    String ssePath;

    @Inject
    ConfigurationRepo crepo;;

    private SseEventSource eventSource = null;

    public SseEventSource getEventSource(String hubip, String appId)
            throws NoSuchAlgorithmException, KeyManagementException {

        log.info("Subscribe for event  Philips Hub");
        if (eventSource == null) {

            Client client = ClientTools.getClient();
            client.register(new AddHeaderOnRequestFilter(appId));
            String sseAddress = String.format("https://%s%s", hubip, ssePath);
            WebTarget target = client.target(sseAddress);

            eventSource = SseEventSource.target(target).reconnectingEvery(5, TimeUnit.SECONDS).build();

            eventSource.open();
            log.info("SSE CREATED AND OPENED");

        }

        return eventSource;

    }

    void onStop(@Observes ShutdownEvent ev) {
        eventSource.close();
    }

}
