package com.example.storeData;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.storeData.Network.ApiClient;
import com.example.displayData.ConstantsVal;
import com.example.displayData.Data;
import com.example.displayData.Singleton;
import com.example.displayData.module.CurrentWeatherResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class StoreActivity extends AppCompatActivity {


    List<Data> mDataList = new ArrayList<>();

    public static StoreActivity instance;
    Intent mServiceIntent;
    private UpdateDataService mYourService;
    String TAG = "STOREDATA MainActivity ";
    private GpsTracker gpsTracker;

    private String apiKey;
    private CompositeDisposable disposable = new CompositeDisposable();
    private ApiService apiService;

    private String strBatteryLevel = "", strDeviceInfo = "", strNetworkType = "", strCapacity = "", strWeatherReport = "";

    @Override
    protected void onResume() {
        super.onResume();


    }

    public static boolean isServiceRunning(String serviceClassName) {
        final ActivityManager activityManager = (ActivityManager) instance.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClassName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {


            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_store);

            apiService = ApiClient.getClient().create(ApiService.class);


            instance = this;
            mYourService = new UpdateDataService();
            mServiceIntent = new Intent(getApplicationContext(), mYourService.getClass());

            if (mServiceIntent == null) {
                mYourService = new UpdateDataService();
                mServiceIntent = new Intent(getApplicationContext(), mYourService.getClass());

            } else if (mServiceIntent != null && !isServiceRunning("com.example.storeData.UpdateDataService") && !Singleton.getInstance().getStringShowData(ConstantsVal.INTER_VALUE, this).equals(""))
                startService(mServiceIntent);


        } catch (Exception e) {
            Log.e("MainActivity2: ", e.getMessage());
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    String getNetworkType() {
        if (Connectivity.isConnected(this)) {
            if (Connectivity.isConnectedMobile(this))
                return "Mobile Network";
            else
                return "Wifi Network";

        }
        return "No internet connection";

    }

    public static String getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long BlockSize = stat.getBlockSize();
        long TotalBlocks = stat.getBlockCount();
        return formatSize(TotalBlocks * BlockSize);
    }

    public static String getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.
                    getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long BlockSize = stat.getBlockSize();
            long TotalBlocks = stat.getBlockCount();
            return formatSize(TotalBlocks * BlockSize);
        } else {
            return "Zero";
        }
    }

    public static boolean externalMemoryAvailable() {
        return Environment.
                getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static String formatSize(long size) {
        String suffixSize = null;

        if (size >= 1024) {
            suffixSize = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffixSize = "MB";
                size /= 1024;
            }
        }

        StringBuilder BufferSize = new StringBuilder(
                Long.toString(size));

        int commaOffset = BufferSize.length() - 3;
        while (commaOffset > 0) {
            BufferSize.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffixSize != null) BufferSize.append(suffixSize);
        return BufferSize.toString();
    }


    void submitValues() {

        if (!Singleton.getInstance().getStringShowData(ConstantsVal.INTER_VALUE, this).equals("")) {

            Handler mHandler = new Handler();
            Timer mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            BatteryManager bm = (BatteryManager) getApplicationContext().getSystemService(BATTERY_SERVICE);
                            int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                            Gson gson = new Gson();
                            String jsonString1 = Singleton.getInstance().getStringShowData(ConstantsVal.SELECTED_PROPERTY, StoreActivity.this);
                            Type typeSelect = new TypeToken<Data>() {
                            }.getType();
                            Data dataSelected = gson.fromJson(jsonString1, typeSelect);

                            if (dataSelected != null) {
                                if (dataSelected.isBatteryLevel)
                                    strBatteryLevel = "Battery Level: " + batLevel;
                                if (dataSelected.isDeviceInfo)
                                    strDeviceInfo = "Device Name and OS Version:" + Build.MODEL + " " + Build.VERSION.SDK_INT;
                                if (dataSelected.isNetworkType)
                                    strNetworkType = "Current Network type:" + getNetworkType();
                                if (dataSelected.isDeviceStoragecapacity)
                                    strCapacity = "Available Device storage capacity(Internal/External):" + getTotalInternalMemorySize() + "/" + getTotalExternalMemorySize();
                                if (dataSelected.isWeatherReport && Connectivity.isConnected(StoreActivity.this)) {
                                    getLocation();

                                } else {
                                    if (dataSelected.isWeatherReport)
                                        strWeatherReport = "Need Internet connection";
                                    else
                                        strWeatherReport = "";
                                    storeData();
                                }
                            } else {
                                storeData();
                            }

                        }
                    });
                }
            }, 0, TimeUnit.MINUTES.toMillis(Long.parseLong(Singleton.getInstance().getStringShowData(ConstantsVal.INTER_VALUE, this))));
        }

    }

    public void getLocation() {

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        gpsTracker = new GpsTracker(StoreActivity.this);
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(latitude, longitude, 1);
                getCurrentWeather(addresses.get(0).getSubAdminArea());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getSubAdminArea());
                Toast.makeText(getApplicationContext(), addresses.get(0).getSubAdminArea(), Toast.LENGTH_SHORT).show();
            } else {
                // do your stuff
            }

        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    private void getCurrentWeather(String cityName) {
        String defaultLang = "en";

        apiKey = getResources().getString(R.string.api_key_value);
        disposable.add(
                apiService.getCurrentWeather(
                        cityName, ConstantsVal.UNITS, defaultLang, apiKey)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<CurrentWeatherResponse>() {
                            @Override
                            public void onSuccess(CurrentWeatherResponse currentWeatherResponse) {

                                int val = (int) currentWeatherResponse.getMain().getTemp();
                                strWeatherReport = "Current Weather report: " + val + "°C";

                                storeData();
                            }

                            @Override
                            public void onError(Throwable e) {
                                //  binding.swipeContainer.setRefreshing(false);
                                try {
//                                    HttpException error = (HttpException) e;
//                                    handleErrorCode(error);
                                } catch (Exception exception) {
                                    e.printStackTrace();
                                    Log.e(TAG, e.getMessage());
                                }
                            }
                        })

        );
    }

    void storeData() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = dateFormat.format(new Date());


        Gson gson = new Gson();
        Data data = new Data(time, strBatteryLevel, strDeviceInfo, strNetworkType, strCapacity, strWeatherReport);

        String jsonString = Singleton.getInstance().getString(ConstantsVal.DATA, StoreActivity.this);
        Type type = new TypeToken<List<Data>>() {
        }.getType();
        List<Data> dataList = gson.fromJson(jsonString, type);

        if (jsonString != null && jsonString.trim().length() > 4) {
            mDataList = dataList;
        }
        mDataList.add(data);

        Singleton.getInstance().setString(ConstantsVal.DATA, Singleton.getJsonStringFromObject(mDataList), StoreActivity.this);

        Log.e(TAG, "Time: " + time);

        Toast.makeText(getApplicationContext(), "scanning" + mDataList.size(), Toast.LENGTH_SHORT).show();

    }


}






