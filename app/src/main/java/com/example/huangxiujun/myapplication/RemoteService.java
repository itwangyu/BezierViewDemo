package com.example.huangxiujun.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


/**
 * Created by WangYu on 2018/2/8.
 */

public class RemoteService extends Service {
    private String TAG = "wangyu";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: "+this.getClass().getSimpleName()+"  process:"+android.os.Process.myPid());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: "+this.getClass().getSimpleName()+"  process:"+android.os.Process.myPid());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
