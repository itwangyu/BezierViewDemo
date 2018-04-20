package com.example.huangxiujun.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by WangYu on 2018/4/18.
 */
public class ScanActivity extends AppCompatActivity {

    private ScanView scanView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        scanView = findViewById(R.id.scan_view);
        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               scanView.start();
            }
        });
    }
}
