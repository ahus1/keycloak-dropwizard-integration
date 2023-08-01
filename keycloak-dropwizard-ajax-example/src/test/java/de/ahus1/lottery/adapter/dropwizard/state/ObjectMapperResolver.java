package de.ahus1.lottery.adapter.dropwizard.state;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ObjectMapperResolver implements ContextResolver<ObjectMapper> {
    private final ObjectMapper mapper;

    public ObjectMapperResolver() {
        mapper = Jackson.newObjectMapper();
    }

    @Override
    public ObjectMapper getContext(Class<?> cls) {
        return mapper;
    }
}
