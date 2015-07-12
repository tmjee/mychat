package com.tmjee.mychat.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tmjee.mychat.server.jooq.generated.Tables;
import com.tmjee.mychat.server.jooq.generated.tables.records.AvatarRecord;
import com.tmjee.mychat.server.jooq.generated.tables.records.MomentRecord;
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

    public static final String MYCHAT_IMAGES_PREFIX = "/mychat/images/";

    public static final String MOMENT_URI_PREFIX = MYCHAT_IMAGES_PREFIX+"moment/";
    public static final String AVATAR_URI_PREFIX = MYCHAT_IMAGES_PREFIX+"avatar/";

    private static final String AVATAR_PREFIX = "/avatar/";
    private static final String MOMENT_PREFIX = "/moment/";



    private static final Logger LOG = Logger.getLogger(MyChatImageServlet.class.getName());



    private Provider<DSLContext> dslProvider;

    @Inject
    public void setDslProvider(@DSLContextAnnotation Provider<DSLContext> dslProvider) {
        this.dslProvider = dslProvider;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        LOG.log(Level.FINEST, format("request path %s", req.getPathInfo()));


        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo != null) {
                DSLContext dsl = dslProvider.get();

                if (pathInfo.startsWith(MOMENT_PREFIX)) {
                    String momentId = pathInfo.substring(MOMENT_PREFIX.length());
                    int _momentId = Integer.parseInt(momentId);

                    MomentRecord momentRecord = dsl.selectFrom(Tables.MOMENT)
                            .where(Tables.MOMENT.MOMENT_ID.eq(_momentId))
                            .fetchOne();

                    if (momentRecord == null) {
                        byte[] b = momentId.getBytes();

                        resp.setContentType(momentRecord.getMimeType());
                        resp.setContentLength(b.length);
                        resp.setDateHeader("Modified-Since",
                                momentRecord.getModificationDate() == null ?
                                        momentRecord.getCreationDate().getTime() :
                                        momentRecord.getModificationDate().getTime());
                        resp.getOutputStream().write(b);
                        resp.flushBuffer();
                        return;
                    }


                    return;

                } else if (pathInfo.startsWith(AVATAR_PREFIX)) {
                    String avatarId = pathInfo.substring(AVATAR_PREFIX.length());
                    int _avatarId =  Integer.parseInt(avatarId);


                    AvatarRecord avatarRecord = dsl.selectFrom(Tables.AVATAR)
                            .where(Tables.AVATAR.AVATAR_ID.eq(_avatarId))
                            .fetchOne();
                    if (avatarRecord != null) {

                        byte[] bytes = avatarRecord.getBytes();

                        resp.setContentType(avatarRecord.getMimeType());
                        resp.setContentLength(bytes.length);
                        resp.setDateHeader("Modified-Since",
                                avatarRecord.getModificationDate() == null ?
                                        avatarRecord.getCreationDate().getTime() :
                                        avatarRecord.getModificationDate().getTime());
                        resp.getOutputStream().write(bytes);
                        resp.flushBuffer();
                        return;
                    }
                }
            }
        }catch(NumberFormatException e) {
            LOG.log(Level.WARNING, e, ()->format("failed to convert %s to image", pathInfo));
        }
        resp.setStatus(404);
    }
}
