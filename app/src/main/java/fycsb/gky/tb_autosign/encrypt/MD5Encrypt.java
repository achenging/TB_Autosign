package fycsb.gky.tb_autosign.encrypt;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Encrypt {

    public static String getMD5(String encryptStr) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] hash;
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
