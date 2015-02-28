package fycsb.gky.tb_autosign.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fycsb.gky.tb_autosign.R;
import fycsb.gky.tb_autosign.adapter.CusPageChangedListener;
import fycsb.gky.tb_autosign.adapter.CusPagerAdapter;
import fycsb.gky.tb_autosign.adapter.CusTabListener;
import fycsb.gky.tb_autosign.api.TieBaApi;
import fycsb.gky.tb_autosign.ui.fragment.OneByOneSignFragment;
import fycsb.gky.tb_autosign.ui.fragment.SevenLevelSignFragment;

public class AutoSignActivity extends BaseActivity {
    private String                 username;
    private String                 tbs;
    private ViewPager              mViewPager;
    private OneByOneSignFragment   mAllFragment;
    private SevenLevelSignFragment mSomeFragment;
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
        ActionBar actionBar = getSupportActionBar();
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mAllFragment = OneByOneSignFragment.newInstance(username, tbs);
        mSomeFragment = SevenLevelSignFragment.newInstance(username, tbs);
        fragmentList.add(mAllFragment);
        fragmentList.add(mSomeFragment);
        CusPagerAdapter pagerAdapter = new CusPagerAdapter(getSupportFragmentManager(),fragmentList);
        CusPageChangedListener pageChangedListener = new CusPageChangedListener(actionBar);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(pageChangedListener);
        CusTabListener tabListener = new CusTabListener(mViewPager);
        actionBar.setNavigationMode(getSupportActionBar().NAVIGATION_MODE_TABS);
        ActionBar.Tab mAllTab = actionBar.newTab().setText("全部").setTabListener(tabListener);
        ActionBar.Tab mSomeTab = actionBar.newTab().setText("部分").setTabListener(tabListener);
        actionBar.addTab(mAllTab);
        actionBar.addTab(mSomeTab);

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

                break;
            case R.id.about:
                Intent aboutIntent = new Intent(AutoSignActivity.this,AboutActivity.class);
                aboutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(aboutIntent);
                break;
            case R.id.exit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
