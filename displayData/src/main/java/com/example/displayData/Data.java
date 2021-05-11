package com.example.displayData;

import java.util.ArrayList;

public class Data {

    public String batteryLevel = "";
    public String deviceInfo = "";
    public String networkType = "";
    public String deviceStorageCapacity = "";
    public String weatherReport = "";

    public String time = "";

    int timeInterval;
    public boolean isBatteryLevel, isDeviceInfo, isNetworkType, isDeviceStoragecapacity, isWeatherReport;


    public Data(String time, String batteryLevel, String deviceInfo, String networkType, String deviceStorageCapacity, String weatherReport) {
        this.time = time;
        this.batteryLevel = batteryLevel;
        this.deviceInfo = deviceInfo;
        this.networkType = networkType;
        this.deviceStorageCapacity = deviceStorageCapacity;
        this.weatherReport = weatherReport;
    }

    public Data(int timeInterval, boolean isBatteryLevel, boolean isDeviceInfo, boolean isNetworkType, boolean isDeviceStoragecapacity, boolean isWeatherReport) {
        this.timeInterval = timeInterval;
        this.isBatteryLevel = isBatteryLevel;
        this.isDeviceInfo = isDeviceInfo;
        this.isNetworkType = isNetworkType;
        this.isDeviceStoragecapacity = isDeviceStoragecapacity;
        this.isWeatherReport = isWeatherReport;
    }

}
