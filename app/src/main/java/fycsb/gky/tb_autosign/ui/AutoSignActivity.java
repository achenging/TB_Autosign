package fycsb.gky.tb_autosign.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fycsb.gky.tb_autosign.R;
import fycsb.gky.tb_autosign.api.TieBaApi;
import fycsb.gky.tb_autosign.entity.ForumInfo;
import fycsb.gky.tb_autosign.entity.ForumState;
import fycsb.gky.tb_autosign.entity.SignState;
import fycsb.gky.tb_autosign.entity.TBList;
import fycsb.gky.tb_autosign.http.TiebaRequest;
import fycsb.gky.tb_autosign.http.VolleySingleton;
import fycsb.gky.tb_autosign.utils.PostUrlUtil;

public class AutoSignActivity extends ActionBarActivity implements View.OnClickListener {
    private TextView        mUsername;
    private Button          mStartSignBut;
    private ProgressBar     mProgressBar;
    private TextView        mSignMsg;
    private String          bduss;
    private String          userID;
    private long            time;
    private Date            date;
    private String          username;
    private String          tbs;
    private List<ForumInfo> idList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_sign);
        init();
        if (!hasUserTiebaData()) {
            Toast.makeText(AutoSignActivity.this, "第一次使用该账号,正在获取贴吧id...", Toast.LENGTH_LONG).show();
            initUserTiebaData();
        } else {
            mStartSignBut.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void init() {

        mUsername = (TextView) findViewById(R.id.username);
        mStartSignBut = (Button) findViewById(R.id.start_sign_but);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mSignMsg = (TextView) findViewById(R.id.sign_msg);
        mSignMsg.setTextSize(25);
        mStartSignBut.setOnClickListener(this);
        username = getIntent().getStringExtra(TieBaApi.NAME);
        tbs = getIntent().getStringExtra(TieBaApi.TBS);
        mUsername.setText("贴吧用户名:" + username);
        date = new Date();
        time = date.getTime();
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.config) + username, MODE_PRIVATE);
        bduss = sharedPreferences.getString(TieBaApi.DBUSS, null);
        userID = sharedPreferences.getString(TieBaApi.USER_ID, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_sign_but:
                autoSign();
              //  likeTieba();
                break;
        }
    }

    //得到超过七级的贴吧的id，并保存到文件
    private void initUserTiebaData() {
        TiebaRequest tiebaIdRequest = new TiebaRequest(Request.Method.POST, TieBaApi.HOST_URL + TieBaApi.SIGN_LIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("START LOAD DATA", response);
                Gson gson = new Gson();
                TBList tbList = gson.fromJson(response, TBList.class);
                String errorCode = tbList.getErrorCode();
                if (errorCode.equals("0")) {
                    saveUserTiebaID(tbList);
                    Toast.makeText(AutoSignActivity.this, "获取成功...", Toast.LENGTH_SHORT).show();
                    if (loadUserTiebaData()) {
                        mStartSignBut.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(AutoSignActivity.this, "账户已过期或密码错误...", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AutoSignActivity.this, "未知错误...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String sign = PostUrlUtil.getTieBaIDSign(bduss, userID, time);
                return PostUrlUtil.getTiebaIDParams(bduss, userID, time, sign);
            }
        };
        VolleySingleton.getInstance(AutoSignActivity.this).getmRequestQueue().add(tiebaIdRequest);
    }

    private void saveUserTiebaID(TBList tbList) {
        List<ForumInfo> idList = tbList.getForumInfo();
        Gson gson = new Gson();
        String forumInfoJson = gson.toJson(idList);
        Log.i("JSON>>>>>>>>>>>>>>>>>>>>>.", forumInfoJson);
        SharedPreferences userTieBaIDSharedPreferences = getSharedPreferences(getString(R.string.tieba_id) + username, MODE_PRIVATE);
        SharedPreferences.Editor userTieBaEditor = userTieBaIDSharedPreferences.edit();
        userTieBaEditor.putString(TieBaApi.FORUM_INFO, forumInfoJson);
        userTieBaEditor.commit();

    }

    private boolean loadUserTiebaData() {
        SharedPreferences userTieBaIDSharedPreferences = getSharedPreferences(getString(R.string.tieba_id) + username, MODE_PRIVATE);
        boolean flag = userTieBaIDSharedPreferences.contains(TieBaApi.FORUM_INFO);
        if (flag) {
            String forumJson = userTieBaIDSharedPreferences.getString(TieBaApi.FORUM_INFO, null);
            Gson gson = new Gson();
            idList = gson.fromJson(forumJson, new TypeToken<List<ForumInfo>>() {
            }.getType());
            return true;
        }
        return false;

    }

    private boolean hasUserTiebaData() {
        return loadUserTiebaData();
    }

    private void autoSign() {
        TiebaRequest signRequest = new TiebaRequest(Request.Method.POST, TieBaApi.HOST_URL + TieBaApi.SIGN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject errorJsonObject = jsonObject.getJSONObject("error");
                    Log.i("errorJson---->",errorJsonObject.toString());
                    String usermsg = errorJsonObject.getString("errno");
                    if ("340011".equals(usermsg)) {

//                          签过之后再签到的情况，暂时是不知道为什么会返回这样的json数据
//                        mSignMsg.setText(errorJsonObject.getString("usermsg"));
                        Toast.makeText(AutoSignActivity.this,"七级以上的贴吧已签到...",Toast.LENGTH_SHORT).show();
                    }else {
                        Gson gson = new Gson();
                        SignState signState = gson.fromJson(response,SignState.class);
                        List<ForumState> forumStateList = signState.getInfo();
                        printLog(forumStateList);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AutoSignActivity.this,"请检查网络....",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String forumIds = PostUrlUtil.parseForumdids(idList);
                String str = PostUrlUtil.getTieBaForumIdsContent(bduss, userID, tbs, time, forumIds);
                String sign = PostUrlUtil.getSign(str);
                str = PostUrlUtil.removeFlag(str,sign);
                return PostUrlUtil.getMapParams(str + "sign=" + sign);
            }
        };
        VolleySingleton.getInstance(AutoSignActivity.this).getmRequestQueue().add(signRequest);
    }

    private <T> void printLog(List<T> list) {
        Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()) {
            mSignMsg.append(iterator.next().toString());
        }
    }


    private void likeTieba() {
        TiebaRequest likeRequest = new TiebaRequest(Request.Method.POST, TieBaApi.HOST_URL + TieBaApi.LIKE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AutoSignActivity.this,"请检查网络....",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String str = PostUrlUtil.getLikeTieBaContent(bduss,time);
                String sign = PostUrlUtil.getSign(str);
                return PostUrlUtil.getMapParams(str + "sign=" + sign);
            }
        };
        VolleySingleton.getInstance(AutoSignActivity.this).getmRequestQueue().add(likeRequest);
    }
}
