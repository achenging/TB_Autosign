package fycsb.gky.tb_autosign.adapter;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;

/**
 * Created by codefu on 2015/2/27.
 */
public class CusPageChangedListener implements ViewPager.OnPageChangeListener {
    private ActionBar actionBar;

    public CusPageChangedListener(ActionBar actionBar) {
        this.actionBar = actionBar;
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        actionBar.selectTab(actionBar.getTabAt(i));
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
