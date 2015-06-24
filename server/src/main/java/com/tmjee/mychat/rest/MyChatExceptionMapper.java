package com.tmjee.mychat.rest;

import com.tmjee.mychat.exception.InvalidAccessTokenException;
import com.tmjee.mychat.exception.InvalidApplicationTokenException;
import com.tmjee.mychat.exception.MyChatException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author tmjee
 */
@Provider
public class MyChatExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(MyChatExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable throwable) {

        if (throwable.getClass().isAssignableFrom(InvalidApplicationTokenException.class)) {
            V1.Res res = new V1.Res();
            res.valid = false;
            res.addMessage(throwable.getMessage());
            LOGGER.log(Level.FINEST, "", throwable);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(res)
                    .build();
        } else if (throwable.getClass().isAssignableFrom(InvalidAccessTokenException.class)) {
            V1.Res res = new V1.Res();
            res.valid = false;
            res.addMessage(throwable.getMessage());
            LOGGER.log(Level.FINEST, "", throwable);
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
