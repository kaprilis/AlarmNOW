package com.whaim.alarmnow.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**Operate alarm database
 * Created by whaim on 14-2-21.
 */
public class AlarmDBM extends SQLiteOpenHelper{


    public AlarmDBM(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("Create table if not exists AlarmInfo(" +
                "AlarmEnabled boolean," +
                "AlarmVibrate boolean," +
                "AlarmRepeat  varchar," +
                "AlarmSnooze  int," +
                "AlarmLabel   varchar," +
                "AlarmTime    long");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
