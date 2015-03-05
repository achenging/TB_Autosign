package fycsb.gky.tb_autosign.utils;

import android.content.Context;
import android.content.SharedPreferences;

import fycsb.gky.tb_autosign.R;
import fycsb.gky.tb_autosign.api.TieBaApi;
import fycsb.gky.tb_autosign.entity.UserMsg;

/**
 * Created by codefu on 2015/3/5.
 */
public class ProfileUtil {
    public static void saveLoginSuccessUser(Context context,String name, String tbs) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.already_login_user), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, tbs);
        editor.commit();
    }


    public static void saveLastLoginUser(Context context,String name, String tbs) {
        String profileName = context.getString(R.string.last_login_user);
        clearLastLogin(context,profileName);
        SharedPreferences sharedPreferences = context.getSharedPreferences(profileName, context.MODE_PRIVATE);
        sharedPreferences.edit().putString(TieBaApi.NAME, name).putString(TieBaApi.TBS, tbs).commit();
    }


    public static void saveUser(Context context,UserMsg userMsg) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.config) + userMsg.getUser().getName(),
                context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TieBaApi.USER_ID, userMsg.getUser().getId())
                .putString(TieBaApi.NAME, userMsg.getUser().getName())
                .putString(TieBaApi.DBUSS, userMsg.getUser().getBDUSS())
                .putString(TieBaApi.PORTRAIT, userMsg.getUser().getPortrait())
                .putString(TieBaApi.TBS, userMsg.getAnti().getTbs());
        editor.commit();
        String name = userMsg.getUser().getName();
        String tbs = userMsg.getAnti().getTbs();
        ProfileUtil.saveLastLoginUser(context, name, tbs);
        ProfileUtil.saveLoginSuccessUser(context,name, tbs);
    }
    public static void clearLastLogin(Context context,String profileName) {
        context.getSharedPreferences(profileName, context.MODE_PRIVATE)
                .edit().clear().commit();
    }
}
