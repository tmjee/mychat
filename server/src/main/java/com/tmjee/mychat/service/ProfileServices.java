package com.tmjee.mychat.service;

import com.google.inject.Provider;
import com.tmjee.jooq.generated.Tables;
import com.tmjee.jooq.generated.tables.records.AvatarRecord;
import com.tmjee.mychat.rest.GetAvatar;
import com.tmjee.mychat.rest.PostAvatar;
import com.tmjee.mychat.rest.Profile;
import com.tmjee.mychat.service.annotations.AccessTokenAnnotation;
import com.tmjee.mychat.service.annotations.ApplicationTokenAnnotation;
import com.tmjee.mychat.service.annotations.TransactionAnnotation;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Base64;

/**
 * @author tmjee
 */
public class ProfileServices {

    private final Provider<DSLContext> dslProvider;

    public ProfileServices(Provider<DSLContext> dslProvider) {
        this.dslProvider = dslProvider;
    }


    @TransactionAnnotation
    @ApplicationTokenAnnotation
    @AccessTokenAnnotation
    public Profile.Res contactDetails(Profile.Req req) {
        DSLContext dsl = dslProvider.get();

        Record r =
        dsl.select()
                .from(Tables.MYCHAT_USER)
                .leftOuterJoin(Tables.PROFILE).on(Tables.PROFILE.MYCHAT_USER_ID.eq(Tables.MYCHAT_USER.MYCHAT_USER_ID))
                .leftOuterJoin(Tables.AVATAR).on(Tables.AVATAR.MYCHAT_USER_ID.eq(Tables.MYCHAT_USER.MYCHAT_USER_ID))
                .where(Tables.MYCHAT_USER.MYCHAT_USER_ID.eq(req.myChatUserId))
                .fetchOne();

        return Profile.Res.success(r);
    }

    @TransactionAnnotation
    @ApplicationTokenAnnotation
    @AccessTokenAnnotation
    public PostAvatar.Res postAvatar(PostAvatar.Req req) throws IOException {
        DSLContext dsl = dslProvider.get();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead = -1;
        InputStream is = req.is;
        while((bytesRead=is.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }

        Record1<Integer> count =
        dsl.selectCount()
                .from(Tables.AVATAR)
                .where(Tables.AVATAR.MYCHAT_USER_ID.eq(req.myChatUserId))
                .fetchOne();

        if (count.value1() <= 0) { // insert
            dsl.insertInto(Tables.AVATAR,
                    Tables.AVATAR.MYCHAT_USER_ID,
                    Tables.AVATAR.MODIFICATION_DATE,
                    Tables.AVATAR.CREATION_DATE,
                    Tables.AVATAR.BYTES,
                    Tables.AVATAR.ENCODED,
                    Tables.AVATAR.MIME_TYPE,
                    Tables.AVATAR.FILENAME)
                    .values(
                            req.myChatUserId,
                            null,
                            new Timestamp(System.currentTimeMillis()),
                            baos.toByteArray(),
                            Base64.getMimeEncoder().encode(baos.toByteArray()),
                            req.d.getType(),
                            req.d.getFileName()
                    ).returning().fetchOne();
        } else { // update
            dsl.update(Tables.AVATAR)
                    .set(Tables.AVATAR.BYTES, baos.toByteArray())
                    .set(Tables.AVATAR.MODIFICATION_DATE, new Timestamp(System.currentTimeMillis()))
                    .set(Tables.AVATAR.FILENAME, req.d.getFileName())
                    .set(Tables.AVATAR.ENCODED, Base64.getMimeEncoder().encode(baos.toByteArray()))
                    .where(Tables.AVATAR.MYCHAT_USER_ID.eq(req.myChatUserId))
                    .returning()
                    .fetchOne();
        }

        return PostAvatar.Res.success();
    }



    @TransactionAnnotation
    @ApplicationTokenAnnotation
    @AccessTokenAnnotation
    public GetAvatar.Res getAvatar(GetAvatar.Req req) {
        DSLContext dsl = dslProvider.get();

        AvatarRecord avatarRecord=
        dsl.selectFrom(Tables.AVATAR)
                .where(Tables.AVATAR.MYCHAT_USER_ID.eq(req.myChatUserId))
                .fetchOne();

        return GetAvatar.Res.success(avatarRecord);
    }
}
