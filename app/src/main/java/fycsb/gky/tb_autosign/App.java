package fycsb.gky.tb_autosign;

import android.app.Application;
import android.content.Intent;

import fycsb.gky.tb_autosign.service.AutoSignService;
import fycsb.gky.tb_autosign.service.NetworkService;
import fycsb.gky.tb_autosign.utils.ProfileUtil;

/**
 * Created by codefu.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startNetworkService();
        if (ProfileUtil.isOpenAutoSign(this)) {
            startTimeToSign();
        }
    }

    private void startNetworkService() {
        Intent networkService = new Intent(this, NetworkService.class);
        startService(networkService);
    }

    private void startTimeToSign() {
        Intent autoSignService = new Intent(this, AutoSignService.class);
        startService(autoSignService);
    }

}
