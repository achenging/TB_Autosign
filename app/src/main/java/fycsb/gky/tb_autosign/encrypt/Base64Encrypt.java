package fycsb.gky.tb_autosign.encrypt;


import android.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by codefu on 2014/8/27.
 */
public class Base64Encrypt {
    public static byte[] decode(String key) throws IOException {
        return Base64.decode(key.getBytes(), Base64.DEFAULT);
    }

    public static String encode(byte[] key) throws UnsupportedEncodingException {
        return Base64.encodeToString(key,Base64.NO_WRAP);
    }

}

