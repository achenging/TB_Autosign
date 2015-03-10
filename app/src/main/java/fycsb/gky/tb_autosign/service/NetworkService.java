package fycsb.gky.tb_autosign.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;

import fycsb.gky.tb_autosign.broadcast.ConnectionChangeReceiver;

/**
 * Created by codefu on 2015/2/28.
 */
public class NetworkService extends Service{

    private static ConnectionChangeReceiver receiver;

    @Override
    public void onCreate() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new ConnectionChangeReceiver();
        registerReceiver(receiver, filter);
        log(this.getClass(), ">>>>>register");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log(this.getClass(),"Start Again...");
        return super.onStartCommand(intent, flags, startId);
    }
    private void log(Class<?> c, String log) {
        Log.d(c.getName(), log);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }


}
