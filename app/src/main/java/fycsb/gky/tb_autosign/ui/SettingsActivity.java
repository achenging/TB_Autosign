package fycsb.gky.tb_autosign.ui;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import fycsb.gky.tb_autosign.R;


public class SettingsActivity extends PreferenceActivity {
    private Toolbar mToolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolbar.setTitle(R.string.title_activity_settings);

        addPreferencesFromResource(R.xml.pref_general);
        Preference preference = findPreference(getString(R.string.default_sign_mode_list));
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager.getDefaultSharedPreferences(this).getString(preference.getKey(),"0"));

        loadSetting(preference);

    }

    private Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener
            = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                preference.setSummary(index >= 0 ?
                        listPreference.getEntries()[index] : null);
                saveSetting("SIGN_MODE", index);
            }
            return true;
        }
    };


    private void loadSetting(Preference preference) {
        PreferenceManager.getDefaultSharedPreferences(this).getInt("SIGN_MODE",0);

    }

    private void saveSetting(String key, String value) {
        SharedPreferences.Editor editor  =  PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString(key, value).commit();

    }

    private void saveSetting(String key, int value) {
        SharedPreferences.Editor editor  =  PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putInt(key, value).commit();

    }


}
