package com.whaim.alarmnow.app;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class AlarmAlertActivity extends ActionBarActivity {

    private Ringtone ringtone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_alert);
        Uri uri= Uri.parse(getIntent().getStringExtra("alarm_ringtone"));
        ringtone=RingtoneManager.getRingtone(this,uri);
        System.out.println("AlarmReceiver-----------"+getIntent().getStringExtra("alarm_ringtone"));
        System.out.println(ringtone.getTitle(this));
        new AlertDialog.Builder(AlarmAlertActivity.this).setTitle("闹钟时间到！").setMessage("懒虫起床！").setPositiveButton("朕知道了。",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ringtone.stop();
                AlarmAlertActivity.this.finish();
            }
        }).setNegativeButton("再睡一会！",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Alarm alarm=new Alarm(AlarmAlertActivity.this);
                alarm.mSnooze=getIntent().getStringExtra("alarm_snooze");

                AlarmManager mAlarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
                PendingIntent sender = PendingIntent.getBroadcast(AlarmAlertActivity.this,0,getIntent(),PendingIntent.FLAG_UPDATE_CURRENT);
                mAlarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+Integer.parseInt(alarm.mSnooze)*1000,sender);

                Toast.makeText(AlarmAlertActivity.this, "已延迟" + Integer.parseInt(alarm.mSnooze) + "分钟！", Toast.LENGTH_LONG).show();
                ringtone.stop();
                AlarmAlertActivity.this.finish();
            }
        }).show();

        ringtone.play();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.alarm_alert, menu);
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

}
