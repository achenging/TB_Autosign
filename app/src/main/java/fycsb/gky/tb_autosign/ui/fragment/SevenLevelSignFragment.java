package fycsb.gky.tb_autosign.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

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


public class SevenLevelSignFragment extends Fragment implements View.OnClickListener {
    private ShimmerTextView mUsername;
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
    private Shimmer         shimmer;

    public static SevenLevelSignFragment newInstance(String username, String tbs) {
        SevenLevelSignFragment fragment = new SevenLevelSignFragment();
        Bundle args = new Bundle();
        args.putString(TieBaApi.NAME, username);
        args.putString(TieBaApi.TBS, tbs);
        fragment.setArguments(args);
        return fragment;
    }

    public SevenLevelSignFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(TieBaApi.NAME);
            tbs = getArguments().getString(TieBaApi.TBS);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seven_level_sign, container, false);
        init(view);
        if (!hasUserTiebaData()) {
            Toast.makeText(getActivity(), "第一次使用该账号,正在获取贴吧id...", Toast.LENGTH_LONG).show();
            initUserTiebaData();
        } else {
            mStartSignBut.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
        return view;
    }


    private void init(View view) {

        mUsername = (ShimmerTextView) view.findViewById(R.id.username);
        shimmer = new Shimmer();
        shimmer.setDuration(2000)
                .setStartDelay(300).start(mUsername);
        mStartSignBut = (Button) view.findViewById(R.id.start_sign_but);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        mSignMsg = (TextView) view.findViewById(R.id.sign_msg);
        mSignMsg.setTextSize(20);
        mStartSignBut.setOnClickListener(this);
        mUsername.setText("用户名:" + username);
        date = new Date();
        time = date.getTime();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.config) + username, getActivity().MODE_PRIVATE);
        bduss = sharedPreferences.getString(TieBaApi.DBUSS, null);
        userID = sharedPreferences.getString(TieBaApi.USER_ID, null);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_sign_but:
                if (idList.size() > 0)
                    autoSign();
                else
                    Toast.makeText(getActivity(), "此用户暂时没有七级以上的喜欢的贴吧，请切换模式", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), "获取成功...", Toast.LENGTH_SHORT).show();
                    if (loadUserTiebaData()) {
                        mStartSignBut.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getActivity(), "获取失败...", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "未知错误...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                String content = PostUrlUtil.getTieBaIDContent(bduss, userID, time);
                String sign = PostUrlUtil.getSign(content);
                content = PostUrlUtil.removeFlag(content);
                return PostUrlUtil.getParams(content + "sign=" + sign);
            }
        };
        VolleySingleton.getInstance(getActivity()).getmRequestQueue().add(tiebaIdRequest);
    }

    private void saveUserTiebaID(TBList tbList) {
        List<ForumInfo> idList = tbList.getForumInfo();
        Gson gson = new Gson();
        String forumInfoJson = gson.toJson(idList);
        Log.i("JSON>>>>>>>>>>>>>>>>>>>>>.", forumInfoJson);
        SharedPreferences userTieBaIDSharedPreferences = getActivity().getSharedPreferences(getString(R.string.tieba_id) + username, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor userTieBaEditor = userTieBaIDSharedPreferences.edit();
        userTieBaEditor.putString(TieBaApi.FORUM_INFO, forumInfoJson);
        userTieBaEditor.commit();

    }

    private boolean loadUserTiebaData() {
        SharedPreferences userTieBaIDSharedPreferences = getActivity().getSharedPreferences(getString(R.string.tieba_id) + username, getActivity().MODE_PRIVATE);
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
                    Log.i("errorJson---->", errorJsonObject.toString());
                    String errno = errorJsonObject.getString("errno");
                    if ("340011".equals(errno)) {

//                          签过之后再签到的情况，暂时是不知道为什么会返回这样的json数据
//                        mSignMsg.setText(errorJsonObject.getString("usermsg"));
                        Toast.makeText(getActivity(), "七级以上的贴吧已签到...", Toast.LENGTH_SHORT).show();
                    } else {
                        Gson gson = new Gson();
                        SignState signState = gson.fromJson(response, SignState.class);
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
                Toast.makeText(getActivity(), "请检查网络....", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                String forumIds = PostUrlUtil.parseForumdids(idList);
                String str = PostUrlUtil.getTieBaForumIdsContent(bduss, userID, tbs, time, forumIds);
                String sign = PostUrlUtil.getSign(str);
                str = PostUrlUtil.removeFlag(str);
                return PostUrlUtil.getParams(str + "sign=" + sign);
            }
        };
        VolleySingleton.getInstance(getActivity()).getmRequestQueue().add(signRequest);
    }

    private <T> void printLog(List<T> list) {
        Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()) {
            mSignMsg.append(iterator.next().toString());
        }
    }

    @Override
    public void onStop() {
        shimmer.cancel();
        super.onStop();
    }

    @Override
    public void onResume() {
        shimmer.start(mUsername);
        super.onResume();
    }
}
