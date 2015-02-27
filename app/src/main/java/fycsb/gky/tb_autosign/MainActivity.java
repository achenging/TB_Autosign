package fycsb.gky.tb_autosign;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

import fycsb.gky.tb_autosign.api.TieBaApi;
import fycsb.gky.tb_autosign.entity.UserMsg;
import fycsb.gky.tb_autosign.http.TiebaRequest;
import fycsb.gky.tb_autosign.http.VolleySingleton;
import fycsb.gky.tb_autosign.ui.AutoSignActivity;
import fycsb.gky.tb_autosign.ui.BaseActivity;
import fycsb.gky.tb_autosign.utils.PostUrlUtil;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private EditText                                    mUsername;
    private EditText                                    mPassword;
    private Button                                      mLoginButton;
    private com.android.volley.toolbox.NetworkImageView mVcodeImage;
    private EditText                                    mVcodeEditText;
    private String                                      sign;
    private String                                      vcode;
    private String                                      vcodeMD5;
    private Date                                        date;
    private long                                        timeStamp;
    private String                                      vcodeUrl;
    private String                                      name;
    private String                                      tbs;
    private String errorCode = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.login_activity_name);
        if (getIntent().hasExtra("NoGetUserInfo")){

        }else {
            initConfig();
            init();
        }

    }

    private void initConfig() {
        if (isLastLogin()) {
            goTiebaAutoSign(name, tbs);
        }

    }

    private void init() {
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mVcodeImage = (com.android.volley.toolbox.NetworkImageView) findViewById(R.id.vcode_img);
        mVcodeEditText = (EditText) findViewById(R.id.vcode_msg);
        mLoginButton = (Button) findViewById(R.id.login_but);
        mLoginButton.setOnClickListener(this);
        mVcodeEditText.addTextChangedListener(new TextWatcherListener());
        date = new Date();
        timeStamp = date.getTime();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_but:
                if (!PostUrlUtil.isConnected(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, "请检查网络连接...>>>", Toast.LENGTH_SHORT).show();
                    break;
                }
                final String username = mUsername.getText().toString();
                final String password = mPassword.getText().toString();
                TiebaRequest stringRequest = null;
                stringRequest = new TiebaRequest(
                        Request.Method.POST,
                        TieBaApi.HOST_URL + TieBaApi.LOGIN,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    errorCode = jsonObject.getString("error_code");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Gson gson = new Gson();
                                UserMsg userMsg = gson.fromJson(response, UserMsg.class);
                                if (errorCode.equals("0")) {
                                    saveUser(userMsg);
                                } else if (errorCode.equals("5")) {
                                    mVcodeEditText.setVisibility(View.VISIBLE);
                                    mVcodeImage.setVisibility(View.VISIBLE);
                                    setImageVcodeData(userMsg);
                                    vcode = mVcodeEditText.getText().toString();
                                } else if (errorCode.equals("6")) {
                                    Toast.makeText(MainActivity.this, "验证码输入有误，请重新输入", Toast.LENGTH_SHORT).show();
                                    setImageVcodeData(userMsg);
                                    timeStamp = date.getTime();
                                } else {
                                    Toast.makeText(MainActivity.this, "Unknown Error!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "请检查网络连接...", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        String content = null;
                        try {
                            if (errorCode.equals("0")) {
                                content = PostUrlUtil.getPostContent(username, password, timeStamp);
                            } else if (errorCode.equals("5") || errorCode.equals("6")) {
                                content = PostUrlUtil.getPostContent(username, password, timeStamp, vcode, vcodeMD5);
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        sign = PostUrlUtil.getSign(content);
                        content = PostUrlUtil.removeFlag(content);
                        return PostUrlUtil.getParams(content + "sign=" + sign);
                    }
                };
                if (!("".equals(username) && "".equals(password)))
                    VolleySingleton.getInstance(MainActivity.this).addRequest(stringRequest);
                else
                    Toast.makeText(MainActivity.this, "贴吧id或密码不能为空", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void setImageVcodeData(UserMsg userMsg) {
        vcodeUrl = userMsg.getAnti().getVcodePicUrl();
        vcodeMD5 = userMsg.getAnti().getVcodeMd5();
        mVcodeImage.setImageUrl(vcodeUrl, VolleySingleton.getInstance(MainActivity.this).getImageLoader());
    }

    private void saveUser(UserMsg userMsg) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.config) + userMsg.getUser().getName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TieBaApi.USER_ID, userMsg.getUser().getId())
                .putString(TieBaApi.NAME, userMsg.getUser().getName())
                .putString(TieBaApi.DBUSS, userMsg.getUser().getBDUSS())
                .putString(TieBaApi.PORTRAIT, userMsg.getUser().getPortrait())
                .putString(TieBaApi.TBS, userMsg.getAnti().getTbs());

        editor.commit();
        saveLastLoginUser(userMsg.getUser().getName(), userMsg.getAnti().getTbs());
        goTiebaAutoSign(userMsg.getUser().getName(), userMsg.getAnti().getTbs());
    }

    private void saveLastLoginUser(String name, String tbs) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.last_login_user), MODE_PRIVATE);
        sharedPreferences.edit().putString(TieBaApi.NAME, name).putString(TieBaApi.TBS, tbs).commit();
    }

    private boolean isLastLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.last_login_user), MODE_PRIVATE);
        name = sharedPreferences.getString(TieBaApi.NAME, null);
        tbs = sharedPreferences.getString(TieBaApi.TBS, null);
        return name == null && tbs == null ? false : true;
    }

    private void goTiebaAutoSign(String name, String tbs) {
        Intent autoSignIntent = new Intent(MainActivity.this, AutoSignActivity.class);
        autoSignIntent.putExtra(TieBaApi.NAME, name);
        autoSignIntent.putExtra(TieBaApi.TBS, tbs);
        autoSignIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(autoSignIntent);
    }

    private class TextWatcherListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            vcode = s.toString();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_SPACE:
                finish();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


}
