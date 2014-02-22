package com.whaim.alarmnow.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.widget.Toast;

import java.util.Set;

/**
 * Created by whaim on 14-2-21.
 */
public class Alarm {
    public long mTime;
    public boolean mEnabled;
    public boolean mVibrate;
    public Set<String> mRepeat;
    public String mRingtone;
    public String mLabel;
    public String mSnooze;

    private AlarmManager mAlarmManager;
    private int mWeekday;
    private int mHour;
    private int mMinimum;
    private Context mContext;

    public Alarm(Context context){
        this.mContext=context;
        mSnooze="0";
        mLabel="";
        mRingtone="";

    }

    public void set(){
        mAlarmManager=(AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent= new Intent(mContext,AlarmReceiver.class);
        intent.putExtra("alarm_snooze",mSnooze);
        intent.putExtra("alarm_ringtone",mRingtone);
        PendingIntent sender = PendingIntent.getBroadcast(mContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP,mTime,sender);

        Toast.makeText(mContext,"闹钟设定成功！",Toast.LENGTH_LONG).show();
    }

    public void cancel(){
        mAlarmManager=(AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent= new Intent(mContext,AlarmReceiver.class);
        intent.putExtra("alarm_snooze",mSnooze);
        intent.putExtra("alarm_ringtone",mRingtone);
        PendingIntent sender = PendingIntent.getBroadcast(mContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.cancel(sender);

        Toast.makeText(mContext,"闹钟已取消！",Toast.LENGTH_LONG).show();
    }

    public void setSnooze(){
        mAlarmManager=(AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent= new Intent(mContext,AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(mContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+Integer.parseInt(mSnooze)*1000,sender);

        Toast.makeText(mContext,"已延迟"+Integer.parseInt(mSnooze)+"分钟！",Toast.LENGTH_LONG).show();
    }
}
