package com.tmjee.mychat.utils;

import org.junit.Test;

import static java.lang.String.format;

/**
 * @author tmjee
 */
public class DigestUtilsTest {

    @Test
    public void test() throws Exception {

        long randomizedNumber = DigestUtils.randomizeNumber();
        String hexOfRandomizednumber = DigestUtils.toHex(randomizedNumber);
        String passwd1 = DigestUtils.hashPassword("test", hexOfRandomizednumber);

        String passwd2 = DigestUtils.hashPassword("test", hexOfRandomizednumber);

        System.out.println(format("randomizedNumber=%s", randomizedNumber));
        System.out.println(format("hexOfRandomziedNumber=%s", hexOfRandomizednumber));
        System.out.println(format("passwd1=%s", passwd1));
        System.out.println(format("passwd2=%s", passwd2));



    }
}
