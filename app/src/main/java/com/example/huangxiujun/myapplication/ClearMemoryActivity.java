package com.example.huangxiujun.myapplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class ClearMemoryActivity extends Activity {
    private static final String TAG = "ClearMemoryActivity";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("wangyu", MemInfoUtil.getMemTotal() + "   " + MemInfoUtil.getMemAvailable());

        Log.i("wangyu", UtilsSystem.getMemoryTotalSize() + "   " + UtilsSystem.getMemoryFreeSize(this));
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ApplicationInfo> list = getPackageManager().getInstalledApplications(0);
        for (ApplicationInfo info : list) {
            Log.i("wangyu","kill:"+info.packageName);
            am.killBackgroundProcesses(info.packageName);
        }
        Log.i("wangyu", UtilsSystem.getMemoryTotalSize() + "   " + UtilsSystem.getMemoryFreeSize(this));
//        clean();

// Get a list of running apps
//        List<AndroidAppProcess> processes = AndroidProcesses.getRunningAppProcesses();
//        try {
//            for (AndroidAppProcess process : processes) {
//                // Get some information about the process
//
//                String processName = process.name;
//
//                Stat stat = process.stat();
//                int pid = stat.getPid();
//                int parentProcessId = stat.ppid();
//                long startTime = stat.stime();
//                int policy = stat.policy();
//                char state = stat.state();
//
//                Statm statm = process.statm();
//                long totalSizeOfProcess = statm.getSize();
//                long residentSetSize = statm.getResidentSetSize();
//
//                PackageInfo packageInfo = process.getPackageInfo(this, 0);
//                String appName = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
//
//                Log.i(TAG, "onCreate: "+processName);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    private void clean() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infoList = am.getRunningAppProcesses();
        List<ActivityManager.RunningServiceInfo> serviceInfos = am.getRunningServices(100);

        long beforeMem = getAvailMemory(ClearMemoryActivity.this);
        Log.d(TAG, "-----------before memory info : " + beforeMem);
        int count = 0;
        if (infoList != null) {
            for (int i = 0; i < infoList.size(); ++i) {
                ActivityManager.RunningAppProcessInfo appProcessInfo = infoList.get(i);
                Log.d(TAG, "process name : " + appProcessInfo.processName);
                //importance 该进程的重要程度  分为几个级别，数值越低就越重要。
                Log.d(TAG, "importance : " + appProcessInfo.importance);

                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_SERVICE的进程都长时间没用或者空进程了
                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_VISIBLE的进程都是非可见进程，也就是在后台运行着
//                if (appProcessInfo.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//                    String[] pkgList = appProcessInfo.pkgList;
//                    for (int j = 0; j < pkgList.length; ++j) {//pkgList 得到该进程下运行的包名
//                        Log.d(TAG, "It will be killed, package name : " + pkgList[j]);
//                        am.killBackgroundProcesses(pkgList[j]);
//                        count++;
//                    }
//                }

            }
        }

        long afterMem = getAvailMemory(ClearMemoryActivity.this);
        Log.d(TAG, "----------- after memory info : " + afterMem);
        Toast.makeText(ClearMemoryActivity.this, "clear " + count + " process, "
                + (afterMem - beforeMem) + "M", Toast.LENGTH_LONG).show();
    }


    //获取可用内存大小
    private long getAvailMemory(Context context) {
        // 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        //return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
        Log.d(TAG, "可用内存---->>>" + mi.availMem / (1024 * 1024));
        return mi.availMem / (1024 * 1024);
    }
}