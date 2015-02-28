package fycsb.gky.tb_autosign;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import fycsb.gky.tb_autosign.service.NetworkService;
import fycsb.gky.tb_autosign.utils.ConnectionChangeReceiver;

/**
 * Created by codefu on 2015/2/28.
 */
public class App extends Application {
    private Context mContext;
    private static ConnectionChangeReceiver receiver;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        startNetworkService();

    }
    private void startNetworkService() {
        Intent networkService = new Intent(mContext, NetworkService.class);
        startService(networkService);
    }
}
