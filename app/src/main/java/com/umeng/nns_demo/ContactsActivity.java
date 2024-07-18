package com.umeng.nns_demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;

import android.content.pm.PackageManager;
import android.database.Cursor;

import android.os.Bundle;
import android.provider.ContactsContract;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private TextView tv;
    private final List<String> contractsList = new LinkedList<>();
    Button readContactBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        tv = findViewById(R.id.contactsTv);

        readContactBtn = findViewById(R.id.readContactBtn);

        readContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readTheContacts();
            }
        });

        requestPermissionIfNeeded();
    }


    private void requestPermissionIfNeeded() {
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
        )) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 101);
        }
    }

    /**
     * 读取联系人数据
     */
    private void readTheContacts() {
        contractsList.clear();
        Log.i("[nns_demo]", "调用ContentResolver.query方法读取联系人。");
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        // 遍历每一条联系人的数据
        while (cursor.moveToNext()) {
            // 读取联系人数据在系统中的id，以及联系人名称
            @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            // 读取联系人的电话号码
            StringBuilder numbers = new StringBuilder();
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
            while (phones.moveToNext()) {
                @SuppressLint("Range") String num = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                numbers.append(num).append("  ");
            }
            phones.close();

            // 读取联系人的邮箱
            StringBuilder address = new StringBuilder();
            Cursor mails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id, null, null);
            while (mails.moveToNext()) {
                @SuppressLint("Range") String mail = mails.getString(mails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                address.append(mail).append("  ");
            }
            mails.close();
            contractsList.add(String.format("[%s] %s\n%s\n%s", id, name, numbers.toString(), address.toString()));
        }
        cursor.close();

        // 界面显示联系人的数据
        updateUI();
    }

    private void updateUI() {
        tv.setText("");
        for (String s : contractsList) {
            tv.append(s);
            tv.append("\n\n");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101:
                if (grantResults.length > 0 && PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                    Toast.makeText(this, " 已授予读取联系人权限 ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, " 你拒绝了读取联系人权限 ", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }
}