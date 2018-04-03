package com.example.huangxiujun.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by WangYu on 2018/3/30.
 */
public class AppProtectService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static String TAG = "wangyu";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("wangyu", "AppProtectService onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("wangyu", "AppProtectService onStartCommmand");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "run: lalala ");

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }
}
