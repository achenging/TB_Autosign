package fycsb.gky.tb_autosign.ui;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import fycsb.gky.tb_autosign.R;
import fycsb.gky.tb_autosign.api.TieBaApi;
import fycsb.gky.tb_autosign.ui.fragment.OneByOneSignFragment;
import fycsb.gky.tb_autosign.ui.fragment.SevenLevelSignFragment;

public class AutoSignActivity extends BaseActivity {
    private static final String SEVEN_LEVEL_TAG = "SEVEN_LEVEL";
    private static final String ONE_BY_ONE_TAG  = "ONE_BY_ONE";
    private String    username;
    private String    tbs;
    private ViewPager mViewPager;
    private Fragment  mAllFragment;
    private Fragment  mSomeFragment;
    private List<Fragment> fragmentList = new ArrayList<>();
    private int            pos          = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_sign);
        username = getIntent().getStringExtra(TieBaApi.NAME);
        tbs = getIntent().getStringExtra(TieBaApi.TBS);
        mAllFragment = OneByOneSignFragment.newInstance(username, tbs);
        mSomeFragment = SevenLevelSignFragment.newInstance(username, tbs);
        fragmentList.add(mAllFragment);
        fragmentList.add(mSomeFragment);
        FragmentManager fm = getSupportFragmentManager();
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        CusPagerAdapter adapter = new CusPagerAdapter(fm, this);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(adapter);
        fm.beginTransaction().add(R.id.viewpager, mAllFragment).commit();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.Tab allTab = actionBar.newTab().setText("全部").setTabListener(new CusTabListener(ONE_BY_ONE_TAG, mAllFragment));
        ActionBar.Tab someTab = actionBar.newTab().setText("部分").setTabListener(new CusTabListener(SEVEN_LEVEL_TAG, mSomeFragment));
        actionBar.addTab(someTab);
        actionBar.addTab(allTab);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_auto_sign, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.chang_mode:
//                if (currentTag.equals(SEVEN_LEVEL_TAG)) {
//                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(ONE_BY_ONE_TAG);
//                    if (fragment == null)
//                        fragment = OneByOneSignFragment.newInstance(username, tbs);
//                    getSupportFragmentManager().beginTransaction().replace(R.id.viewpager, fragment).commit();
//                    currentTag = ONE_BY_ONE_TAG;
//                    setTitle(R.string.title_fragment_all_sign);
//                } else {
//                    setTitle(R.string.title_fragment_some_sign);
//                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(SEVEN_LEVEL_TAG);
//                    if (fragment == null)
//                        fragment = SevenLevelSignFragment.newInstance(username, tbs);
//                    getSupportFragmentManager().beginTransaction().replace(R.id.viewpager, fragment).commit();
//                    currentTag = SEVEN_LEVEL_TAG;
//                }
//                break;
//            case R.id.chang_user:
//
//                break;
//            case R.id.about:
//                Intent aboutIntent = new Intent(AutoSignActivity.this,AboutActivity.class);
//                aboutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(aboutIntent);
//                break;
//            case R.id.exit:
//                finish();
//                break;
//        }
        return super.onOptionsItemSelected(item);
    }

    private class CusTabListener implements ActionBar.TabListener {

        private String   tag;
        private Fragment fragment;

        public CusTabListener(String tag, Fragment fragment) {
            this.tag = tag;
            this.fragment = fragment;
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            fragmentTransaction.replace(R.id.viewpager, fragment, tag);
            mViewPager.setCurrentItem(pos);
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            fragmentTransaction.remove(fragment);
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        }
    }

    private class CusPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener{
        private Context context;

        public CusPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            pos = position;
            getSupportActionBar().setSelectedNavigationItem(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
            String fname = fragmentList.get(position).getClass().getName();
            Log.i("Name -->",fname);
            return Fragment.instantiate(context, fname);
        }


    }
}
