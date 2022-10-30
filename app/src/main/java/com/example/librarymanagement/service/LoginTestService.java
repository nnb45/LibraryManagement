package com.example.librarymanagement.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.example.librarymanagement.dao.UserDAO;

public class LoginTestService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Bundle bundle = intent.getExtras();
        String user = bundle.getString("user");
        String pass = bundle.getString("pass");

        UserDAO userDAO = new UserDAO(this);
        boolean check = userDAO.kiemTraDangNhap(user, pass);

        //gui Data
        Intent intentBR = new Intent();
        Bundle bundleBR = new Bundle();
        bundleBR.putBoolean("check", check);
        intentBR.putExtras(bundleBR);
        intentBR.setAction("kiemTraDangNhap");
        sendBroadcast(intentBR);

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
