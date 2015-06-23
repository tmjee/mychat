package com.tmjee.mychat.rest;

import com.google.inject.Injector;
import com.tmjee.mychat.MyChatFunction;
import com.tmjee.mychat.MyChatGuiceServletContextListener;
import com.tmjee.mychat.exception.MyChatException;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author tmjee
 */
@Path("/v1")
public class V1<REQ extends V1.Req, RES extends V1.Res> {


    protected Response action(REQ req, MyChatFunction<REQ, RES> f) {

            req.validate();
            if (req.hasValidationErrors()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorEntity(req.errors)).build();
            }

            Res res = f.apply(req);

            if (req.hasValidationErrors()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorEntity(req.errors)).build();
            }

            return Response.ok().entity(res).build();
    }


    protected <I> I getInstance(Class<I> iClass) {
        return getInjector().getInstance(iClass);
    }

    protected Injector getInjector() {
        Injector injector = MyChatGuiceServletContextListener.getV1Injector();
        return injector;
    }



    private static class ErrorEntity {
        public final List<String> errors = new ArrayList<>();

        public ErrorEntity(List<String> errors) {
            errors.addAll(errors);
        }
    }




    public static abstract class Req {

        public String applicationToken;
        public List<String> errors = new ArrayList<>();

        protected boolean hasValidationErrors() {
            return (!errors.isEmpty());
        }

        protected void addValidationError(String error) {
            errors.add(error);
        }

        protected abstract void validate();
    }


    public static class Res {

        public List<String> messages = new ArrayList<>();
        public boolean valid = true;

        protected void addMessage(String message) {
            messages.add(message);
        }

    }
}
