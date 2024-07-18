package com.umeng.nns_demo;

import android.Manifest;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;


public class SmsActivity extends AppCompatActivity {
    private String smsInfo;
    private TextView smsTv;
    private Button readSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        smsTv = findViewById(R.id.smsTv);
        readSms = findViewById(R.id.readSmsBtn);
        readSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smsInfo = getSmsInPhone();
                smsTv.setText(smsInfo);
            }
        });
        requestPermissionIfNeeded();
    }



    public String getSmsInPhone() {
        final String SMS_URI_INBOX = "content://sms/inbox"; // 已接收短信

        StringBuilder smsBuilder = new StringBuilder();

        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            String[] projection = new String[] { "_id", "address", "person",
                    "body", "date", "type", };
            Cursor cur = getContentResolver().query(uri, null, null,
                    null, null);
            // 获取已接收短信
            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");

                do {
                    String strAddress = cur.getString(index_Address);
                    int intPerson = cur.getInt(index_Person);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int intType = cur.getInt(index_Type);

                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd hh:mm:ss");
                    Date d = new Date(longDate);
                    String strDate = dateFormat.format(d);

                    String strType = "";
                    if (intType == 1) {
                        strType = "接收";
                        smsBuilder.append("[ ");
                        smsBuilder.append(strAddress + ", ");
                        smsBuilder.append(intPerson + ", ");
                        smsBuilder.append(strbody + ", ");
                        smsBuilder.append(strDate + ", ");
                        smsBuilder.append(strType);
                        smsBuilder.append(" ]\n\n");
                    } else {
                        strType = "null";
                    }


                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            } else {
                smsBuilder.append("未检测到已接收短信。");
            }

            smsBuilder.append("获取短信完毕。");

        } catch (SQLiteException ex) {
            Log.d("[nns_demo]", ex.getMessage());
        }

        return smsBuilder.toString();
    }

    private void requestPermissionIfNeeded() {
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_SMS
        )) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 101);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101:
                if (grantResults.length > 0 && PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                    Toast.makeText(this, " 已授予读取短信权限 ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, " 你拒绝授予读取短信权限 ", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }
}
