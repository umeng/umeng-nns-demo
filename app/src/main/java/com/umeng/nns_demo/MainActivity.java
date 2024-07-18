package com.umeng.nns_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.umeng.nns_demo.record.AudioRecorder;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    AudioRecorder audioRecorder;
    Button startRecordBtn;
    public static String getClipContent(Context context) {
        ClipboardManager manager = (ClipboardManager) context.getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null) {
            if (manager.hasPrimaryClip()) {
                int count = manager.getPrimaryClip().getItemCount();
                if (count > 0) {
//                CharSequence addedText = manager.getPrimaryClip().getItemAt(0).getText();
//                String addedTextString = String.valueOf(addedText);
//                if (!TextUtils.isEmpty(addedTextString)) {
//                    return addedTextString;
//                }
                }

            }
        }
        return "";
    }

    @SuppressLint({"MissingPermission", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context appContext = this.getApplicationContext();
        setContentView(R.layout.activity_main);

        audioRecorder = AudioRecorder.getInstance();
        AudioRecorder.setAppContext(this.getApplicationContext());
        Button readImeiBtn = findViewById(R.id.ImeiBtn);
        readImeiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT < 29) {
                    if (ContextCompat.checkSelfPermission(appContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= 26) {
                            Log.i("[nns_demo]", "当前Android系统低于 10，尝试读取IMEI");
                            String imei = "";
                            TelephonyManager tm = (TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE);
                            if (tm != null) {
                                imei = tm.getImei();
                            }
                        } else {
                            Log.i("[nns_demo]", "当前Android系统低于 10，尝试读取IMEI");
                            String imei = "";
                            TelephonyManager tm = (TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE);
                            if (tm != null) {
                                imei = tm.getDeviceId();
                            }
                        }

                    }
                } else {
                    Log.i("[nns_demo]", "当前Android系统高于或等于 10，不读取IMEI");
                }
            }
        });

        Button readImsiBtn = findViewById(R.id.ImsiBtn);
        readImsiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT < 29) {
                    if (ContextCompat.checkSelfPermission(appContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        Log.i("[nns_demo]", "当前Android系统低于 10，尝试读取IMSI");
                        String imsi = "";
                        TelephonyManager tm = (TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE);
                        if (tm != null) {
                            imsi = tm.getSubscriberId();
                        }
                    }
                } else {
                    Log.i("[nns_demo]", "当前Android系统高于或等于 10，不读取IMSI");
                }
            }
        });

        Button readAidBtn = findViewById(R.id.AidBtn);
        readAidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("[nns_demo]", "读取Android_ID");
                String androidId = Settings.Secure.getString(appContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        });

        Button readIccidBtn = findViewById(R.id.IccidBtn);
        readIccidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT < 29) {
                    if (ContextCompat.checkSelfPermission(appContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        Log.i("[nns_demo]", "当前Android系统低于 10，尝试读取ICCID");
                        String iccid = "";
                        TelephonyManager tm = (TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE);
                        if (tm != null) {
                            iccid = tm.getSimSerialNumber();
                        }
                    }
                } else {
                    Log.i("[nns_demo]", "当前Android系统高于或等于 10，不读取ICCID");
                }
            }
        });

        Button contactsBtn = findViewById(R.id.contactsBtn);
        contactsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(appContext, ContactsActivity.class);
                startActivity(intent);
            }
        });

        Button messageBtn = findViewById(R.id.messageBtn);
        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(appContext, SmsActivity.class);
                startActivity(intent);
            }
        });

        Button clipContentBtn = findViewById(R.id.getClipContentBtn);
        clipContentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = getClipContent(appContext);
            }
        });

        startRecordBtn = findViewById(R.id.startRecordBtn);
        startRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (audioRecorder.getStatus() == AudioRecorder.Status.STATUS_NO_READY) {
                        //初始化录音
                        String fileName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
                        audioRecorder.createDefaultAudio(fileName);
                        audioRecorder.startRecord();

                        startRecordBtn.setText("停止录音");

                    } else {
                        //停止录音
                        audioRecorder.stopRecord();
                        startRecordBtn.setText("开始录音");

                    }

                } catch (IllegalStateException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        verifyAudioRecordPermissions(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (audioRecorder.getStatus() == AudioRecorder.Status.STATUS_NO_READY || audioRecorder.getStatus() == AudioRecorder.Status.STATUS_READY) {

        } else {
            audioRecorder.stopRecord();
            startRecordBtn.setText("开始录音");
        }

    }
//申请录音权限

    private static final int REQUEST_PERMISSION = 123;

    private static String[] PERMISSION_ALL = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    /** 申请录音权限
     ** 测试录音功能需授予此处申请相关权限
     **/
    public static void verifyAudioRecordPermissions(Activity activity) {
        boolean permission = (ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
                || (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                || (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED);
        if (permission) {
            ActivityCompat.requestPermissions(activity, PERMISSION_ALL,
                    REQUEST_PERMISSION);
        }
    }
}