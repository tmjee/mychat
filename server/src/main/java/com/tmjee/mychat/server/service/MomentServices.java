package com.tmjee.mychat.server.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tmjee.mychat.common.domain.MomentTypeEnum;
import com.tmjee.mychat.server.jooq.generated.Tables;
import com.tmjee.mychat.server.jooq.generated.tables.records.MomentRecord;
import com.tmjee.mychat.server.rest.ListMoments;
import com.tmjee.mychat.server.rest.PostMoment;
import com.tmjee.mychat.server.service.annotations.AccessTokenAnnotation;
import com.tmjee.mychat.server.service.annotations.ApplicationTokenAnnotation;
import com.tmjee.mychat.server.service.annotations.DSLContextAnnotation;
import com.tmjee.mychat.server.service.annotations.TransactionAnnotation;
import com.tmjee.mychat.server.utils.IOUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import java.io.IOException;
import java.sql.Timestamp;

/**
 * @author tmjee
 */
public class MomentServices {

    private final Provider<DSLContext> dslProvider;

    @Inject
    public MomentServices(@DSLContextAnnotation Provider<DSLContext> dslProvider) {
       this.dslProvider = dslProvider;
    }


    @TransactionAnnotation
    @ApplicationTokenAnnotation
    @AccessTokenAnnotation
    public PostMoment.Res postMoment(PostMoment.Req req) throws IOException {
        DSLContext dsl = dslProvider.get();

        byte[] b = null;
        byte[] eb = null;
        String fileName = null;
        String mimeType = "text/plain";
        MomentTypeEnum momentType = MomentTypeEnum.TEXT;
        if (req.is != null) {
            b = IOUtils.toBytes(req.is);
            eb = IOUtils.base64Encoded(b);
            fileName = req.b.getContentDisposition().getFileName() == null ?
                        req.b.getName():
                        req.b.getContentDisposition().getFileName();
            mimeType = req.b.getMediaType().toString();
            if (mimeType != null) {
                if (mimeType.toLowerCase().startsWith("audio")) {
                    momentType = MomentTypeEnum.AUDIO;
                } else if (mimeType.toLowerCase().startsWith("video")) {
                    momentType = MomentTypeEnum.VIDEO;
                } else if (mimeType.toLowerCase().startsWith("image")) {
                    momentType = MomentTypeEnum.IMAGE;
                } else {
                    momentType = MomentTypeEnum.UNKNOWN;
                }
            }

        }
        MomentRecord momentRecord =
            dsl.insertInto(Tables.MOMENT)
                .columns(Tables.MOMENT.MYCHAT_USER_ID,
                        Tables.MOMENT.MESSAGE,
                        Tables.MOMENT.BYTES,
                        Tables.MOMENT.ENCODED,
                        Tables.MOMENT.CREATION_DATE,
                        Tables.MOMENT.TYPE,
                        Tables.MOMENT.MIME_TYPE,
                        Tables.MOMENT.FILENAME)
                .values(req.myChatUserId,
                        req.message,
                        b,
                        eb,
                        new Timestamp(System.currentTimeMillis()),
                        momentType.name(),
                        mimeType,
                        fileName
                )
                .returning()
                .fetchOne();

        return PostMoment.Res.success(momentRecord);
    }



    @TransactionAnnotation
    @ApplicationTokenAnnotation
    @AccessTokenAnnotation
    public ListMoments.Res listMoments(ListMoments.Req req) {
        DSLContext dsl = dslProvider.get();

        Result<Record> r =
        dsl.select()
                .from(Tables.MOMENT)
                .leftOuterJoin(Tables.MYCHAT_USER).on(Tables.MYCHAT_USER.MYCHAT_USER_ID.eq(Tables.MOMENT.MYCHAT_USER_ID))
                .leftOuterJoin(Tables.PROFILE).on(Tables.PROFILE.MYCHAT_USER_ID.eq(Tables.MOMENT.MYCHAT_USER_ID))
                .where(Tables.MOMENT.MYCHAT_USER_ID.eq(req.myChatUserId))
                .fetch();


        return ListMoments.Res.success(r);
    }
}
