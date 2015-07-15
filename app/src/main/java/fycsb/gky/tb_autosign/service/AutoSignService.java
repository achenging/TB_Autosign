package fycsb.gky.tb_autosign.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import java.util.Date;

import fycsb.gky.tb_autosign.broadcast.TimerReceiver;
import fycsb.gky.tb_autosign.utils.ProfileUtil;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 * Created by codefu.
 */
public class AutoSignService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AutoSignService(String name) {
        super(name);
    }

    public AutoSignService() {
        this("AutoSignService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        long toDoTime = ProfileUtil.isFirstOpen(getBaseContext())
                ? System.currentTimeMillis()
                : ProfileUtil.setFlagTime(getBaseContext());
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent it = new Intent(getBaseContext(), TimerReceiver.class);
        it.setAction("TIME_TO_SIGN");
        it.putExtra("toDoTime",toDoTime);
        PendingIntent pi = PendingIntent.getBroadcast(getBaseContext(), 0, it, FLAG_UPDATE_CURRENT);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, toDoTime, AlarmManager.INTERVAL_DAY, pi);
    }
}
