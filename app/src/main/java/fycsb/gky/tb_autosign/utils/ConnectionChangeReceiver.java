package fycsb.gky.tb_autosign.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by codefu on 2014/12/7.
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {
    public boolean checkConnect() {
        return isConnect;
    }

    private boolean isConnect = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("RECEVIER", ">>>>");
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifiInfo != null && wifiInfo.isConnected()) {
            isConnect = true;
        }
        if (mobileNetInfo != null && mobileNetInfo.isConnected()) {
            isConnect = true;
        }
        boolean wifiDisconnected = false;
        boolean gprsDisconnected = false;
        if (wifiInfo == null || wifiInfo != null
                && wifiInfo.getState() == NetworkInfo.State.DISCONNECTED) {
            wifiDisconnected = true;
        }
        if (mobileNetInfo == null || mobileNetInfo != null
                && mobileNetInfo.getState() == NetworkInfo.State.DISCONNECTED) {
            gprsDisconnected = true;
        }
        if (wifiDisconnected && gprsDisconnected) {
            Toast.makeText(context.getApplicationContext(),"网络不可用...",Toast.LENGTH_LONG).show();
            isConnect = false;
        }
    }


}
