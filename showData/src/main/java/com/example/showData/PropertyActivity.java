package com.example.showData;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.displayData.ConstantsVal;
import com.example.displayData.Data;
import com.example.displayData.DataActivity;
import com.example.displayData.Singleton;

public class PropertyActivity extends AppCompatActivity implements View.OnClickListener {


    CheckBox cbBatteryLevel, cbDeviceInfo, cbNetworkType, cbStorageCapacity, cbWeatherReport;


    public static PropertyActivity instance;
    EditText mTimeInter;
    Button mSubmit;
    public static int Intervalue = 1;
    String TAG = "STOREDATA MainActivity ";

    @Override
    protected void onResume() {
        super.onResume();
        try {


            if (Singleton.getInstance() != null &&
                    !Singleton.getInstance().getString(ConstantsVal.DATA, this).equals("")) {
                startActivity(new Intent(getApplicationContext(), DataActivity.class));
            }


        } catch (Exception e) {
            Log.e(TAG, "property:ex " + e.getMessage());

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {


            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_property);


            instance = this;


            mTimeInter = findViewById(R.id.time_inter);
            mSubmit = findViewById(R.id.submit);

            cbBatteryLevel = (CheckBox) findViewById(R.id.cbBatteryLevel);
            cbDeviceInfo = (CheckBox) findViewById(R.id.cbDeviceInfo);
            cbNetworkType = (CheckBox) findViewById(R.id.cbNetworkType);
            cbStorageCapacity = (CheckBox) findViewById(R.id.cbStorageCapacity);
            cbWeatherReport = (CheckBox) findViewById(R.id.cbWeatherReport);

            mSubmit.setOnClickListener(this);


        } catch (Exception e) {
            Log.e("MainActivity2: ", e.getMessage());
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.submit:
                try {
                    if (mTimeInter.getText().toString().isEmpty()) {
                        return;
                    }
                    Intervalue = Integer.parseInt(mTimeInter.getText().toString());
                    Log.e("value", String.valueOf(Intervalue));


                    if (Intervalue < 5) {
                        mTimeInter.setError("Minumum  value - 5");
                        Toast.makeText(PropertyActivity.this, "Minimum  value - 5", Toast.LENGTH_SHORT).show();
                    } else {
                        Data data = new Data(Intervalue, cbBatteryLevel.isChecked(), cbDeviceInfo.isChecked(), cbNetworkType.isChecked(), cbStorageCapacity.isChecked(), cbWeatherReport.isChecked());
                        Singleton.getInstance().setStringShowData(ConstantsVal.SELECTED_PROPERTY, Singleton.getJsonStringFromObject(data), PropertyActivity.this);
                        Singleton.getInstance().setStringShowData(ConstantsVal.INTER_VALUE, String.valueOf(Intervalue), this);

                        Toast.makeText(getApplicationContext(), "value: " + Singleton.getInstance().getStringShowData(ConstantsVal.INTER_VALUE, this), Toast.LENGTH_SHORT).show();
                        finishAffinity();
                    }

                    break;


                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
        }


    }
}






