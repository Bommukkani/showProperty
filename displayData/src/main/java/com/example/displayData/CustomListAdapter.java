package com.example.displayData;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<Data> {

    private final Activity context;
    private ArrayList<Data> dataArrayList;

    public CustomListAdapter(Activity context, ArrayList<Data> dataArrayList) {
        super(context, R.layout.cell_data, dataArrayList);

        this.context = context;
        this.dataArrayList = dataArrayList;
    }


    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.cell_data, null, true);

        TextView tvBatteryLevel = (TextView) rowView.findViewById(R.id.tv_battery_level);
        TextView tvNetworkType = (TextView) rowView.findViewById(R.id.tv_networkType);
        TextView tvTime = (TextView) rowView.findViewById(R.id.tv_time);
        TextView tvDeviceInfo = (TextView) rowView.findViewById(R.id.tv_deviceInfo);
        TextView tvStorageCapacity = (TextView) rowView.findViewById(R.id.tv_deviceStorage);
        TextView tvWeatherReport = (TextView) rowView.findViewById(R.id.tv_weatherReport);


        Data data = getItem(position);

        tvBatteryLevel.setText(data.batteryLevel);
        tvNetworkType.setText(data.networkType);
        tvTime.setText(data.time);
        tvDeviceInfo.setText(data.deviceInfo);
        tvStorageCapacity.setText(data.deviceStorageCapacity);
        tvWeatherReport.setText(data.weatherReport);


        return rowView;
    }

}
