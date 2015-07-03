package com.tmjee.mychat.client;

import com.tmjee.mychat.common.domain.GenderEnum;
import com.tmjee.mychat.common.domain.MyChatUserIdentificationTypeEnum;
import com.tmjee.mychat.common.domain.MyChatUserStatusEnum;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.Timestamp;

/**
 * @author tmjee
 */
public interface MySelfAware {

    MySelf getMySelf() throws IOException, URISyntaxException;


    /**
     * @author tmjee
     */
    public static class MySelf {

        private final String myChatUserId;
        private final MyChatUserIdentificationTypeEnum identificationTypeEnum;
        private final String identification;
        private final MyChatUserStatusEnum statusEnum;
        private final Timestamp creationDate;
        private final String whatsup;
        private final GenderEnum genderEnum;
        private final String fullName;


        MySelf(String myChatUserId,
               MyChatUserIdentificationTypeEnum identificationTypeEnum,
               String identification,
               MyChatUserStatusEnum statusEnum,
               Timestamp creationDate,
               String whatsup,
               GenderEnum genderEnum,
               String fullName) {
            this.myChatUserId = myChatUserId;
            this.identificationTypeEnum = identificationTypeEnum;
            this.identification = identification;
            this.statusEnum = statusEnum;
            this.creationDate = creationDate;
            this.whatsup = whatsup;
            this.genderEnum = genderEnum;
            this.fullName = fullName;
        }

        public String getMyChatUserId() { return myChatUserId; }
        public MyChatUserIdentificationTypeEnum getIdentificationType() { return identificationTypeEnum; }
        public String getIdentification() { return identification; }
        public MyChatUserStatusEnum getStatus() { return statusEnum; }
        public Timestamp getCreationDate() { return creationDate; }
        public GenderEnum getGender() { return genderEnum; }
        public String getFullName() { return fullName; }
    }
}
