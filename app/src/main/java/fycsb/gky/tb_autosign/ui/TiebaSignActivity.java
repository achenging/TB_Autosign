package fycsb.gky.tb_autosign.ui;

import android.content.Intent;
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

public class TiebaSignActivity extends BaseActivity {
    private String mUsername;
    private String mTbs;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_sign);
        mUsername = getIntent().getStringExtra(TieBaApi.NAME);
        mTbs = getIntent().getStringExtra(TieBaApi.TBS);
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.slidingTabLayout);
        OneByOneSignFragment allFragment = OneByOneSignFragment.newInstance(mUsername, mTbs);
        SevenLevelSignFragment someFragment = SevenLevelSignFragment.newInstance(mUsername, mTbs);
        fragmentList.add(allFragment);
        fragmentList.add(someFragment);
        CusPagerAdapter pagerAdapter = new CusPagerAdapter(getSupportFragmentManager(), fragmentList,
                Arrays.asList("全部", "部分"));
        mViewPager.setAdapter(pagerAdapter);
        slidingTabLayout.setViewPager(mViewPager, getResources().getDisplayMetrics().widthPixels);
        slidingTabLayout.setTabStripDividerColor(getResources().getColor(R.color.grey));


        int index = PreferenceManager.getDefaultSharedPreferences(this).getInt("SIGN_MODE", 0);
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
                Intent changUserLoginIntent = new Intent(TiebaSignActivity.this, ChangeUserLoginActivity.class);
                changUserLoginIntent.putExtra(TieBaApi.NAME, mUsername);
                changUserLoginIntent.putExtra(TieBaApi.TBS, mTbs);
                startActivity(changUserLoginIntent);
                break;
            case R.id.settings:
                Intent settingIntent = new Intent(TiebaSignActivity.this, SettingsActivity.class);
                startActivity(settingIntent);
                break;
            case R.id.exit:
                finish();
                break;
//            case R.id.notice:
//                Intent noticeIntent = new Intent(this, NoticeActivity.class);
//                startActivity(noticeIntent);
//                break;
            case R.id.about:
                Intent aboutIntent = new Intent(TiebaSignActivity.this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
