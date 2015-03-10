package fycsb.gky.tb_autosign.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fycsb.gky.tb_autosign.R;
import fycsb.gky.tb_autosign.adapter.CustomListViewAdapter;
import fycsb.gky.tb_autosign.api.TieBaApi;
import fycsb.gky.tb_autosign.utils.ProfileUtil;

/**
 * Created by codefu on 2015/3/4.
 */
public class ChangeUserLoginActivity extends BaseActivity {
    private ListView              mListView;
    private Toolbar               mToolbar;
    private List<String>          users;
    private CustomListViewAdapter mAdapter;
    private String                username;
    private String                tbs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chang_user_login);
        initCurrentUser();
        initView();


    }

    private void initView() {
        View footerView = LayoutInflater.from(this).inflate(R.layout.listview_footer_view, null);
        footerView.findViewById(R.id.addUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileUtil.clearLastLogin(ChangeUserLoginActivity.this, getString(R.string.last_login_user));
                Intent loginActity = new Intent(ChangeUserLoginActivity.this, MainActivity.class);
                loginActity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginActity);
            }
        });

        mListView = (ListView) findViewById(R.id.userlist);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setTitle(R.string.change_user_info);

        Map<String, ?> userMap = loadLoginSuccessUser();
        users = new ArrayList<>(userMap.keySet());
        mListView.addFooterView(footerView);
        mAdapter = new CustomListViewAdapter(this, users, username);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new ChangUserItemOnClick(this,userMap,users));

    }

    private Map<String, ?> loadLoginSuccessUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.already_login_user), MODE_PRIVATE);
        Map<String, ?> userMap = sharedPreferences.getAll();
        if (userMap == null) return new HashMap<>();
        return userMap;
    }



    private void initCurrentUser() {
        username = getIntent().getStringExtra(TieBaApi.NAME);
        tbs = getIntent().getStringExtra(TieBaApi.TBS);
    }



    private class ChangUserItemOnClick implements AdapterView.OnItemClickListener{
        private Map<String,?> map;
        private List<String> datas;
        private Context mContext;
        private ChangUserItemOnClick(Context mContext,Map<String, ?> map,List<String> datas) {
            this.map = map;
            this.mContext = mContext;
            this.datas = datas;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String name = datas.get(position);
            String tbs = (String) map.get(name);
            Log.d(">>>>>>>>>  " + name + "-->",tbs);
            Intent intent = new Intent(mContext, AutoSignActivity.class);
            intent.putExtra(TieBaApi.NAME, name);
            intent.putExtra(TieBaApi.TBS,tbs);
            ProfileUtil.saveLastLoginUser(ChangeUserLoginActivity.this,name,tbs);
            mContext.startActivity(intent);
        }
    }


}
