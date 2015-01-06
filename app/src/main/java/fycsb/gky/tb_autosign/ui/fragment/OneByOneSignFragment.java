package fycsb.gky.tb_autosign.ui.fragment;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
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
import fycsb.gky.tb_autosign.entity.ForumList;
import fycsb.gky.tb_autosign.entity.LikeTieba;
import fycsb.gky.tb_autosign.http.TiebaRequest;
import fycsb.gky.tb_autosign.http.VolleySingleton;
import fycsb.gky.tb_autosign.utils.PostUrlUtil;

public class OneByOneSignFragment extends Fragment implements View.OnClickListener {
    private ShimmerTextView mUsername;
    private Button          mStartSignBut;
    private TextView        mSignMsg;
    private String          bduss;
    private String          tbs;
    private String          username;
    private long            time;
    private Date            date;
    private List<ForumList> forumLists;
    private Shimmer         shimmer;
    private int             count;
    private int             signCount;
    private int             alreadySign;

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
            username = getArguments().getString(TieBaApi.NAME);
            tbs = getArguments().getString(TieBaApi.TBS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_by_one_sign, container, false);
        init(view);
        mStartSignBut.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_sign_but:
                likeTieba();
//                mSignMsg.append("你关注的贴吧数为:"+ count + ",本次签到数为:" + signCount + ",已签到数为:" + alreadySign);
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    private void init(View view) {
        mUsername = (ShimmerTextView) view.findViewById(R.id.username);
        shimmer = new Shimmer();
        shimmer.setDuration(2000)
                .setStartDelay(300).start(mUsername);
        mStartSignBut = (Button) view.findViewById(R.id.start_sign_but);
        mSignMsg = (TextView) view.findViewById(R.id.sign_msg);
        mSignMsg.setTextSize(15);
        mStartSignBut.setOnClickListener(this);
        mUsername.setText("用户名:" + username);
        date = new Date();
        time = date.getTime();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.config) + username, getActivity().MODE_PRIVATE);
        bduss = sharedPreferences.getString(TieBaApi.DBUSS, null);

    }


    private void likeTieba() {
        TiebaRequest likeRequest = new TiebaRequest(Request.Method.POST, TieBaApi.HOST_URL + TieBaApi.LIKE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                LikeTieba likeTieba = gson.fromJson(response, LikeTieba.class);
                forumLists = likeTieba.getForumList();
                count = forumLists.size();
                Iterator<ForumList> iterator = forumLists.iterator();
                while (iterator.hasNext()) {
                    ForumList forumList = iterator.next();
                    sign(forumList.getId(), forumList.getName());
                    Log.i("QUEUE NUMBER >>>>>", VolleySingleton.getInstance(getActivity()).getmRequestQueue().getSequenceNumber() + "");
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
                String str = PostUrlUtil.getLikeTieBaContent(bduss, time);
                String sign = PostUrlUtil.getSign(str);
                str = PostUrlUtil.removeFlag(str);
                return PostUrlUtil.getParams(str + "sign=" + sign);
            }
        };
        VolleySingleton.getInstance(getActivity()).getmRequestQueue().add(likeRequest);
    }

    private void sign(final String fid, final String kw) {
        TiebaRequest signRequest = new TiebaRequest(Request.Method.POST, TieBaApi.HOST_URL + TieBaApi.SINGLE_SIGN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                String errorCode = null;
                try {
                    jsonObject = new JSONObject(response);
                    errorCode = jsonObject.getString("error_code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (errorCode.equals("0")) {
                    mSignMsg.append(kw + " >> 签到成功\n");
                    ++signCount;
                } else if (errorCode.equals("160002")) {
                    mSignMsg.append(kw + " >> 已签过了..Orz..\n");
                    ++alreadySign;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
        VolleySingleton.getInstance(getActivity()).getmRequestQueue().add(signRequest);

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
