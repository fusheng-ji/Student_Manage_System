package com.example.studentmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


public class RealWelcomeActivity extends AppCompatActivity {

    boolean isFirst;//判断APP是否第一次启动

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_welcome);

        //读取数据
        SharedPreferences sp = getSharedPreferences("isFirst",MODE_PRIVATE);
        isFirst = sp.getBoolean("isFirst",true);
        if(isFirst){
            startActivity(new Intent(RealWelcomeActivity.this,TeachWelcomeActivity.class));
        }else{
            startActivity(new Intent(RealWelcomeActivity.this,AdvertisementActivity.class));
        }
        //如果用户是第一次进入滑动Image界面，进入到倒计时界面
        finish();
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean("isFirst",false);
        editor.commit();
    }
}
