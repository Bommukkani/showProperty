package com.example.storeData;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class UpdateDataService extends IntentService {
    String TAG = "YourService";


    public UpdateDataService() {
        super("YourService");
    }


    @Override
    public void onCreate() {

        super.onCreate();


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            super.onStartCommand(intent, flags, startId);

            if (StoreActivity.instance != null)
                StoreActivity.instance.submitValues();


        } catch (Exception e) {
        }
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }


}
