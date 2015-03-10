package fycsb.gky.tb_autosign.encrypt;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import fycsb.gky.tb_autosign.api.TieBaApi;

public class MD5Encrypt {

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

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String str = "_client_id=wappc_1419610591515_691&_client_type=2&_client_version=5.1.1&_phone_imei=000000000000000&channel_id=3971370984300604405&channel_uid=1055742506309009273&cuid=709831DDD4CBADB28DAA316524933278%7C000000000000000&from=tieba&isphone=0&model=Motorola+Moto+X&net_type=3&passwd=Mjc2NDgyNDI=&stErrorNums=0&stMethod=1&stMode=1&stSize=93&stTime=48&stTimesNum=0&timestamp=1420395749850&un=codefu" + TieBaApi.FLAG;
        System.out.println(getMD5(URLDecoder.decode(str).replaceAll("&","")));

    }
}
