package fycsb.gky.tb_autosign.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;


import fycsb.gky.tb_autosign.R;
import fycsb.gky.tb_autosign.api.TieBaApi;
import fycsb.gky.tb_autosign.entity.UserMsg;
import fycsb.gky.tb_autosign.http.TiebaRequest;
import fycsb.gky.tb_autosign.http.VolleySingleton;
import fycsb.gky.tb_autosign.utils.PostUrlUtil;
import fycsb.gky.tb_autosign.utils.ProfileUtil;


public class LoginActivity extends BaseActivity implements View.OnClickListener, View.OnKeyListener {
    private AutoCompleteTextView                        mUsername;
    private AutoCompleteTextView                        mPassword;
    private Button                                      mLoginButton;
    private com.android.volley.toolbox.NetworkImageView mVcodeImage;
    private EditText                                    mVcodeEditText;
    private ProgressBar                                 mProgress;
    private String                                      sign;
    private String                                      vcode;
    private String                                      vcodeMD5;
    private Date                                        date;
    private long                                        timeStamp;
    private String                                      name;
    private String                                      tbs;
    private String errorCode = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.login_activity_name);
        if (!getIntent().hasExtra("NoGetUserInfo")) {
            initConfig();
            init();
        }
    }

    private void initConfig() {
        if (isLastLogin()) {
            goTiebaAutoSign(name, tbs);
        }

    }

    private boolean isLastLogin() {
        String[] loginInfo = ProfileUtil.lastLoginInfo(this);
        name = loginInfo[0];
        tbs = loginInfo[1];
        return !(name == null && tbs == null);
    }


    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mProgress = (ProgressBar) findViewById(R.id.progressbar);
        mUsername = (AutoCompleteTextView) findViewById(R.id.username);
        mPassword = (AutoCompleteTextView) findViewById(R.id.password);
        mVcodeImage = (com.android.volley.toolbox.NetworkImageView) findViewById(R.id.vcode_img);
        mVcodeEditText = (EditText) findViewById(R.id.vcode_msg);
        mLoginButton = (Button) findViewById(R.id.login_but);


        mUsername.setAdapter(new ArrayAdapter<String>(this, R.layout.dropdown_list,
                new ArrayList<String>(ProfileUtil.loadLoginSuccessUser(this).keySet())));
        mLoginButton.setOnClickListener(this);
        mVcodeEditText.addTextChangedListener(new TextWatcherListener());
        date = new Date();
        timeStamp = date.getTime();
        setSupportActionBar(toolbar);

        mPassword.setOnKeyListener(this);
        mVcodeEditText.setOnKeyListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_but:
                mProgress.setVisibility(View.VISIBLE);
                mLoginButton.setClickable(false);
                if (!PostUrlUtil.isConnected(LoginActivity.this)) {
                    Toast.makeText(LoginActivity.this, "请检查网络连接...>>>", Toast.LENGTH_SHORT).show();
                    mProgress.setVisibility(View.GONE);
                    break;
                }
                final String username = mUsername.getText().toString();
                final String password = mPassword.getText().toString();
                TiebaRequest stringRequest;
                stringRequest = new TiebaRequest(
                        Request.Method.POST,
                        TieBaApi.HOST_URL + TieBaApi.LOGIN,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                loginVerify(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(LoginActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        String content = null;
                        try {
                            if (errorCode.equals("0")) {
                                content = PostUrlUtil.getPostContent(username, password, timeStamp);
                            } else if (errorCode.equals("5") || errorCode.equals("6") || errorCode.equals("2")) {
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
                if (!("".equals(username) && "".equals(password))) {
                    VolleySingleton.getInstance(LoginActivity.this).addRequest(stringRequest);
                } else {
                    Toast.makeText(LoginActivity.this, "贴吧id或密码不能为空", Toast.LENGTH_SHORT).show();
                }
                mLoginButton.setClickable(true);
                break;
        }
    }

    private void loginVerify(String response) {
        mProgress.setVisibility(View.GONE);
        try {
            JSONObject jsonObject = new JSONObject(response);
            errorCode = jsonObject.getString("error_code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        UserMsg userMsg = gson.fromJson(response, UserMsg.class);
        switch (errorCode) {
            case "0":
                ProfileUtil.saveUser(LoginActivity.this, userMsg);
                goTiebaAutoSign(userMsg.getUser().getName(), userMsg.getAnti().getTbs());

                break;
            case "2":
                Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_LONG).show();
                mLoginButton.setClickable(true);
                break;
            case "5":
                mVcodeEditText.setVisibility(View.VISIBLE);
                mVcodeImage.setVisibility(View.VISIBLE);
                mPassword.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                if (mVcodeEditText.requestFocus()) {
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .showSoftInput(mVcodeEditText
                                    , InputMethodManager.SHOW_IMPLICIT);
                }
                setImageVcodeData(userMsg);
                vcode = mVcodeEditText.getText().toString();
                break;
            case "6":
                Toast.makeText(LoginActivity.this, "验证码输入有误，请重新输入", Toast.LENGTH_SHORT).show();
                mLoginButton.setClickable(true);
                setImageVcodeData(userMsg);
                if (mVcodeEditText.requestFocus()) {
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .showSoftInput(mVcodeEditText
                                    , InputMethodManager.SHOW_IMPLICIT);
                }
                timeStamp = date.getTime();
                break;
            default:
                mLoginButton.setClickable(true);
                Toast.makeText(LoginActivity.this, "Unknown Error!!", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private void setImageVcodeData(UserMsg userMsg) {
        String vcodeUrl = userMsg.getAnti().getVcodePicUrl();
        vcodeMD5 = userMsg.getAnti().getVcodeMd5();
        mVcodeImage.setImageUrl(vcodeUrl, VolleySingleton.getInstance(LoginActivity.this).getImageLoader());
    }


    private void goTiebaAutoSign(String name, String tbs) {
        Intent autoSignIntent = new Intent(LoginActivity.this, TiebaSignActivity.class);
        autoSignIntent.putExtra(TieBaApi.NAME, name);
        autoSignIntent.putExtra(TieBaApi.TBS, tbs);
        startActivity(autoSignIntent);
        finish();
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(mPassword.getWindowToken(), 0);
            mgr.hideSoftInputFromWindow(mVcodeEditText.getWindowToken(), 0);

            return true;
        }
        return false;

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


}
