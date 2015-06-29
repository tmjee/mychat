package com.tmjee.mychat.server.utils;

import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * @author tmjee
 */
public class IOUtils {

    public static byte[] toBytes(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int bytesRead = -1;
        byte[] buffer = new byte[1024];
        while((bytesRead=is.read(buffer))!= -1) {
            baos.write(buffer, 0, bytesRead);
        }
        return baos.toByteArray();
    }

    public static byte[] base64Encoded(byte[] i) {
        return Base64.getMimeEncoder().encode(i);
    }

}
