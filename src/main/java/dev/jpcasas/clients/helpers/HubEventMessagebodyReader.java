package dev.jpcasas.clients.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.plugins.providers.sse.EventInput;
import org.jboss.resteasy.plugins.providers.sse.SseEventInputImpl;


@Provider
@Consumes("text/event-stream")
public class HubEventMessagebodyReader implements MessageBodyReader<EventInput> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }


    @Override
    public EventInput readFrom(Class<EventInput> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException {
        return new SseEventInputImpl(annotations, mediaType, mediaType, httpHeaders, entityStream);
    }

}
