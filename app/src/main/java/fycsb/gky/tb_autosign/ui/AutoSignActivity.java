package fycsb.gky.tb_autosign.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fycsb.gky.tb_autosign.R;
import fycsb.gky.tb_autosign.adapter.CusPagerAdapter;
import fycsb.gky.tb_autosign.api.TieBaApi;
import fycsb.gky.tb_autosign.ui.fragment.OneByOneSignFragment;
import fycsb.gky.tb_autosign.ui.fragment.SevenLevelSignFragment;
import fycsb.gky.tb_autosign.ui.view.SlidingTabLayout;

public class AutoSignActivity extends BaseActivity {
    private String                 username;
    private String                 tbs;
    private ViewPager              mViewPager;
    private OneByOneSignFragment   mAllFragment;
    private SevenLevelSignFragment mSomeFragment;
    private Toolbar                mToolbar;
    private SlidingTabLayout       mSlidingTabLayout;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_sign);
        username = getIntent().getStringExtra(TieBaApi.NAME);
        tbs = getIntent().getStringExtra(TieBaApi.TBS);
        initView();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.slidingTabLayout);
        mAllFragment = OneByOneSignFragment.newInstance(username, tbs);
        mSomeFragment = SevenLevelSignFragment.newInstance(username, tbs);
        fragmentList.add(mAllFragment);
        fragmentList.add(mSomeFragment);
        CusPagerAdapter pagerAdapter = new CusPagerAdapter(getSupportFragmentManager(),fragmentList,
                Arrays.asList(new String[]{"全部","部分"}));
        mViewPager.setAdapter(pagerAdapter);
        mSlidingTabLayout.setViewPager(mViewPager,getResources().getDisplayMetrics().widthPixels);
        mSlidingTabLayout.setTabStripDividerColor(getResources().getColor(R.color.grey));


        int index = PreferenceManager.getDefaultSharedPreferences(this).getInt("SIGN_MODE",0);
        mViewPager.setCurrentItem(index);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_auto_sign, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chang_user:
                Intent changUserLoginIntent = new Intent(AutoSignActivity.this,ChangeUserLoginActivity.class);
                changUserLoginIntent.putExtra(TieBaApi.NAME, username);
                changUserLoginIntent.putExtra(TieBaApi.TBS, tbs);
                startActivity(changUserLoginIntent);
                break;
            case R.id.settings:
                Intent settingIntent = new Intent(AutoSignActivity.this,SettingsActivity.class);
                startActivity(settingIntent);
                break;
//            case R.id.about:
//                Intent aboutIntent = new Intent(AutoSignActivity.this, AboutActivity.class);
//                aboutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(aboutIntent);
//                break;
            case R.id.exit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
