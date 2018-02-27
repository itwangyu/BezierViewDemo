package com.example.huangxiujun.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private CustomsTextView tv;
    private TextView clickMe;
    private TextView nope;
    private ColorfulTextView tv1;
    private String TAG = "wangyu";
    private Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        tv1 = findViewById(R.id.tv);
//        Log.i(TAG, "onCreate: "+android.os.Process.myPid());
//        Button bt = findViewById(R.id.bt);
//        bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                i = new Intent(MainActivity.this, RemoteService.class);
//                startService(i);
//            }
//        });
//        findViewById(R.id.bt2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                stopService(i);
//            }
//        });
//        Toast.makeText(this, BuildConfig.FLAVOR, Toast.LENGTH_LONG).show();
//
//        OkHttpClient okHttpClient = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url("http://www.baidu.com")
//                .get()
//                .build();
//        okHttpClient.newCall(request)
//                .enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        System.out.println(response.toString());
//                    }
//                });
//        StringBuffer sb = new StringBuffer();
//        LogUtil.e(ping("baidu.com",1,sb)+"   \n"+sb.toString());
    }

    public static boolean ping(String host, int pingCount, StringBuffer stringBuffer) {
        String line = null;
        Process process = null;
        BufferedReader successReader = null;
//        String command = "ping -c " + pingCount + " -w 5 " + host;
        String command = "ping -c " + pingCount + " " + host;
        boolean isSuccess = false;
        try {
            process = Runtime.getRuntime().exec(command);
            if (process == null) {
                LogUtil.e("ping fail:process is null.");
                append(stringBuffer, "ping fail:process is null.");
                return false;
            }
            successReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = successReader.readLine()) != null) {
                LogUtil.i(line);
                append(stringBuffer, line);
            }
            int status = process.waitFor();
            if (status == 0) {
                LogUtil.i("exec cmd success:" + command);
                append(stringBuffer, "exec cmd success:" + command);
                isSuccess = true;
            } else {
                LogUtil.e("exec cmd fail.");
                append(stringBuffer, "exec cmd fail.");
                isSuccess = false;
            }
            LogUtil.i("exec finished.");
            append(stringBuffer, "exec finished.");
        } catch (IOException e) {
//            LogUtil.e(e);
            e.printStackTrace();
        } catch (InterruptedException e) {
//            LogUtil.e(e);
            e.printStackTrace();
        } finally {
            LogUtil.i("ping exit.");
            if (process != null) {
                process.destroy();
            }
            if (successReader != null) {
                try {
                    successReader.close();
                } catch (IOException e) {
//                    LogUtil.e(e);
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;
    }

    private static void append(StringBuffer stringBuffer, String text) {
        if (stringBuffer != null) {
            stringBuffer.append(text + "\n");
        }
    }
}
