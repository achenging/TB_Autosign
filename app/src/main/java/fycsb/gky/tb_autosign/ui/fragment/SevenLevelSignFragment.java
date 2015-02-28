package fycsb.gky.tb_autosign.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import fycsb.gky.tb_autosign.adapter.CustomRecyclerAdapter;
import fycsb.gky.tb_autosign.api.TieBaApi;
import fycsb.gky.tb_autosign.entity.ForumInfo;
import fycsb.gky.tb_autosign.entity.ForumState;
import fycsb.gky.tb_autosign.entity.SignState;
import fycsb.gky.tb_autosign.entity.TBList;
import fycsb.gky.tb_autosign.http.TiebaRequest;
import fycsb.gky.tb_autosign.http.VolleySingleton;
import fycsb.gky.tb_autosign.ui.MainActivity;
import fycsb.gky.tb_autosign.utils.PostUrlUtil;


public class SevenLevelSignFragment extends Fragment
        implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {
    private ShimmerTextView       mUsername;
    private Button                mStartSignBut;
    private ProgressBar           mProgressBar;
    private TextView              mSignMsg;
    private RecyclerView          mRecyclerView;
    private CustomRecyclerAdapter mRecyclerAdapter;
    private String                tbs;
    private List<ForumInfo>       idList;
    private SwipeRefreshLayout    mSwipeRefreshLayout;
    private String                bduss;
    private String                userID;
    private long                  time;
    private Date                  date;
    private String                username;
    private Shimmer               shimmer;

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

    public static final String DEBUG_TAG = ">>>>>>>>>>>>>>>>>>>>";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            username = getArguments().getString(TieBaApi.NAME);
            tbs = getArguments().getString(TieBaApi.TBS);

        } else {
            Toast.makeText(getActivity(),"不能获取信息",Toast.LENGTH_SHORT).show();
            Intent it = new Intent(getActivity(),MainActivity.class);
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            it.putExtra("NoGetUserInfo", true);
            startActivity(it);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seven_level_sign, container, false);
        init(view);

        return view;
    }



    //viewpager加载的时候在可见时才加载网络部分
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!hasUserTiebaData()) {
                Toast.makeText(getActivity(), "第一次使用该账号,正在获取贴吧id...", Toast.LENGTH_LONG).show();
                initUserTiebaData();
            } else {
                mStartSignBut.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }



    @Override
    public void onRefresh() {
        TextView textView = new TextView(getActivity());
        textView.setText("正在刷新贴吧列表");
        mSwipeRefreshLayout.setTag(textView);
        mSwipeRefreshLayout.addView(textView);
        initUserTiebaData();
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


    private boolean hasUserTiebaData() {
        return loadUserTiebaData();
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



    private void init(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipelayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        date = new Date();
        time = date.getTime();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.config) + username, getActivity().MODE_PRIVATE);
        bduss = sharedPreferences.getString(TieBaApi.DBUSS, null);
        userID = sharedPreferences.getString(TieBaApi.USER_ID, null);

    }



    //得到超过七级的贴吧的id，并保存到文件
    private void initUserTiebaData() {

        TiebaRequest tiebaIdRequest = new TiebaRequest(Request.Method.POST, TieBaApi.HOST_URL + TieBaApi.SIGN_LIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("START LOAD DATA", response);
                Gson gson = new Gson();
                TBList tbList = gson.fromJson(response, TBList.class);
                String errorCode = tbList.getErrorCode();
                if (errorCode.equals("0")) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    saveUserTiebaID(tbList);
                    Toast.makeText(getActivity(), "获取成功...", Toast.LENGTH_SHORT).show();

//暂时不知道做什么写的的代码，貌似是检查是否保存成功
//                    if (loadUserTiebaData()) {
//                        mStartSignBut.setVisibility(View.VISIBLE);
//                        mProgressBar.setVisibility(View.GONE);
//                    }
                } else if (errorCode.equals("1")) {         //账号信息不存在或者出现错误，导致的未登录行为，删除配置，重新登陆
                    getActivity().getSharedPreferences(getString(R.string.last_login_user), getActivity().MODE_PRIVATE).edit().clear().commit();
                    startActivity(new Intent(getActivity(),MainActivity.class));
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                else {
                    Toast.makeText(getActivity(), "获取失败...", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
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

    //保存需要签到的贴吧id，下次可以不用重新获取，省电省流量
    private void saveUserTiebaID(TBList tbList) {
        List<ForumInfo> idList = tbList.getForumInfo();
        Gson gson = new Gson();
        String forumInfoJson = gson.toJson(idList);
        Log.i("JSON>>>>>>>>>>>>>>>>>>.", forumInfoJson);
        SharedPreferences userTieBaIDSharedPreferences = getActivity().getSharedPreferences(getString(R.string.tieba_id) + username, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor userTieBaEditor = userTieBaIDSharedPreferences.edit();
        userTieBaEditor.putString(TieBaApi.FORUM_INFO, forumInfoJson);
        userTieBaEditor.commit();
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



}
