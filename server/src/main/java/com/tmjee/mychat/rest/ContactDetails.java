package com.tmjee.mychat.rest;

/**
 * @author tmjee
 */
public class ContactDetails extends V1<ContactDetails.Req, ContactDetails.Res> {



    public static class Req extends V1.Req {

        @Override
        protected void validate() {

        }
    }


    public static class Res extends V1.Res {

    }

}
