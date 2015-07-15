package fycsb.gky.tb_autosign.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fycsb.gky.tb_autosign.R;
import fycsb.gky.tb_autosign.adapter.ChangeUserListViewAdapter;
import fycsb.gky.tb_autosign.api.TieBaApi;
import fycsb.gky.tb_autosign.utils.ProfileUtil;

/**
 * Created by codefu.
 */
public class ChangeUserLoginActivity extends BaseActivity {
    private String                   username;
    private String                   tbs;

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
                Intent loginActity = new Intent(ChangeUserLoginActivity.this, LoginActivity.class);
                loginActity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginActity);
            }
        });

        ListView listView = (ListView) findViewById(R.id.userlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setTitle(R.string.change_user_info);

        Map<String, ?> userMap = ProfileUtil.loadLoginSuccessUser(this);
        List<String> users = new ArrayList<>(userMap.keySet());
        listView.addFooterView(footerView);
        ChangeUserListViewAdapter mAdapter = new ChangeUserListViewAdapter(this, users, username);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new ChangUserItemOnClick(this, userMap, users));

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
            Intent intent = new Intent(mContext, TiebaSignActivity.class);
            intent.putExtra(TieBaApi.NAME, name);
            intent.putExtra(TieBaApi.TBS,tbs);
            ProfileUtil.saveLastLoginUser(ChangeUserLoginActivity.this,name,tbs);
            mContext.startActivity(intent);
        }
    }


}
