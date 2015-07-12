package com.tmjee.mychat.server.utils;

import org.junit.Test;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author tmjee
 */
public class DigestUtilsTest {

    @Test
    public void test() throws Exception {

        long randomizedNumber = DigestUtils.randomizeNumber();
        long anotherRandomizedNumber = DigestUtils.randomizeNumber();
        String hexOfRandomizednumber = DigestUtils.toHex(randomizedNumber);
        String anotherHexOfRandomizednumber = DigestUtils.toHex(anotherRandomizedNumber);

        String passwd1 = DigestUtils.hashPassword("test", hexOfRandomizednumber);
        String passwd2 = DigestUtils.hashPassword("test", hexOfRandomizednumber);
        String passwd3 = DigestUtils.hashPassword("test", anotherHexOfRandomizednumber);

        assertNotEquals(randomizedNumber, anotherRandomizedNumber);
        assertNotEquals(hexOfRandomizednumber, anotherHexOfRandomizednumber);
        assertEquals(passwd1, passwd2);
        assertNotEquals(passwd1, passwd3);
    }
}
