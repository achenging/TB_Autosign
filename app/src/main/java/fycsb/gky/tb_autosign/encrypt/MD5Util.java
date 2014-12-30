package fycsb.gky.tb_autosign.encrypt;

/**
 * Created by codefu on 2014/8/27.
 */

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

public class MD5Util {

    public static String getMD5(String encryptStr) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] hash = null;
        hash = MessageDigest.getInstance("MD5").digest(encryptStr.getBytes("UTF-8"));
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
}
