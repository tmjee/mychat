package com.tmjee.mychat.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tmjee.mychat.server.service.annotations.DSLContextAnnotation;
import org.jooq.DSLContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;

/**
 * @author tmjee
 */
public class MyChatImageServlet extends HttpServlet {

    private static final String MOMENT_PREFIX = "/moment/";
    private static final String AVATAR_PREFIX = "/avatar/";

    public static final String MOMENT_URI_PREFIX = "/mychat/images"+ MOMENT_PREFIX;
    public static final String AVATAR_URI_PREFIX = "/mychat/images"+ AVATAR_PREFIX;



    private static final Logger LOG = Logger.getLogger(MyChatImageServlet.class.getName());



    private Provider<DSLContext> dslProvider;

    @Inject
    public void setDslProvider(@DSLContextAnnotation Provider<DSLContext> dslProvider) {
        this.dslProvider = dslProvider;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo != null) {
                if (pathInfo.startsWith(MOMENT_PREFIX)) {
                    String momentId = pathInfo.substring(MOMENT_PREFIX.length());
                    int _momentId = Integer.parseInt(momentId);

                    //dslProvider.get().selectFrom(Tables.)
                } else if (pathInfo.startsWith(AVATAR_PREFIX)) {
                    String avatarId = pathInfo.substring(AVATAR_PREFIX.length());
                    int _avatarId =  Integer.parseInt(avatarId);
                }
            }
        }catch(NumberFormatException e) {
            LOG.log(Level.WARNING, e, ()->format("failed to convert %s to image", pathInfo));
        }
        resp.setStatus(404);
    }
}
