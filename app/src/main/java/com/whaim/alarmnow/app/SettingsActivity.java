package com.whaim.alarmnow.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SettingsActivity extends PreferenceActivity {

    private TimePicker timePicker;
    private Button buttonSave;
    private Button buttonDelete;

    private AlarmManager alarmManager;
    private Alarm alarm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        addPreferencesFromResource(R.xml.pref_settings);

        bindPreferenceSummaryToValue(findPreference("alarm_snooze"));
        bindPreferenceSummaryToValue(findPreference("alarm_label"));
        bindPreferenceSummaryToValue(findPreference("alarm_repeat"));
        bindPreferenceSummaryToValue(findPreference("alarm_ringtone"));

        timePicker=(TimePicker)findViewById(R.id.timePicker);
        buttonSave=(Button)findViewById(R.id.button_save);
        buttonDelete=(Button)findViewById(R.id.button_delete);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlarm();
                alarm.set();
                SettingsActivity.this.finish();
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlarm();
                alarm.cancel();
                SettingsActivity.this.finish();
            }
        });

    }

    private void setAlarm() {
        Time time=new Time();
        time.setToNow();
        time.hour=timePicker.getCurrentHour();
        time.minute=timePicker.getCurrentMinute();
        time.second=0;
        alarm=new Alarm(this);
        alarm.mTime=time.toMillis(true);
        alarm.mSnooze=PreferenceManager.getDefaultSharedPreferences(this).getString("alarm_snooze", "");
        alarm.mEnabled=PreferenceManager.getDefaultSharedPreferences(this).getBoolean("alarm_enabled",true);
        alarm.mRepeat=PreferenceManager.getDefaultSharedPreferences(this).getStringSet("alarm_repeat",new HashSet<String>());
        alarm.mRingtone=PreferenceManager.getDefaultSharedPreferences(this).getString("alarm_ringtone","");
        alarm.mVibrate=PreferenceManager.getDefaultSharedPreferences(this).getBoolean("alarm_vibrate",true);
        alarm.mLabel=PreferenceManager.getDefaultSharedPreferences(this).getString("alarm_label","");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {


            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(value.toString());

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);
            } else if (preference instanceof MultiSelectListPreference) {
                    Set<String> values = (Set<String>) value;
                    String summary=new String();
                    CharSequence[] entries = ((MultiSelectListPreference) preference).getEntries();
                    CharSequence[] entryValues=((MultiSelectListPreference) preference).getEntryValues();
                    if (values.size() == 0) {
                        preference.setSummary("无");
                    } else {
                        if(values.size()!=7){
                            for(int i=0;i<entries.length;++i){
                                if(values.contains(entryValues[i].toString())){
                                    summary+=entries[i].toString()+' ';
                                }
                            }
                                if (summary.contentEquals("周一 周二 周三 周四 周五 ")){
                                    summary="工作日";
                                }
                                if (summary.contentEquals("周六 周日 ")){
                                    summary="周末";
                                }

                                preference.setSummary(summary);
                            } else{
                                preference.setSummary("每天");
                            }
                        }
            } else if (preference instanceof RingtonePreference) {
                        // For ringtone preferences, look up the correct display value
                        // using RingtoneManager.
                        if (TextUtils.isEmpty(value.toString())) {
                            // Empty values correspond to 'silent' (no ringtone).
                            preference.setSummary("静音");

                        } else {
                            Ringtone ringtone = RingtoneManager.getRingtone(
                                    preference.getContext(), Uri.parse(value.toString()));

                            if (ringtone == null) {
                                // Clear the summary if there was a lookup error.
                            preference.setSummary(null);
                        } else {
                            // Set the summary to reflect the new ringtone display
                            // name.
                            String name = ringtone.getTitle(preference.getContext());
                            preference.setSummary(name);
                        }
                    }

            } else {
                    // For all other preferences, set the summary to the value's
                    // simple string representation.
                    preference.setSummary(value.toString());
            }
            return true;
        }
    };

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        Object value;
        System.out.println(preference.getContext());
        if (preference instanceof MultiSelectListPreference){
            value=PreferenceManager
                    .getDefaultSharedPreferences(preference.getContext())
                    .getStringSet(preference.getKey(), new HashSet<String>());
        } else{
            value=PreferenceManager
                    .getDefaultSharedPreferences(preference.getContext())
                    .getString(preference.getKey(), "");
        }

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,value);
    }

}
