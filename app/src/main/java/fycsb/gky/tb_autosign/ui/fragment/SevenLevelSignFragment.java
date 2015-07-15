package fycsb.gky.tb_autosign.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import fycsb.gky.tb_autosign.R;
import fycsb.gky.tb_autosign.adapter.UniversalListViewAdapter;
import fycsb.gky.tb_autosign.api.TieBaApi;
import fycsb.gky.tb_autosign.entity.ForumInfo;
import fycsb.gky.tb_autosign.entity.ForumState;
import fycsb.gky.tb_autosign.entity.SignState;
import fycsb.gky.tb_autosign.entity.TBList;
import fycsb.gky.tb_autosign.http.TiebaRequest;
import fycsb.gky.tb_autosign.http.VolleySingleton;
import fycsb.gky.tb_autosign.ui.LoginActivity;
import fycsb.gky.tb_autosign.utils.PostUrlUtil;


public class SevenLevelSignFragment extends Fragment
        implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private ShimmerTextView          mShimmerUsername;
    private Button                   mStartSignBut;
    private ProgressBar              mProgressBar;
    private String                   mTbs;
    private List<ForumInfo>          mIdList;
    private SwipeRefreshLayout       mSwipeRefreshLayout;
    private String                   mBduss;
    private String                   mUserID;
    private long                     mTime;
    private String                   mUsername;
    private Shimmer                  mShimmer;
    private boolean                  mIsInit;
    private ListView                 mListView;
    private UniversalListViewAdapter mAdapter;
    private List<ForumState> mForumStateList = new ArrayList<>(50);

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
            mUsername = getArguments().getString(TieBaApi.NAME);
            mTbs = getArguments().getString(TieBaApi.TBS);
        } else {
            Toast.makeText(getActivity(), "不能获取信息", Toast.LENGTH_SHORT).show();
            Intent it = new Intent(getActivity(), LoginActivity.class);
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            it.putExtra("NoGetUserInfo", true);
            startActivity(it);
        }
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seven_level_sign, container, false);
        init(view);
        if (!mIsInit) {
            mStartSignBut.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
        return view;
    }

    //viewpager加载的时候在可见时才加载网络部分
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) if (!hasUserTiebaData()) {
            Toast.makeText(getActivity(), "第一次使用该账号,正在获取贴吧id...",
                    Toast.LENGTH_LONG).show();
            initUserTiebaData();
        } else {
            if (mStartSignBut == null && mProgressBar == null) return;
            mStartSignBut.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mIsInit = true;
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
            case R.id.btn_start_sign_but:
                if (mIdList.size() > 0)
                    autoSign();
                else
                    Toast.makeText(getActivity(), "此用户暂时没有七级以上的喜欢的贴吧，请切换模式"
                            , Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onStop() {
        mShimmer.cancel();
        super.onStop();
    }

    @Override
    public void onResume() {
        mShimmer.start(mShimmerUsername);
        super.onResume();
    }


    private boolean hasUserTiebaData() {
        return loadUserTiebaData();
    }

    private boolean loadUserTiebaData() {
        SharedPreferences userTieBaIDSharedPreferences = getActivity()
                .getSharedPreferences(getString(R.string.tieba_id) + mUsername, Context.MODE_PRIVATE);
        boolean flag = userTieBaIDSharedPreferences.contains(TieBaApi.FORUM_INFO);
        if (flag) {
            String forumJson = userTieBaIDSharedPreferences.getString(TieBaApi.FORUM_INFO, null);
            Gson gson = new Gson();
            mIdList = gson.fromJson(forumJson, new TypeToken<List<ForumInfo>>() {
            }.getType());
            return true;
        }
        return false;

    }


    private void init(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipelayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mShimmerUsername = (ShimmerTextView) view.findViewById(R.id.username);
        mStartSignBut = (Button) view.findViewById(R.id.btn_start_sign_but);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        mListView = (ListView) view.findViewById(R.id.lv_seven_sign_list);
        mAdapter = new UniversalListViewAdapter<>(getActivity(), mForumStateList);
        mListView.setAdapter(mAdapter);
        mStartSignBut.setOnClickListener(this);
        mShimmerUsername.setText("用户名:" + mUsername);
        mShimmer = new Shimmer();
        mShimmer.setDuration(2000).setStartDelay(300).start(mShimmerUsername);
        Date date = new Date();
        mTime = date.getTime();
        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences(getString(R.string.config) + mUsername, Context.MODE_PRIVATE);
        mBduss = sharedPreferences.getString(TieBaApi.DBUSS, null);
        mUserID = sharedPreferences.getString(TieBaApi.USER_ID, null);

    }


    //得到超过七级的贴吧的id，并保存到文件
    private void initUserTiebaData() {

        TiebaRequest tiebaIdRequest = new TiebaRequest(Request.Method.POST, TieBaApi.HOST_URL
                + TieBaApi.SIGN_LIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                TBList tbList = gson.fromJson(response, TBList.class);
                String errorCode = tbList.getErrorCode();
                switch (errorCode) {
                    case "0":
                        mSwipeRefreshLayout.setRefreshing(false);
                        saveUserTiebaID(tbList);
                        Toast.makeText(getActivity(), "获取成功...", Toast.LENGTH_SHORT).show();
                        //暂时不知道做什么写的的代码，貌似是检查是否保存成功
                        if (loadUserTiebaData()) {
                            mStartSignBut.setVisibility(View.VISIBLE);
                            mProgressBar.setVisibility(View.GONE);
                        }
                        break;
                    case "1":          //账号信息不存在或者出现错误，导致的未登录行为，删除配置，重新登陆
                        getActivity().getSharedPreferences(getString(R.string.last_login_user)
                                , Context.MODE_PRIVATE).edit().clear().commit();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;
                    default:
                        Toast.makeText(getActivity(), "获取失败...", Toast.LENGTH_SHORT).show();
                        mProgressBar.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;
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
                String content = PostUrlUtil.getTieBaIDContent(mBduss, mUserID, mTime);
                String sign = PostUrlUtil.getSign(content);
                content = PostUrlUtil.removeFlag(content);
                return PostUrlUtil.getParams(content + "sign=" + sign);
            }
        };
        VolleySingleton.getInstance(getActivity()).getRequestQueue().add(tiebaIdRequest);
    }

    //保存需要签到的贴吧id，下次可以不用重新获取，省电省流量
    private void saveUserTiebaID(TBList tbList) {
        List<ForumInfo> idList = tbList.getForumInfo();
        Gson gson = new Gson();
        String forumInfoJson = gson.toJson(idList);
        SharedPreferences userTieBaIDSharedPreferences = getActivity()
                .getSharedPreferences(getString(R.string.tieba_id) + mUsername, Context.MODE_PRIVATE);
        SharedPreferences.Editor userTieBaEditor = userTieBaIDSharedPreferences.edit();
        userTieBaEditor.putString(TieBaApi.FORUM_INFO, forumInfoJson);
        userTieBaEditor.apply();
    }


    private void autoSign() {
        TiebaRequest signRequest = new TiebaRequest(Request.Method.POST
                , TieBaApi.HOST_URL + TieBaApi.SIGN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("==>",response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject errorJsonObject = jsonObject.getJSONObject("error");
                    String errno = errorJsonObject.getString("errno");
                    if ("340011".equals(errno)) {
                        Toast.makeText(getActivity(), "七级以上的贴吧已签到..."
                                , Toast.LENGTH_SHORT).show();
                    } else {
                        Gson gson = new Gson();
                        SignState signState = gson.fromJson(response, SignState.class);
                        List<ForumState> forumStates = signState.getInfo();
                        mForumStateList.addAll(forumStates);
                        mAdapter.notifyDataSetChanged();
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
                String forumIds = PostUrlUtil.parseForumdids(mIdList);
                String str = PostUrlUtil.getTieBaForumIdsContent(mBduss, mUserID, mTbs, mTime, forumIds);
                String sign = PostUrlUtil.getSign(str);
                str = PostUrlUtil.removeFlag(str);
                return PostUrlUtil.getParams(str + "sign=" + sign);
            }
        };
        VolleySingleton.getInstance(getActivity()).getRequestQueue().add(signRequest);
    }
}
