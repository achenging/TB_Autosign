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

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fycsb.gky.tb_autosign.R;
import fycsb.gky.tb_autosign.api.TieBaApi;
import fycsb.gky.tb_autosign.entity.ForumInfo;
import fycsb.gky.tb_autosign.entity.TBList;
import fycsb.gky.tb_autosign.http.TiebaRequest;
import fycsb.gky.tb_autosign.http.VolleySingleton;
import fycsb.gky.tb_autosign.utils.PostUrlUtil;

public class AutoSignActivity extends ActionBarActivity implements View.OnClickListener {
    private TextView    mUsername;
    private Button      mStartSignBut;
    private ProgressBar mProgressBar;
    private String      bduss;
    private String      userID;
    private long        time;
    private Date        date;
    private String      username;
    private Set<String> forumIDSet;
    private Set<String> forumNameSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_sign);
        initWiget();
        init();
        if (!hasUserTiebaData()) {
            Toast.makeText(AutoSignActivity.this,"第一次使用该账号,正在获取贴吧id...",Toast.LENGTH_LONG).show();
            initUserTiebaData();
        }else {
            mStartSignBut.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }


    }
    private void initWiget() {
        mUsername = (TextView) findViewById(R.id.username);
        mStartSignBut = (Button) findViewById(R.id.start_sign_but);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mStartSignBut.setOnClickListener(this);
        username = getIntent().getStringExtra(TieBaApi.NAME);
        mUsername.setText("贴吧用户名:" + username);
    }
    private void init() {

        date = new Date();
        time = date.getTime();
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.config) + username, MODE_PRIVATE);
        bduss = sharedPreferences.getString(TieBaApi.DBUSS, null);
        userID = sharedPreferences.getString(TieBaApi.ID, null);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_sign_but:

                break;
        }
    }


    private void initUserTiebaData() {
        TiebaRequest tiebaIdRequest = new TiebaRequest(Request.Method.POST, TieBaApi.HOST_URL + TieBaApi.SIGN_LIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("START LOAD DATA",response);
                Gson gson = new Gson();
                TBList tbList = gson.fromJson(response, TBList.class);
                String errorCode = tbList.getErrorCode();
                if (errorCode.equals("0")) {
                    saveUserTiebaID(tbList);
                    mStartSignBut.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(AutoSignActivity.this,"获取成功...",Toast.LENGTH_SHORT).show();
                }else {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AutoSignActivity.this,"未知错误...",Toast.LENGTH_SHORT).show();
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
        Set<String> forumIDSet = new HashSet<>();
        Set<String> forumNameSet = new HashSet<>();
        Iterator<ForumInfo> iterator = idList.iterator();
        while (iterator.hasNext()) {
            ForumInfo forumInfo = iterator.next();
            forumIDSet.add(forumInfo.getForumId());
            forumNameSet.add(forumInfo.getForumName());
        }
        SharedPreferences userTieBaIDSharedPreferences = getSharedPreferences(getString(R.string.tieba_id) + username, MODE_PRIVATE);
        SharedPreferences.Editor userTieBaEditor = userTieBaIDSharedPreferences.edit();
        userTieBaEditor.putStringSet(TieBaApi.FORUM_ID, forumIDSet);
        userTieBaEditor.putStringSet(TieBaApi.FORUM_NAME, forumNameSet);
        userTieBaEditor.commit();

    }

    private void loadUserTiebaData() {
        SharedPreferences userTieBaIDSharedPreferences = getSharedPreferences(getString(R.string.tieba_id) + username, MODE_PRIVATE);
        forumIDSet = userTieBaIDSharedPreferences.getStringSet(TieBaApi.FORUM_ID,null);
        forumNameSet = userTieBaIDSharedPreferences.getStringSet(TieBaApi.FORUM_NAME,null);
    }

    private boolean hasUserTiebaData() {
        loadUserTiebaData();
        if (forumIDSet != null && forumNameSet != null) {
            return true;
        }
        return false;
    }

}
