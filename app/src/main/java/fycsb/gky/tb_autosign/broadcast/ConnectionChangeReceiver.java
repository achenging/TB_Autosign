package fycsb.gky.tb_autosign.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by codefu on 2014/12/7.
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {
    final String info = "网络不给力呀...";

    public enum NetState {
        NET_NO, NET_2G, NET_3G, NET_4G, NET_WIFI, NET_UNKNOWN
    }

    private static final int NETWORK_NO_CONNECT_MESSAGE = -1;
    public OnNetChangedListener mOnNetChangedListener;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case NETWORK_NO_CONNECT_MESSAGE:
                    Context ctx = (Context) msg.obj;
                    toastLog(ctx,info);
                    break;
                default:
            }
            return false;
        }
    });


    //定义一个接口，以后可能需要使用
    public interface OnNetChangedListener {
        void onNetChanged(NetState stateCode);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("RECEVIER", ">>>>");
        CheckNetwork(context);
    }



    public void setOnNetChangedListener(OnNetChangedListener onNetChangedListener) {
        mOnNetChangedListener = onNetChangedListener;
    }

    private void toastLog(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private void CheckNetwork(final Context context) {
        NetState stateCode = getNetState(context);
        if (stateCode == NetState.NET_NO || stateCode == NetState.NET_UNKNOWN) {
            toastLog(context, info);
            Timer timer = new Timer("tips");
            timer.schedule(new TimerTask() {
                int second = 3;

                @Override
                public void run() {
                    while (second > 0) {
                        Message msg = handler.obtainMessage();
                        msg.obj = context;
                        msg.what = NETWORK_NO_CONNECT_MESSAGE;
                        handler.sendMessage(msg);
                        --second;
                    }
                }
            }, 5000);
            if (mOnNetChangedListener == null) return;
            mOnNetChangedListener.onNetChanged(stateCode);


        }
    }


    private NetState getNetState(Context context) {
        NetState stateCode = NetState.NET_NO;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            switch (networkInfo.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    stateCode = NetState.NET_WIFI;
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    switch (networkInfo.getSubtype()) {
                        case TelephonyManager.NETWORK_TYPE_GPRS: //联通2g
                        case TelephonyManager.NETWORK_TYPE_CDMA: //电信2g
                        case TelephonyManager.NETWORK_TYPE_EDGE: //移动2g
                            stateCode = NetState.NET_2G;
                            break;
                        case TelephonyManager.NETWORK_TYPE_EVDO_A: //电信3g
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                            stateCode = NetState.NET_3G;
                            break;
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            stateCode = NetState.NET_4G;
                            break;
                        default:
                            stateCode = NetState.NET_UNKNOWN;
                    }
                    break;
                default:
                    stateCode = NetState.NET_UNKNOWN;
            }
        }
        return stateCode;
    }
}

