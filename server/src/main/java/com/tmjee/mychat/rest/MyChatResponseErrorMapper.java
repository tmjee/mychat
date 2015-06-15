package com.tmjee.mychat.rest;

import org.glassfish.jersey.server.spi.ResponseErrorMapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author tmjee
 */
@Provider
public class MyChatResponseErrorMapper implements ResponseErrorMapper {

    private static final Logger LOGGER = Logger.getLogger(MyChatResponseErrorMapper.class.getName());

    @Override
    public Response toResponse(Throwable throwable) {
        LOGGER.log(Level.WARNING, "", throwable);

        return Response.serverError()
                .build();
    }
}