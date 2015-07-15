package fycsb.gky.tb_autosign.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import fycsb.gky.tb_autosign.R;
import fycsb.gky.tb_autosign.api.TieBaApi;
import fycsb.gky.tb_autosign.entity.ForumList;
import fycsb.gky.tb_autosign.entity.LikeTieba;
import fycsb.gky.tb_autosign.http.TiebaRequest;
import fycsb.gky.tb_autosign.http.VolleySingleton;
import fycsb.gky.tb_autosign.ui.LoginActivity;
import fycsb.gky.tb_autosign.ui.TiebaSignActivity;
import fycsb.gky.tb_autosign.utils.PostUrlUtil;
import fycsb.gky.tb_autosign.utils.ProfileUtil;

/**
 * Created by codefu.
 */
public class TimerReceiver extends BroadcastReceiver {


    private int failureCount;
    private int successCount;
//    private List<Map<String, String>> failureMap;

    @Override
    public void onReceive(Context context, Intent intent) {
        long toDoTime = intent.getLongExtra("toDoTime", 0L);
        if (toDoTime == ProfileUtil.getFlagTime(context)) {
            likeTieba(context);
        }
    }


    private void generalNatification(Context context, Class<?> cls, String tickerTitle, String contentTitle, String contentText) {
        Intent reLoginIntent = null;
        PendingIntent pi = null;
        if (cls != null) {
            reLoginIntent = new Intent(context, cls);
            pi = PendingIntent.getActivity(context, 0, reLoginIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        }

        Notification notification = new Notification(R.drawable.ic_icon, tickerTitle, System.currentTimeMillis());
        notification.setLatestEventInfo(context, contentTitle, contentText, pi);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification);

    }


    private String currentTime() {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
    }

    private void likeTieba(final Context context) {

        String[] lastLoginInfo = ProfileUtil.lastLoginInfo(context);
        final String name = lastLoginInfo[0];
        final String tbs = lastLoginInfo[1];
        Date date = new Date();
        final long time = date.getTime();


        SharedPreferences userConfigSharedPreferences = context.getSharedPreferences(
                context.getString(R.string.config) + name,
                Context.MODE_PRIVATE);
        final String bduss = userConfigSharedPreferences.getString(TieBaApi.DBUSS, null);

        TiebaRequest likeRequest = new TiebaRequest(Request.Method.POST, TieBaApi.HOST_URL + TieBaApi.LIKE_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Gson gson = new Gson();
                LikeTieba likeTieba = gson.fromJson(response, LikeTieba.class);
                if (likeTieba.getErrorCode().equals("1")) {
                    //登陆失败情况
                    generalNatification(context, LoginActivity.class, "账号验证出错啦，请重新登陆!!",
                            "验证出错啦！", currentTime() + "\t点击重新登陆!!");
                    return;
                }
                final List<ForumList> forumLists = likeTieba.getForumList();
                for (ForumList forumList : forumLists) {
                    sign(context, forumList.getId(), forumList.getName(), bduss, tbs, time);
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            //发送签到结果的广播
                            if (forumLists.size() == (successCount + failureCount)) {
                                String detail = String.format("Success:%d\nFailure:%d", successCount, failureCount);
//                                if (failureCount > 0) {
//                                    generalNatification(context, TiebaSignActivity.class, detail, "签到情况", currentTime() + "\t" + detail);
//                                }else {
//                                    generalNatification(context, null, detail, "签到情况", currentTime() + "\t" + detail);
//                                }
                                generalNatification(context, null, detail, "签到情况", currentTime() + "\t" + detail);
                                break;
                            }
                        }
                    }
                }).start();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                generalNatification(context, null, "贴吧签到需要网络呀！！", "检查网络", "请手动打开网络");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                String str = PostUrlUtil.getLikeTieBaContent(bduss, time);
                String sign = PostUrlUtil.getSign(str);
                str = PostUrlUtil.removeFlag(str);
                return PostUrlUtil.getParams(str + "sign=" + sign);
            }
        };

        VolleySingleton.getInstance(context).getRequestQueue().add(likeRequest);
    }


    private void sign(final Context context, final String fid, final String kw, final String bduss, final String tbs, final long time) {
        TiebaRequest signRequest = new TiebaRequest(Request.Method.POST, TieBaApi.HOST_URL + TieBaApi.SINGLE_SIGN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String errorCode = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    errorCode = jsonObject.getString("error_code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                switch (errorCode) {
                    case "0":
                    case "160002":
                        ++successCount;
                        break;
                    case "340011":
                    case "1989004":
                    default:
                        ++failureCount;
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ++failureCount;
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                String content = PostUrlUtil.getSingleSignContent(bduss, fid, kw, tbs, time);
                String sign = PostUrlUtil.getSign(content);
                content = PostUrlUtil.removeFlag(content);
                return PostUrlUtil.getParams(content + "sign=" + sign);
            }
        };
        VolleySingleton.getInstance(context).getRequestQueue().add(signRequest);
    }

}
