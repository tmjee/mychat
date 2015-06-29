package com.tmjee.mychat.server.rest;

import com.tmjee.mychat.server.exception.InvalidAccessTokenException;
import com.tmjee.mychat.server.exception.InvalidApplicationTokenException;
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
        if (throwable instanceof InvalidApplicationTokenException) {
            V1.Res res = new V1.Res();
            res.valid = false;
            res.addMessage("Invalid / Missing application token");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(res)
                    .build();
        } else if (throwable instanceof InvalidAccessTokenException) {
            V1.Res res = new V1.Res();
            res.valid = false;
            res.addMessage("Invalid / Missing access token");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(res)
                    .build();
        } else {
            LOGGER.log(Level.WARNING, "", throwable);
        }

        V1.Res res = new V1.Res();
        res.valid = false;
        res.addMessage(throwable.getMessage());
        return Response.serverError()
                .entity(res)
                .build();
    }
}