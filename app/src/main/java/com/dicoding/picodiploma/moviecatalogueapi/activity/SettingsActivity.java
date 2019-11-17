package com.dicoding.picodiploma.moviecatalogueapi.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.dicoding.picodiploma.moviecatalogueapi.R;
import com.dicoding.picodiploma.moviecatalogueapi.alarm.AlarmReceiver;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
        private SwitchPreferenceCompat dailyReminder;
        private SwitchPreferenceCompat releaseReminder;

        private AlarmReceiver alarmReceiver;
        private Context context;

        private String DAILY_REMINDER;
        private String RELEASE_REMINDER;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            init();
            setSummaries();
        }

        private void init() {
            DAILY_REMINDER = getResources().getString(R.string.key_daily_reminder);
            RELEASE_REMINDER = getResources().getString(R.string.key_release_reminder);
            alarmReceiver = new AlarmReceiver();

            dailyReminder = findPreference(DAILY_REMINDER);
            releaseReminder = findPreference(RELEASE_REMINDER);
        }

        private void setSummaries() {
            SharedPreferences sh = getPreferenceManager().getSharedPreferences();
            dailyReminder.setChecked(sh.getBoolean(DAILY_REMINDER, false));
            dailyReminder.setChecked(sh.getBoolean(DAILY_REMINDER, false));
        }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);

            this.context = context;
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(DAILY_REMINDER)){
                boolean dailyReminderBool = sharedPreferences.getBoolean(DAILY_REMINDER, false);

                dailyReminder.setChecked(dailyReminderBool);

                if (dailyReminderBool)
                    alarmReceiver.setReminder(context, AlarmReceiver.TYPE_DAILY_REMINDER);
                else
                    alarmReceiver.cancelAlarm(context, AlarmReceiver.TYPE_DAILY_REMINDER);
            }

            if (key.equals(RELEASE_REMINDER)){
                boolean releaseReminderBool = sharedPreferences.getBoolean(RELEASE_REMINDER, false);

                releaseReminder.setChecked(releaseReminderBool);

                if (releaseReminderBool)
                    alarmReceiver.setReminder(context, AlarmReceiver.TYPE_RELEASE_REMINDER);
                else
                    alarmReceiver.cancelAlarm(context, AlarmReceiver.TYPE_RELEASE_REMINDER);
            }
        }


    }
}