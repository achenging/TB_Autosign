package fycsb.gky.tb_autosign.encrypt;

/**
 * Created by codefu on 2014/8/27.
 */

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import fycsb.gky.tb_autosign.api.TieBaApi;
import fycsb.gky.tb_autosign.entity.TBList;
import fycsb.gky.tb_autosign.utils.PostUrlUtil;

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

    public static void main(String args[]) throws Exception {
//        String str = "BDUSS=3p1c0ZHRm9jb3phT1FPdmxTRVlRb0RBfkN5REpjQzVSM0NHdUxVTUNGZ0dHc3BVQVFBQUFBJCQAAAAAAAAAAAEAAADDuv8qNWZvcm1hbmNlAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaNolQGjaJUb%7C273005364cda565ca9fc948cdd1d7add&_client_id=wappc_1419855724901_354&_client_type=2&_client_version=5.1.1&_phone_imei=000000000000000&cuid=F8BA4B6A9E02CD1176592D9307BDC7D6%7C000000000000000&forum_ids=1828429%2C3293026%2C1193037%2C93356%2C1546493%2C1498934%2C6331%2C16058%2C4751673%2C1035919%2C1601022%2C47320%2C2188650%2C1391285%2C110019%2C11772%2C407248%2C104632%2C122873%2C5341367%2C527200%2C261856%2C271566%2C14%2C1957107%2C693735%2C2181499%2C1993444%2C2705120%2C50377%2C119038%2C1320431%2C14540%2C2819090%2C1204311%2C734708%2C280050%2C3203214%2C51084%2C1000542%2C24887%2C584234&from=yuyintie&model=Google+Nexus+4+-+4.2.2+-+API+17+-+768x1280&net_type=3&stErrorNums=0&stMethod=2&stMode=1&stSize=8369&stTime=174&stTimesNum=0&tbs=4554c780244acf101419939078&timestamp=1419939182354&user_id=721402563&" + TieBaApi.FLAG;
//        str = URLDecoder.decode(str).replaceAll("&", "");
//        System.out.println(str);
//        System.out.println(">>" + getMD5(str));
//        String[] ids = {
//                "1828429", "3293026",
//                "1193037", "93356",
//                "1546493", "1498934",
//                "6331", "16058",
//                "4751673", "1035919",
//                "1601022", "47320",
//                "2188650", "1391285",
//                "110019", "11772",
//                "407248", "104632",
//                "122873", "5341367",
//                "527200", "261856",
//                "271566", "14",
//                "1957107", "693735",
//                "2181499", "1993444",
//                "2705120", "50377",
//                "119038", "1320431",
//                "14540", "2819090", "1204311", "734708",
//                "280050", "3203214", "51084", "1000542", "24887", "584234"
//
//        };
//
//        String sign = PostUrlUtil.getTieBaForumIdsSign("3p1c0ZHRm9jb3phT1FPdmxTRVlRb0RBfkN5REpjQzVSM0NHdUxVTUNGZ0dHc3BVQVFBQUFBJCQAAAAAAAAAAAEAAADDuv8qNWZvcm1hbmNlAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaNolQGjaJUb|273005364cda565ca9fc948cdd1d7add", "721402563", "4554c780244acf101419939078", 1419939182354L,ids);
//        System.out.println(">>" + sign);
    }
}
