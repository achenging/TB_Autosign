package fycsb.gky.tb_autosign.utils;

import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import fycsb.gky.tb_autosign.R;
import fycsb.gky.tb_autosign.api.TieBaApi;
import fycsb.gky.tb_autosign.entity.UserMsg;

/**
 * Created by codefu.
 */
public class ProfileUtil {
    public static void saveLoginSuccessUser(Context context, String name, String tbs) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.already_login_user), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, tbs);
        editor.apply();
    }


    public static void saveLastLoginUser(Context context, String name, String tbs) {
        String profileName = context.getString(R.string.last_login_user);
        clearLastLogin(context, profileName);
        SharedPreferences sharedPreferences = context.getSharedPreferences(profileName, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(TieBaApi.NAME, name).putString(TieBaApi.TBS, tbs).apply();
    }
    public static String[] lastLoginInfo(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.last_login_user), Context.MODE_PRIVATE);
        String name = sharedPreferences.getString(TieBaApi.NAME, null);
        String tbs = sharedPreferences.getString(TieBaApi.TBS, null);

        return new String[]{name,tbs};
    }

    public static void saveUser(Context context, UserMsg userMsg) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.config) + userMsg.getUser().getName(),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TieBaApi.USER_ID, userMsg.getUser().getId())
                .putString(TieBaApi.NAME, userMsg.getUser().getName())
                .putString(TieBaApi.DBUSS, userMsg.getUser().getBDUSS())
                .putString(TieBaApi.PORTRAIT, userMsg.getUser().getPortrait())
                .putString(TieBaApi.TBS, userMsg.getAnti().getTbs());
        editor.apply();
        String name = userMsg.getUser().getName();
        String tbs = userMsg.getAnti().getTbs();
        ProfileUtil.saveLastLoginUser(context, name, tbs);
        ProfileUtil.saveLoginSuccessUser(context, name, tbs);
    }

    public static void clearLastLogin(Context context, String profileName) {
        context.getSharedPreferences(profileName, Context.MODE_PRIVATE)
                .edit().clear().apply();
    }


    public static Map<String, ?> loadLoginSuccessUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.already_login_user), Context.MODE_PRIVATE);
        Map<String, ?> userMap = sharedPreferences.getAll();
        if (userMap == null) return new HashMap<>();
        return userMap;
    }

    public static long setFlagTime(Context context) {
        long currentTime = System.currentTimeMillis();
        String appSetTime = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.time_to_do_key), "0:0");
        String[] times = appSetTime.split(":");
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTimeInMillis(currentTime);
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(times[0]));
        calendar.set(Calendar.MINUTE, Integer.valueOf(times[1]));
        calendar.set(Calendar.MILLISECOND,0);
        long settime = calendar.getTimeInMillis();
        if (settime < currentTime) {
            settime += AlarmManager.INTERVAL_DAY;
        }
        return settime;
    }


    public static long getFlagTime(Context context) {
        long currentTime = System.currentTimeMillis();
        String appSetTime = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.time_to_do_key), "0:0");
        String[] times = appSetTime.split(":");
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTimeInMillis(currentTime);
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(times[0]));
        calendar.set(Calendar.MINUTE, Integer.valueOf(times[1]));
        calendar.set(Calendar.MILLISECOND,0);
        long settime = calendar.getTimeInMillis();
        return settime;
    }


    public static boolean isFirstOpen(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isFirst = preferences.getBoolean("is_first",true);
        if (isFirst) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("is_first",false).apply();
        }
        return isFirst;
    }

    public static boolean isOpenAutoSign(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.switch_time_open_or_close_key), false);
    }

    public static void setIsFirstTrue(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("is_first",true).apply();
    }
}
