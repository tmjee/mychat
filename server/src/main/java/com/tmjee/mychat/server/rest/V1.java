package com.tmjee.mychat.server.rest;

import com.google.inject.Injector;
import com.tmjee.mychat.server.MyChatFunction;
import com.tmjee.mychat.server.MyChatGuiceServletContextListener;
import com.tmjee.mychat.server.domain.RolesEnum;
import com.tmjee.mychat.server.exception.RoleAccessDeniedException;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * @author tmjee
 */
@Provider
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
        public String accessToken;
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


    public interface RolesAware {
        void action(EnumSet<RolesEnum> rolesEnumSet) throws RoleAccessDeniedException;
    }
}
