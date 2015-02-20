package fycsb.gky.tb_autosign.ui;


import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import fycsb.gky.tb_autosign.utils.ConnectionChangeReceiver;

/**
 * Created by codefu on 2014/12/27.
 */
public abstract class BaseActivity extends ActionBarActivity {
    public ConnectionChangeReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new ConnectionChangeReceiver();
        registerReceiver(receiver, filter);
        log(this.getClass(), ">>>>>register");
    }

    public void log(Class<?> c, String log) {
        Log.i(c.getName(), log);
    }

    public ConnectionChangeReceiver getReceiver() {
        return receiver;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        log(this.getClass(), ">>>>>unregister");
        unregisterReceiver(receiver);
    }
}
