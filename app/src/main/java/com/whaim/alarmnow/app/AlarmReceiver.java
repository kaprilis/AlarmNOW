package com.whaim.alarmnow.app;

import android.app.AlertDialog;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by 海明 on 14-2-22.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent in=new Intent(context,AlarmAlertActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        in.putExtra("alarm_snooze",intent.getStringExtra("alarm_snooze"));
        in.putExtra("alarm_ringtone",intent.getStringExtra("alarm_ringtone"));
        System.out.println("AlarmReceiverNew-----------"+in.getStringExtra("alarm_ringtone"));
        System.out.println("AlarmReceiverIntent-----------"+intent.getStringExtra("alarm_ringtone"));
        context.startActivity(in);
    }
}
