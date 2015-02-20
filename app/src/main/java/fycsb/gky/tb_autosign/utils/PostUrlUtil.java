package fycsb.gky.tb_autosign.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fycsb.gky.tb_autosign.api.TieBaApi;
import fycsb.gky.tb_autosign.encrypt.Base64Encrypt;
import fycsb.gky.tb_autosign.encrypt.MD5Encrypt;
import fycsb.gky.tb_autosign.entity.ForumInfo;

/**
 * Created by codefu on 2014/12/27.
 */
public class PostUrlUtil {


    public static String getPostContent(String username, String password, long time, String... vcodes)
            throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer(50);
        sb.append(TieBaApi.CLIENT_ID).append("&");
        sb.append(TieBaApi.CLIENT_TYPE).append("&");
        sb.append(TieBaApi.CLIENT_VERSION).append("&");
        sb.append(TieBaApi.PHONE_IMEI).append("&");
        sb.append(TieBaApi.CHANNEL_ID).append("&");
        sb.append(TieBaApi.CHANNEL_UID).append("&");
        sb.append(TieBaApi.CUID).append("&");
        sb.append(TieBaApi.FROM).append("&");
        sb.append(TieBaApi.ISPHONE).append("&");
        sb.append(TieBaApi.MODEL).append("&");
        sb.append(TieBaApi.NET_TYPE).append("&");
        sb.append(TieBaApi.PASSWD).append(Base64Encrypt.encode(password.getBytes("UTF-8"))).append("&");
        sb.append(TieBaApi.STERRORNUMS).append("&");
        sb.append(TieBaApi.STMETHOD).append("&");
        sb.append(TieBaApi.STMODE).append("&");
        sb.append(TieBaApi.STSIZE).append("&");
        sb.append(TieBaApi.STTIME).append("&");
        sb.append(TieBaApi.STTIMESNUM).append("&");
        sb.append(TieBaApi.TIMESTAMP).append(time).append("&");
        sb.append(TieBaApi.UN).append(username).append("&");
        if (vcodes.length == 2 && vcodes[0] != null && vcodes[1] != null) {
            sb.append(TieBaApi.VCODE).append("=").append(vcodes[0]).append("&");
            sb.append(TieBaApi.VCODE_MD5).append("=").append(vcodes[1]).append("&");
        }
        sb.append(TieBaApi.FLAG);
        return sb.toString();
    }


    public static String getSign(String params) {
        String sign = null;
        try {
            sign = MD5Encrypt.getMD5(URLDecoder.decode(params.replaceAll("&", ""))).toUpperCase();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sign;
    }

    public static String getTieBaIDContent(String bduss, String userID, long time) {
        StringBuilder sb = new StringBuilder(50);
        sb.append(TieBaApi.BDUSS_KEY).append("=").append(bduss).append("&");
        sb.append(TieBaApi.CLIENT_ID).append("&");
        sb.append(TieBaApi.CLIENT_TYPE).append("&");
        sb.append(TieBaApi.CLIENT_VERSION).append("&");
        sb.append(TieBaApi.PHONE_IMEI).append("&");
        sb.append(TieBaApi.CUID).append("&");
        sb.append(TieBaApi.FROM).append("&");
        sb.append(TieBaApi.MODEL).append("&");
        sb.append(TieBaApi.NET_TYPE).append("&");
        sb.append(TieBaApi.STERRORNUMS).append("&");
        sb.append(TieBaApi.STMETHOD).append("&");
        sb.append(TieBaApi.STMODE).append("&");
        sb.append(TieBaApi.STSIZE).append("&");
        sb.append(TieBaApi.STTIME).append("&");
        sb.append(TieBaApi.STTIMESNUM).append("&");
        sb.append(TieBaApi.TIMESTAMP).append(time).append("&");
        sb.append(TieBaApi.USER_ID_KEY).append("=").append(userID).append("&");
        sb.append(TieBaApi.FLAG);
        return sb.toString();
    }

    public static String getTieBaForumIdsContent(String bduss, String uerID, String tbs, long time,
                                                 String forumIds) {
        StringBuilder sb = new StringBuilder();
        sb.append(TieBaApi.BDUSS_KEY).append("=").append(bduss).append("&");
        sb.append(TieBaApi.CLIENT_ID).append("&");
        sb.append(TieBaApi.CLIENT_TYPE).append("&");
        sb.append(TieBaApi.CLIENT_VERSION).append("&");
        sb.append(TieBaApi.PHONE_IMEI).append("&");
        sb.append(TieBaApi.CUID).append("&");
        sb.append(TieBaApi.FORUM_IDS_KEY).append("=").append(forumIds).append("&");
        sb.append(TieBaApi.FROM).append("&");
        sb.append(TieBaApi.MODEL).append("&");
        sb.append(TieBaApi.NET_TYPE).append("&");
        sb.append(TieBaApi.STERRORNUMS).append("&");
        sb.append(TieBaApi.STMETHOD).append("&");
        sb.append(TieBaApi.STMODE).append("&");
        sb.append(TieBaApi.STSIZE).append("&");
        sb.append(TieBaApi.STTIME).append("&");
        sb.append(TieBaApi.STTIMESNUM).append("&");
        sb.append(TieBaApi.TBS_KEY).append("=").append(tbs).append("&");
        sb.append(TieBaApi.TIMESTAMP_KEY).append("=").append(time).append("&");
        sb.append(TieBaApi.USER_ID_KEY).append("=").append(uerID).append("&");
        sb.append(TieBaApi.FLAG);
        return sb.toString();
    }

    public static String getLikeTieBaContent(String bduss, long time) {
        StringBuilder sb = new StringBuilder();
        sb.append(TieBaApi.BDUSS_KEY).append("=").append(bduss).append("&");
        sb.append(TieBaApi.CLIENT_ID).append("&");
        sb.append(TieBaApi.CLIENT_TYPE).append("&");
        sb.append(TieBaApi.CLIENT_VERSION).append("&");
        sb.append(TieBaApi.PHONE_IMEI).append("&");
        sb.append(TieBaApi.CUID).append("&");
        sb.append(TieBaApi.FROM).append("&");
        sb.append(TieBaApi.MODEL).append("&");
        sb.append(TieBaApi.NET_TYPE).append("&");
        sb.append(TieBaApi.STERRORNUMS).append("&");
        sb.append(TieBaApi.STMETHOD).append("&");
        sb.append(TieBaApi.STMODE).append("&");
        sb.append(TieBaApi.STSIZE).append("&");
        sb.append(TieBaApi.STTIME).append("&");
        sb.append(TieBaApi.STTIMESNUM).append("&");
        sb.append(TieBaApi.TIMESTAMP_KEY).append("=").append(time).append("&");
        sb.append(TieBaApi.FLAG);
        return sb.toString();
    }

    public static Map<String, String> getParams(String params) {
        String[] keyValues = params.split("&");
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (int i = 0; i < keyValues.length; i++) {
            String[] args = keyValues[i].split("=", 2);
            map.put(args[0], args[1]);
        }
        return map;
    }

    public static String removeFlag(String str) {
        return str.substring(0, str.length() - TieBaApi.FLAG.length());

    }

    public static String parseForumdids(List<ForumInfo> forumInfos) {
        StringBuilder sb = new StringBuilder();
        int len = forumInfos.size();
        for (int i = 0; i < len; i++) {
            sb.append(forumInfos.get(i).getForumId());
            if (i < len - 1) sb.append(",");
        }
        return sb.toString();
    }

    public static String getSingleSignContent(String bduss, String fid, String kw, String tbs, long time) {
        StringBuilder sb = new StringBuilder(40);
        sb.append(TieBaApi.BDUSS_KEY).append("=").append(bduss).append("&");
        sb.append(TieBaApi.CLIENT_ID).append("&");
        sb.append(TieBaApi.CLIENT_TYPE).append("&");
        sb.append(TieBaApi.CLIENT_VERSION).append("&");
        sb.append(TieBaApi.PHONE_IMEI).append("&");
        sb.append(TieBaApi.CUID).append("&");
        sb.append(TieBaApi.FID_KEY).append("=").append(fid).append("&");
        sb.append(TieBaApi.FROM).append("&");
        sb.append(TieBaApi.KW_KEY).append("=").append(kw).append("&");
        sb.append(TieBaApi.MODEL).append("&");
        sb.append(TieBaApi.NET_TYPE).append("&");
        sb.append(TieBaApi.STERRORNUMS).append("&");
        sb.append(TieBaApi.STMETHOD).append("&");
        sb.append(TieBaApi.STMODE).append("&");
        sb.append(TieBaApi.STSIZE).append("&");
        sb.append(TieBaApi.STTIME).append("&");
        sb.append(TieBaApi.STTIMESNUM).append("&");
        sb.append(TieBaApi.TBS_KEY).append("=").append(tbs).append("&");
        sb.append(TieBaApi.TIMESTAMP).append(time).append("&");
        sb.append(TieBaApi.FLAG);
        return sb.toString();
    }

    public static boolean isConnected(Context connext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) connext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        }
        return false;
    }
}
