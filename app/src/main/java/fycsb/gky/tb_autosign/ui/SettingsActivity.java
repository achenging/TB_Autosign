package fycsb.gky.tb_autosign.ui;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import fycsb.gky.tb_autosign.R;
import fycsb.gky.tb_autosign.broadcast.TimerReceiver;
import fycsb.gky.tb_autosign.service.AutoSignService;
import fycsb.gky.tb_autosign.ui.view.TimePreference;
import fycsb.gky.tb_autosign.utils.ProfileUtil;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;


public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitle(R.string.title_activity_settings);

        addPreferencesFromResource(R.xml.pref_general);

        Preference signModePreference = findPreference(getString(R.string.default_sign_mode_list_key));
        sBindPreferenceSummaryToValueListener.onPreferenceChange(signModePreference,
                PreferenceManager.getDefaultSharedPreferences(this).getString(signModePreference.getKey(),"0"));

        Preference switchPreference = findPreference(getString(R.string.switch_time_open_or_close_key));
        sBindPreferenceSummaryToValueListener.onPreferenceChange(switchPreference,
                PreferenceManager.getDefaultSharedPreferences(this).getBoolean(switchPreference.getKey(),false));

        Preference timePreference = findPreference(getString(R.string.time_to_do_key));
        timePreference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        signModePreference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        switchPreference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);




    }

    private boolean isStartService;

    private Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener
            = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = Integer.valueOf(stringValue);
                preference.setSummary(listPreference.getEntries()[index]);
            } else if (preference instanceof SwitchPreference) {
                SwitchPreference switchPreference = (SwitchPreference) preference;
                boolean enable = stringValue.equals("true");
                switchPreference.setChecked(enable);
                setTimePreferenceEnable(enable);
                Intent autoSignService = new Intent(SettingsActivity.this, AutoSignService.class);
                if (enable && !isStartService) {
                    startService(autoSignService);
                    isStartService = true;
                } else {
                    stopService(autoSignService);
                    isStartService = false;
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    Intent it = new Intent(getBaseContext(), TimerReceiver.class);
                    it.setAction("TIME_TO_SIGN");
                    PendingIntent pi = PendingIntent.getBroadcast(getBaseContext(), 0, it, PendingIntent.FLAG_NO_CREATE);
                    alarmManager.cancel(pi);
                }
            } else if (preference instanceof TimePreference) {
                if (isStartService) {
                    Intent autoSignService = new Intent(SettingsActivity.this, AutoSignService.class);

                    startService(autoSignService);
                }
            }
            return true;
        }
    };

    private void setTimePreferenceEnable(boolean enable) {
        findPreference(getString(R.string.time_to_do_key)).setEnabled(enable);
    }
}
