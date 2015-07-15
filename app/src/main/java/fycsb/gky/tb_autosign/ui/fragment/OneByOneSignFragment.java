package fycsb.gky.tb_autosign.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fycsb.gky.tb_autosign.R;
import fycsb.gky.tb_autosign.adapter.OneByOneSignAdapter;
import fycsb.gky.tb_autosign.api.TieBaApi;
import fycsb.gky.tb_autosign.entity.ForumList;
import fycsb.gky.tb_autosign.entity.LikeTieba;
import fycsb.gky.tb_autosign.http.TiebaRequest;
import fycsb.gky.tb_autosign.http.VolleySingleton;
import fycsb.gky.tb_autosign.ui.LoginActivity;
import fycsb.gky.tb_autosign.utils.PostUrlUtil;

public class OneByOneSignFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener {
    private ShimmerTextView           mUsernameShimmer;
    private Button                    mStartSignBtn;
    private ListView                  mListView;
    private OneByOneSignAdapter       mListViewAdapter;
    private Shimmer                   mShimmer;
    private List<Map<String, String>> mData;
    private String                    mBduss;
    private String                    mTbs;
    private String                    mUsername;
    private long                      mTime;

    private List<String> asyncTaskList = new ArrayList<>(50);

    public static OneByOneSignFragment newInstance(String username, String tbs) {
        OneByOneSignFragment fragment = new OneByOneSignFragment();
        Bundle args = new Bundle();
        args.putString(TieBaApi.NAME, username);
        args.putString(TieBaApi.TBS, tbs);
        fragment.setArguments(args);
        return fragment;
    }

    public OneByOneSignFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUsername = getArguments().getString(TieBaApi.NAME);
            mTbs = getArguments().getString(TieBaApi.TBS);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_by_one_sign, container, false);
        init(view);
        mStartSignBtn.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        App.getRefWatcher(getActivity()).watch(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_sign_but:
                mData.clear();
                mListViewAdapter.notifyDataSetChanged();
                mListViewAdapter.getItemAnims().clear();
                Toast.makeText(getActivity(), "正在尝试签到...", Toast.LENGTH_SHORT).show();
                mStartSignBtn.setClickable(false);
                likeTieba();
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
        mShimmer.start(mUsernameShimmer);
        super.onResume();
    }


    private void init(View view) {
        mUsernameShimmer = (ShimmerTextView) view.findViewById(R.id.username);
        mShimmer = new Shimmer();
        mShimmer.setDuration(2000)
                .setStartDelay(300).start(mUsernameShimmer);
        mStartSignBtn = (Button) view.findViewById(R.id.btn_start_sign_but);
        mListView = (ListView) view.findViewById(R.id.listview);
        mStartSignBtn.setOnClickListener(this);
        mUsernameShimmer.setText("用户名:" + mUsername);
        Date date = new Date();
        mTime = date.getTime();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.config) + mUsername, Context.MODE_PRIVATE);
        mBduss = sharedPreferences.getString(TieBaApi.DBUSS, null);
        mData = new ArrayList<>(50);
        mListViewAdapter = new OneByOneSignAdapter(getActivity(), mData);
        mListView.setAdapter(mListViewAdapter);
        mListView.setOnItemClickListener(this);
    }


    private void likeTieba() {
        TiebaRequest likeRequest = new TiebaRequest(Request.Method.POST, TieBaApi.HOST_URL + TieBaApi.LIKE_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Gson gson = new Gson();
                LikeTieba likeTieba = gson.fromJson(response, LikeTieba.class);
                if (likeTieba.getErrorCode().equals("1")) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                List<ForumList> forumLists = likeTieba.getForumList();
                for (ForumList forumList : forumLists) {
                    sign(forumList.getId(), forumList.getName());
                    asyncTaskList.add(forumList.getName());
                }


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {

                            if (asyncTaskList.size() == 0) {
                                if (Looper.myLooper() == Looper.getMainLooper()) {
                                    mStartSignBtn.setClickable(true);
                                } else {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mStartSignBtn.setClickable(true);
                                        }
                                    });
                                }
                                break;
                            }
                        }
                    }
                }).start();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "请检查网络....", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                String str = PostUrlUtil.getLikeTieBaContent(mBduss, mTime);
                String sign = PostUrlUtil.getSign(str);
                str = PostUrlUtil.removeFlag(str);
                return PostUrlUtil.getParams(str + "sign=" + sign);
            }
        };

        VolleySingleton.getInstance(getActivity()).getRequestQueue().add(likeRequest);
    }


    private void sign(final String fid, final String kw) {
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

                        Map<String, String> success = new HashMap<>(1);
                        success.put(getString(R.string.sign_detail_key),
                                String.format(getString(R.string.sign_detail_value), kw));
                        success.put(getString(R.string.sign_issuccess_key),
                                getString(R.string.yes));
                        mData.add(success);
                        asyncTaskList.remove(kw);
                        mListViewAdapter.notifyDataSetChanged();
                        break;
                    case "340011":
                        Toast.makeText(getActivity(), "你签得太快了，先看看贴子再来签吧!\n签到已停止!!",
                                Toast.LENGTH_LONG).show();
                        VolleySingleton.getInstance(getActivity()).getRequestQueue().cancelAll(getActivity());
                        mStartSignBtn.setClickable(true);
                        asyncTaskList.clear();
                        break;
                    case "1989004":
                    default:
                        Map<String, String> fail = new HashMap<>(1);
                        fail.put(getString(R.string.sign_detail_key)
                        ,String.format(getString(R.string.sign_detail_value),kw));
                        fail.put(getString(R.string.sign_issuccess_key),getString(R.string.no));
                        fail.put(getString(R.string.sign_issuccess_key),fid);
                        mData.add(fail);
                        Toast.makeText(getActivity(), kw + "数据加载失败...", Toast.LENGTH_LONG).show();
                        asyncTaskList.remove(kw);
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "网络错误,请检查网络", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                String content = PostUrlUtil.getSingleSignContent(mBduss, fid, kw, mTbs, mTime);
                String sign = PostUrlUtil.getSign(content);
                content = PostUrlUtil.removeFlag(content);
                return PostUrlUtil.getParams(content + "sign=" + sign);
            }
        };
        signRequest.setTag(getActivity());
        VolleySingleton.getInstance(getActivity()).getRequestQueue().add(signRequest);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String,String> map = (Map<String, String>) mListViewAdapter.getItem(position);
        String kw = map.get(getString(R.string.sign_detail_key));
        kw = kw.substring(0,kw.length()-1);
        String fid = map.get(getString(R.string.fid_key));
        boolean flag = Boolean.valueOf(map.get(getString(R.string.sign_issuccess_key)));
        if (!flag){
            sign(fid,kw);
        }else{
            Toast.makeText(getActivity(),"该帖吧已经签到成功啦,请点击那些未成功的！！",Toast.LENGTH_LONG).show();
        }
    }
}
