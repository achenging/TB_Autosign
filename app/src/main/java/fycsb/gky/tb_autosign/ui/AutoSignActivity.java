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
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

import fycsb.gky.tb_autosign.R;
import fycsb.gky.tb_autosign.api.TieBaApi;
import fycsb.gky.tb_autosign.entity.ForumInfo;
import fycsb.gky.tb_autosign.entity.TBList;
import fycsb.gky.tb_autosign.http.TiebaRequest;
import fycsb.gky.tb_autosign.http.VolleySingleton;
import fycsb.gky.tb_autosign.utils.PostUrlUtil;

public class AutoSignActivity extends ActionBarActivity implements View.OnClickListener {
    private TextView        mUsername;
    private Button          mStartSignBut;
    private ProgressBar     mProgressBar;
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
                break;
        }
    }


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
                    mStartSignBut.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(AutoSignActivity.this, "获取成功...", Toast.LENGTH_SHORT).show();
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
        String forumJson = userTieBaIDSharedPreferences.getString(TieBaApi.FORUM_INFO, null);
        if (forumJson != null) {
            Gson gson = new Gson();
            idList = gson.fromJson(forumJson,new TypeToken<List<ForumInfo>>(){}.getType());
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
                Log.i("AUTO_SIGN >>>>>>>>>>>>>>>>>>>>.",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String forumIds = PostUrlUtil.parseForumdids(idList);
                String sign = PostUrlUtil.getTieBaForumIdsSign(bduss,userID,tbs,time,forumIds);
                return PostUrlUtil.getAutoSignParams(bduss,userID,tbs,time,sign,forumIds);
            }
        };
        VolleySingleton.getInstance(AutoSignActivity.this).getmRequestQueue().add(signRequest);
    }

}
