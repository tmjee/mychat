package com.tmjee.mychat.utils;

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

        System.out.println(format("randomizedNumber=%s", randomizedNumber));
        System.out.println(format("anotherRandomizedNumber=%s", anotherRandomizedNumber));
        System.out.println(format("hexOfRandomziedNumber=%s", hexOfRandomizednumber));
        System.out.println(format("anotherHexOfRandomziedNumber=%s", anotherHexOfRandomizednumber));
        System.out.println(format("passwd1=%s", passwd1));
        System.out.println(format("passwd2=%s", passwd2));
        System.out.println(format("passwd3=%s", passwd3));

        assertNotEquals(randomizedNumber, anotherRandomizedNumber);
        assertNotEquals(hexOfRandomizednumber, anotherHexOfRandomizednumber);
        assertEquals(passwd1, passwd2);
        assertNotEquals(passwd1, passwd3);
    }
}
