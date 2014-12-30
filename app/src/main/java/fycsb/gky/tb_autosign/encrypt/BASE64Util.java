package fycsb.gky.tb_autosign.encrypt;


import android.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by codefu on 2014/8/27.
 */
public class BASE64Util {
    public static byte[] deBASE(String key) throws IOException {
        return Base64.decode(key.getBytes(), Base64.DEFAULT);
    }

    public static String enBASE64(byte[] key) throws UnsupportedEncodingException {
        return new String( Base64.encode(key, Base64.DEFAULT),"utf-8");
    }

}

