package com.example.displayData;


import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataActivity extends AppCompatActivity {


    public static Object instance;
    ListView lvData;
    ArrayList<Data> mDataList = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();
        try {
            lvData = (ListView) findViewById(R.id.lvData);
            updateValues();

        } catch (Exception e) {
            Log.e("SHOWDATA", e.getMessage());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {


            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            instance = this;


        } catch (Exception e) {
            Log.e("MainActivity2: ", e.getMessage());
        }

    }

    public void updateValues() {
        Gson gson = new Gson();
        String jsonString = Singleton.getInstance().getString(ConstantsVal.DATA, DataActivity.this);
        Type type = new TypeToken<List<Data>>() {
        }.getType();
        ArrayList<Data> dataList = gson.fromJson(jsonString, type);

        if (jsonString != null && jsonString.trim().length() > 4) {
            mDataList = dataList;
        }
        CustomListAdapter adapter = new CustomListAdapter(this, mDataList);
        lvData.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


}






