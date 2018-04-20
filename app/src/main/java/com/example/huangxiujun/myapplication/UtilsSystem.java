
package com.example.huangxiujun.myapplication;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UtilsSystem {
    private static final String VALUE_STRING_PROC_STAT_FILE = "/proc/stat";
    private static final String VALUE_STRING_MEMORY_FILE = "/proc/meminfo";

    public static long getCpuTotalTime() {
        String strCpuStat = UtilsIO
                .readFileFirstLine(VALUE_STRING_PROC_STAT_FILE);
        if (TextUtils.isEmpty(strCpuStat))
            return 0;

        String[] arraystr = strCpuStat.split("\\s+");
        if (null == arraystr || arraystr.length < 8)
            return 0;

        long lCPUTimeTotal = 0;
        for (int nIndex = 1; nIndex < 8; nIndex++)
            lCPUTimeTotal += Long.parseLong(arraystr[nIndex]);

        return lCPUTimeTotal;
    }

    public static long getCpuIdleTime() {
        String strCpuStat = UtilsIO
                .readFileFirstLine(VALUE_STRING_PROC_STAT_FILE);
        if (TextUtils.isEmpty(strCpuStat))
            return 0;

        String[] arraystr = strCpuStat.split("\\s+");
        if (null == arraystr || arraystr.length < 8)
            return 0;

        return Long.parseLong(arraystr[4]);
    }

    public static double getCpuTotalUsage() {
        long lCpuTotalTime1 = getCpuTotalTime();
        long lCpuIdleTime1 = getCpuIdleTime();
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long lCpuTotalTime2 = getCpuTotalTime();
        long lCpuIdleTime2 = getCpuIdleTime();
        if (lCpuTotalTime2 - lCpuTotalTime1 == 0)
            return 0;

        double dCpuUsageTotal = ((double) ((lCpuTotalTime2 - lCpuTotalTime1) - (lCpuIdleTime2 - lCpuIdleTime1)))
                / (lCpuTotalTime2 - lCpuTotalTime1);
        return dCpuUsageTotal;
    }

//    public static double getCpuTotalUsageWithoutOwn() {
//        long lCpuTotalTime1 = getCpuTotalTime();
//        long lCpuIdleTime1 = getCpuIdleTime();
//        long lCpuTimeOwn1 = getProcessCpuTime(UtilsProcess.getMyPID());
//        try {
//            Thread.sleep(500);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        long lCpuTotalTime2 = getCpuTotalTime();
//        long lCpuIdleTime2 = getCpuIdleTime();
//        long lCpuTimeOwn2 = getProcessCpuTime(UtilsProcess.getMyPID());
//        if (lCpuTotalTime2 - lCpuTotalTime1 == 0)
//            return 0;
//
//        double dCpuUsageTotal = ((double) ((lCpuTotalTime2 - lCpuTotalTime1) - (lCpuIdleTime2 - lCpuIdleTime1))
//                - (lCpuTimeOwn2 - lCpuTimeOwn1))
//                / (lCpuTotalTime2 - lCpuTotalTime1);
//        return dCpuUsageTotal;
//    }

    public static long getProcessCpuTime(int nProcessID) {
        String strStat = UtilsIO.readFileFirstLine("/proc/" + nProcessID + "/stat");
        if (TextUtils.isEmpty(strStat))
            return 0;

        String[] arraystr = strStat.split("\\s+");
        if (null == arraystr || arraystr.length < 17)
            return 0;

        long lCpuTimeRun = Long.parseLong(arraystr[13])
                + Long.parseLong(arraystr[14]) + Long.parseLong(arraystr[15])
                + Long.parseLong(arraystr[16]);

        return lCpuTimeRun;
    }

    public static List<Integer> getProcessIDList() {
        List<Integer> listProcessID = new ArrayList<Integer>();
        File fileProc = new File("/proc/");
        String[] arraystrFile = fileProc.list();
        if (null == arraystrFile)
            return null;

        for (int nIndex = 0; nIndex < arraystrFile.length; nIndex++) {
            if (arraystrFile[nIndex].matches("[0-9]+")) {
                listProcessID.add(Integer.parseInt(arraystrFile[nIndex]));
            }
        }

        return listProcessID;
    }

    public static long getMemoryTotalSize() {
        String str = UtilsIO.readFileFirstLine(VALUE_STRING_MEMORY_FILE);
        if (TextUtils.isEmpty(str))
            return 0;

        int begin = str.indexOf(':');
        int end = str.indexOf('k');
        str = str.substring(begin + 1, end).trim();
        return Long.parseLong(str) * 1024;
    }

    public static long getMemoryFreeSize(Context context){
        if (null == context)
            return 0;

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem;
    }

    public static int getBatteryTemperature(Context context) {
        if (null == context)
            return 0;

        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if (null == intent)
            return 0;

        return intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
    }

    public static int getBatteryScale(Context context) {
        if (null == context)
            return 0;

        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if (null == intent)
            return 0;

        return intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
    }

    public static int getBatteryLevel(Context context) {
        if (null == context)
            return 0;

        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if (null == intent)
            return 0;

        return intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
    }
}
