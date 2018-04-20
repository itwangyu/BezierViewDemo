package com.example.huangxiujun.myapplication;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by WangYu on 2018/4/17.
 */
public class DevicePolicyActivity extends AppCompatActivity {
    /**
     * 激活组件的请求码
     */
    private static final int REQUEST_CODE_ACTIVE_COMPONENT = 1;

    /**
     * 设备安全管理服务，2.2之前需要通过反射技术获取
     */
    private DevicePolicyManager devicePolicyManager = null;
    /**
     * 对应自定义DeviceAdminReceiver的组件
     */
    private ComponentName componentName = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devicepolicy);

        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, MyDeviceAdminReceiver.class);

        /**
         * 激活设备管理器
         */
        findViewById(R.id.btn_active).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdminActive()) {
                    Toast.makeText(DevicePolicyActivity.this, "设备管理器已激活", Toast.LENGTH_SHORT).show();
                } else {
                    // 打备管理器的激活窗口
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    // 指定需要激活的组件
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "(激活窗口中的描述信息)");
                    startActivityForResult(intent, REQUEST_CODE_ACTIVE_COMPONENT);
                }
            }
        });

        /**
         * 取消激活设备管理器
         */
        findViewById(R.id.btn_cancel_active).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdminActive()) {
                    devicePolicyManager.removeActiveAdmin(componentName);
                    Toast.makeText(DevicePolicyActivity.this, "将触发DeviceAdminReceiver.onDisabled", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DevicePolicyActivity.this, "设备管理器未激活", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * 修改锁屏密码
         */
        findViewById(R.id.btn_change_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdminActive()) {
                    devicePolicyManager.resetPassword("1234", 0);
                    Toast.makeText(DevicePolicyActivity.this, "若发生改变，则将触发DeviceAdminReceiver.onPasswordChanged", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DevicePolicyActivity.this, "设备管理器未激活", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * 取消锁屏密码
         */
        findViewById(R.id.btn_cancel_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdminActive()) {
                    devicePolicyManager.resetPassword("", 0);
                    Toast.makeText(DevicePolicyActivity.this, "若发生改变，则将触发DeviceAdminReceiver.onPasswordChanged", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DevicePolicyActivity.this, "设备管理器未激活", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * 锁屏
         */
        findViewById(R.id.btn_lock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdminActive()) {
                    devicePolicyManager.lockNow();
                } else {
                    Toast.makeText(DevicePolicyActivity.this, "设备管理器未激活", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * 设置锁屏时间
         */
        findViewById(R.id.btn_lock_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdminActive()) {
                    devicePolicyManager.setMaximumTimeToLock(componentName, 5000);
                    Toast.makeText(DevicePolicyActivity.this, "锁屏设置将不可用", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DevicePolicyActivity.this, "设备管理器未激活", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * 取消锁屏时间
         */
        findViewById(R.id.btn_cancel_lock_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdminActive()) {
                    devicePolicyManager.setMaximumTimeToLock(componentName, Long.MAX_VALUE);
                    Toast.makeText(DevicePolicyActivity.this, "锁屏设置恢复可用", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DevicePolicyActivity.this, "设备管理器未激活", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * 恢复出厂设置
         */
        findViewById(R.id.btn_wipe_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdminActive()) {
                    // 模拟器上无效，真机上慎用
//            devicePolicyManager.wipeData(0);
                    Toast.makeText(DevicePolicyActivity.this, "源码已屏蔽，慎用", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DevicePolicyActivity.this, "设备管理器未激活", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /**
     * 判断该组件是否有系统管理员的权限（【系统设置-安全-设备管理器】中是否激活）
     * @return
     */
    private boolean isAdminActive() {
        return devicePolicyManager.isAdminActive(componentName);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ACTIVE_COMPONENT) {
            // 激活组件的响应
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "用户手动取消激活", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "已触发DeviceAdminReceiver.onEnabled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
