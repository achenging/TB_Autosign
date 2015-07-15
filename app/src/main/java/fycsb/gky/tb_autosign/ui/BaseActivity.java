package fycsb.gky.tb_autosign.ui;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fycsb.gky.tb_autosign.broadcast.ConnectionChangeReceiver;
import fycsb.gky.tb_autosign.service.NetworkService;
import fycsb.gky.tb_autosign.utils.ProfileUtil;

/**
 * Created by codefu.
 */
public abstract class BaseActivity extends ActionBarActivity  {

    private static List<Activity> activities = new ArrayList<>(10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activities.add(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        activities.remove(this);
        if (activities.size() == 0) {
            Intent it = new Intent(this, NetworkService.class);
            stopService(it);
            ProfileUtil.setIsFirstTrue(this);
        }
    }
}
